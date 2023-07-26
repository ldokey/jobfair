package kr.happyjob.study.community.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import kr.happyjob.study.community.controller.WebSocketHandler;
import kr.happyjob.study.community.dao.ChatDao;
import kr.happyjob.study.community.model.ChatRoomList;
import kr.happyjob.study.community.model.SocketEntity;


@Service
public class ChatServiceImp implements ChatService {
	
	@Autowired ChatDao chatDao;
	@Autowired WebSocketHandler webSocketHandler;
	
	


	
	// 로거 설정 
	private final Logger logger = LogManager.getLogger(this.getClass());
	private final String className = this.getClass().toString();
	
	


	
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
		return chatDao.selectAll();
	}

	
	
	// read - > count update 
	@Override
	public List<SocketEntity> updateMessage(Map<String, Object> requestBody) throws Exception {
		
		
		// 객체 조립 : 채팅방에서 넘어온 개별 객체
		int roomNo = (int) requestBody.get("chatRoomNo");
		String chatRoomNo1 = Integer.toString(roomNo);
		String sender = (String) requestBody.get("loginId");
		
		int participantCount = webSocketHandler.getConnectedUserCountInRoom(chatRoomNo1);
		
		int chatRoomNo = roomNo;
		
		
		
 		// 날짜 설정  
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);
        logger.info(formattedDate);
		
		// 조회 : 채팅방 하나의 대화 히스토리 전체, 매개변수 : 채팅방 번호  
		List<SocketEntity> afterMsgs = getChatHistory(chatRoomNo);
		logger.info("과거 내역 불러오기"+afterMsgs);
		
		
		// 세션에 접속한 숫자 (접속자) 
		
		
		logger.info("세션인원체크" + participantCount);
        
		
		if (participantCount == 0) {
			return afterMsgs;
			
		} else {
		
		// 읽음 관련 분기 처리 
        for (SocketEntity updateMessage : afterMsgs) {
        	
        	if(participantCount ==2) { // 세션 2개 일때, 
        		updateMessage.setReadCount(0);
        		updateMessage.setReadDate(formattedDate);
        		logger.info("세션 2개일 때 "+updateMessage);
    			
    		} else if(participantCount==1){ // 세션 1개 일때, 
    			if(sender.equals(updateMessage.getName())) { // 보낸사람과 db의 보낸사람이 같으면 readCount "1"로 바꾸기. 2에서 -1 처리  
    				updateMessage.setReadCount(1);
    				updateMessage.setReadDate(null);
    				logger.info("세션 1개일 때 "+updateMessage.getName());
    				logger.info(updateMessage);
    			} else if (!sender.equals(updateMessage.getName()) && updateMessage.getReadCount()==1 ){ // 보낸사람와 db이름이 다르고, 리드카운트가 1이면 다읽음처리 
    				updateMessage.setReadCount(0);
    				updateMessage.setReadDate(formattedDate);
    				
    				logger.info(updateMessage);
    			}
    		}
        	chatDao.updateMessage(updateMessage);
        }
		
		return afterMsgs;
		}
	}

	// 조회 : 방생성하거나 기존방 불러오기 
	@Override
	public Integer createOrGetChatRoom(String loginId, String targetUserId) throws Exception {
		
		logger.info("createOrGetChatRoom SV 입장 상대아이디 : " + targetUserId);
		logger.info("createOrGetChatRoom SV 입장 내 아이디 : " + loginId);
		
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("loginId", loginId);
		paramMap.put("targetUserId", targetUserId);
		
		//기존 방번호 출력  
		Integer existingChatRoomNo = chatDao.createOrGetChatRoom(paramMap);
		
		// loginId,targetUserId
		logger.info(" 기존 방 번호 출력  existingChatRoomNo: "+ existingChatRoomNo);
		
		// 기존 방 번호 null 값 받을 수 있게 변동 
		Optional<Integer> optionalChatRoomNo = Optional.ofNullable(existingChatRoomNo);
		
		// 기존방 번호 존재 여부 확인 
		if(optionalChatRoomNo.isPresent()) { // 있다면, 
			int chatRoomNo = optionalChatRoomNo.get();
			
			logger.info(" 기존 방 번호 출력 chatRoomNo : "+ chatRoomNo);
			
			return chatRoomNo;
			
		} else { // 없다면, 
			int newChatRoom = createChatRoom(loginId, targetUserId);
			logger.info(" 새로운 방 생성 출력 newChatRoom : "+ newChatRoom);
			return newChatRoom;
		}
	}
	
	// 기존 채팅방이 없다면, 채팅방 생성하기 
	@Override
	public int createChatRoom(String loginId, String targetUserId) throws Exception {
		
		
		logger.info("createChatRoom SV 입장 상대아이디 : " + targetUserId);
		
		
		// 날짜 생성 
		MyDateFormatter now = new MyDateFormatter();
		
		// 채팅방 객체 생성 
		ChatRoomList newChatRoom = new ChatRoomList();
		
		// 채팅방 조립 - 제목 / 생성시간 
		String chatTitle = loginId + " 님과 " +targetUserId + "님의 대화방";
		newChatRoom.setIsDelete(2);
		newChatRoom.setChatTitle(chatTitle);
		newChatRoom.setRegDate(now.getFormattedDate());
		
		// 업데이트 해줌 
		int newChatRoomNo = chatDao.createChatRoom(newChatRoom);
		logger.info(" 새로운 채팅 방 생성 여부  : " +newChatRoom);
		
		
		// 새로 만든 방 가장 높은 번호 불러오기 
		int maxChatRoomNo = chatDao.selectMaxChatRoomNo();
		logger.info(" 새로운 채팅 방 번호   : " +newChatRoom);
		
		// 새로운 메시지 두개 추가하기 (받는사람, 보내는 사람에 강제 메시지 부여= sender/receiver가 loginId로 통일됨 ) 
		// 객체생성 및 조립 
	
		logger.info(loginId);
		logger.info(targetUserId);
		
		
		SocketEntity newMsg = new SocketEntity();
		newMsg.setChatRoomNo(maxChatRoomNo);
		newMsg.setName(targetUserId); // 작성자 loginId, 칼럼이 하나이기에, 2명의 참여자를 기록하기 위해 상대방 자료를 입력 
		newMsg.setMsg(targetUserId + loginId + "의 대화방");
		newMsg.setReadCount(2);
		newMsg.setRegDate(now.getFormattedDate());
		
		int saveId = save(newMsg);
		
		logger.info(" 새로운 대화 객체 생성 완료 내용 : " + newMsg.getName() + saveId);
		
		newMsg.setChatRoomNo(maxChatRoomNo);
		newMsg.setName(loginId); // 작성자 loginId, 칼럼이 하나이기에, 2명의 참여자를 기록하기 위해 상대방 자료를 입력 
		newMsg.setMsg(loginId+"님이 입장했습니다.");
		newMsg.setReadCount(2);
		newMsg.setRegDate(now.getFormattedDate());

		save(newMsg);

		logger.info(" 새로운 대화 객체 생성 완료 내용 : " + newMsg.getName() + saveId);
		
		return maxChatRoomNo;
	}
	


	// 현재시간 조립 
	public class MyDateFormatter{
		public String getFormattedDate() {
	        // 현재 시간을 가져옴
	        LocalDateTime now = LocalDateTime.now();

	        // 원하는 형식으로 포맷팅
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        String formattedDate = now.format(formatter);

	        return formattedDate;
		}
	}

	@Override
	public int selectMaxChatRoomNo() {
		return chatDao.selectMaxChatRoomNo();
	}

	@Override
	public int updateId(SocketEntity newMsg) {
		return chatDao.updateID(newMsg);
	}

	@Override
	public List<ChatRoomList> selectAllById(ChatRoomList chatRoomList) throws Exception {
		logger.info("sv접속 ");
	    return chatDao.selectAllById(chatRoomList);
	}


	// 채팅방 개수 
	@Override
	public int countChatRoomList(Map<String, Object> paramMap) throws Exception {
		
		return chatDao.countChatRoomList(paramMap);
	}

	@Override
	public int deleteChatRoom(int chatRoomNo) {
		
		return chatDao.deleteChatRoom(chatRoomNo);
	}

	@Override
	public int selectChatRoom(int chatNo) {
		return chatDao.selectChatRoom(chatNo);
	}
}
