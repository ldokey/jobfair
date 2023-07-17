package kr.happyjob.study.community.service;

import org.springframework.stereotype.Service;

import kr.happyjob.study.community.dao.ChatDao;
import kr.happyjob.study.community.model.SocketEntity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatServiceImp implements ChatService {
	private final ChatDao chatDao;
	

	@Override
	public int save(SocketEntity chatData) throws Exception {

    	int saveId = chatDao.save(chatData);
    	
    	System.out.println(saveId);
    	
		return saveId;
	}
}
