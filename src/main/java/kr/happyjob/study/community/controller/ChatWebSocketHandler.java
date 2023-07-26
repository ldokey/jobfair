package kr.happyjob.study.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import kr.happyjob.study.community.dao.SessionStats;

@Component
public class ChatWebSocketHandler extends AbstractWebSocketHandler {

    private final SessionStats sessionStats;
    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public ChatWebSocketHandler(SessionStats sessionStats, SimpMessageSendingOperations messagingTemplate) {
        this.sessionStats = sessionStats;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessionStats.incrementSessionCount();
        sendSessionCount();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessionStats.decrementSessionCount();
        sendSessionCount();
    }

    private void sendSessionCount() {
        int sessionCount = sessionStats.getSessionCount();
        messagingTemplate.convertAndSend("/topic/sessionNumbers", "{\"count\": " + sessionCount + "}");
    }
}
