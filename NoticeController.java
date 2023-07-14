package kr.happyjob.study.community.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.happyjob.study.community.model.NoticeModel;
import kr.happyjob.study.community.service.NoticeService;

@Controller
@RequestMapping("/community/")
public class NoticeController {
	
	@Autowired
	NoticeService noticeService;

	// Set logger
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	// Get class name for logger
//	private final String className = this.getClass().toString();
	
	@RequestMapping("notice.do")
	public String notice(Model model, @RequestParam Map<String, Object> paramMap, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		model.addAttribute("loginId", (String) session.getAttribute("loginId"));
		model.addAttribute("userNm", (String) session.getAttribute("userNm"));
		
		return "community/notice/notice";
	}
	
	@RequestMapping("noticelist.do")
	public String noticelist(Model model, @RequestParam Map<String, Object> paramMap, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		logger.info("   - paramMap : " + paramMap);
		List<NoticeModel> noticelist = noticeService.noticelist(paramMap);
		
		model.addAttribute("noticelist", noticelist);
		
		return "community/notice/noticeList";
	}
	@RequestMapping("noticesavefile.do")
	@ResponseBody
	public Map<String, Object> noticesave(Model model, @RequestParam Map<String, Object> paramMap, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		logger.info("   - paramMap : " + paramMap);
		logger.info("   - request : " + request);
		
		paramMap.put("loginId", session.getAttribute("loginId"));
		
		String action = (String) paramMap.get("action");
		
		if("I".equals(action)){
			noticeService.noticenewsave(paramMap);
		} 
//		else if("U".equals(action)){
//			
//		} else if("D".equals(action))
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		returnMap.put("result", "SUCCESS");
		
		return returnMap;
	}
	
	
}
