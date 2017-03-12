package com.radical.lms.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.radical.lms.beans.DashBoardForm;
import com.radical.lms.beans.LeadsEntityBean;
import com.radical.lms.beans.MailTemplateBean;
import com.radical.lms.constants.Constants;
import com.radical.lms.entity.CourseCategeoryEntity;
import com.radical.lms.entity.CourseEntity;
import com.radical.lms.entity.LeadsEntity;
import com.radical.lms.entity.SendEmailEntity;
import com.radical.lms.entity.UsersEntity;
import com.radical.lms.quartz.MailReadingJob;
import com.radical.lms.quartz.MailSendingJob;
import com.radical.lms.service.EmailService;
import com.radical.lms.service.LeadService;
import com.radical.lms.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private LeadService leadService;

	@Autowired
	private EmailService emailService;

	private boolean getData = false;

	/*public void setUserService(UserService userService) {
		this.userService = userService;
	}*/

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
			@RequestParam(value = "isFromFilter", defaultValue = "false", required = false) Boolean isFromFilter,
			@RequestParam(value = "isFromPagination", defaultValue = "false", required = false) Boolean isFromPagination,
			@RequestParam(value = "isFromViewMailTemplate", defaultValue = "false", required = false) Boolean isFromViewMailTemplate) {
		HttpSession session = request.getSession();

		DashBoardForm dashBoardForm = null;
		if (session.getAttribute("dashBoardForm") != null) {
			dashBoardForm = (DashBoardForm) session.getAttribute("dashBoardForm");
		} else {
			dashBoardForm = new DashBoardForm();
			dashBoardForm.setPageNumber(1);
			dashBoardForm.setPageLimit(20);
		}

		if (!isFromPagination) {
			dashBoardForm.setPageNumber(1);
		}

		List countList = this.userService.getCountByStatusType(dashBoardForm);
		Map<Integer, Integer> countMap = new ConcurrentHashMap<Integer, Integer>();
		countMap.put(1, 0);
		countMap.put(2, 0);
		countMap.put(3, 0);
		countMap.put(4, 0);
		countMap.put(5, 0);
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
		long hotCount = countMap.get(5).intValue();
		int totalCount = (int) newCount + (int) openCount + (int) closeCount + (int) deletedCount + (int) hotCount;

		dashBoardForm.setNewCount((int) newCount);
		dashBoardForm.setOpenCount((int) openCount);
		dashBoardForm.setClosedCount((int) closeCount);
		dashBoardForm.setHotCount((int) hotCount); 

		dashBoardForm.setCurrentStatus(leadStatus);

		int pageTotalCount = 0;

		if (isFromViewMailTemplate) {
			pageTotalCount = userService.getTemplatesCount(dashBoardForm);
		} else {
			if (leadStatus == 0) {
				pageTotalCount = totalCount;
			} else {
				pageTotalCount = (int) countMap.get(leadStatus).intValue();
			}
		}

		dashBoardForm.setPageTotalCount(pageTotalCount);
		dashBoardForm.setTotalLeadsCount(totalCount);

		int totalPages = (pageTotalCount / dashBoardForm.getPageLimit());
		if (pageTotalCount % dashBoardForm.getPageLimit() > 0) {
			totalPages += 1;
		}
		dashBoardForm.setTotalPages(totalPages);
		
		
		int statLimit = ((dashBoardForm.getPageNumber() - 1) * dashBoardForm.getPageLimit()) + 1;
		int endLimit = dashBoardForm.getPageLimit() * dashBoardForm.getPageNumber();
		dashBoardForm.setStartLimit(statLimit);
		if (pageTotalCount > endLimit) {
			dashBoardForm.setEndLimit(endLimit);
		} else {
			dashBoardForm.setEndLimit(pageTotalCount);
		}

		List<Integer> limitList = new ArrayList<Integer>();
		limitList.add(20);
		limitList.add(40);
		limitList.add(60);
		limitList.add(80);
		limitList.add(100);
		dashBoardForm.setLimitList(limitList);

		if (isFromViewMailTemplate) {
			List<MailTemplateBean> templateList = userService.getMailTemplateList(dashBoardForm);
			map.addAttribute("templateList", templateList);
			dashBoardForm.setViewPage("viewMailTemplate");
		} else {
			List<LeadsEntityBean> leadsList = this.userService.getLeadsStatus(dashBoardForm);
			map.addAttribute("leadsList", leadsList);
			dashBoardForm.setViewPage("viewLeads");
		}

		Map<Integer, String> leadSourceMapping = this.userService.getLeadSourceMapping();
		Map<Integer, String> courseCategories = userService.getCourseCategories();
		Map<Integer, String> coursesMap = this.userService.getCourses();
		/*List<CourseCategeoryEntity> courseTemplates = userService.getCategoryList(dashBoardForm);
		map.addAttribute("courseTemplates", courseTemplates);*/

		map.addAttribute("dashBoardForm", dashBoardForm);
		map.addAttribute("coursesMap", coursesMap);
		map.addAttribute("leadSourceMapping", leadSourceMapping);
		map.addAttribute("courseCategories", courseCategories);
		session.setAttribute("dashBoardForm", dashBoardForm);

		return "dashboard";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam("userName") String userName, @RequestParam("password") String password,
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

	@RequestMapping(value = "/loginpage", method = RequestMethod.GET)
	public String loginpage() {
		return "login";
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
		/*MailReadingJob mail = new MailReadingJob();*/
		emailService.readMailInbox();
		// MailSendingJob mail = new MailSendingJob();
		/*mail.executeInternal(null);*/
		return "login";
	}

	@RequestMapping(value = "/addlead", method = RequestMethod.POST)
	public String addLead(@ModelAttribute(value = "addLeadForm") LeadsEntity leadFormEntity, Model model,
			@RequestParam("courseList") List<Integer> courseIdList) {
		
		LeadsEntity leadsEntity = null;
		for (Integer courseId : courseIdList) {
			leadsEntity = new LeadsEntity();
			leadsEntity.setName(leadFormEntity.getName());
			leadsEntity.setMobileNo(leadFormEntity.getMobileNo());
			leadsEntity.setEmailId(leadFormEntity.getEmailId());
			leadsEntity.setLeadSource(leadFormEntity.getLeadSource());
			leadsEntity.setComments(leadFormEntity.getComments());
			leadsEntity.setAddress(leadFormEntity.getAddress());
			leadsEntity.setArea(leadFormEntity.getArea());
			leadsEntity.setCity(leadFormEntity.getCity());
			leadsEntity.setLocation(leadFormEntity.getLocation());
			leadsEntity.setAssignedTo(leadFormEntity.getAssignedTo());
			leadsEntity.setModeofTraining(leadFormEntity.getModeofTraining());
			leadsEntity.setTypeofTraining(leadFormEntity.getTypeofTraining());
			leadsEntity.setLabels(leadFormEntity.getLabels());
			/* int courseId = Integer.parseInt(course); */
			int courseCategeory = this.userService.getCoursesCategeoryMapping().get(courseId);
			// int courseCategeory =
			// this.userService.getCoursesCategeoryMapping().get(leadsEntity.getCourse());
			leadsEntity.setCourse(courseId);
			leadsEntity.setStatus(1);
			leadsEntity.setCourseCategeory(courseCategeory);
			leadsEntity.setCreatedDate(new Date());
			leadsEntity.setLastUpdatedDate(new Date());
			this.leadService.saveLead(leadsEntity);
		}
		
		if (leadsEntity != null) {
			if (leadsEntity.getEmailId() != null) {
				if(leadsEntity.getCourseCategeory()!=0){
					CourseCategeoryEntity category = userService.getCategoryListBasedOnCourseId(leadsEntity.getCourseCategeory());
					emailService.sendMail(leadsEntity.getEmailId(),category.getSubject(),category.getMessagebody());
				} else {
				emailService.sendMail(leadsEntity.getEmailId(), Constants.MAIL_SUBJECT,null);
				}
			}
			if (leadsEntity.getMobileNo() != null) {
				userService.sendSms(Constants.SMS_TEMPLATE,
						leadsEntity.getMobileNo());
			}
		}
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
	public String filterByDateAndCourse(HttpServletRequest request,HttpServletResponse response,@RequestParam("fromDate") String fromDate,
			@RequestParam("toDate") String toDate, @RequestParam("courseCategeory") int category,
			@RequestParam("course") int course, @RequestParam("filterType") int filterType,
			@RequestParam("modeofTraining") String modeofTraining,
			@RequestParam("locationCenter") String locationCenter,
			@RequestParam("assignedToByFilter") String assignedToByFilter,
			@RequestParam("leadSource") String leadSource,
			@RequestParam("labelByFilter") String labelByFilter,
			@RequestParam("filterByTraining") String filterByTraining
			){
		HttpSession session = request.getSession();
		DashBoardForm dashBoardForm = null;
		if (session.getAttribute("dashBoardForm") != null) {
			dashBoardForm = (DashBoardForm) session.getAttribute("dashBoardForm");
		}
		dashBoardForm.setFromDate(fromDate);
		dashBoardForm.setToDate(toDate);
		if(course!=0){
			dashBoardForm.setCourse(course);
		} 
		if(category !=0){
			dashBoardForm.setCategory(category);
		}
		dashBoardForm.setFilterType(filterType);
		if(null != labelByFilter  && !(labelByFilter.equalsIgnoreCase(""))){
			dashBoardForm.setLabels(labelByFilter);
		}
		if(null != filterByTraining  && !(filterByTraining.equalsIgnoreCase(""))){
			dashBoardForm.setTypeOfTraining(filterByTraining);
		}
		if(null != leadSource  && !(leadSource.equalsIgnoreCase(""))){
			int leadSourceType = Integer.parseInt(leadSource);
			dashBoardForm.setLeadSource(leadSourceType);
		}
		if(null != modeofTraining  && !(modeofTraining.equalsIgnoreCase(""))){
			dashBoardForm.setModeOfTraining(modeofTraining);
		}
		if(null != locationCenter  && !(locationCenter.equalsIgnoreCase(""))){
			dashBoardForm.setLocation(locationCenter);
		}
		if(null != assignedToByFilter  && !(assignedToByFilter.equalsIgnoreCase(""))){
			int assigned = Integer.parseInt(assignedToByFilter);
			dashBoardForm.setAssignedTo(assigned);
		}
		session.setAttribute("dashBoardForm", dashBoardForm);
		if (filterType == 0) {
			return "redirect:/dashboard?leadStatus=" + dashBoardForm.getCurrentStatus() + "&isFromFilter=true";
		} else if (filterType == 1) {
			List<LeadsEntityBean> leadsList = this.userService.getLeadsStatus(dashBoardForm);
			XSSFWorkbook workbook = this.userService.downloadLeadsSheet(leadsList);
			try {
				String fileName = "leadsdump-" + new Date().getTime() + ".xlsx";
				ServletOutputStream out = response.getOutputStream();
				response.setContentType("application/vnd.ms-excel");
				response.addHeader("content-disposition", "attachment; filename=" + fileName);
				workbook.write(out);
				out.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return null;

	}

	@RequestMapping(value = "/getPaginationData", method = RequestMethod.POST)
	public String getPaginationData(HttpServletRequest request, @RequestParam("currentPage") int currentPage,
			@RequestParam(value = "isFromViewMailTemplate", defaultValue = "false", required = false) Boolean isFromViewMailTemplate) {
		HttpSession session = request.getSession();
		DashBoardForm dashBoardForm = (DashBoardForm) session.getAttribute("dashBoardForm");
		dashBoardForm.setPageNumber(currentPage);
		session.setAttribute("dashBoardForm", dashBoardForm);
		if (isFromViewMailTemplate) {
			return "redirect:/dashboard?leadStatus=1&isFromViewMailTemplate=true&isFromPagination=true";
		} else {
			return "redirect:/dashboard?leadStatus=" + dashBoardForm.getCurrentStatus() + "&isFromPagination=true";
		}
	}

	@RequestMapping(value = "/getShowingData", method = RequestMethod.POST)
	public String getShowingData(HttpServletRequest request, @RequestParam("pageLimit") int pageLimit,
			@RequestParam(value = "isFromViewMailTemplate", defaultValue = "false", required = false) Boolean isFromViewMailTemplate) {
		HttpSession session = request.getSession();
		DashBoardForm dashBoardForm = (DashBoardForm) session.getAttribute("dashBoardForm");
		dashBoardForm.setPageNumber(1);
		dashBoardForm.setPageLimit(pageLimit);
		session.setAttribute("dashBoardForm", dashBoardForm);
		if (isFromViewMailTemplate) {
			return "redirect:/dashboard?leadStatus=1&isFromViewMailTemplate=true";
		} else {
			return "redirect:/dashboard?leadStatus=" + dashBoardForm.getCurrentStatus();
		}
	}

	@RequestMapping(value = "/searchByCourse", method = RequestMethod.POST)
	public String searchByCourse(HttpServletRequest request, @RequestParam("course") String courseName) {
		HttpSession session = request.getSession();
		DashBoardForm dashBoardForm = (DashBoardForm) session.getAttribute("dashBoardForm");
		dashBoardForm.setSearchData(courseName);
		dashBoardForm.setCourse(0);
		dashBoardForm.setCategory(0);
		dashBoardForm.setMobileNumber(null);
		dashBoardForm.setEmail(null);

		if (userService.getCategoryNameIdMapping().containsKey(courseName)) {
			dashBoardForm.setCategory(userService.getCategoryNameIdMapping().get(courseName));
		} else if (userService.getCourseNameIdMapping().containsKey(courseName)) {
			dashBoardForm.setCourse(userService.getCourseNameIdMapping().get(courseName));
		} else if (courseName.contains("@")) {
			dashBoardForm.setEmail(courseName);
		} else if (NumberUtils.isNumber(courseName)) {
			dashBoardForm.setMobileNumber(courseName);
		}

		session.setAttribute("dashBoardForm", dashBoardForm);
		return "redirect:/dashboard?leadStatus=0";// + dashBoardForm.getCurrentStatus();
	}

	@RequestMapping(value = "/getLeadInfo", method = RequestMethod.POST)
	@ResponseBody
	public LeadsEntity getLeadInfoByLeadId(@RequestParam("leadId") int leadId) {
		return leadService.getLeadByLeadId(leadId);
	}

	@RequestMapping(value = "/editlead", method = RequestMethod.POST)
	public String editLead(@ModelAttribute(value = "editLeadForm") LeadsEntity leadsEntity, Model model) {
		LeadsEntity lead = leadService.getLeadByLeadId(leadsEntity.getLeadiId());
		leadsEntity.setCreatedDate(lead.getCreatedDate());
		leadsEntity.setLastUpdatedDate(new Date());
		leadsEntity.setReason(lead.getReason());
		leadService.saveLead(leadsEntity);
		return "redirect:/dashboard?leadStatus=" + leadsEntity.getStatus();
	}

	@RequestMapping(value = "/downloadLeadsToSheet", method = RequestMethod.POST)
	public String downloadLeadsToSheet(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("leadIds") String downloadLeadIds) {
		HttpSession session = request.getSession();
		DashBoardForm dashBoardForm = null;
		if (session.getAttribute("dashBoardForm") != null) {
			dashBoardForm = (DashBoardForm) session.getAttribute("dashBoardForm");
		}

		String[] downloadLeadIdsSplitArray = downloadLeadIds.split(",");
		List<Integer> downloadLeadIdsList = new ArrayList<Integer>();
		for (String leadId : downloadLeadIdsSplitArray) {
			downloadLeadIdsList.add(Integer.parseInt(leadId));
		}
		List<LeadsEntityBean> leadsList = this.userService.getLeadsListForDownload(downloadLeadIdsList);
		XSSFWorkbook workbook = this.userService.downloadLeadsSheet(leadsList);
		try {
			String fileName = "leadsdump-" + new Date().getTime() + ".xlsx";
			ServletOutputStream out = response.getOutputStream();
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("content-disposition", "attachment; filename=" + fileName);
			workbook.write(out);
			out.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/getCoursesBasedOnCategoryId", method = RequestMethod.POST)
	@ResponseBody
	public List<CourseEntity> getCourseBasedOnCategoryId(@RequestParam String categeoryId) {
		int intCategoryId = Integer.parseInt(categeoryId);
		List<CourseEntity> courseList = this.userService.getCourseList(intCategoryId);
		return courseList;
	}

	@RequestMapping(value = "/clearFilter", method = RequestMethod.GET)
	public String clearFilter(HttpServletRequest request) {
		HttpSession session = request.getSession();
		DashBoardForm dashBoardForm = null;
		if (session.getAttribute("dashBoardForm") != null) {
			dashBoardForm = (DashBoardForm) session.getAttribute("dashBoardForm");
		}
		dashBoardForm.setFromDate("");
		dashBoardForm.setToDate("");
		dashBoardForm.setCourse(0);
		dashBoardForm.setCategory(0);
		dashBoardForm.setSearchData("");
		dashBoardForm.setMobileNumber(null);
		dashBoardForm.setEmail(null);
		dashBoardForm.setModeOfTraining(null);
		dashBoardForm.setTypeOfTraining(null);
		dashBoardForm.setLabels(null);
		dashBoardForm.setLocation(null);
		dashBoardForm.setAssignedTo(0);
		dashBoardForm.setLeadSource(0);
		
		session.setAttribute("dashBoardForm", dashBoardForm);
		return "redirect:/dashboard?leadStatus=1";
	}

	@RequestMapping(value = "/createMailTemplate", method = RequestMethod.POST)
	public String saveTemplate(@ModelAttribute(value = "createMailTemplateForm") CourseCategeoryEntity categeoryEntity,
			Model model) {
		CourseCategeoryEntity category = userService.getCategoryListBasedOnCourseId(categeoryEntity.getCategoryId());
		category.setSubject(categeoryEntity.getSubject());
		category.setMessagebody(categeoryEntity.getMessagebody());
		category.setCreatedTime(new Date());
		
		/*CourseEntity course = userService.getCourseListBasedOnCourseId(courseEntity.getCourseId());
		course.setSubject(courseEntity.getSubject());
		course.setMessagebody(courseEntity.getMessagebody());
		course.setCreatedTime(new Date());*/
		userService.saveTemplate(category);
		return "redirect:/dashboard?leadStatus=1";
	}

	@RequestMapping(value = "/sendTemplatedMail", method = RequestMethod.POST)
	public String sendTemplatedMail(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("leadIds") String sendTemplateLeadIds, @RequestParam("categeoryId") int category) {
		HttpSession session = request.getSession();
		DashBoardForm dashBoardForm = null;
		if (session.getAttribute("dashBoardForm") != null) {
			dashBoardForm = (DashBoardForm) session.getAttribute("dashBoardForm");
		}
		/*SendEmailEntity sendEmailEntity = new SendEmailEntity();
		String[] sendTemplateLeadIdsSplitArray = sendTemplateLeadIds.split(",");
		for (String leadId : sendTemplateLeadIdsSplitArray) {
			LeadsEntity lead = leadService.getLeadByLeadId(Integer.parseInt(leadId));
			sendEmailEntity.setCourseId(course);
			sendEmailEntity.setReceiverMailId(lead.getEmailId());
			sendEmailEntity.setStatus(0);
			sendEmailEntity.setCreatedTime(new Date());
			userService.sendTemplatedEmail(sendEmailEntity);
		}*/
		String[] sendTemplateLeadIdsSplitArray = sendTemplateLeadIds.split(",");
		for (String leadId : sendTemplateLeadIdsSplitArray) {
			LeadsEntity leadsEntity = leadService.getLeadByLeadId(Integer.parseInt(leadId));
			
			if (leadsEntity != null) {
				if (leadsEntity.getEmailId() != null) {
					if(leadsEntity.getCourseCategeory()!=0){
						CourseCategeoryEntity categoryEntity = userService.getCategoryListBasedOnCourseId(leadsEntity.getCourseCategeory());
						emailService.sendMail(leadsEntity.getEmailId(), categoryEntity.getSubject(), categoryEntity.getMessagebody());
					} else {
						emailService.sendMail(leadsEntity.getEmailId(), Constants.MAIL_SUBJECT,null);
					}
				}
			}
		}
		
		
		return "redirect:/dashboard?leadStatus=" + dashBoardForm.getCurrentStatus();
	}

	@RequestMapping(value = "/nonTemplatedEmailOrSms", method = RequestMethod.POST)
	public String nonTemplatedEmailOrSms(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("nonTemplatedSms") String sms, @RequestParam("nonTemplatedEmailSubject") String subject,
			@RequestParam("nonTemplatedEmailBody") String mailbody,
			@RequestParam("leadIds") String sendNonTemplateLeadIds, @RequestParam("optradio") int type) {
		HttpSession session = request.getSession();
		DashBoardForm dashBoardForm = null;
		if (session.getAttribute("dashBoardForm") != null) {
			dashBoardForm = (DashBoardForm) session.getAttribute("dashBoardForm");
		}
		String[] sendNonTemplateLeadIdsSplitArray = sendNonTemplateLeadIds.split(",");
		for (String leadId : sendNonTemplateLeadIdsSplitArray) {
			LeadsEntity lead = leadService.getLeadByLeadId(Integer.parseInt(leadId));
			if (type == 1) {
				if (lead.getEmailId() != null) {
					emailService.sendMail(lead.getEmailId(), subject, mailbody);
				}
			} else if (type == 0) {
				if (lead.getMobileNo() != null) {
					userService.sendSms(sms, lead.getMobileNo());
				}
			}
		}
		return "redirect:/dashboard?leadStatus=" + dashBoardForm.getCurrentStatus();
	}

	@RequestMapping(value = "/viewTemplatedMail", method = RequestMethod.GET)
	public String viewTemplatedMail() {
		return "redirect:/dashboard?leadStatus=1&isFromViewMailTemplate=true";
	}

	@RequestMapping(value = "/editMailTemplate", method = RequestMethod.POST)
	public String editTemplate(@ModelAttribute(value = "editMailTemplateForm")CourseCategeoryEntity categeoryEntity,
			Model model) {
		CourseCategeoryEntity category = userService.getCategoryListBasedOnCourseId(categeoryEntity.getCategoryId());
		category.setSubject(categeoryEntity.getSubject());
		category.setMessagebody(categeoryEntity.getMessagebody());
		category.setCreatedTime(new Date());
		userService.saveTemplate(category);
		/*CourseEntity course = userService.getCourseListBasedOnCourseId(courseEntity.getCourseId());
		course.setSubject(courseEntity.getSubject());
		course.setMessagebody(courseEntity.getMessagebody());
		course.setCreatedTime(new Date());
		userService.saveTemplate(course);*/
		return "redirect:/dashboard?leadStatus=1&isFromViewMailTemplate=true";
	}

}
