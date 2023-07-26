package kr.happyjob.study.community.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.happyjob.study.community.model.ChatRoomList;
import kr.happyjob.study.community.model.SocketEntity;
import kr.happyjob.study.community.service.ChatService;

@Controller
public class StompConfig {
	
	// 기본설정 : 의존성 주입 
	@Autowired
	ChatService chatService;

	

	
	
	// 기본설정 : 로거 설정 
	private final Logger logger = LogManager.getLogger(this.getClass());
	private final String className = this.getClass().toString();

	
	// 기본설정 : 메시지컨트롤러 	
	@MessageMapping("/server/{roomId}")
	@SendTo("/topic/{roomId}")
	public SocketEntity server( SocketEntity socketMsg, @PathVariable String roomId) {
		System.out.println("roomId>>>>>>>>>>>>> : "+roomId);
		
//		String username = principal.getName();
//		socketMsg.setName(username);
//		
//		
//		logger.info("웹소켓 principal : "+ principal.toString());
//		logger.info("웹소켓 username : "+ username);
		
		return socketMsg;
	}
	
	@MessageMapping("/sessionNumbers")
	@SendTo("/topic/sessionNumbers")
	public String getSessionNumbers() {
		int sessionCount = 1;
		return "{\"count\": " + sessionCount + "}";
	}
	
	
	
	
	// 접속 : 첫 화면 ( 아이디, 유저네임 전달 )  
	@RequestMapping("/community/chat.do")
	public String chat(Model model, @RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		
		String loginId = (String) session.getAttribute("loginId");
		model.addAttribute("loginId",loginId);
		model.addAttribute("userNm",(String)session.getAttribute("userNm"));
		
		logger.info("로그인한 사용자 아이디>>>>: " + loginId);

	    model.addAttribute("loginId",loginId);
	
		
		return "community/chat/chat";
	}
	
	
	
	
	// 접속 : 채팅방 리스트
	@PostMapping("/community/chatRoomList.do")
	@ResponseBody
	public List<ChatRoomList> roomList (HttpSession session, Model model, ChatRoomList chatRoomList ) throws Exception {
		
		String loginId = (String) session.getAttribute("loginId");
		logger.info("로그인한 사용자 아이디 chatRoomList : " + loginId);
		logger.info(chatRoomList.getCpage());
//		logger.info(paramMap);
//		
//		// 페이지 사이징 
//		int pageSize = Integer.parseInt((String) paramMap.get("pageSize"));
//		int cPage = Integer.parseInt((String) paramMap.get("cpage"));
		int pageIndex = (chatRoomList.getCpage() - 1) * chatRoomList.getPageSize(); // 시작 번호 
		chatRoomList.setCpage(pageIndex);
		chatRoomList.setLoginId(loginId);
//		paramMap.put("pageIndex", pageIndex);
//		paramMap.put("pageSize", pageSize);
//		paramMap.put("loginId", loginId);
//		int countChatRoomList = chatService.countChatRoomList(paramMap);
		List<ChatRoomList> chatList = chatService.selectAllById(chatRoomList);
		logger.info(chatList);
//		
		
		// 채팅방 목록 불러오기  
		// 조회 : 접속 아이디로 필터링된 채팅방리스트 조회 

//	    model.addAttribute("loginId",loginId);
//		model.addAttribute("chatRoomList", chatRoomList);
//		model.addAttribute(countChatRoomList);
		
		
		return chatList; // 
//		return "community/chat/chatRoomList"; // 
	}
	
	
	// 접속 : 개별 채팅방 ???? 
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
	    
	    return "community/chat/chatRoom";
	}

	
	
	
    
    
    // 채팅 내역 조회  ajax ( 방 하나 히스토리 부르기 )
    @GetMapping("/chatHistory.do")
    @ResponseBody
    public List<SocketEntity> getChatHistory(int chatRoomNo) throws Exception {
    	
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
    public List<SocketEntity> markMessageAsRead(@RequestBody Map<String, Object> requestBody, HttpSession session) throws Exception {
       
    	 int chatNo = (int) requestBody.get("chatNo");
    	logger.info("컨트롤러 채팅방 번호"+ chatNo);
    	// paramMap 객체 조립 
    	// 접속 아이디 불러오기 + 채팅방 번호 불러오기 
    	String loginId = (String) session.getAttribute("loginId");
    	int chatRoomNo = chatService.selectChatRoom(chatNo);
    	
    	// 서비스로 보낼 paramMap
    	Map<String, Object> paramMap = new HashMap<>();
    	paramMap.put("loginId", loginId);
    	paramMap.put("chatRoomNo", chatRoomNo);
    	
    	
		// 채팅 업데이트하기 
		List <SocketEntity> markedMessage = chatService.updateMessage(paramMap);
		return markedMessage;
    }
    
    
    // 채팅 임시 발송 
    @GetMapping("/asdf.do")
    public String createOrGetChatRoom1(Model model) {
    	
    	String user = "ehunt";
        model.addAttribute("user", user);
        System.out.println("user : "+user);
        
//        return ResponseEntity.ok(chatRoomNo);
    	 return "community/chat/newChat";
    }
    
    
    // 새로운 채팅방 만들기 
    @PostMapping("/createOrGetChatRoom")
    public ResponseEntity<?> createOrGetChatRoom(@RequestBody String targetUserId, HttpSession session) throws Exception{
    	
    	String loginId = (String)session.getAttribute("loginId");
    	
    	logger.info("컨트롤러 입장 : 로그인 아이디 "+loginId);
    	
    	
    	// 기존 방 출력 
    	int chatRoomNo = chatService.createOrGetChatRoom(loginId, targetUserId);
    	
    	
    	logger.info("생성완료  : 생성된 번호 및  아이디 "+ chatRoomNo + targetUserId);
    	
    	return ResponseEntity.ok(chatRoomNo);
    }
    
    // 채팅방 나가기 
	@RequestMapping("/deleteChat.do")
	@ResponseBody
	public void deleteChat(Model model, @RequestBody Map<String, Object> requestBody,
			HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		
			
		Map<String, String> responseMap = new HashMap<>();
    	try {
    		int chatRoomNo = (int) requestBody.get("chatRoomNo");
    		logger.info("   - roomNo : " + chatRoomNo);
    		
    		int chatRoomList = chatService.deleteChatRoom(chatRoomNo);
    		responseMap.put("reslut","success");
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		responseMap.put("result","error");
    	}
    }
}
