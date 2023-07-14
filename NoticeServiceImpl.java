package kr.happyjob.study.community.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.happyjob.study.community.dao.NoticeDao;
import kr.happyjob.study.community.model.NoticeModel;

@Service
public class NoticeServiceImpl implements NoticeService {
	
	@Autowired
	NoticeDao noticeDao;
	
	@Override
	public List<NoticeModel> noticelist(Map<String, Object> paramMap) throws Exception {
		
		return noticeDao.noticelist(paramMap);                       
	}
	
	@Override
	public int noticenewsave(Map<String, Object> paramMap) throws Exception {
		
		return noticeDao.noticenewsave(paramMap);
	}

}
