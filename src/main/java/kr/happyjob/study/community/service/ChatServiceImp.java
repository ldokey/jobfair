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
				
		
		// 채팅방에서 넘어온 개별 객체 조립 
		int chatRoomNo = (int) requestBody.get("chatRoomNum");
		String sender = (String) requestBody.get("name");
		
		
		// db에서 불러온 채팅 객체 하나 ( 매개변수 : 채팅방번호, 채팅번호 ) -> 채팅 히스토리 전체를 불러와야함. 
//		SocketEntity chatMsg = getChatMessageByChatNoAndChatRoomNo(chatRoomNo,chatNo);
		List<SocketEntity> afterMsgs = getChatHistory(chatRoomNo);
		
		
		// 세션에 접속한 숫자 (접속자) 
		int participantCount = webSocketHandler.getParticipantsCount();
		

        
		// 읽음 관련 분기 처리 
		// 세션에 접속자 두개일때, 
        
        for (SocketEntity updateMessage : afterMsgs) {
        	
    		// 날짜 설정  
    		LocalDateTime now = LocalDateTime.now();
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = now.format(formatter);
    		
    		// db에서 불러온 해당 카운트 
    		int readCount = updateMessage.getReadCount();
        	
        	
        	if(participantCount ==2) { // 세션 2개 일때, 
        		updateMessage.setReadCount(0);
    			
    		} else if(participantCount==1){ // 세션 1개 일때, 
    			if(sender.equals(updateMessage.getName())) { // 보낸사람과 db의 보낸사람이 같으면 readCount "1"로 바꾸기. 2에서 -1 처리  
    				updateMessage.setReadCount(1);
    			} else if (!sender.equals(updateMessage.getName()) && updateMessage.getReadCount()==1 ){ // 보낸사람와 db이름이 다르고, 리드카운트가 1이면 다읽음처리 
    				updateMessage.setReadCount(0);
    				updateMessage.setReadDate(formattedDate);
    			}
    		}
        	chatDao.updateMessage(updateMessage);
        }
		
		return afterMsgs;
	}
}
