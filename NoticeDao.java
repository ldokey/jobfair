package kr.happyjob.study.community.dao;

import java.util.List;
import java.util.Map;

import kr.happyjob.study.community.model.NoticeModel;

public interface NoticeDao {
	public List<NoticeModel> noticelist(Map<String, Object> paramMap) throws Exception;
	
	public int noticenewsave(Map<String, Object> paramMap) throws Exception;
}
