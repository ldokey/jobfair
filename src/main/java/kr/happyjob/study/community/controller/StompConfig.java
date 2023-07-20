package kr.happyjob.study.community.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.happyjob.study.community.model.ChatRoomList;
import kr.happyjob.study.community.model.SocketEntity;
import kr.happyjob.study.community.service.ChatService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class StompConfig {
	
	//의존성 주입 
	private final ChatService chatService;

	
	// 로거 설정 
	private final Logger logger = LogManager.getLogger(this.getClass());
	private final String className = this.getClass().toString();

	
	// 메시지컨트롤러 	
	@MessageMapping("/server/{roomId}")
	@SendTo("/topic/{roomId}")
	public SocketEntity server(SocketEntity socketMsg, @PathVariable String roomId) {
		System.out.println("roomId>>>>>>>>>>>>> : "+roomId);
		return socketMsg;
	}
	
	
	// 채팅리스트 접속
	@GetMapping("/community/chat.do")
	public String roomList(HttpSession session, Model model) throws Exception {
		
		List<ChatRoomList> chatList = chatService.selectAll();
	    String userId = (String) session.getAttribute("loginId");
	    logger.info("로그인한 사용자 아이디: " + userId);

	    model.addAttribute("userId",userId);
		model.addAttribute("chatList", chatList);
		return "community/chat/chatRoomList"; // changed 
	}
	
	
	//채팅방 접속 ajax
	@GetMapping("/community/chatRoomNo.do")
	public String enter (HttpSession session, @RequestParam int chatRoomNo, Model model) throws Exception {
	   
		// 세션에서 로그인한 사용자 아이디를 가져옵니다.
	    String userId = (String) session.getAttribute("loginId");
	    logger.info("로그인한 사용자 아이디: " + userId);

	    model.addAttribute("userChatDto",userId);
	    model.addAttribute("chatRoomNo",chatRoomNo);
	    
	    // 업데이트 대비용 -> updateMesaage 로 넘기는 파라미터 
	    Map<String, Object> requestBody = new HashMap<>();
	    requestBody.put("chatRoomNo", chatRoomNo);
	    requestBody.put("userId", userId);
	    
	    chatService.updateMessage(requestBody);
	    
	    
	    
	    
//	    // 응답 맵을 준비합니다.
//	    Map<String, Object> responseMap = new HashMap<>();
//
//	    try {
//	        // 클라이언트에서 전달한 chatRoomNo를 가져옵니다.
//	        int chatRoomNo = (int) requestBody.get("chatRoomNo");
//
//	        // 채팅 메시지의 읽은 개수와 읽은 날짜를 업데이트합니다.
//	        List<SocketEntity> chatContent = chatService.updateMessage(requestBody);
//	        
//	        if (chatContent == null) {
//	            chatContent = new ArrayList<>();
//	        }
//
//	        // 업데이트된 채팅 메시지와 사용자 정보를 응답 맵에 추가합니다.
//	        responseMap.put("chatList", chatContent);
//	        responseMap.put("userChatDto", userChatDto);
//
//	        // 응답 맵을 반환합니다.
//	        return responseMap;
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        // 필요한 경우 에러를 처리합니다.
//	        responseMap.put("error", "채팅방에 입장하는데 실패했습니다.");
//	        return responseMap;
//	    }
	    return "community/chat/chatRoom";
	}

	
	
	
	
	
//	 // 채팅방에 입장 !  
//    @PostMapping("/community/chatRoomNo.do")
//    @ResponseBody
//    public Map<String, Object>  room (Model model, HttpSession session, @RequestParam Map<String, Object> requestBody) throws Exception {
//    	
//    	// 세션에서 로그인 아이디 삽입 
//    	String userId = (String) session.getAttribute("loginId");
//    	logger.info("login ID===>"+userId);
//    	
//    	ChatUserDto userChatDto = new ChatUserDto();
//    	userChatDto.setName(userId);
//    
//    	// 서비스 처리 - count 감소  
//    	List<SocketEntity> chatList = chatService.updateMessage(requestBody);
//    	
//    	int chatRoomNo = Integer.parseInt(requestBody.get("chatRoomNo").toString());
//	    model.addAttribute("chatRoomNo", chatRoomNo);
//
//		Map<String, Object> returnMap = new HashMap<String, Object>();
//		returnMap.put("chatList", chatList);
//		returnMap.put("userChatDto", userChatDto);
//		
//
//    	return returnMap;
//    }
//    
    
    
    // 채팅 내역 조회  ajax ( 방 하나 히스토리 부르기 )
    @GetMapping("/chatHistory.do")
    @ResponseBody
    public List<SocketEntity> getChatHistory( int chatRoomNo) throws Exception {
    	
        return chatService.getChatHistory(chatRoomNo);
    }
    
	
	
    
    // 채팅 정보 저장 
    @PostMapping("/saveChat.do")
    @ResponseBody
    public ResponseEntity<String> saveChatMessage(@RequestBody SocketEntity chatData){
    	
    	logger.info("chatData"+chatData.toString());
    	
    	try{
    		chatData.setReadCount(2);
    		chatService.save(chatData);
    		
    		logger.info("chat data"+chatData);
        	return new ResponseEntity<>("채팅 저장 완료", HttpStatus.OK);
    	} catch(Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>("채팅 저장 불가", HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    
    
    
    // 채팅 읽음 확인 처리
    // readCount활용 2->0으로 만들기 
    // 0이 될 때, 날짜 입력해 읽은 날짜 기능 추가 
    @PostMapping("/chat/read.do")
    @ResponseBody
    public ResponseEntity<String> markMessageAsRead(@RequestParam("chatRoomNo") int chatRoomNo, HttpSession session) {
    	
    	try {
    		// 채팅 업데이트하기 
//    		chatService.updateMessage(chatRoomNo, userId);
    		return ResponseEntity.ok("메시지 읽음 처리 완료");
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		return ResponseEntity.ok("메시지 읽음 처리 실패");
    	}
    }
    
    
    // 채팅 임시 발송 
    
}
