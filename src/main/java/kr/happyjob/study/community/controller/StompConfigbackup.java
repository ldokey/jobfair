//package kr.happyjob.study.community.controller;
//
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpSession;
//
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import kr.happyjob.study.community.model.ChatRoomList;
//import kr.happyjob.study.community.model.SocketEntity;
//import kr.happyjob.study.community.service.ChatService;
//import lombok.RequiredArgsConstructor;
//
//@Controller
//@RequiredArgsConstructor
//public class StompConfigbackup {
//	
//	// 기본설정 : 의존성 주입 
//	private final ChatService chatService;
//
//	
//	// 기본설정 : 로거 설정 
//	private final Logger logger = LogManager.getLogger(this.getClass());
//	private final String className = this.getClass().toString();
//
//	
//	// 기본설정 : 메시지컨트롤러 	
//	@MessageMapping("/server/{roomId}")
//	@SendTo("/topic/{roomId}")
//	public SocketEntity server(SocketEntity socketMsg, @PathVariable String roomId) {
//		System.out.println("roomId>>>>>>>>>>>>> : "+roomId);
//		return socketMsg;
//	}
//	
//	
//	// 접속 : 채팅방 리스트
//	@GetMapping("/community/chat.do")
//	public String roomList(HttpSession session, Model model) throws Exception {
//
//		String loginId = (String) session.getAttribute("loginId");
//		
//		// 조회 : 전체 조회 
//		List<ChatRoomList> chatList1 = chatService.selectAll();
//		
//		// 조회 : 접속 아이디로 필터링된 채팅방리스트 조회 
//		List<ChatRoomList> chatList = chatService.selectAllById(loginId);
//		
//	    logger.info("로그인한 사용자 아이디: " + loginId);
//
//	    model.addAttribute("loginId",loginId);
//		model.addAttribute("chatList", chatList);
//		return "community/chat/chatRoomList"; // 
//	}
//	
//	
//	// 접속 : 개별 채팅방
//	@GetMapping("/community/chatRoomNo.do")
//	public String enter (HttpSession session, @RequestParam int chatRoomNo, Model model) throws Exception {
//	   
//		// 세션에서 로그인한 사용자 아이디를 가져옵니다.
//	    String userId = (String) session.getAttribute("loginId");
//	    logger.info("로그인한 사용자 아이디: " + userId);
//
//	    model.addAttribute("userChatDto",userId);
//	    model.addAttribute("chatRoomNo",chatRoomNo);
//	    
//	    // 업데이트 대비용 -> updateMesaage 로 넘기는 파라미터 
//	    Map<String, Object> requestBody = new HashMap<>();
//	    requestBody.put("chatRoomNo", chatRoomNo);
//	    requestBody.put("userId", userId);
//	    
//	    chatService.updateMessage(requestBody);
//	    
//	    return "community/chat/chatRoom";
//	}
//
//	
//	
//	
//	
//	
////	 // 채팅방에 입장 !  
////    @PostMapping("/community/chatRoomNo.do")
////    @ResponseBody
////    public Map<String, Object>  room (Model model, HttpSession session, @RequestParam Map<String, Object> requestBody) throws Exception {
////    	
////    	// 세션에서 로그인 아이디 삽입 
////    	String userId = (String) session.getAttribute("loginId");
////    	logger.info("login ID===>"+userId);
////    	
////    	ChatUserDto userChatDto = new ChatUserDto();
////    	userChatDto.setName(userId);
////    
////    	// 서비스 처리 - count 감소  
////    	List<SocketEntity> chatList = chatService.updateMessage(requestBody);
////    	
////    	int chatRoomNo = Integer.parseInt(requestBody.get("chatRoomNo").toString());
////	    model.addAttribute("chatRoomNo", chatRoomNo);
////
////		Map<String, Object> returnMap = new HashMap<String, Object>();
////		returnMap.put("chatList", chatList);
////		returnMap.put("userChatDto", userChatDto);
////		
////
////    	return returnMap;
////    }
////    
//    
//    
//    // 채팅 내역 조회  ajax ( 방 하나 히스토리 부르기 )
//    @GetMapping("/chatHistory.do")
//    @ResponseBody
//    public List<SocketEntity> getChatHistory(int chatRoomNo) throws Exception {
//    	
//        return chatService.getChatHistory(chatRoomNo);
//    }
//    
//	
//	
//    
//    // 채팅 정보 저장 
//    @PostMapping("/saveChat.do")
//    @ResponseBody
//    public ResponseEntity<String> saveChatMessage(@RequestBody SocketEntity chatData){
//    	
//    	logger.info("chatData"+chatData.toString());
//    	
//    	try{
//    		chatData.setReadCount(2);
//    		chatService.save(chatData);
//    		
//    		logger.info("chat data"+chatData);
//        	return new ResponseEntity<>("채팅 저장 완료", HttpStatus.OK);
//    	} catch(Exception e) {
//    		e.printStackTrace();
//    		return new ResponseEntity<>("채팅 저장 불가", HttpStatus.INTERNAL_SERVER_ERROR);
//    	}
//    }
//    
//    
//    
//    
//    // 채팅 읽음 확인 처리
//    // readCount활용 2->0으로 만들기 
//    // 0이 될 때, 날짜 입력해 읽은 날짜 기능 추가 
//    @PostMapping("/chat/read.do")
//    @ResponseBody
//    public ResponseEntity<String> markMessageAsRead(@RequestParam("chatRoomNo") int chatRoomNo, HttpSession session) {
//    	
//    	try {
//    		// 채팅 업데이트하기 
////    		chatService.updateMessage(chatRoomNo, userId);
//    		return ResponseEntity.ok("메시지 읽음 처리 완료");
//    		
//    	} catch (Exception e) {
//    		e.printStackTrace();
//    		return ResponseEntity.ok("메시지 읽음 처리 실패");
//    	}
//    }
//    
//    
//    // 채팅 임시 발송 
//    @GetMapping("/asdf.do")
//    public String createOrGetChatRoom1(Model model) {
//    	
//    	String user = "ehunt";
//        model.addAttribute("user", user);
//        System.out.println("user : "+user);
//        
////        return ResponseEntity.ok(chatRoomNo);
//    	 return "community/chat/newChat";
//    }
//    
//    
//    // 새로운 채팅방 만들기 
//    @PostMapping("/createOrGetChatRoom")
//    public ResponseEntity<?> createOrGetChatRoom(@RequestBody String targetUserId, HttpSession session) throws Exception{
//    	
//    	String loginId = (String)session.getAttribute("loginId");
//    	
//    	logger.info("컨트롤러 입장 : 로그인 아이디 "+loginId);
//    	
//    	
//    	// 기존 방 출력 
//    	int chatRoomNo = chatService.createOrGetChatRoom(loginId, targetUserId);
//    	
//    	
//    	logger.info("생성완료  : 생성된 번호 및  아이디 "+ chatRoomNo + targetUserId);
//    	
////    	if(existingChatRoomNo != -1) {
////    		return ResponseEntity.ok(existingChatRoomNo);
////    	} else {
////    		int newChatRoomNo = chatService.creatNewChatRoom();
////    		return ResponseEntity.ok(newChatRoomNo);
////    	}
//    	return ResponseEntity.ok(chatRoomNo);
//    }
//    
//    
//}
