package kr.happyjob.study.community.dao;

import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SessionStats {

    private final AtomicInteger sessionCount = new AtomicInteger(0);

    public void incrementSessionCount() {
        sessionCount.incrementAndGet();
    }

    public void decrementSessionCount() {
        sessionCount.decrementAndGet();
    }

    public int getSessionCount() {
        return sessionCount.get();
    }
}
