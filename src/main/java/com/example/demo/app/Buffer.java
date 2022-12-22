package com.example.demo.app;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class Buffer {
    private Vector<Request> requests;
    private AtomicInteger occupiedCells = new AtomicInteger();

    private final Report report;
    private final Object bufferNotEmptyNotifier;

    public Buffer(int size, Report report, Object bufferNotEmptyNotifier) {
        this.requests = new Vector<>(size);
        this.report = report;
        this.bufferNotEmptyNotifier = bufferNotEmptyNotifier;
        for (int i = 0; i < size; i++) {
            requests.add(null);
        }
    }

    public Request get(int index) {
        Request request = requests.get(index);
        requests.set(index, null);
        System.out.println("Buffer  : request " + request.getNumber() + " taken for processing");
        System.out.println(request.getCostTransportation() + " " + request.getPrepayment());
        occupiedCells.decrementAndGet();
        return request;
    }

    public int getOldestRequest() {
        int minID = -1, i = 0, minIndex = 0;
        while (i < requests.size()) {
            if (requests.get(i) != null) {
                minID = requests.get(i).getNumber();
                minIndex = i;
                break;
            }
            i++;
        }
        while (i < requests.size()) {
            if (minID > requests.get(i).getNumber()) {
                minID = requests.get(i).getNumber();
                minIndex = i;
            }
            i++;
        }
        return minIndex;
    }

    public void set(int index, Request request) {
        Request oldRequest = requests.get(index);
        if (oldRequest == null) {
            requests.set(index, request);
            occupiedCells.incrementAndGet();
            synchronized (bufferNotEmptyNotifier) {
                bufferNotEmptyNotifier.notify();
            }
        } else {
            requests.set(getOldestRequest(), request);
            report.addRequestTimeInBuffer(oldRequest.getNumber(), System.currentTimeMillis() - oldRequest.getArrivalTime());
            System.out.println("Request " + oldRequest.getNumber() + " canceled");
            report.incrementRejectedRequestCount(oldRequest.getSourceNumber());
        }
    }

    public boolean isEmpty() {
        return occupiedCells.get() == 0;
    }

    public Vector<Request> getRequestsList() {
        return requests;
    }
}
