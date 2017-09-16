package com.radical.lms.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Hibernate;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.radical.lms.beans.CourseBean;
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
			/*switch (user.getRoleId()) {
			case 1:
				return "redirect:/dashboard?leadStatus=1";
			default:
				return "login";
			}*/
			return "redirect:/dashboard?leadStatus=1";
		}
		return "login";
	}

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String dashBoard(HttpServletRequest request, @RequestParam("leadStatus") int leadStatus, ModelMap map,
			@RequestParam(value = "isFromFilter", defaultValue = "false", required = false) Boolean isFromFilter,
			@RequestParam(value = "isFromPagination", defaultValue = "false", required = false) Boolean isFromPagination,
			@RequestParam(value = "isFromViewMailTemplate", defaultValue = "false", required = false) Boolean isFromViewMailTemplate,
			@RequestParam(value = "messageText", defaultValue = "", required = false) String messageText) {
		HttpSession session = request.getSession();
		UsersEntity user = (UsersEntity) session.getAttribute("userInfo");

		DashBoardForm dashBoardForm = null;
		if (session.getAttribute("dashBoardForm") != null) {
			dashBoardForm = (DashBoardForm) session.getAttribute("dashBoardForm");
		} else {
			dashBoardForm = new DashBoardForm();
			dashBoardForm.setPageNumber(1);
			dashBoardForm.setPageLimit(100);
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
		dashBoardForm.setDeleteCount((int) deletedCount);
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
		limitList.add(100);
		limitList.add(500);
		limitList.add(1000);
		limitList.add(2000);
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
		List<UsersEntity> agentsList = userService.getUsersList();

		map.addAttribute("dashBoardForm", dashBoardForm);
		map.addAttribute("coursesMap", coursesMap);
		map.addAttribute("leadSourceMapping", leadSourceMapping);
		map.addAttribute("courseCategories", courseCategories);
		map.addAttribute("messageText", messageText);
		map.addAttribute("userName", user.getUserName());
		map.addAttribute("agentsList", agentsList);
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
			/*switch (user.getRoleId()) {
			case 1:*/
				return "redirect:/dashboard?leadStatus=1";
			/*}*/
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
		/*MailReadingJob mail = new MailReadingJob();
		//emailService.readMailInbox();
		// MailSendingJob mail = new MailSendingJob();
		mail.executeInternal(null);
		LeadsEntity leadsEntity = new LeadsEntity();
		leadsEntity.setEmailId("chandu.mutta@gmail.com");
		leadsEntity.setCourseCategeory(0);
		
		if (leadsEntity.getEmailId() != null) {
			CourseCategeoryEntity caterogy = null;
			if (leadsEntity.getCourseCategeory() != 0) {
				caterogy = userService.getCategoryByCategoryId(leadsEntity.getCourseCategeory());
			}
			if (caterogy == null) {							
				emailService.sendMailWithAttachementDynamically(leadsEntity.getEmailId(), null, null, null);
			} else {
				emailService.sendMailWithAttachementDynamically(leadsEntity.getEmailId(), caterogy.getSubject(), caterogy.getMailerPath(), null);
			}
		}*/
		return "login";
	}

	@RequestMapping(value = "/addlead", method = RequestMethod.POST)
	public String addLead(@ModelAttribute(value = "addLeadForm") LeadsEntity leadFormEntity, Model model,
			@RequestParam("courseList") List<Integer> courseIdList, @RequestParam("sendingeMailAndSmsType") String sendingeMailAndSmsType,
			@RequestParam("nonTemplatedSms") String sms, @RequestParam("nonTemplatedEmailSubject") String subject,
			@RequestParam("nonTemplatedEmailBody") String mailbody,@RequestParam("nonTemplateFile") MultipartFile attachementFile
			) {
		
		LeadsEntity leadsEntity = null;
		if (courseIdList != null && !courseIdList.isEmpty()) {			
			for (Integer courseId : courseIdList) {
				leadsEntity = new LeadsEntity();
				leadsEntity = leadService.getLeadEntityBean(leadFormEntity, leadsEntity, courseId);
				this.leadService.saveLead(leadsEntity);
			}
		} else {
			leadsEntity = new LeadsEntity();
			leadsEntity = leadService.getLeadEntityBean(leadFormEntity, leadsEntity, 0);
			this.leadService.saveLead(leadsEntity);
		}
		
		if (leadsEntity != null) {
			if ("defaultmailandsms".equals(sendingeMailAndSmsType)) {
				if (leadsEntity.getEmailId() != null) {
					if(leadsEntity.getCourseCategeory()!=0){
						CourseCategeoryEntity category = userService.getCategoryListBasedOnCourseId(leadsEntity.getCourseCategeory());
						CourseEntity courseEntity = userService.getCourseByCourseId(leadsEntity.getCourse());
						emailService.sendMailWithAttachementDynamically(leadsEntity.getEmailId(),category.getSubject(), category.getMailerPath(), courseEntity);
					}
				}
				if (leadsEntity.getMobileNo() != null) {
					userService.sendSms(Constants.SMS_TEMPLATE,
							leadsEntity.getMobileNo());
				}
			} else if ("manualmailandsms".equals(sendingeMailAndSmsType)) {
				if (leadsEntity.getEmailId() != null) {
					if (!attachementFile.isEmpty()) {
					emailService.sendMailWithAttachementManually(leadsEntity.getEmailId(), subject, mailbody, attachementFile);
					} else {
					emailService.sendMail(leadsEntity.getEmailId(), subject, mailbody);
					}
				}
				if (leadsEntity.getMobileNo() != null) {
					userService.sendSms(sms, leadsEntity.getMobileNo());
				}
			}
				
			return "redirect:/dashboard?leadStatus="+leadFormEntity.getStatus()+"&messageText=Lead Added successfully";
		}
		return "redirect:/dashboard?leadStatus="+leadFormEntity.getStatus()+"&messageText=Lead Not Added";
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
		clearDashBoardFilter(dashBoardForm);
		String[] changeStatusLeadIdsSplitArray = changeStatusLeadIds.split(",");
		List<Integer> changeStatusLeadIdsList = new ArrayList<Integer>();
		for (String leadId : changeStatusLeadIdsSplitArray) {
			changeStatusLeadIdsList.add(Integer.parseInt(leadId));
		}
		String leadsChangeStatus = this.userService.leadsChangeStatus(changeStatusLeadIdsList,
				Integer.parseInt(statusType), reason);
		return "redirect:/dashboard?leadStatus=" + dashBoardForm.getCurrentStatus()+"&messageText=Status Changed Successfully";
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
			@RequestParam("filterByTraining") String filterByTraining,
			@RequestParam("filterByStatus") String filterByStatus
			){
		HttpSession session = request.getSession();
		DashBoardForm dashBoardForm = null;
		if (session.getAttribute("dashBoardForm") != null) {
			dashBoardForm = (DashBoardForm) session.getAttribute("dashBoardForm");
		}
		clearDashBoardFilter(dashBoardForm);
		if(null != fromDate && !(fromDate.equalsIgnoreCase(""))){
			dashBoardForm.setFromDate(fromDate);
		}
		if(null != toDate && !(toDate.equalsIgnoreCase(""))){
			dashBoardForm.setToDate(toDate);
		}
		if(course!=0){
			dashBoardForm.setCourse(course);
		} 
		if(category !=0){
			dashBoardForm.setCategory(category);
		}
		int statusFilter = 0;
		if(null != filterByStatus && !(filterByStatus.equalsIgnoreCase(""))) {
			statusFilter = Integer.parseInt(filterByStatus);
		} else {
			statusFilter = dashBoardForm.getCurrentStatus();
		}
		/*int filterTypeByLead= 0;
		if(Integer.parseInt(filterLeadType) == 0){
			filterTypeByLead = dashBoardForm.getCurrentStatus();
		} else {
			filterTypeByLead = Integer.parseInt(filterLeadType);
		}*/
		
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
			return "redirect:/dashboard?leadStatus=" + statusFilter + "&isFromFilter=true";
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
		//clearDashBoardFilter(dashBoardForm);
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
		//clearDashBoardFilter(dashBoardForm);
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
		clearDashBoardFilter(dashBoardForm);
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
		} else if (NumberUtils.isNumber(courseName) && courseName.length() == 10) {
			dashBoardForm.setMobileNumber(courseName);
		} else if (NumberUtils.isNumber(courseName)) {
			dashBoardForm.setLeadId(Integer.parseInt(courseName));
		} else {
			dashBoardForm.setName(courseName);
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
		
		leadsEntity.setLastUpdatedDate(new Date());
		leadsEntity.setReason(lead.getReason());
		if (leadsEntity.getCourse() != lead.getCourse() && leadsEntity.getCourse() != 0) {
			leadsEntity.setLeadiId(0);
			leadsEntity.setCreatedDate(new Date());
		} else {
			leadsEntity.setCreatedDate(lead.getCreatedDate());
		}
		
		leadService.saveLead(leadsEntity);
		
		return "redirect:/dashboard?leadStatus=" + leadsEntity.getStatus()+"&messageText=Lead Updated Successfully";
	}

	@RequestMapping(value = "/downloadLeadsToSheet", method = RequestMethod.POST)
	public String downloadLeadsToSheet(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("leadIds") String downloadLeadIds) {
		HttpSession session = request.getSession();
		DashBoardForm dashBoardForm = null;
		if (session.getAttribute("dashBoardForm") != null) {
			dashBoardForm = (DashBoardForm) session.getAttribute("dashBoardForm");
		}
		clearDashBoardFilter(dashBoardForm);
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
	
	public void clearDashBoardFilter(DashBoardForm dashBoardForm){
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
		dashBoardForm.setName(null);
		dashBoardForm.setLeadId(0);
	}

	@RequestMapping(value = "/clearFilter", method = RequestMethod.GET)
	public String clearFilter(HttpServletRequest request) {
		HttpSession session = request.getSession();
		DashBoardForm dashBoardForm = null;
		if (session.getAttribute("dashBoardForm") != null) {
			dashBoardForm = (DashBoardForm) session.getAttribute("dashBoardForm");
		}
		clearDashBoardFilter(dashBoardForm);
		session.setAttribute("dashBoardForm", dashBoardForm);
		return "redirect:/dashboard?leadStatus=1";
	}
	
	@RequestMapping(value = "/createCourseAttachment", method = RequestMethod.POST)
	public String createCourseAttachment(@RequestParam("courseFile") MultipartFile uploadFile,
			@RequestParam("courseId") int courseId, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		HttpSession session = request.getSession();
		CourseEntity courseEntity = userService.getCourseByCourseId(courseId);
		if (!uploadFile.isEmpty()) {
			try {
				courseEntity.setMailerPath(uploadFile.getOriginalFilename());
				courseEntity.setFileType(uploadFile.getContentType());
				Blob blob = Hibernate.createBlob(uploadFile.getInputStream());
				courseEntity.setContent(blob);
				courseEntity.setCreatedTime(new Date());
				userService.saveCourse(courseEntity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "redirect:/viewCourseAttachements?messageText=Course attachemnt added successfully";
	}


	@RequestMapping(value = "/createMailTemplate", method = RequestMethod.POST)
	public String saveTemplate(@RequestParam("file") MultipartFile uploadFile,
			@RequestParam("categoryId") String categeoryId, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		HttpSession session = request.getSession();
		CourseCategeoryEntity category = userService.getCategoryListBasedOnCourseId(Integer.parseInt(categeoryId));
		if (!uploadFile.isEmpty()) {
			try {
				String name = "";
				String rootPath = System.getProperty(Constants.CATALINA_PATH) + File.separator + "webapps"
						+ File.separator + "CategoryMailer" + File.separator + "resources" + File.separator + "images"
						+ File.separator;
				
				File dir = new File(rootPath);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				name = uploadFile.getOriginalFilename();
				int pos = name.lastIndexOf(".");
				if (pos != -1) {
					name = name.substring(0, pos);
				}
				BufferedImage bufImage = ImageIO.read(new ByteArrayInputStream(uploadFile.getBytes()));
				File serverFile = new File(dir.getAbsolutePath() + File.separator + name + Constants.JPG_IMAGE);
				String imagePath = dir.getAbsolutePath() + File.separator + name+ Constants.JPG_IMAGE;
				ImageIO.write(bufImage, Constants.JPG, serverFile);
				imagePath = imagePath.replace(
						System.getProperty(Constants.CATALINA_PATH) + File.separator + "webapps" + File.separator,
						"http://www.radicaltechnologies.org/").replace(File.separator, "/");
				category.setMailerPath(imagePath);
				/*category.setSubject(categeoryEntity.getSubject());
				category.setMessagebody(categeoryEntity.getMessagebody());*/
				category.setCreatedTime(new Date());

				/*
				 * CourseEntity course =
				 * userService.getCourseListBasedOnCourseId(courseEntity.
				 * getCourseId()); course.setSubject(courseEntity.getSubject());
				 * course.setMessagebody(courseEntity.getMessagebody());
				 * course.setCreatedTime(new Date());
				 */
				userService.saveTemplate(category);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "redirect:/dashboard?leadStatus=1&messageText=Mail Template Created Successfully";
	}

	@RequestMapping(value = "/sendTemplatedMail", method = RequestMethod.POST)
	public String sendTemplatedMail(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("leadIds") String sendTemplateLeadIds, @RequestParam("categeoryId") int category) {
		HttpSession session = request.getSession();
		DashBoardForm dashBoardForm = null;
		if (session.getAttribute("dashBoardForm") != null) {
			dashBoardForm = (DashBoardForm) session.getAttribute("dashBoardForm");
		}
		clearDashBoardFilter(dashBoardForm);
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
						emailService.sendMailWithAttachementDynamically(leadsEntity.getEmailId(), categoryEntity.getSubject(), categoryEntity.getMailerPath(), null);
					}
				}
			}
		}		
		return "redirect:/dashboard?leadStatus=" + dashBoardForm.getCurrentStatus()+"&messageText=Mail Sent Successfully";
	}

	@RequestMapping(value = "/nonTemplatedEmailOrSms", method = RequestMethod.POST)
	public String nonTemplatedEmailOrSms(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("nonTemplatedSms") String sms, @RequestParam("nonTemplatedEmailSubject") String subject,
			@RequestParam("nonTemplatedEmailBody") String mailbody,
			@RequestParam("leadIds") String sendNonTemplateLeadIds, @RequestParam("optradio") int type,@RequestParam("nonTemplateFile") MultipartFile attachementFile) {
		HttpSession session = request.getSession();
		DashBoardForm dashBoardForm = null;
		if (session.getAttribute("dashBoardForm") != null) {
			dashBoardForm = (DashBoardForm) session.getAttribute("dashBoardForm");
		}
		clearDashBoardFilter(dashBoardForm);
		String message = "";
		String[] sendNonTemplateLeadIdsSplitArray = sendNonTemplateLeadIds.split(",");
		for (String leadId : sendNonTemplateLeadIdsSplitArray) {
			LeadsEntity lead = leadService.getLeadByLeadId(Integer.parseInt(leadId));
			if (type == 1) {
				if (lead.getEmailId() != null) {
					if (!attachementFile.isEmpty()) {
						emailService.sendMailWithAttachementManually(lead.getEmailId(), subject, mailbody, attachementFile);
					} else {
						emailService.sendMail(lead.getEmailId(), subject, mailbody);
					}
					message = "&messageText=Mail Sent Successfully";
				}
			} else if (type == 0) {
				if (lead.getMobileNo() != null) {
					userService.sendSms(sms, lead.getMobileNo());
					message = "&messageText=Message Sent Successfully";
				}
			}
		}
		return "redirect:/dashboard?leadStatus=" + dashBoardForm.getCurrentStatus()+message;
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
		return "redirect:/dashboard?leadStatus=1&isFromViewMailTemplate=true&messageText=Mail Template Updated Successfully";
	}
	
	@RequestMapping(value = "/viewAgents", method = RequestMethod.GET)
	public String viewAgents(ModelMap map, @RequestParam(value = "messageText", defaultValue = "", required = false) String messageText) {
		List<UsersEntity> agentsList = userService.getUsersList();
		map.addAttribute("agentsList", agentsList);
		map.addAttribute("message", messageText);
		map.addAttribute("viewPage", "viewagents");
		return "adminactivities";
	}
	
	@RequestMapping(value = "/viewCourseAttachements", method = RequestMethod.GET)
	public String viewCourseAttachements(ModelMap map, @RequestParam(value = "messageText", defaultValue = "", required = false) String messageText) {
		List<CourseEntity> coursesList = userService.getCoursesListForEmailer();
		List<CourseEntity> course = userService.getCoursesList();
		map.addAttribute("course", course);
		map.addAttribute("coursesList", coursesList);
		map.addAttribute("message", messageText);
		map.addAttribute("viewPage", "viewCourseAttachements");
		return "adminactivities";
	}
	
	
	@RequestMapping(value = "/getAgentInfo", method = RequestMethod.POST)
	@ResponseBody
	public UsersEntity getAgentInfoByUserId(@RequestParam("userId") int userId) {
		return userService.getUsers(userId);
	}
	
	@RequestMapping(value = "/addAgent", method = RequestMethod.POST)
	public String addAgent(@ModelAttribute(value = "addAgentForm") UsersEntity userEntity, Model model) {
		UsersEntity user = new UsersEntity();
		user.setUserName(userEntity.getUserName());
		user.setPassword(userEntity.getPassword());
		user.setEmail(userEntity.getEmail());
		user.setCreatedTime(new Date());
		user.setIsActive(1);
		user.setRoleId(2);
		userService.saveOrUpdateUser(user);
		return "redirect:/viewAgents?messageText=Agent added successfully";
	}
	
	@RequestMapping(value = "/editAgent", method = RequestMethod.POST)
	public String editAgentInfo(@ModelAttribute(value = "editAgentForm") UsersEntity userEntity, Model model) {
		UsersEntity user = userService.getUsers(userEntity.getUserId());
		user.setPassword(userEntity.getPassword());
		user.setEmail(userEntity.getEmail());
		user.setLastUpdatedtime(new Date());
		userService.saveOrUpdateUser(user);
		return "redirect:/viewAgents?messageText=Agent updated successfully";
	}
	
	
	@RequestMapping(value = "/isUserExits", method = RequestMethod.POST)
	@ResponseBody
	public String isUserExits(@RequestParam("userName") String name) {
		UsersEntity user = userService.getUserByUserName(name);
		if (user == null) {
			return "no";
		} else {
			return "yes";
		}
	}
	
	@RequestMapping(value = "/updateAgent", method = RequestMethod.POST)
	@ResponseBody
	public String updateAgent(@RequestParam("userId") int userId, @RequestParam("value") int value) {
		UsersEntity user = userService.getUsers(userId);
		user.setIsActive(value);
		user.setLastUpdatedtime(new Date());
		userService.saveOrUpdateUser(user);
		return "success";
	}
	
	@RequestMapping(value = "/viewCategories", method = RequestMethod.GET)
	public String viewCategories(ModelMap map, @RequestParam(value = "messageText", defaultValue = "", required = false) String messageText) {
		List<CourseCategeoryEntity> categoriesList = userService.getCourseCategoriesList();
		map.addAttribute("categoriesList", categoriesList);
		map.addAttribute("viewPage", "viewcategories");
		map.addAttribute("message", messageText);
		return "adminactivities";
	}
	
	@RequestMapping(value = "/updateCategory", method = RequestMethod.POST)
	@ResponseBody
	public String updateCategory(@RequestParam("categoryId") int categoryId, @RequestParam("value") int value) {
		CourseCategeoryEntity category = userService.getCategoryByCategoryId(categoryId);
		category.setIsActive(value);
		category.setUpdatedTime(new Date());
		userService.saveCategory(category);
		userService.getAllCourseCategories();
		return "success";
	}
	
	@RequestMapping(value = "/addCategory", method = RequestMethod.POST)
	public String addCategory(@RequestParam("categoryName") String categoryName) {
		CourseCategeoryEntity category = userService.getCategoryByCategoryName(categoryName);
		if (category == null) {			
			CourseCategeoryEntity categoryEntity = new CourseCategeoryEntity();
			categoryEntity.setCategeoryName(categoryName);
			categoryEntity.setCreatedTime(new Date());
			userService.saveCategory(categoryEntity);
			return "redirect:/viewCategories?messageText=Category added successfully";
		} else {
			return "redirect:/viewCategories?messageText=Category already exists";
		}
	}
	
	@RequestMapping(value = "/viewCourses", method = RequestMethod.GET)
	public String viewCourses(ModelMap map, @RequestParam(value = "messageText", defaultValue = "", required = false) String messageText) {
		List<CourseEntity> courseList = userService.getCoursesList();
		List<CourseBean> courseBeanList = userService.populateCourses(courseList);
		
		Map<Integer, String> courseCategories = userService.getCourseCategories();
		map.addAttribute("courseCategories", courseCategories);
		map.addAttribute("courseList", courseBeanList);
		map.addAttribute("viewPage", "viewcourses");
		map.addAttribute("message", messageText);
		return "adminactivities";
	}
	
	@RequestMapping(value = "/leadsBulkUpload", method = RequestMethod.GET)
	public String viewCourses(ModelMap map) {
		map.addAttribute("viewPage", "leadsBulkUpload");
		//map.addAttribute("templateDownloadFile", "radical.xlx");
		return "adminactivities";
	}
	

	@RequestMapping("/downloadFile/{filePath}")
	public String downloadFile(@PathVariable("filePath") String filePath,
			HttpServletResponse response) {
		try {
			this.userService.downloadXlsFileBasedOnFileName(filePath, response);
		} catch (Exception ex) {
			throw new RuntimeException("Some thing went wrong while download the status of the upload file");
		}
		return null;
	}
	
	@RequestMapping("/downloadCourseFile/{courseId}")
	public String downloadCourseFile(@PathVariable("courseId") int courseId,
			HttpServletResponse response) {
		try {
			this.userService.downloadCourseFile(courseId,response);
			//this.userService.downloadXlsFileBasedOnFileName(filePath, response);
		} catch (Exception ex) {
			throw new RuntimeException("Some thing went wrong while download the status of the upload file");
		}
		return null;
	}
	
	
	@RequestMapping(value = "/uploadBulkLeads", method = RequestMethod.POST)
	public String uploadBulkLeads(@RequestParam("file") MultipartFile uploadFile,
			HttpServletRequest request, HttpServletResponse response, Model model) {
			HttpSession session = request.getSession();
			if (!uploadFile.isEmpty()) {
				try {
					File convFile = new File(uploadFile.getOriginalFilename());
					convFile.createNewFile();
					FileOutputStream fos = new FileOutputStream(convFile);
					fos.write(uploadFile.getBytes());
					fos.close();
					FileInputStream file = new FileInputStream(convFile);
					XSSFWorkbook workbook = new XSSFWorkbook(file);
					XSSFSheet sheet = workbook.getSheetAt(0);
					Iterator<Row> rowIterator = sheet.rowIterator();
					List<String> childIds = new ArrayList<String>();
					int statusColumnIndex = 12;
					userService.processUploadBulkLeadsFile(rowIterator, statusColumnIndex);
					String rootPath = System.getProperty(Constants.CATALINA_PATH);
					File dir = new File(rootPath + File.separator + Constants.TMP);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					File statusFile = new File(
							dir.getAbsolutePath() + File.separator + "Leads" + new Date().getTime() + Constants.XLS);
					FileOutputStream out = new FileOutputStream(statusFile);
					workbook.write(out);
					model.addAttribute(Constants.FILE_PATHS, statusFile.getName());
					out.close();
					file.close();
					model.addAttribute(Constants.MESSAGE, Constants.FILE_PROCESSING_DONE_SUCCESSFULLY);
					model.addAttribute("viewPage", "leadsBulkUpload");
					model.addAttribute("templateDownloadFile", "radical.xlx");
				} catch (Exception ex) {
					throw new RuntimeException(Constants.LEADS_ADDED_FAILED);
				}
			} else {
				model.addAttribute(Constants.MESSAGE, Constants.PLEASE_UPLOAD_THE_FILE);
			}
			return "adminactivities";
		}
	
	@RequestMapping(value = "/updateCourse", method = RequestMethod.POST)
	@ResponseBody
	public String updateCourse(@RequestParam("courseId") int courseId, @RequestParam("value") int value) {
		CourseEntity courseEntity = userService.getCourseByCourseId(courseId);
		courseEntity.setIsActive(value);
		courseEntity.setUpdatedTime(new Date());
		userService.saveCourse(courseEntity);
		userService.getAllCourses();
		return "success";
	}
	
	@RequestMapping(value = "/isCourseExits", method = RequestMethod.POST)
	@ResponseBody
	public String isCourseExits(@RequestParam("courseName") String name) {
		CourseEntity courseEntity = userService.getCourseByCourseName(name);
		if (courseEntity == null) {
			return "no";
		} else {
			return "yes";
		}
	}
	
	@RequestMapping(value = "/addCourse", method = RequestMethod.POST)
	public String addCourse(@RequestParam("courseName") String courseName, @RequestParam("categeoryId") int categoryId) {
			CourseEntity courseEntity = new CourseEntity();
			courseEntity.setCourseName(courseName);
			courseEntity.setCategeoryId(categoryId);
			courseEntity.setCreatedTime(new Date());
			userService.saveCourse(courseEntity);
			return "redirect:/viewCourses?messageText=Course added successfully";
	}
	
	@RequestMapping(value="/viewImages/{categoryId}", method = RequestMethod.GET)
	public String viewImages(Model model,HttpServletRequest request,@PathVariable int categoryId) {
		HttpSession session = request.getSession();
		try {
			CourseCategeoryEntity category = userService.getCategoryListBasedOnCourseId(categoryId);
			model.addAttribute("reportImagesList",category.getMailerPath()/*.replace("www.radicaltechnologies.org", "D://radical-pune/trunk/radicallms/target/tomcat/webapps")*/);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "viewImage";
	} 
}
