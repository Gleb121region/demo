package com.example.demo.app;

import java.util.Vector;

public class BufferManager {
    private int indexOfLastRequest;

    private final Buffer buffer;

    public BufferManager(Buffer buffer) {
        this.buffer = buffer;
    }

    public void emplace(Request request) {
        synchronized (buffer) {
            Vector<Request> requestsList = buffer.getRequestsList();
            for (int i = 0; i < requestsList.size(); i++) {
                if (requestsList.get(i) == null) {
                    buffer.set(i, request);
                    indexOfLastRequest = i;
                    return;
                }
            }
            buffer.set(indexOfLastRequest, request);
        }
    }
}
