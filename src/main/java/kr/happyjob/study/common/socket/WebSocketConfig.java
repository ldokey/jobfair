package kr.happyjob.study.common.socket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import kr.happyjob.study.community.controller.CustomWebSocketHandlerDecorator;
import kr.happyjob.study.community.dao.SessionStats;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
	
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    
    
    
    

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
	
		registry.enableSimpleBroker("/topic");
		registry.setApplicationDestinationPrefixes("app");
	}

	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		
		registry.addEndpoint("/chat/room").setAllowedOrigins("*").withSockJS();
	}

	
	
	// 웹소켓 전송 구성 설정 메소드 ( 메시지 오류 처리 및 메시지 크기 제한 )
	@Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        try {
            registry.setSendTimeLimit(10 * 1000); // 10초의 전송 시간 제한 설정
        } catch (Exception e) {
            e.printStackTrace();
        }

        registry.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
            
        	// 오류 메시지 전달 위한 웹소켓데코레이터 ( GoF 중 데코레이터 패턴 )
        	@Override
            public WebSocketHandler decorate(WebSocketHandler handler) {
                return new CustomWebSocketHandlerDecorator(handler, messagingTemplate);
            }
        });

        registry.setSendBufferSizeLimit(5 * 1024 * 1024); // 5MB의 버퍼 크기 제한 설정
    }


	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configureClientOutboundChannel(ChannelRegistration registration) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		// TODO Auto-generated method stub
	}
	
    @Bean
    public SessionStats sessionStats() {
        return new SessionStats();
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
	
}
