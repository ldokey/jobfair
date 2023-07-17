package kr.happyjob.study.community.controller;

import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import kr.happyjob.study.community.model.ChatUserDto;
import kr.happyjob.study.community.model.SocketEntity;
import kr.happyjob.study.community.service.ChatService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {
		
	private final ChatService chatService;
		
   // Set logger
	private final Logger logger = LogManager.getLogger(this.getClass());

   // Get class name for logger
	private final String className = this.getClass().toString();
	
    @ResponseBody
    @RequestMapping("/hi")
    public String Hi (){
        System.out.println("true = " + true);
        return "hi";
    }
    
    @GetMapping("/chat.do")
    public String enter () {
    	
    	System.out.println("chat.do");
    	
    	return "community/chat/chatRoom";
    }
    
    
    @RequestMapping("/chatList.do")
    public String chatList () {
    	
    	System.out.println("chatList");
    	
    	return "community/chat/chatList";
    }
    
    
    //
    
    @GetMapping("/list")
    public String list () {
    	return "chatRoomList";
    }
    
    
    
    // 채팅방 입장 
    @GetMapping("/community/chat.do")
    public String room (Model model, HttpSession session) {
    	
    	String userId = (String) session.getAttribute("loginId");
    	
    	logger.info("login ID===>"+userId);
    	
    	ChatUserDto userChatDto = new ChatUserDto();
    	userChatDto.setName(userId);
    	
//    	
    	model.addAttribute("userChatDto", userChatDto);

    	return "community/chat/chatRoom";
    }
    
    @PostMapping("/saveChat.do")
    @ResponseBody
    public ResponseEntity<String> saveChatMessage(@RequestBody SocketEntity chatData){
    	logger.info("controller 도착");
    	logger.info("getName"+chatData.toString());
    	try{
    		chatService.save(chatData);
    		logger.info("chat data"+chatData);
        	return new ResponseEntity<>("채팅 저장 완료", HttpStatus.OK);
    	} catch(Exception e) {
    		e.printStackTrace();
    		System.out.println(e.getStackTrace());
    		return new ResponseEntity<>("채팅 저장 불가", HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    


    
}
