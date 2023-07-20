package kr.happyjob.study.community.model;

import lombok.Data;

@Data
public class ChatRoomList {
	private int chatRoomNo;
	private String chatTitle;
	private String regDate;
	private int isDelete;
}
