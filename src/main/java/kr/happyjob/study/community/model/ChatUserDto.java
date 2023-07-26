package kr.happyjob.study.community.model;



public class ChatUserDto {
	private String name;
	private String fileCd;
	private String regDate;
	private int chatRoomNo;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFileCd() {
		return fileCd;
	}
	public void setFileCd(String fileCd) {
		this.fileCd = fileCd;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public int getChatRoomNo() {
		return chatRoomNo;
	}
	public void setChatRoomNo(int chatRoomNo) {
		this.chatRoomNo = chatRoomNo;
	}
	public ChatUserDto(String name, String fileCd, String regDate, int chatRoomNo) {
		super();
		this.name = name;
		this.fileCd = fileCd;
		this.regDate = regDate;
		this.chatRoomNo = chatRoomNo;
	}
	
}
