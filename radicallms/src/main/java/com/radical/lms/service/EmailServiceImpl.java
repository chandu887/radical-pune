package com.radical.lms.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.radical.lms.constants.Constants;
import com.radical.lms.dao.EmailDao;
import com.radical.lms.entity.CourseCategeoryEntity;
import com.radical.lms.entity.EmailTimeEntity;
import com.radical.lms.entity.LeadsEntity;
import com.radical.lms.entity.SendEmailEntity;
import com.radical.lms.thread.EmailThread;

public class EmailServiceImpl implements EmailService {

	final static Logger logger = Logger.getLogger(EmailServiceImpl.class);
	@Autowired
	private LeadService leadService;

	@Autowired
	private EmailDao emailDao;

	@Autowired
	private UserService userService;

	Session session = null;

	Store store = null;

	private Properties properties = new Properties();

	@PostConstruct
	public void init() {
		setEmailSession();
		setMailStore();
		new Thread(new EmailThread()).start();
	}

	private void setEmailSession() {
		try {
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(Constants.LMS_PROP));
			String userid = properties.getProperty(Constants.PROPERTIES_MAILID);
			String password = properties.getProperty(Constants.PROPERTIES_PASSWORD);
			String host = properties.getProperty("host");
			String port = properties.getProperty("port");
			Properties props = System.getProperties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host);
			props.setProperty("mail.transport.protocol", "smtps");
			props.put("mail.smtp.user", userid);
			props.put("mail.smtp.password", password);
			props.put("mail.smtp.port", port);
			props.put("mail.smtps.auth", "true");
			session = Session.getDefaultInstance(props, null);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void setMailStore() {
		String userid = properties.getProperty(Constants.PROPERTIES_MAILID);
		String password = properties.getProperty(Constants.PROPERTIES_PASSWORD);

		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getInstance(props, null);
		try {
			Store store = session.getStore();
			store.connect("imap.gmail.com", userid, password);
			setStore(store);
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public void readMailInbox() {
		Folder inbox;
		long firstEmailTimeInMillis = 0;
		long lastEmailTimeInMillis = emailDao.getLastEmailTimeInMillis();
		LeadsEntity leadsEntity = null;
		try {
			inbox = getStore().getFolder("INBOX");
			inbox.open(Folder.READ_WRITE);

			Message[] messages = inbox.getMessages(1, inbox.getMessageCount());

			for (int i = messages.length - 1; i > 0; i--) {
				try {
					Message message = messages[i];
					Address[] addressArray = message.getFrom();
					Date emailReceivedDate = message.getReceivedDate();
					if (firstEmailTimeInMillis == 0) {
						firstEmailTimeInMillis = emailReceivedDate.getTime();
					}
					if (lastEmailTimeInMillis >= emailReceivedDate.getTime()) {
						break;
					}
					String mailsubject = message.getSubject();
					String mailContent = "";
					String mailId = addressArray[0].toString();
					Object content = message.getContent();
					if (mailsubject.contains("Radical Technologies Bangalore - Course Inquiry Details")) {
						Multipart multiPart = (Multipart) content;
						BodyPart bodyPart = multiPart.getBodyPart(0);
						mailContent = (String) bodyPart.getContent();
						leadsEntity = processWebsiteEnquiryContent(mailContent, emailReceivedDate);
					} else if (mailId.contains("blrfeedback@justdial.com") || mailId.contains("care@yet5.com")
							|| mailId.contains("ypleads@sulekhanotifications.com")) {
						if (content instanceof String) {
							mailContent = (String) content;
							Document doc = Jsoup.parse(mailContent);
							mailContent = doc.text();
							if (mailId.contains("care@yet5.com")) {
								leadsEntity = processYet5MailContentHTML(mailContent, emailReceivedDate);
							} else if (mailId.contains("blrfeedback@justdial.com")) {
								leadsEntity = processJustDailMailContentHTML(mailContent, emailReceivedDate);
							} else if (mailId.contains("ypleads@sulekhanotifications.com")) {
								leadsEntity = processSulekhaMailContentHTML(mailContent, emailReceivedDate);
							}
						} else if ((content instanceof Multipart)) {
							Multipart multiPart = (Multipart) content;
							BodyPart bodyPart = multiPart.getBodyPart(0);
							mailContent = (String) bodyPart.getContent();
							if (mailId.contains("care@yet5.com")) {
								leadsEntity = processYet5MailContent(mailContent, emailReceivedDate);
							} else if (mailId.contains("blrfeedback@justdial.com")) {
								if (mailsubject.contains("www.justdial.com")) {
									leadsEntity = processJustDailMailContentTypeOne(mailContent, emailReceivedDate);
								} else if (mailsubject.contains("enquiry for you at")) {
									leadsEntity = processJustDailMailContentTypeTwo(mailContent, emailReceivedDate);
								} else if (mailsubject.contains("VN Number")) {
									leadsEntity = processJustDailMailContentTypeThree(mailContent, emailReceivedDate);
								}
							} else if (mailId.contains("ypleads@sulekhanotifications.com")) {
								if (mailContent.contains("Hi Radical Technologies")) {
									leadsEntity = processSulekhaMailContentHTMLNew(mailContent, emailReceivedDate);
								} else if (mailContent.contains("Dear Radical Technologies")) {
									leadsEntity = processSulekhaMailContentHTMLNewTwo(mailContent, emailReceivedDate);
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (firstEmailTimeInMillis == 0) {
			firstEmailTimeInMillis = lastEmailTimeInMillis;
		}

		/*
		 * if (leadsEntity != null) { if (leadsEntity.getEmailId() != null) {
		 * if(leadsEntity.getCourseCategeory()!=0){ CourseCategeoryEntity
		 * category = userService.getCategoryListBasedOnCourseId(leadsEntity.
		 * getCourseCategeory());
		 * sendMail(leadsEntity.getEmailId(),category.getSubject(),category.
		 * getMessagebody()); } else { sendMail(leadsEntity.getEmailId(),
		 * Constants.MAIL_SUBJECT,null); } } if (leadsEntity.getMobileNo() !=
		 * null) { userService.sendSms(Constants.SMS_TEMPLATE,
		 * leadsEntity.getMobileNo()); } }
		 */

		EmailTimeEntity emailTimeEntity = new EmailTimeEntity();
		emailTimeEntity.setId(1);
		emailTimeEntity.setLastEmailTime(firstEmailTimeInMillis);
		emailDao.saveEmailTime(emailTimeEntity);
	}

	private LeadsEntity processRadicalMailContent(String mailContent, Date date) {
		LeadsEntity leadsEntity = new LeadsEntity();
		try {
			String[] spiltContent = mailContent.split("\\n");
			System.out.println(spiltContent);
			String name = spiltContent[6].replace("</td>", "").substring(65).trim();
			String email = spiltContent[10].replace("</td>", "").substring(65).trim();
			String mobileNumber = spiltContent[14].replace("</td>", "").substring(65).trim();
			String course = spiltContent[18].replace("</td>", "").substring(53).trim();

			leadsEntity.setName(name);
			leadsEntity.setMobileNo(mobileNumber);
			leadsEntity.setEmailId(email);
			leadsEntity.setStatus(1);
			leadsEntity.setCreatedDate(date);
			leadsEntity.setLastUpdatedDate(date);
			int courseId = 0;
			for (Map.Entry<Integer, String> entry : userService.getCourses().entrySet()) {
				if (course.equalsIgnoreCase(entry.getValue())) {
					courseId = entry.getKey();
					break;
				}
			}
			int courseCategory = 0;
			if (courseId != 0) {
				courseCategory = userService.getCoursesCategeoryMapping().get(courseId);
			}
			leadsEntity.setCourse(courseId);
			leadsEntity.setCourseCategeory(courseCategory);

			leadsEntity.setLeadSource(getLeadSoureId("Radical"));

			leadService.saveLead(leadsEntity);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while Processing Radical Manual mail" + e.getMessage());
		}
		return leadsEntity;

	}

	private LeadsEntity processYet5MailContent(String mailContent, Date date) {
		LeadsEntity leadsEntity = new LeadsEntity();
		try {
			String[] spiltContent = mailContent.split("\\n");
			String[] nameArray = spiltContent[142].split(":");
			String name = nameArray[1].replace("*", "").trim();

			String[] mobileNoArray = spiltContent[16].split(":");
			String mobileNo = mobileNoArray[1].trim();

			String[] emailArray = spiltContent[18].split(":");
			String email = emailArray[1].replace("*", "").trim();

			String[] courseSplit = spiltContent[22].split(":");
			String course = courseSplit[1].replace("*", "").trim();

			leadsEntity.setName(name);
			leadsEntity.setMobileNo(mobileNo);
			leadsEntity.setEmailId(email);
			leadsEntity.setStatus(1);
			leadsEntity.setCreatedDate(date);
			leadsEntity.setLastUpdatedDate(date);
			int courseId = 0;
			for (Map.Entry<Integer, String> entry : userService.getCourses().entrySet()) {
				if (course.equalsIgnoreCase(entry.getValue())) {
					courseId = entry.getKey();
					break;
				}
			}
			int courseCategory = 0;
			if (courseId != 0) {
				courseCategory = userService.getCoursesCategeoryMapping().get(courseId);
			}
			leadsEntity.setCourse(courseId);
			leadsEntity.setCourseCategeory(courseCategory);

			leadsEntity.setLeadSource(getLeadSoureId("YET5"));

			leadService.saveLead(leadsEntity);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while Processing yet5 mail" + e.getMessage());
		}
		return leadsEntity;

	}

	private LeadsEntity processYet5MailContentHTML(String mailContent, Date date) {
		LeadsEntity leadsEntity = new LeadsEntity();
		try {
			String name = mailContent.substring(mailContent.indexOf("Name:"), mailContent.indexOf("Mobile No.:"))
					.replace("Name:", "").trim();
			String mobileNo = mailContent.substring(mailContent.indexOf("Mobile No.:"), mailContent.indexOf("Email ID"))
					.replace("Mobile No.:", "").replace("+91", "").trim();
			String email = mailContent.substring(mailContent.indexOf("Email ID:"), mailContent.indexOf("City & Area:"))
					.replace("Email ID:", "").trim();
			String course = mailContent.substring(mailContent.indexOf("Course:"), mailContent.indexOf("Comments:"))
					.replace("Course:", "").trim();
			leadsEntity.setName(name);
			leadsEntity.setMobileNo(mobileNo);
			leadsEntity.setEmailId(email);
			leadsEntity.setStatus(1);
			leadsEntity.setCreatedDate(date);
			leadsEntity.setLastUpdatedDate(date);
			leadsEntity.setCourseName(course);
			int courseId = 0;
			System.out.println(email);
			for (Map.Entry<Integer, String> entry : userService.getCourses().entrySet()) {
				if (course.equalsIgnoreCase(entry.getValue())) {
					courseId = entry.getKey();
					break;
				}
			}
			int courseCategory = 0;
			if (courseId != 0) {
				courseCategory = userService.getCoursesCategeoryMapping().get(courseId);
			}
			leadsEntity.setCourse(courseId);
			leadsEntity.setCourseCategeory(courseCategory);

			leadsEntity.setLeadSource(getLeadSoureId("YET5"));

			leadService.saveLead(leadsEntity);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while Processing yet5 mail" + e.getMessage());
		}
		return leadsEntity;
	}

	private int getLeadSoureId(String leadSource) {
		for (Map.Entry<Integer, String> entry : userService.getLeadSourceMapping().entrySet()) {
			if (leadSource.equalsIgnoreCase(entry.getValue())) {
				return entry.getKey();
			}
		}
		return 0;
	}

	private void processUrbanProMailContent(String mailContent, Date date) {
		System.out.println(mailContent);
		String[] spiltContent = mailContent.split("\\n");
		System.out.println(spiltContent);
		String name = spiltContent[12];
		String course = spiltContent[14];
		LeadsEntity leadsEntity = new LeadsEntity();
		leadsEntity.setName(name);
		leadService.saveLead(leadsEntity);

	}

	private LeadsEntity processJustDailMailContent(String mailContent, Date date) {
		LeadsEntity leadsEntity = new LeadsEntity();
		try {
			System.out.println(mailContent);
			String[] spiltContent = mailContent.split("\\n");

			String[] nameArray = spiltContent[3].split(":");
			String name = nameArray[1].replace("*", "").trim();

			String[] mobileNoArray = spiltContent[8].split(":");
			String mobileNo = mobileNoArray[1].replace("*", "").trim();
			mobileNo = mobileNo.replace("+91", "").trim();

			String[] cityArray = spiltContent[7].split(":");
			String city = cityArray[1].replace("*", "").trim();

			leadsEntity.setName(name);
			leadsEntity.setMobileNo(mobileNo);
			leadsEntity.setStatus(1);
			leadsEntity.setCreatedDate(date);
			leadsEntity.setLastUpdatedDate(date);
			leadsEntity.setLeadSource(getLeadSoureId("jsutdail"));
			leadsEntity.setCity(city);
			leadService.saveLead(leadsEntity);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while Processing JustDail mail" + e.getMessage());
		}
		return leadsEntity;

	}

	private LeadsEntity processJustDailMailContentHTML(String mailContent, Date date) {
		LeadsEntity leadsEntity = new LeadsEntity();
		try {

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while Processing JustDail mail" + e.getMessage());
		}
		return leadsEntity;

	}

	private LeadsEntity processJustDailMailContentTypeThree(String mailContent, Date date) {
		LeadsEntity leadsEntity = new LeadsEntity();
		try {
			String[] spiltContent = mailContent.split("\\n");
			if (spiltContent[0].contains("Caller Name :")) {
				String name = spiltContent[0]
						.substring(spiltContent[0].indexOf("Caller Name :"), spiltContent[0].indexOf("Caller Phone :"))
						.replace("Caller Name :", "").trim();
				leadsEntity.setName(name);
			}
			String mobileNextWord = "";
			if (mailContent.split("\\n")[0].contains("Caller Email :")) {
				mobileNextWord = "Caller Email :";
			} else {
				mobileNextWord = "Call Date :";
			}
			String mobileNumber = spiltContent[0]
					.substring(spiltContent[0].indexOf("Caller Phone :"), spiltContent[0].indexOf(mobileNextWord))
					.replace("Caller Phone :", "").replace("+91", "").trim();
			if (spiltContent[0].contains("Caller Email :")) {
				String email = spiltContent[0]
						.substring(spiltContent[0].indexOf("Caller Email :"), spiltContent[0].indexOf("Call Date :"))
						.replace("Caller Email :", "").trim();
				leadsEntity.setEmailId(email);
			}
			String cityName = spiltContent[0]
					.substring(spiltContent[0].indexOf("City Name :"), spiltContent[0].indexOf("Get more leads"))
					.replace("City Name :", "").trim();
			leadsEntity.setMobileNo(mobileNumber);
			leadsEntity.setStatus(1);
			leadsEntity.setCity(cityName);
			leadsEntity.setCreatedDate(date);
			leadsEntity.setLastUpdatedDate(date);
			int courseId = 0;
			int courseCategory = 0;
			leadsEntity.setCourse(courseId);
			leadsEntity.setCourseCategeory(courseCategory);
			leadsEntity.setLeadSource(getLeadSoureId("Justdail"));
			leadService.saveLead(leadsEntity);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while Processing JustDail mail" + e.getMessage());
		}
		return leadsEntity;

	}

	private LeadsEntity processJustDailMailContentTypeTwo(String mailContent, Date date) {
		LeadsEntity leadsEntity = new LeadsEntity();
		try {
			String[] spiltContent = mailContent.split("\\n");
			String name = spiltContent[1].substring((spiltContent[1].indexOf("Caller Name:")))
					.replace("Caller Name:", "").replace("Caller", "").trim();
			String course = spiltContent[2].substring(spiltContent[2].indexOf("Requirement:"))
					.replace("Call Date &", "").replace("Requirement:", "").trim();
			String city = spiltContent[3].substring(spiltContent[3].indexOf("LayoutCity:")).replace("Caller", "")
					.replace("LayoutCity:", "").trim();
			String mobileNumber = spiltContent[4].substring(spiltContent[4].indexOf("Phone:")).replace("Caller", "")
					.replace("+91", "").replace("Phone:", "").trim();
			String emailId = spiltContent[5].substring(spiltContent[5].indexOf("Email:")).replace("Get", "")
					.replace("Email:", "").trim();
			leadsEntity.setName(name);
			leadsEntity.setMobileNo(mobileNumber);
			leadsEntity.setEmailId(emailId);
			leadsEntity.setStatus(1);
			leadsEntity.setCity(city);
			leadsEntity.setCreatedDate(date);
			leadsEntity.setLastUpdatedDate(date);
			leadsEntity.setCourseName(course);
			int courseId = 0;
			System.out.println(emailId);
			for (Map.Entry<Integer, String> entry : userService.getCourses().entrySet()) {
				if (course.equalsIgnoreCase(entry.getValue())) {
					courseId = entry.getKey();
					break;
				}
			}
			int courseCategory = 0;
			if (courseId != 0) {
				courseCategory = userService.getCoursesCategeoryMapping().get(courseId);
			}
			leadsEntity.setCourse(courseId);
			leadsEntity.setCourseCategeory(courseCategory);

			leadsEntity.setLeadSource(getLeadSoureId("Justdail"));

			leadService.saveLead(leadsEntity);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while Processing JustDail mail" + e.getMessage());
		}
		return leadsEntity;

	}

	private LeadsEntity processSulekhaMailContent(String mailContent, Date date) {
		LeadsEntity leadsEntity = new LeadsEntity();
		try {
			String[] spiltContent = mailContent.split("\\n");
			String[] mobileNoArray = spiltContent[14].split(":");
			String mobileNo = mobileNoArray[1].replace("*", "").trim();
			leadsEntity.setMobileNo(mobileNo);
			leadsEntity.setStatus(1);
			leadsEntity.setCreatedDate(date);
			leadsEntity.setLastUpdatedDate(date);
			leadsEntity.setLeadSource(getLeadSoureId("sulekha"));
			leadService.saveLead(leadsEntity);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while Processing sulekha+ mail" + e.getMessage());
		}
		return leadsEntity;
	}

	public List<SendEmailEntity> getEmailEntries() {
		return emailDao.getEmailEntries();
	}

	public void updateEmailEntries(SendEmailEntity emailEntry) {
		emailDao.updateEmailEntries(emailEntry);
	}

	@Transactional
	public boolean sendMail(String toMailId, String subject, String mailBody) {
		try {
			if (mailBody == null) {
				mailBody = properties.getProperty(Constants.MAIL_BODY);
			} else {
				mailBody = properties.getProperty(Constants.MAIL_HEAD) + mailBody + properties.getProperty(Constants.MAIL_TAIL);
			}
			if (subject == null) {
				subject = Constants.MAIL_SUBJECT;
			}
			String userid = properties.getProperty(Constants.PROPERTIES_MAILID);
			String password = properties.getProperty(Constants.PROPERTIES_PASSWORD);
			String host = properties.getProperty("host");
			MimeMessage message = new MimeMessage(session);
			InternetAddress fromAddress = null;
			InternetAddress toAddress = null;
			try {
				fromAddress = new InternetAddress(userid);
				toAddress = new InternetAddress(toMailId);
			} catch (AddressException e) {
				e.printStackTrace();
			}

			message.setFrom(fromAddress);
			message.setRecipient(RecipientType.TO, toAddress);
			message.setSubject(subject);
			BodyPart messageBodyPartBody = new MimeBodyPart();
			
			messageBodyPartBody.setContent(mailBody, "text/html");
			MimeMultipart multipart = new MimeMultipart("related");
			multipart.addBodyPart(messageBodyPartBody);
			message.setContent(multipart);
			Transport transport = session.getTransport("smtps");
			transport.connect(host, userid, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	private LeadsEntity processJustDailMailContentTypeOne(String mailContent, Date date) {
		LeadsEntity leadsEntity = new LeadsEntity();
		try {
			String[] spiltContent = mailContent.split("\\n");
			String name = spiltContent[2].trim();
			String course = spiltContent[5].trim();
			String city = spiltContent[14].trim();
			String mobilenumber = spiltContent[17].replace("+91", "").trim();
			String emailId = null;
			if (spiltContent[19].contains("Caller Email :")) {
				emailId = spiltContent[20].trim();
			}
			System.out.println(emailId);
			leadsEntity.setName(name);
			leadsEntity.setCity(city);
			leadsEntity.setMobileNo(mobilenumber);
			leadsEntity.setEmailId(emailId);
			leadsEntity.setStatus(1);
			leadsEntity.setCreatedDate(date);
			leadsEntity.setLastUpdatedDate(date);
			leadsEntity.setCourseName(course);
			int courseId = 0;
			for (Map.Entry<Integer, String> entry : userService.getCourses().entrySet()) {
				if (course.equalsIgnoreCase(entry.getValue())) {
					courseId = entry.getKey();
					break;
				}
			}
			int courseCategory = 0;
			if (courseId != 0) {
				courseCategory = userService.getCoursesCategeoryMapping().get(courseId);
			}
			leadsEntity.setCourse(courseId);
			leadsEntity.setCourseCategeory(courseCategory);
			leadsEntity.setLeadSource(getLeadSoureId("Justdail"));
			leadService.saveLead(leadsEntity);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while Processing JustDail mail" + e.getMessage());
		}
		return leadsEntity;

	}

	private LeadsEntity processSulekhaMailContentHTML(String mailContent, Date date) {
		LeadsEntity leadsEntity = new LeadsEntity();
		try {

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while Processing sulekha mail" + e.getMessage());
		}
		return leadsEntity;
	}
	
	private LeadsEntity processWebsiteEnquiryContent(String mailContent, Date date) {
		LeadsEntity leadsEntity = new LeadsEntity();
		try {
			String name = mailContent.substring(mailContent.indexOf("Course Inquiry Details"),mailContent.indexOf("Email")).replace("Course Inquiry Details", "").trim().replace("Name", "").trim();
			String email = mailContent.substring(mailContent.indexOf("Email"),mailContent.indexOf("Phone")).replace("Email", "").trim();
			String phone = mailContent.substring(mailContent.indexOf("Phone"),mailContent.indexOf("Course Name")).replace("Phone", "").trim().replace("+", "").trim();
			String courseName = mailContent.substring(mailContent.indexOf("Course Name"), mailContent.indexOf("Message")).replace("Course Name", "").trim();
			String message = mailContent.substring(mailContent.indexOf("Message")).replace("Message", "").trim();
			int courseId = 0;
			for (Map.Entry<Integer, String> entry : userService.getCourses().entrySet()) {
				if (courseName.equalsIgnoreCase(entry.getValue())) {
					courseId = entry.getKey();
					break;
				}
			}
			int courseCategory = 0;
			if (courseId != 0) {
				courseCategory = userService.getCoursesCategeoryMapping().get(courseId);
			}
			leadsEntity.setCourseName(courseName);
			leadsEntity.setName(name);
			leadsEntity.setCourse(courseId);
			leadsEntity.setCourseCategeory(courseCategory);
			leadsEntity.setMobileNo(phone);
			leadsEntity.setEmailId(email);
			leadsEntity.setStatus(1);
			leadsEntity.setCreatedDate(date);
			leadsEntity.setLastUpdatedDate(date);
			leadsEntity.setLeadSource(getLeadSoureId("Web site"));
			leadsEntity.setComments(message);
			leadService.saveLead(leadsEntity);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while Processing sulekha mail" + e.getMessage());
		}
		return leadsEntity;
	}

	private LeadsEntity processSulekhaMailContentHTMLNewTwo(String mailContent, Date date) {
		LeadsEntity leadsEntity = new LeadsEntity();
		try {
			String name = mailContent
					.substring(mailContent.indexOf("just spoke to"), mailContent.indexOf("from Bangalore"))
					.replace("just spoke to", "").trim();
			String courseName = mailContent
					.substring(mailContent.indexOf("looking for"), mailContent.indexOf(". We are sharing"))
					.replace("looking for", "").trim();
			String mobileNumber = mailContent
					.substring(mailContent.indexOf("further contact:"), mailContent.indexOf("Please leave your"))
					.replace("further contact:", "").replaceAll("\\W", "").replace("91", "").trim();
			int courseId = 0;
			for (Map.Entry<Integer, String> entry : userService.getCourses().entrySet()) {
				if (courseName.equalsIgnoreCase(entry.getValue())) {
					courseId = entry.getKey();
					break;
				}
			}
			int courseCategory = 0;
			if (courseId != 0) {
				courseCategory = userService.getCoursesCategeoryMapping().get(courseId);
			}
			leadsEntity.setCourseName(courseName);
			leadsEntity.setName(name);
			leadsEntity.setCourse(courseId);
			leadsEntity.setCourseCategeory(courseCategory);
			leadsEntity.setMobileNo(mobileNumber);
			leadsEntity.setEmailId(null);
			leadsEntity.setStatus(1);
			leadsEntity.setCreatedDate(date);
			leadsEntity.setLastUpdatedDate(date);
			leadsEntity.setLeadSource(getLeadSoureId("sulekha"));
			leadService.saveLead(leadsEntity);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while Processing sulekha+ mail" + e.getMessage());
		}
		return leadsEntity;
	}

	private LeadsEntity processSulekhaMailContentHTMLNew(String mailContent, Date date) {
		LeadsEntity leadsEntity = new LeadsEntity();
		try {
			String[] spiltContent = mailContent.split("\\n");
			String name = spiltContent[0]
					.substring(spiltContent[0].indexOf("Customer's name:"), spiltContent[0].indexOf("Looking for:"))
					.replace("Customer's name:", "").trim();
			String course = null;
			if (spiltContent[0].contains("Additional info:")) {
				course = spiltContent[0].substring(spiltContent[0].indexOf("Service preference:"),
						spiltContent[0].indexOf("Additional info:")).replace("Service preference:", "").trim();
			} else {
				course = spiltContent[0].substring(spiltContent[0].indexOf("Service preference:"),
						spiltContent[0].indexOf("Contact details")).replace("Service preference:", "").trim();
			}
			String city = spiltContent[0]
					.substring(spiltContent[0].indexOf("City:"), spiltContent[0].indexOf("Locality:"))
					.replace("City:", "").trim();
			String mobileNumber = spiltContent[0]
					.substring(spiltContent[0].indexOf("Mobile number:"),
							spiltContent[0].indexOf("To call this user from"))
					.replace("Mobile number:", "").replaceAll("\\W", "").replace("91", "").trim();
			String emailId = null;
			if ((spiltContent[0].indexOf("Email ID:")) != -1) {
				emailId = spiltContent[0]
						.substring(spiltContent[0].indexOf("Email ID:"), spiltContent[0].indexOf("Mobile number:"))
						.replace("Email ID:", "").trim();
			}

			System.out.println(emailId);
			int courseId = 0;
			for (Map.Entry<Integer, String> entry : userService.getCourses().entrySet()) {
				if (course.equalsIgnoreCase(entry.getValue())) {
					courseId = entry.getKey();
					break;
				}
			}
			int courseCategory = 0;
			if (courseId != 0) {
				courseCategory = userService.getCoursesCategeoryMapping().get(courseId);
			}
			leadsEntity.setCourseName(course);
			leadsEntity.setName(name);
			leadsEntity.setCity(city);
			leadsEntity.setCourse(courseId);
			leadsEntity.setCourseCategeory(courseCategory);
			leadsEntity.setMobileNo(mobileNumber);
			leadsEntity.setEmailId(null);
			leadsEntity.setStatus(1);
			leadsEntity.setCreatedDate(date);
			leadsEntity.setLastUpdatedDate(date);
			leadsEntity.setLeadSource(getLeadSoureId("sulekha"));
			leadService.saveLead(leadsEntity);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while Processing sulekha+ mail" + e.getMessage());
		}
		return leadsEntity;
	}

}
