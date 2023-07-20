package kr.happyjob.study.community.model;

import lombok.Data;

@Data
public class SocketEntity {
	private int chatNo;
	private int chatRoomNo;
	private String name;
	private String msg;
	private String regDate;	
	private int readCount;
	private String readDate;
}
