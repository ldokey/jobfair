package kr.happyjob.study.community.controller;

import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

public class CustomWebSocketHandlerDecorator extends WebSocketHandlerDecorator {

    private SimpMessagingTemplate messagingTemplate;

    public CustomWebSocketHandlerDecorator(WebSocketHandler delegate, SimpMessagingTemplate messagingTemplate) {
        super(delegate);
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (exception instanceof MessageDeliveryException) {
            String errorMessage = "메시지 발송 시간이 초과되었습니다";
            messagingTemplate.convertAndSendToUser(session.getId(), "/queue/errors", errorMessage);
        }
        super.handleTransportError(session, exception);
    }
}
