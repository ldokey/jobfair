package kr.happyjob.study.community.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import kr.happyjob.study.community.controller.WebSocketHandler;
import kr.happyjob.study.community.dao.ChatDao;
import kr.happyjob.study.community.model.ChatRoomList;
import kr.happyjob.study.community.model.SocketEntity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatServiceImp implements ChatService {
	
	private final ChatDao chatDao;
	private final WebSocketHandler webSocketHandler;
	

	@Override
	public int save(SocketEntity chatData) throws Exception {

    	int saveId = chatDao.save(chatData);
    	System.out.println(saveId);
    	
		return saveId;
	}

	@Override
	public List<SocketEntity> getChatHistory(int chatRoomNum) throws Exception {
		return chatDao.getChatHistory(chatRoomNum);
	}

	@Override
	public SocketEntity getChatMessageByChatNo(int chatNo) throws Exception {
		
		return chatDao.getChatMessageByChatNo(chatNo);
	}

	@Override
	public SocketEntity getChatMessageByChatNoAndChatRoomNo(int chatRoomNo, int chatNo) throws Exception {
		
		return chatDao.getChatMessageByChatNoAndChatRoomNo(chatRoomNo,chatNo);
	}

	@Override
	public int getReadCount(int chatNo) throws Exception {
		
		return chatDao.getReadCount(chatNo);
	}

	@Override
	public List<ChatRoomList> selectAll() throws Exception {
		// TODO Auto-generated method stub
		return chatDao.selectAll();
	}

	
	@Override
	public List<SocketEntity> updateMessage(Map<String, Object> requestBody) throws Exception {
		// TODO Auto-generated method stub
		
		// 빈 리스트 객체 생성 
		List<SocketEntity> updatedMessages = new ArrayList<>();
		
		
		// 채팅방에서 넘어온 개별 객체 조립 
		int chatNo = 2;
//				(int) requestBody.get("chatNo");
		int chatRoomNo = 1;
//		(int) requestBody.get("chatRoomNum");
		String sender = (String) requestBody.get("name");
		
		
		// db에서 불러온 채팅 객체 하나 ( 매개변수 : 채팅방번호, 채팅번호 )
		SocketEntity chatMsg = getChatMessageByChatNoAndChatRoomNo(chatRoomNo,chatNo);
		
		
		// db에서 불러온 해당 카운트 
		int readCount = chatMsg.getReadCount();
		
		// 세션에 접속한 숫자  (접속자) 
		int participantCount = webSocketHandler.getParticipantsCount();
		
		// 날짜 설정  
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);
		
		// 읽음 관련 분기 처리 
		// 세션에 접속자 두개일때, 
        
        for (SocketEntity updateMessage : updatedMessages) {
        	if(participantCount ==2) { // 세션 2개 일때, 
    			if(readCount > 0) {
    				chatMsg.setReadCount(readCount-1); // 하나 지우기 
    			} 
    		} else if(participantCount==1){ // 세션 1개 일때, 
    			if( !sender.equals(chatMsg.getName()) && chatMsg.getReadCount()==1 ) { //센더(채팅에서넘어온)와 이름(디비)와 다르다 && 메시지 카운트 1이면 0처리  
    				chatMsg.setReadCount(0);
    				chatMsg.setReadDate(formattedDate);
    			}
    		}
        	chatDao.updateMessage(chatMsg);
        }
		
		return updatedMessages;
	}
}
