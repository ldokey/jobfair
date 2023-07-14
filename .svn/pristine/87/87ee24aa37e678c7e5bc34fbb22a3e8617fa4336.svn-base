package kr.happyjob.study.statistics.controller;

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
import org.springframework.web.multipart.MultipartFile;

import kr.happyjob.study.statistics.model.PrsnlModel;
import kr.happyjob.study.statistics.service.PrsnlService;

@Controller
@RequestMapping("/statistics/")
public class PrsnlController {

	@Autowired
	PrsnlService prsnlService;

	// Set logger
	private final Logger logger = LogManager.getLogger(this.getClass());

	// Get class name for logger
	// private final String className = this.getClass().toString();

	// 통계 메인 페이지
	@RequestMapping("memberMgt.do")
	public String prsnl(Model model, @RequestParam Map<String, Object> paramMap, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		model.addAttribute("loginId", (String) session.getAttribute("loginId"));
		model.addAttribute("userNm", (String) session.getAttribute("userNm"));

		return "statistics/userinfo/notice";
	}

	@RequestMapping("prsnllist.do")
	public String prsnllist(Model model, @RequestParam Map<String, Object> paramMap, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		logger.info("   - paramMap : " + paramMap);
		
		int pageSize = Integer.parseInt((String) paramMap.get("pageSize"));
		int cpage = Integer.parseInt((String) paramMap.get("cpage"));
		int pageindex = (cpage - 1) * pageSize;
		
		paramMap.put("pageindex", pageindex);
		paramMap.put("pageSize", pageSize);
		
		List<PrsnlModel> statslist = prsnlService.prsnllist(paramMap);
		int countnoticelist = prsnlService.countnoticelist(paramMap);
		
		model.addAttribute("prsnllist", statslist);
		model.addAttribute("countnoticelist", countnoticelist);

		return "statistics/userinfo/noticeList";
	}
	
	// 조회수 필요없음
	/*@RequestMapping("pluswatch.do")
	@ResponseBody	
	public Map<String, Object> pluswatch(@RequestParam Map<String, Object> paramMap) throws Exception{
		logger.info("   - paramMap : " + paramMap);
		
		noticeService.pluswatch(paramMap);
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("result", "SUCCESS");

		return returnMap;
	}*/

	// 파일 저장 필요없음
	/*@RequestMapping("noticesavefile.do")
	@ResponseBody
	public Map<String, Object> noticesave(Model model, @RequestParam Map<String, Object> paramMap,
			@RequestParam("addfile") List<MultipartFile> multiFile, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		logger.info("   - paramMap : " + paramMap);
		logger.info("   - request : " + request);

		paramMap.put("loginId", session.getAttribute("loginId"));

		String action = (String) paramMap.get("action");

		if ("I".equals(action)) {
//			noticeService.noticenewsave(paramMap);
			noticeService.noticenewsavefile(paramMap, request, multiFile);
		}
		 else if("U".equals(action)){
//			 noticeService.noticeupdate(paramMap);
			 noticeService.noticenewupdatefile(paramMap, request, multiFile);
		 }
		 else if("D".equals(action)) {
			 noticeService.noticedelete(paramMap);
		 }

		Map<String, Object> returnMap = new HashMap<String, Object>();

		returnMap.put("result", "SUCCESS");

		return returnMap;
	}*/

	
	// 상세보기 
	/*@RequestMapping("noticedetail.do")
	@ResponseBody
	public Map<String, Object> noticedetail(Model model, @RequestParam Map<String, Object> paramMap,
			HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		
		logger.info("   - paramMap : " + paramMap);
		
		NoticeModel detail = noticeService.noticedetail(paramMap);
		
		Map<String, Object> returnMap = new HashMap<String, Object>();

		returnMap.put("detail", detail);
		
		logger.info("   - returnMap : " + returnMap);
		
		return returnMap;
	}*/
	
	// 그래프 부분
	// public Map<> statsgraph() throws Exception {}

}
