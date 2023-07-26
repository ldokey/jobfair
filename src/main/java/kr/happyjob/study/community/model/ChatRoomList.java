package kr.happyjob.study.community.model;



public class ChatRoomList extends defaultVO {
	
	
	private int chatRoomNo;
	private String chatTitle;
	private int isDelete;
	private String regDate;
	private String updateDate;
	public int getChatRoomNo() {
		return chatRoomNo;
	}
	public void setChatRoomNo(int chatRoomNo) {
		this.chatRoomNo = chatRoomNo;
	}
	public String getChatTitle() {
		return chatTitle;
	}
	public void setChatTitle(String chatTitle) {
		this.chatTitle = chatTitle;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public ChatRoomList(int pageSize, int cpage, int chatRoomNo, String chatTitle, int isDelete, String regDate,
			String updateDate) {
		
		this.chatRoomNo = chatRoomNo;
		this.chatTitle = chatTitle;
		this.isDelete = isDelete;
		this.regDate = regDate;
		this.updateDate = updateDate;
	}
	
	public ChatRoomList() {
	}


}
