package com.example.demo;

import com.example.demo.app.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import static com.example.demo.app.Constants.MILLISECONDS_PER_SECOND;

public class Main {
    public static void main(String[] args) {
        boolean stepMode = false;
        final int SIMULATION_TIME = 10 * MILLISECONDS_PER_SECOND;
        final int BUFFER_SIZE = 3;
        final int SOURCES_COUNT = 3;
        final int DEVICES_COUNT = 5;

        Scanner in = new Scanner(System.in);

        Report report = new Report(SOURCES_COUNT, DEVICES_COUNT, BUFFER_SIZE, "Report.xls");
        Object stepReportSynchronizer = new Object();
        Object bufferNotEmptyNotifier = new Object();
        Buffer buffer = new Buffer(BUFFER_SIZE, report, bufferNotEmptyNotifier);
        BufferManager bufferManager = new BufferManager(buffer);
        List<Thread> sourcesThreads = new ArrayList<>(SOURCES_COUNT);
        for (int i = 0; i < SOURCES_COUNT; i++) {
            sourcesThreads.add(new Thread(new Source(bufferManager, report, stepReportSynchronizer)));
        }
        Vector<Device> devices = new Vector<>(DEVICES_COUNT);
        List<Thread> devicesThreads = new ArrayList<>(DEVICES_COUNT);
        for (int i = 0; i < DEVICES_COUNT; i++) {
            Device device = new Device(report, stepReportSynchronizer);
            devices.add(device);
            devicesThreads.add(new Thread(device));
        }
        DeviceManager deviceManager = new DeviceManager(buffer, devices, report, bufferNotEmptyNotifier, stepReportSynchronizer);
        Thread deviceManagerThread = new Thread(deviceManager);
        report.setDeviceManager(deviceManager);
        try {
            devicesThreads.forEach(Thread::start);
            Thread.sleep(1);
            sourcesThreads.forEach(Thread::start);
            Thread.sleep(1);
            deviceManagerThread.start();
            if (stepMode) {
                long userInputWaitTime = 0;
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < SIMULATION_TIME + userInputWaitTime) {
                    synchronized (stepReportSynchronizer) {
                        stepReportSynchronizer.notify();
                    }
                    long startUserInputTime = System.currentTimeMillis();
                    Thread.sleep(10);
                    System.out.println(report.writeConsoleStepReport());
                    System.out.print("Simulation stopped, wait input: \n");
                    in.nextLine();
                    userInputWaitTime += System.currentTimeMillis() - startUserInputTime;
                }
            } else {
                Thread thread = new Thread(() -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        synchronized (stepReportSynchronizer) {
                            stepReportSynchronizer.notify();
                        }
                    }
                });
                thread.start();
                Thread.sleep(SIMULATION_TIME);
                thread.interrupt();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sourcesThreads.forEach(Thread::interrupt);
        devicesThreads.forEach(Thread::interrupt);
        deviceManagerThread.interrupt();
        try {
            for (Thread sourceThread : sourcesThreads) {
                sourceThread.join();
            }
            for (Thread deviceThread : devicesThreads) {
                deviceThread.join();
            }
            deviceManagerThread.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupt");
        }
        try {
            report.writeFileStepReport();
            report.writeTotalReport();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
