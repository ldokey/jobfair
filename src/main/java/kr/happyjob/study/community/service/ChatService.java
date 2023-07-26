package kr.happyjob.study.community.service;


import java.util.List;
import java.util.Map;


import kr.happyjob.study.community.model.ChatRoomList;
import kr.happyjob.study.community.model.SocketEntity;

public interface ChatService{

	public int save(SocketEntity chatData) throws Exception;
	public List<SocketEntity> getChatHistory(int chatRoomNo) throws Exception; 
	public SocketEntity getChatMessageByChatNo(int chatNo) throws Exception;
	public SocketEntity getChatMessageByChatNoAndChatRoomNo(int chatRoomNo, int chatNo) throws Exception;
	public int getReadCount(int chatNo) throws Exception;
	public List<ChatRoomList> selectAll()throws Exception;
	public List<SocketEntity> updateMessage(Map<String, Object> requestBody) throws Exception;
	public Integer createOrGetChatRoom(String loginId, String targetUserId) throws Exception;
	public int createChatRoom(String loginId, String targetUserId) throws Exception;
	public int selectMaxChatRoomNo()throws Exception;
	public int updateId(SocketEntity chatData)throws Exception;
	public List<ChatRoomList> selectAllById(ChatRoomList chatRoomList) throws Exception;
	public int countChatRoomList(Map<String, Object> paramMap)throws Exception;
	public int deleteChatRoom(int chatRoomNo);
	public int selectChatRoom(int chatNo);
}
