package kr.happyjob.study.community.model;



public class SocketEntity {
	private int chatNo;
	private int chatRoomNo;
	private String name;
	private String msg;
	private String regDate;	
	private int readCount;
	private String readDate;
	
	public SocketEntity(int chatNo, int chatRoomNo, String name, String msg, String regDate, int readCount,
			String readDate) {
		super();
		this.chatNo = chatNo;
		this.chatRoomNo = chatRoomNo;
		this.name = name;
		this.msg = msg;
		this.regDate = regDate;
		this.readCount = readCount;
		this.readDate = readDate;
	}
	
	public SocketEntity() {}
	public int getChatNo() {
		return chatNo;
	}
	public void setChatNo(int chatNo) {
		this.chatNo = chatNo;
	}
	public int getChatRoomNo() {
		return chatRoomNo;
	}
	public void setChatRoomNo(int chatRoomNo) {
		this.chatRoomNo = chatRoomNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public int getReadCount() {
		return readCount;
	}
	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}
	public String getReadDate() {
		return readDate;
	}
	public void setReadDate(String readDate) {
		this.readDate = readDate;
	}
}
