package kr.happyjob.study.community.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import kr.happyjob.study.community.model.SocketEntity;

@Controller
public class StompConfig {

	@MessageMapping("/server")
	@SendTo("/topic/a")
	public SocketEntity server(SocketEntity socketMsg) {
		return socketMsg;
	}
}
