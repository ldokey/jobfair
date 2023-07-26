package kr.happyjob.study.community.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import kr.happyjob.study.community.dao.SessionStats;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
	private final Set<WebSocketSession> sessions = new HashSet<>();
	
	
	// session 방마다 채널 설정 
	private final Map<String, Set<WebSocketSession>> roomSessions = new HashMap<>();

	// 방별 세션 값 설정 
	public void addSessionToRoom(String roomId, WebSocketSession session) {
		
		roomSessions.computeIfAbsent(roomId, key-> new HashSet<>()).add(session);
	}
	
	// 방별 세션 방 제거  
	public void removeSessionFromRoom(String roomId, WebSocketSession session) {
		Set<WebSocketSession> sessions = roomSessions.get(roomId);
		System.out.println("세션 제거 "+sessions.toString());
		if(sessions != null){
			sessions.remove(session);
		}	
	}
	
	// 세션에 접속한 유저 카운트 
	public int getConnectedUserCountInRoom(String roomId) {
		Set<WebSocketSession> sessions = roomSessions.get(roomId);
		System.out.println("세션 정보 "+sessions.toString());
		return sessions !=null ? sessions.size() : 0;
	}
	
    // 클라이언트가 웹소켓에 접속했을 때, 해당 클라이언트의 세션을 등록
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }
    
    // 클라이언트로부터 메시지를 받았을 때, 처리하는 로직
    // 채팅방 접속 및 메시지 전송 등의 로직 구현
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)throws Exception{
    	
    }
    
    // 클라이언트가 웹소켓 접속을 종료했을 때, 해당 클라이언트의 세션을 제거
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
    	sessions.remove(session);
    }
    
    // 채팅방에 접속한 클라이언트 수 반환하는 메서드	
    public int getParticipantsCount() {
    	return sessions.size();
    }
    
}
