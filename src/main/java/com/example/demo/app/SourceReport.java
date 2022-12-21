package com.example.demo.app;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class SourceReport {
    private final int number;

    private AtomicInteger generatedRequestCount = new AtomicInteger();
    private AtomicInteger processedRequestCount = new AtomicInteger();
    private AtomicInteger rejectedRequestCount = new AtomicInteger();

    private BigDecimal requestServiceTime;
    private BigDecimal requestTimeInBuffer;

    public SourceReport(int number) {
        this.number = number;
        this.requestServiceTime = new BigDecimal(0);
        this.requestTimeInBuffer = new BigDecimal(0);
    }

    public synchronized int getNumber() {
        return number;
    }

    public synchronized int getGeneratedRequestCount() {
        return generatedRequestCount.get();
    }

    public synchronized int getProcessedRequestCount() {
        return processedRequestCount.get();
    }

    public synchronized int getRejectedRequestCount() {
        return rejectedRequestCount.get();
    }

    public synchronized BigDecimal getRequestServiceTime() {
        return requestServiceTime;
    }

    public synchronized BigDecimal getRequestTimeInBuffer() {
        return requestTimeInBuffer;
    }

    public synchronized void incrementGeneratedRequestCount() {
        generatedRequestCount.incrementAndGet();
    }

    public synchronized void incrementProcessedRequestCount() {
        processedRequestCount.incrementAndGet();
    }

    public synchronized void incrementRejectedRequestCount() {
        rejectedRequestCount.incrementAndGet();
    }

    public synchronized void addRequestServiceTime(long requestServiceTime) {
        this.requestServiceTime = this.requestServiceTime.add(BigDecimal.valueOf(requestServiceTime));
    }

    public synchronized void addRequestTimeInBuffer(long requestTimeInBuffer) {
        this.requestTimeInBuffer = this.requestTimeInBuffer.add(BigDecimal.valueOf(requestTimeInBuffer));
    }
}
