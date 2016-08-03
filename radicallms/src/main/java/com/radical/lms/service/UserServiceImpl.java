package com.radical.lms.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radical.lms.beans.DashBoardForm;
import com.radical.lms.beans.LeadsEntityBean;
import com.radical.lms.dao.UserDao;
import com.radical.lms.entity.CourseCategeoryEntity;
import com.radical.lms.entity.CourseEntity;
import com.radical.lms.entity.LeadSourcesEntity;
import com.radical.lms.entity.LeadsEntity;
import com.radical.lms.entity.UsersEntity;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;

	private Properties properties = new Properties();

	private Map<Integer, String> courseCategories = new ConcurrentHashMap<Integer, String>();

	private Map<Integer, String> courses = new ConcurrentHashMap<Integer, String>();

	private Map<Integer, Integer> coursesCategeoryMapping = new ConcurrentHashMap<Integer, Integer>();

	private Map<Integer, String> leadSourceMapping = new ConcurrentHashMap<Integer, String>();

	public void init() {
		loadCache();
	}

	@Transactional
	public void loadCache() {
		getAllCourseCategories();
		getAllCourses();
		getLeadSources();
	}

	@Transactional
	public void getLeadSources() {
		List<LeadSourcesEntity> leadSourcesEntityList = userDao.getLeadSources();
		Map<Integer, String> leadSourceMapping = new ConcurrentHashMap<Integer, String>();
		for (LeadSourcesEntity leadSourcesEntity : leadSourcesEntityList) {
			leadSourceMapping.put(leadSourcesEntity.getLeadsourceId(), leadSourcesEntity.getLeadSourceName());
		}
		setLeadSourceMapping(leadSourceMapping);
	}

	@Transactional
	public void getAllCourses() {
		List<CourseEntity> courseList = userDao.getCourses();
		Map<Integer, String> courses = new ConcurrentHashMap<Integer, String>();
		Map<Integer, Integer> coursesCategeoryMapping = new ConcurrentHashMap<Integer, Integer>();
		for (CourseEntity courseEntity : courseList) {
			courses.put(courseEntity.getCourseId(), courseEntity.getCourseName());
			coursesCategeoryMapping.put(courseEntity.getCourseId(), courseEntity.getCategeoryId());
		}
		setCourses(courses);
		setCoursesCategeoryMapping(coursesCategeoryMapping);
	}

	@Transactional
	public void getAllCourseCategories() {
		List<CourseCategeoryEntity> courseCategeories = userDao.getCourseCategories();
		Map<Integer, String> courseCategories = new ConcurrentHashMap<Integer, String>();
		for (CourseCategeoryEntity courseCategeoryEntity : courseCategeories) {
			courseCategories.put(courseCategeoryEntity.getCategoryId(), courseCategeoryEntity.getCategeoryName());
		}
		setCourseCategories(courseCategories);
	}

	@Transactional
	public UsersEntity getUsers(String userId) {
		return userDao.getUsers(userId);
	}

	@Transactional
	public UsersEntity checkLoginDetails(String userName, String passWord) {
		return userDao.checkLoginDetails(userName, passWord);
	}

	@Transactional
	public List getCountByStatusType(DashBoardForm dashBoardForm) {
		return userDao.getCountByStatusType(dashBoardForm);
	}

	@Transactional
	public List<LeadsEntityBean> getLeadsStatus(DashBoardForm dashBoardForm) {
		List<LeadsEntity> leads = userDao.getLeadsByStatus(dashBoardForm);
		if (leads != null) {
			List<LeadsEntityBean> leadBeanList = new ArrayList<LeadsEntityBean>();
			String status = "";
			for (LeadsEntity leadsEntity : leads) {
				if (leadsEntity.getStatus() == 1) {
					status = "New";
				} else if (leadsEntity.getStatus() == 2) {
					status = "Open";
				} else if (leadsEntity.getStatus() == 3) {
					status = "Closed";
				} else if (leadsEntity.getStatus() == 4) {
					status = "Deleted";
				}
				String assgniedTo = "";
				if (leadsEntity.getAssignedTo() == 0) {
					assgniedTo = "";
				} else {
					assgniedTo = userDao.getAssignedToName(leadsEntity.getAssignedTo());
				}
				Date createdDate = leadsEntity.getCreatedDate();
				Date updateDate =leadsEntity.getLastUpdatedDate();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
				String createdDateString = dateFormat.format(createdDate);
				String updateDateString = dateFormat.format(updateDate);
				LeadsEntityBean leadsEntityBean = new LeadsEntityBean(leadsEntity.getLeadiId(),leadsEntity.getName(),leadsEntity.getMobileNo(),leadsEntity.getEmailId(),
						status,getCourses().get(leadsEntity.getCourse()),getCourseCategories().get(leadsEntity.getCourseCategeory()),
						getLeadSourceMapping().get(leadsEntity.getLeadSource()),assgniedTo,createdDateString,updateDateString,
						leadsEntity.getCity(),leadsEntity.getComments(),leadsEntity.getReason(),leadsEntity.getAddress(),leadsEntity.getArea(),
						leadsEntity.getLocation(),leadsEntity.getModeofTraining(),leadsEntity.getTypeofTraining());
				leadBeanList.add(leadsEntityBean);
			}
			return leadBeanList;
		}
		return null;
	}

	public Map<Integer, String> getCourseCategories() {
		return courseCategories;
	}

	public void setCourseCategories(Map<Integer, String> courseCategories) {
		this.courseCategories = courseCategories;
	}

	public Map<Integer, String> getCourses() {
		return courses;
	}

	public void setCourses(Map<Integer, String> courses) {
		this.courses = courses;
	}

	public Map<Integer, Integer> getCoursesCategeoryMapping() {
		return coursesCategeoryMapping;
	}

	public void setCoursesCategeoryMapping(Map<Integer, Integer> coursesCategeoryMapping) {
		this.coursesCategeoryMapping = coursesCategeoryMapping;
	}

	public Map<Integer, String> getLeadSourceMapping() {
		return leadSourceMapping;
	}

	public void setLeadSourceMapping(Map<Integer, String> leadSourceMapping) {
		this.leadSourceMapping = leadSourceMapping;
	}

	@Transactional
	public String leadsChangeStatus(List<Integer> changeStatusLeadIdsList, int statusType, String reason) {
		return userDao.leadsChangeStatus(changeStatusLeadIdsList, statusType, reason);
	}

	public XSSFWorkbook downloadLeadsSheet(List<LeadsEntityBean> leadsEntityBeanList) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Leads");
		sheet.setDefaultColumnWidth(30);
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Arial");
		style.setFillForegroundColor(HSSFColor.BLUE.index);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(HSSFColor.WHITE.index);
		style.setFont(font);
		XSSFRow header = sheet.createRow(0);

		header.createCell(0).setCellValue("Id");
		header.getCell(0).setCellStyle(style);

		header.createCell(1).setCellValue("Lead name");
		header.getCell(1).setCellStyle(style);

		header.createCell(2).setCellValue("MobieNumber");
		header.getCell(2).setCellStyle(style);

		header.createCell(3).setCellValue("Email");
		header.getCell(3).setCellStyle(style);

		header.createCell(4).setCellValue("Status");
		header.getCell(4).setCellStyle(style);

		header.createCell(5).setCellValue("Course Name");
		header.getCell(5).setCellStyle(style);

		header.createCell(6).setCellValue("Categeory Name");
		header.getCell(6).setCellStyle(style);

		header.createCell(7).setCellValue("LeadSource");
		header.getCell(7).setCellStyle(style);

		header.createCell(8).setCellValue("Assigned To");
		header.getCell(8).setCellStyle(style);

		header.createCell(9).setCellValue("Created Date");
		header.getCell(9).setCellStyle(style);
		
		header.createCell(10).setCellValue("Update Date");
		header.getCell(10).setCellStyle(style);

		header.createCell(11).setCellValue("City");
		header.getCell(11).setCellStyle(style);

		header.createCell(12).setCellValue("Comments");
		header.getCell(12).setCellStyle(style);

		header.createCell(13).setCellValue("Reason");
		header.getCell(13).setCellStyle(style);

		header.createCell(14).setCellValue("Address");
		header.getCell(14).setCellStyle(style);

		header.createCell(15).setCellValue("Area");
		header.getCell(15).setCellStyle(style);

		header.createCell(16).setCellValue("Location");
		header.getCell(16).setCellStyle(style);

		header.createCell(17).setCellValue("Mode Of training");
		header.getCell(17).setCellStyle(style);

		header.createCell(18).setCellValue("Weekend/ Weekday");
		header.getCell(18).setCellStyle(style);

		int rowCount = 1;
		
		for (LeadsEntityBean LeadsEntityBean : leadsEntityBeanList) {
			XSSFRow aRow = sheet.createRow(rowCount);
			aRow.createCell(0).setCellValue(LeadsEntityBean.getEnqID());
			aRow.createCell(1).setCellValue(LeadsEntityBean.getName());
			aRow.createCell(2).setCellValue(LeadsEntityBean.getMobileNo());
			aRow.createCell(3).setCellValue(LeadsEntityBean.getEmailId());
			aRow.createCell(4).setCellValue(LeadsEntityBean.getStatus());
			aRow.createCell(5).setCellValue(LeadsEntityBean.getCourse());
			aRow.createCell(6).setCellValue(LeadsEntityBean.getCategeory());
			aRow.createCell(7).setCellValue(LeadsEntityBean.getSourceLead());
			aRow.createCell(8).setCellValue(LeadsEntityBean.getAssignedTo());
			aRow.createCell(9).setCellValue(LeadsEntityBean.getCreatedTime());
			aRow.createCell(10).setCellValue(LeadsEntityBean.getUpdatedTime());
			aRow.createCell(11).setCellValue(LeadsEntityBean.getCity());
			aRow.createCell(12).setCellValue(LeadsEntityBean.getComments());
			aRow.createCell(13).setCellValue(LeadsEntityBean.getReason());
			aRow.createCell(14).setCellValue(LeadsEntityBean.getAddress());
			aRow.createCell(15).setCellValue(LeadsEntityBean.getArea());
			aRow.createCell(16).setCellValue(LeadsEntityBean.getLocation());
			aRow.createCell(17).setCellValue(LeadsEntityBean.getModeOfTraining());
			aRow.createCell(18).setCellValue(LeadsEntityBean.getTypeOfTraining());
			rowCount++;
		}

		return workbook;
	}
}
