package com.radical.lms.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radical.lms.beans.CourseBean;
import com.radical.lms.beans.DashBoardForm;
import com.radical.lms.beans.LeadsEntityBean;
import com.radical.lms.beans.MailTemplateBean;
import com.radical.lms.constants.Constants;
import com.radical.lms.dao.UserDao;
import com.radical.lms.entity.CourseCategeoryEntity;
import com.radical.lms.entity.CourseEntity;
import com.radical.lms.entity.LeadSourcesEntity;
import com.radical.lms.entity.LeadsEntity;
import com.radical.lms.entity.SendEmailEntity;
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

	private Map<String, Integer> categoryNameIdMapping = new ConcurrentHashMap<String, Integer>();

	private Map<String, Integer> courseNameIdMapping = new ConcurrentHashMap<String, Integer>();

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
		Map<String, Integer> courseNameIdMapping = new ConcurrentHashMap<String, Integer>();
		for (CourseEntity courseEntity : courseList) {
			courses.put(courseEntity.getCourseId(), courseEntity.getCourseName());
			courseNameIdMapping.put(courseEntity.getCourseName(), courseEntity.getCourseId());
			coursesCategeoryMapping.put(courseEntity.getCourseId(), courseEntity.getCategeoryId());
		}
		setCourses(courses);
		setCourseNameIdMapping(courseNameIdMapping);
		setCoursesCategeoryMapping(coursesCategeoryMapping);
	}

	@Transactional
	public void getAllCourseCategories() {
		List<CourseCategeoryEntity> courseCategeories = userDao.getCourseCategories();
		Map<Integer, String> courseCategories = new ConcurrentHashMap<Integer, String>();
		Map<String, Integer> courseNameIdMapping = new ConcurrentHashMap<String, Integer>();
		for (CourseCategeoryEntity courseCategeoryEntity : courseCategeories) {
			courseCategories.put(courseCategeoryEntity.getCategoryId(), courseCategeoryEntity.getCategeoryName());
			courseNameIdMapping.put(courseCategeoryEntity.getCategeoryName(), courseCategeoryEntity.getCategoryId());
		}
		setCourseCategories(courseCategories);
		setCategoryNameIdMapping(courseNameIdMapping);
	}
	
	@Transactional
	public void saveOrUpdateUser(UsersEntity usersEntity) {
		userDao.saveOrUpdateUser(usersEntity);
	}

	@Transactional
	public UsersEntity getUsers(int userId) {
		return userDao.getUsers(userId);
	}
	
	@Transactional
	public UsersEntity getUserByUserName(String name) {
		return userDao.getUserByUserName(name);
	}

	@Transactional
	public UsersEntity checkLoginDetails(String userName, String passWord) {
		return userDao.checkLoginDetails(userName, passWord);
	}
	
	@Transactional
	public List<UsersEntity> getUsersList() {
		return userDao.getUsersList();
	}

	@Transactional
	public List getCountByStatusType(DashBoardForm dashBoardForm) {
		return userDao.getCountByStatusType(dashBoardForm);
	}

	@Transactional
	public List<LeadsEntityBean> getLeadsStatus(DashBoardForm dashBoardForm) {
		List<LeadsEntity> leads = userDao.getLeadsByStatus(dashBoardForm);
		List<LeadsEntityBean> leadBeanList = getLeadsEntityBeanByLeadsEntity(leads);
		return leadBeanList;
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

	public Map<String, Integer> getCategoryNameIdMapping() {
		return categoryNameIdMapping;
	}

	public void setCategoryNameIdMapping(Map<String, Integer> categoryNameIdMapping) {
		this.categoryNameIdMapping = categoryNameIdMapping;
	}

	public Map<String, Integer> getCourseNameIdMapping() {
		return courseNameIdMapping;
	}

	public void setCourseNameIdMapping(Map<String, Integer> courseNameIdMapping) {
		this.courseNameIdMapping = courseNameIdMapping;
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
		if (null != leadsEntityBeanList) {
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
		} else {
			XSSFRow aRow = sheet.createRow(rowCount);
			aRow.createCell(0).setCellValue("No Leads");
		}

		return workbook;
	}

	private List<LeadsEntityBean> getLeadsEntityBeanByLeadsEntity(List<LeadsEntity> leads) {
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
				} else if(leadsEntity.getStatus() == 5 ) {
					status = "Hot Lead";
				}
				String assgniedTo = "";
				if (leadsEntity.getAssignedTo() == 0) {
					assgniedTo = "";
				} else {
					assgniedTo = userDao.getAssignedToName(leadsEntity.getAssignedTo());
				}
				Date createdDate = leadsEntity.getCreatedDate();
				Date updateDate = leadsEntity.getLastUpdatedDate();
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy / HH:mm a");
				String createdDateString = dateFormat.format(createdDate);
				String updateDateString = "";
				if (updateDate != null) {
					updateDateString = dateFormat.format(updateDate);
				}
				String courseName="";
				if(leadsEntity.getCourse()==0){
					courseName = leadsEntity.getCourseName();
				} else {
					courseName = getCourses().get(leadsEntity.getCourse());
				}

				LeadsEntityBean leadsEntityBean = new LeadsEntityBean(leadsEntity.getLeadiId(), leadsEntity.getName(),
						leadsEntity.getMobileNo(), leadsEntity.getEmailId(), status,
						courseName,
						getCourseCategories().get(leadsEntity.getCourseCategeory()),
						getLeadSourceMapping().get(leadsEntity.getLeadSource()), assgniedTo, createdDateString,
						updateDateString, leadsEntity.getCity(), leadsEntity.getComments(), leadsEntity.getReason(),
						leadsEntity.getAddress(), leadsEntity.getArea(), leadsEntity.getLocation(),
						leadsEntity.getModeofTraining(), leadsEntity.getTypeofTraining());
				leadsEntityBean.setLables(leadsEntity.getLabels());
				leadBeanList.add(leadsEntityBean);
			}
			return leadBeanList;
		}
		return null;
	}

	@Transactional
	public List<LeadsEntityBean> getLeadsListForDownload(List<Integer> downloadLeadIdsList) {
		List<LeadsEntity> leads = userDao.getLeadsListForDownload(downloadLeadIdsList);
		List<LeadsEntityBean> leadBeanList = getLeadsEntityBeanByLeadsEntity(leads);
		return leadBeanList;
	}

	@Transactional
	public List<CourseEntity> getCourseList(int categoryId) {
		return userDao.getCourseList(categoryId);
	}

	@Transactional
	public CourseEntity getCourseListBasedOnCourseId(int courseId) {
		return userDao.getCourseListBasedOnCourseId(courseId);
	}
	
	
	@Transactional
	public CourseCategeoryEntity getCategoryListBasedOnCourseId(int categoryId) {
		return userDao.getCategoryListBasedOnCourseId(categoryId);
	}

	@Transactional
	public void saveTemplate(CourseCategeoryEntity categoryEntiry) {
		userDao.saveTemplate(categoryEntiry);
	}
	
	@Transactional
	public void sendTemplatedEmail(SendEmailEntity sendEmailEntity) {
		userDao.sendTemplatedEmail(sendEmailEntity);
	}
	
	@Transactional
	public List<MailTemplateBean> getMailTemplateList(DashBoardForm dashBoardForm) {
		List<MailTemplateBean> templateList = new ArrayList<MailTemplateBean>();
		List<CourseCategeoryEntity> categoryList = userDao.getCategoryList(dashBoardForm, true);
		if (categoryList == null) {
			return templateList;
		}
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		for (CourseCategeoryEntity categoryEntity : categoryList) {
			MailTemplateBean templateBean = new MailTemplateBean();
			/*templateBean.setCourseId(courseEntity.getCourseId());
			templateBean.setCourseName(courseEntity.getCourseName());*/
			templateBean.setCategoryId(categoryEntity.getCategoryId());
			templateBean.setCategoryName(getCourseCategories().get(categoryEntity.getCategoryId()));
			templateBean.setMailSubject(categoryEntity.getSubject());
			templateBean.setMailBody(categoryEntity.getMessagebody());
			if (categoryEntity.getCreatedTime() != null) {
				templateBean.setCreatedTime(dateFormat.format(categoryEntity.getCreatedTime()));
			}
			templateList.add(templateBean);
		}
		return templateList;
	}
	
	@Transactional
	public List<CourseCategeoryEntity> getCategoryList(DashBoardForm dashBoardForm) {
		List<CourseCategeoryEntity> categoryList = userDao.getCategoryList(dashBoardForm, false);
		return categoryList;
	}
	
	
	
	@Transactional
	public void sendSms(String sms, String mobileNumber) {
		try {
			String sendSms = URLEncoder.encode(sms, "UTF-8");
			String url = "http://sms.xpresssms.in/api/api.php?ver=1&mode=1&action=push_sms&type=1&route=2&login_name=radtec&api_password=e51354d757f40f75d8d6&message="
					+ sendSms + "&number=" + mobileNumber + "&sender=RadTec";
			System.out.println(url);
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			System.out.println("Response Code"+response);
			in.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Transactional
	public int getTemplatesCount(DashBoardForm dashBoardForm) {
		return userDao.getTemplatesCount(dashBoardForm);
	}
	
	@Transactional
	public List<CourseCategeoryEntity>  getCourseCategoriesList() {		
		return userDao.getCourseCategoriesList();
	}
	
	@Transactional
	public void saveCategory(CourseCategeoryEntity categoryEntity) {
		userDao.saveCategory(categoryEntity);
	}
	
	@Transactional
	public CourseCategeoryEntity getCategoryByCategoryId(int categoryId) {
		return userDao.getCategoryByCategoryId(categoryId);
	}
	
	@Transactional
	public CourseCategeoryEntity getCategoryByCategoryName(String categoryName) {
		return userDao.getCategoryByCategoryName(categoryName);
	}
	
	@Transactional
	public List<CourseEntity> getCoursesList() {
		return userDao.getCoursesList();
	}
	
	@Transactional
	public void saveCourse(CourseEntity courseEntity) {
		userDao.saveCourse(courseEntity);
	}
	
	@Transactional
	public CourseEntity getCourseByCourseId(int courseId) {
		return userDao.getCourseByCourseId(courseId);
	}
	
	@Transactional
	public CourseEntity getCourseByCourseName(String courseName) {
		return userDao.getCourseByCourseName(courseName);
	}
	
	@Transactional
	public List<CourseBean> populateCourses(List<CourseEntity> coursesList) {
		List<CourseBean> courseBeanList = new ArrayList<CourseBean>();
		for (CourseEntity courseEntity : coursesList) {
			CourseBean courseBean = new CourseBean();
			courseBean.setCourseId(courseEntity.getCourseId());
			courseBean.setCourseName(courseEntity.getCourseName());
			courseBean.setCategeoryName(courseCategories.get(courseEntity.getCategeoryId()));
			courseBean.setIsActive(courseEntity.getIsActive());
			courseBeanList.add(courseBean);
		}
		return courseBeanList;
	}
}
