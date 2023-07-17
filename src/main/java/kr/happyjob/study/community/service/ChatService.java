package kr.happyjob.study.community.service;


import kr.happyjob.study.community.model.SocketEntity;

public interface ChatService{

	public int save(SocketEntity chatData) throws Exception; 

}
