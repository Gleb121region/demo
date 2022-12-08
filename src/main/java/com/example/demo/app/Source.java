package com.example.demo.app;


import static com.example.demo.app.Constants.LAMBDA;
import static com.example.demo.app.Constants.MILLISECONDS_PER_SECOND;

public class Source implements Runnable {
    private static int count;

    private final int number;

    private final Object stepReportSynchronizer;
    private final Report report;
    private final BufferManager bufferManager;

    public Source(BufferManager bufferManager, Report report, Object stepReportSynchronizer) {
        this.number = count++;
        this.bufferManager = bufferManager;
        this.report = report;
        this.stepReportSynchronizer = stepReportSynchronizer;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (stepReportSynchronizer) {
                try {
                    stepReportSynchronizer.wait();
                } catch (InterruptedException ignored) {
                    break;
                }
            }
            Request request = Generator.generate(number);
            System.out.println("Source " + number + ": generate request " + request.getNumber());
            bufferManager.emplace(request);
            report.incrementGeneratedRequestCount(number);
            try {
                Thread.sleep((long) (Math.log(Math.random()) / (-LAMBDA)) * MILLISECONDS_PER_SECOND);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
