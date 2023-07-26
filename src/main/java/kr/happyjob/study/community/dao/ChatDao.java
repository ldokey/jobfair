package kr.happyjob.study.community.dao;

import java.util.List;
import java.util.Map;

import kr.happyjob.study.community.model.ChatRoomList;
import kr.happyjob.study.community.model.SocketEntity;

public interface ChatDao {
	public int save(SocketEntity chatData) throws Exception;

	public List<SocketEntity> getChatHistory(int chatRoomNum);

	public SocketEntity getChatMessageByChatNo(int chatNo);

	public SocketEntity getChatMessageByChatNoAndChatRoomNo(int chatRoomNum, int chatNo);

	public int getReadCount(int chatNo);

	public List<ChatRoomList> selectAll();

	public SocketEntity updateMessage(SocketEntity chatMsg);

	public Integer createOrGetChatRoom(Map<String, String> paramMap);

	public int createChatRoom(ChatRoomList newChatRoom);

	public int selectMaxChatRoomNo();

	public int updateID(SocketEntity chatData);

	public List<ChatRoomList> selectAllById(ChatRoomList chatRoomList);

	public int countChatRoomList(Map<String, Object> paramMap);

	public int deleteChatRoom(int chatRoomNo);

	public int selectChatRoom(int chatNo); 
}
