package kr.happyjob.study.community.dao;

import kr.happyjob.study.community.model.SocketEntity;

public interface ChatDao {
	public int save(SocketEntity chatData) throws Exception; 
}
