package com.example.demo.app;

import java.io.IOException;
import java.util.Random;

import static com.example.demo.app.Constants.ALPHA;
import static com.example.demo.app.Constants.BETA;

public class Device implements Runnable {
    private static int count;
    private final int number;

    private volatile Request request;
    private volatile boolean isFree;

    private final Report report;
    private final Random random = new Random();
    private final Object newRequestNotifier;
    private final Object stepReportSynchronizer;


    public Device(Report report, Object stepReportSynchronizer) {
        this.number = count++;
        this.report = report;
        this.isFree = true;
        this.newRequestNotifier = new Object();
        this.stepReportSynchronizer = stepReportSynchronizer;
    }


    public boolean isFree() {
        return isFree;
    }

    public void requestProcessing(Request request) {
        synchronized (stepReportSynchronizer) {
            try {
                stepReportSynchronizer.wait();
            } catch (InterruptedException ignored) {
            }
        }
        this.request = request;
        synchronized (newRequestNotifier) {
            isFree = false;
            newRequestNotifier.notify();
        }
        System.out.println("Device " + number + ": start process request " + request.getNumber());
    }


    @Override
    public void run() {
        long startBusyTime;
        long startDownTime = 0;
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (newRequestNotifier) {
                try {
                    startDownTime = System.currentTimeMillis();
                    newRequestNotifier.wait();
                    startBusyTime = System.currentTimeMillis();
                    report.addDeviceDownTime(number, startBusyTime - startDownTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    report.addDeviceDownTime(number, System.currentTimeMillis() - startDownTime);
                    break;
                }
            }
            try {
                Thread.sleep((long) (random.nextDouble() * (BETA - ALPHA + 1)) + ALPHA);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                try {
                    report.incrementRejectedRequestCount(request.getSourceNumber());
                } catch (Exception ex) {
                }
                try {
                    report.addRequestTimeInBuffer(request.getSourceNumber(), startBusyTime - request.getArrivalTime());
                } catch (Exception ex) {
                }

                ResponsesWriter.requestRejection(request);
                System.out.println("Request " + request.getNumber() + " refused");
                break;
            }
            try {
                synchronized (ResponsesWriter.getWorkbook()) {
                    Payment.fileOutput(request, ResponsesWriter.getWorkbook(), ResponsesWriter.getResponsesFileName());
                }
            } catch (IOException e) {
            }
            try {
                report.incrementProcessedRequestCount(request.getSourceNumber());
            } catch (Exception e) {
            }
            try {
                report.addRequestServiceTime(request.getSourceNumber(), System.currentTimeMillis() - request.getArrivalTime());
            } catch (Exception e) {
            }
            report.addDeviceBusyTime(number, System.currentTimeMillis() - startBusyTime);
            System.out.println("Device " + number + ": finish process request " + request.getNumber());
            isFree = true;
        }
    }
}
