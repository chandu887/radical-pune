package com.radical.lms.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.radical.lms.beans.DashBoardForm;
import com.radical.lms.beans.LeadsEntityBean;
import com.radical.lms.entity.UsersEntity;
import com.radical.lms.quartz.MailReadingJob;
import com.radical.lms.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	private boolean getData = false;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String homepage(ModelMap map, HttpServletRequest request) {
		HttpSession session = request.getSession();
		UsersEntity user = (UsersEntity) session.getAttribute("userInfo");
		if (user != null) {
			switch (user.getRoleId()) {
			case 1:
				return "redirect:/dashboard?leadStatus=1";
			default:
				return "login";
			}
		}
		return "login";
	}
	
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String dashBoard(HttpServletRequest request, @RequestParam("leadStatus") int leadStatus,ModelMap map) {
		HttpSession session = request.getSession();
		DashBoardForm dashBoardForm = new DashBoardForm();
		List countList = this.userService.getCountByStatusType();
		Map<Integer,Integer> countMap = new ConcurrentHashMap<Integer, Integer>();
		countMap.put(1, 0);
		countMap.put(2, 0);
		countMap.put(3, 0);
		for (Iterator iter = countList.iterator(); iter.hasNext();) {
			Object[] objects = (Object[]) iter.next();
			int statusId = (Integer) objects[0];
			long count = (Long) objects[1];
			countMap.put(statusId, (int)count);
		}
		long newCount =countMap.get(1).intValue();
		long openCount = countMap.get(2).intValue();
		long closeCount = countMap.get(3).intValue();
		int totalCount = (int) newCount + (int) openCount + (int) closeCount;
		dashBoardForm.setNewCount((int)newCount);
		dashBoardForm.setOpenCount((int)openCount);
		dashBoardForm.setClosedCount((int)closeCount);
		dashBoardForm.setTotalLeadsCount(totalCount);
		dashBoardForm.setPageNumber(1);
		dashBoardForm.setPageLimit(5);
		List<Integer> pageList = new ArrayList<Integer>();
		int i;
		for (i = 1	; i <= totalCount; i = i + dashBoardForm.getPageLimit()) {
			pageList.add(i);
		}
		dashBoardForm.setPageList(pageList);	
		
		List<LeadsEntityBean> leadsList = this.userService.getLeadsByStatus(leadStatus);
		map.addAttribute("leadsList", leadsList);
		map.addAttribute("dashBoardForm", dashBoardForm);
		session.setAttribute("dashBoardForm", dashBoardForm);
		return "dashboard";
	}
	

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginEmployee(@RequestParam("userName") String userName, @RequestParam("password") String password,
			 HttpServletRequest request) {
		UsersEntity user = userService.checkLoginDetails(userName, password);
		if (user != null) {
			HttpSession session = request.getSession();
			session.setAttribute("userInfo", user);
			switch (user.getRoleId()) {
			case 1:
				return "redirect:/dashboard?leadStatus=1";
			}
		}
		return "loginfailure";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("userInfo");
		session.invalidate();
		return "login";
	}
	
	@RequestMapping(value = "/testCron", method = RequestMethod.GET)
	public String testCron(HttpServletRequest request) throws JobExecutionException {
		MailReadingJob mail = new MailReadingJob();
		mail.executeInternal(null);
		return "login";
	}
}
