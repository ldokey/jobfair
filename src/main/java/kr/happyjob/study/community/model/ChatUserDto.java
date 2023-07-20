package kr.happyjob.study.community.model;

import lombok.Data;

@Data
public class ChatUserDto {
	private String name;
	private String fileCd;
	private String regDate;
	private int chatRoomNo;
}
