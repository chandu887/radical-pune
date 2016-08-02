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
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.radical.lms.beans.DashBoardForm;
import com.radical.lms.beans.LeadsEntityBean;
import com.radical.lms.entity.LeadsEntity;
import com.radical.lms.entity.UsersEntity;
import com.radical.lms.quartz.MailReadingJob;
import com.radical.lms.service.LeadService;
import com.radical.lms.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private LeadService leadService;

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
	public String dashBoard(HttpServletRequest request, @RequestParam("leadStatus") int leadStatus, ModelMap map,
			@RequestParam(value = "isFromFilter", defaultValue = "false", required = false) Boolean isFromFilter) {
		HttpSession session = request.getSession();

		DashBoardForm dashBoardForm = null;
		if (session.getAttribute("dashBoardForm") != null) {
			dashBoardForm = (DashBoardForm) session.getAttribute("dashBoardForm");
		} else {
			dashBoardForm = new DashBoardForm();
			dashBoardForm.setPageNumber(1);
			dashBoardForm.setPageLimit(5);
		}

		if (!isFromFilter) {
			dashBoardForm.setFromDate("");
			dashBoardForm.setToDate("");
			dashBoardForm.setCourse(0);
		}
		
		List countList = this.userService.getCountByStatusType(dashBoardForm);
		Map<Integer, Integer> countMap = new ConcurrentHashMap<Integer, Integer>();
		countMap.put(1, 0);
		countMap.put(2, 0);
		countMap.put(3, 0);
		countMap.put(4, 0);
		for (Iterator iter = countList.iterator(); iter.hasNext();) {
			Object[] objects = (Object[]) iter.next();
			int statusId = (Integer) objects[0];
			long count = (Long) objects[1];
			countMap.put(statusId, (int) count);
		}
		long newCount = countMap.get(1).intValue();
		long openCount = countMap.get(2).intValue();
		long closeCount = countMap.get(3).intValue();
		long deletedCount = countMap.get(4).intValue();
		int totalCount = (int) newCount + (int) openCount + (int) closeCount + (int) deletedCount;

		dashBoardForm.setNewCount((int) newCount);
		dashBoardForm.setOpenCount((int) openCount);
		dashBoardForm.setClosedCount((int) closeCount);
		dashBoardForm.setTotalLeadsCount(totalCount);
		dashBoardForm.setCurrentStatus(leadStatus);

		List<Integer> pageList = new ArrayList<Integer>();
		int page = 1;
		int i;
		for (i = 0; i <= totalCount; i = i + dashBoardForm.getPageLimit()) {
			pageList.add(page);
			page += 1;

		}

		dashBoardForm.setPageList(pageList);
		int statLimit = ((dashBoardForm.getPageNumber() - 1) * dashBoardForm.getPageLimit()) + 1;
		int endLimit = dashBoardForm.getPageLimit() * dashBoardForm.getPageNumber();
		dashBoardForm.setStartLimit(statLimit);
		if (totalCount > endLimit) {
			dashBoardForm.setEndLimit(endLimit);
		} else {
			dashBoardForm.setEndLimit(totalCount);
		}
		
		List<Integer> limitList = new ArrayList<Integer>();
		limitList.add(5);
		limitList.add(10);
		limitList.add(15);
		limitList.add(20);
		dashBoardForm.setLimitList(limitList);
		List<LeadsEntityBean> leadsList = this.userService.getLeadsStatus(dashBoardForm);
		Map<Integer, String> coursesMap = this.userService.getCourses();
		Map<Integer, String> leadSourceMapping = this.userService.getLeadSourceMapping();
		map.addAttribute("leadsList", leadsList);
		map.addAttribute("dashBoardForm", dashBoardForm);
		map.addAttribute("coursesMap", coursesMap);
		map.addAttribute("leadSourceMapping", leadSourceMapping);
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

	@RequestMapping(value = "/addlead", method = RequestMethod.POST)
	public String saveColor(@ModelAttribute(value = "addLeadForm") LeadsEntity leadsEntity, Model model) {
		int courseCategeory = this.userService.getCoursesCategeoryMapping().get(leadsEntity.getCourse());
		leadsEntity.setStatus(1);
		leadsEntity.setCourseCategeory(courseCategeory);
		this.leadService.saveLead(leadsEntity);
		return "redirect:/dashboard?leadStatus=1";
	}

	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public String changeStatus(@RequestParam("leadIds") String changeStatusLeadIds,
			@RequestParam("statusType") String statusType, @RequestParam("reason") String reason,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		DashBoardForm dashBoardForm = null;
		if (session.getAttribute("dashBoardForm") != null) {
			dashBoardForm = (DashBoardForm) session.getAttribute("dashBoardForm");
		}
		String[] changeStatusLeadIdsSplitArray = changeStatusLeadIds.split(",");
		List<Integer> changeStatusLeadIdsList = new ArrayList<Integer>();
		for (String leadId : changeStatusLeadIdsSplitArray) {
			changeStatusLeadIdsList.add(Integer.parseInt(leadId));
		}
		String leadsChangeStatus = this.userService.leadsChangeStatus(changeStatusLeadIdsList,
				Integer.parseInt(statusType), reason);
		return "redirect:/dashboard?leadStatus=" + dashBoardForm.getCurrentStatus();
	}

	@RequestMapping(value = "/filterByDateAndCourse", method = RequestMethod.POST)
	public String filterByDateAndCourse(@RequestParam("fromDate") String fromDate,
			@RequestParam("toDate") String toDate, @RequestParam("course") int course,
			@RequestParam("filterType") int filterType, HttpServletRequest request) {
		HttpSession session = request.getSession();
		DashBoardForm dashBoardForm = null;
		if (session.getAttribute("dashBoardForm") != null) {
			dashBoardForm = (DashBoardForm) session.getAttribute("dashBoardForm");
		}
		dashBoardForm.setFromDate(fromDate);
		dashBoardForm.setToDate(toDate);
		dashBoardForm.setCourse(course);
		dashBoardForm.setFilterType(filterType);
		session.setAttribute("dashBoardForm", dashBoardForm);
		return "redirect:/dashboard?leadStatus=" + dashBoardForm.getCurrentStatus() + "&isFromFilter=true";

	}
	
	@RequestMapping(value = "/getPaginationData", method = RequestMethod.POST)
	public String getPaginationData(HttpServletRequest request, @RequestParam("currentPage") int currentPage) {
		HttpSession session = request.getSession();
		DashBoardForm dashBoardForm = (DashBoardForm) session.getAttribute("dashBoardForm");
		dashBoardForm.setPageNumber(currentPage);
		session.setAttribute("dashBoardForm", dashBoardForm);
		return "redirect:/dashboard?leadStatus=" + dashBoardForm.getCurrentStatus();
	}

	@RequestMapping(value = "/getShowingData", method = RequestMethod.POST)
	public String getShowingData(HttpServletRequest request, @RequestParam("pageLimit") int pageLimit) {
		HttpSession session = request.getSession();
		DashBoardForm dashBoardForm = (DashBoardForm) session.getAttribute("dashBoardForm");
		dashBoardForm.setPageNumber(1);
		dashBoardForm.setPageLimit(pageLimit);
		session.setAttribute("dashBoardForm", dashBoardForm);
		return "redirect:/dashboard?leadStatus=" + dashBoardForm.getCurrentStatus();
	}

}
