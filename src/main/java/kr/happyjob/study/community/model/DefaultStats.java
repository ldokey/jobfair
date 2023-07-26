package kr.happyjob.study.community.model;

import java.util.concurrent.atomic.AtomicInteger;

public class DefaultStats {
	

	private final AtomicInteger total = new AtomicInteger();

    private final AtomicInteger webSocket = new AtomicInteger();

    private final AtomicInteger httpStreaming = new AtomicInteger();

    private final AtomicInteger httpPolling = new AtomicInteger();

    private final AtomicInteger limitExceeded = new AtomicInteger();

    private final AtomicInteger noMessagesReceived = new AtomicInteger();

    private final AtomicInteger transportError = new AtomicInteger();
    
    
    public int getTotalSessions() {
        return this.total.get();
    }

    
    public int getWebSocketSessions() {
        return this.webSocket.get();
    }
	
}
