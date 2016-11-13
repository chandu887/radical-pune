package com.radical.lms.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.radical.lms.constants.Constants;
import com.radical.lms.dao.EmailDao;
import com.radical.lms.entity.EmailTimeEntity;
import com.radical.lms.entity.LeadsEntity;
import com.radical.lms.entity.SendEmailEntity;

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

	public void init() {
		setEmailSession();
		setMailStore();
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
				Message message = messages[i];
				Address[] addressArray = message.getFrom();
				Date emailReceivedDate = message.getReceivedDate();
				if (firstEmailTimeInMillis == 0) {
					firstEmailTimeInMillis = emailReceivedDate.getTime();
				}
				if (lastEmailTimeInMillis >= emailReceivedDate.getTime()) {
					break;
				}
				/*if (addressArray[0].toString().equalsIgnoreCase("Radicaltechnologies <radicaltechnologies.co.in@gmail.com>")) {*/

					/*String mailsubject = message.getSubject();
					Multipart multiPart = (Multipart) message.getContent();
					BodyPart bodyPart = multiPart.getBodyPart(0);
					String mailContent = (String) bodyPart.getContent();*/

					String mailsubject = message.getSubject();
					String mailContent = "";
					Object content = message.getContent();  
					if (content instanceof String)  
					{  
						mailContent = (String)content;  
					}  
					else if (content instanceof Multipart)  
					{  
						Multipart multiPart = (Multipart) content;
						BodyPart bodyPart = multiPart.getBodyPart(0);
						mailContent = (String) bodyPart.getContent();
  
					}
					
					
					if (mailsubject.contains("Yet5.com")) {
						leadsEntity = processYet5MailContent(mailContent, emailReceivedDate);
					} else if (mailsubject.contains("enquiry for you at")) {
						leadsEntity = processJustDailMailConten(mailContent, emailReceivedDate);
					} /*
						 * else if (mailsubject.contains("UrbanPro-Customers"))
						 * { processUrbanProMailContent(mailContent, date); }
						 */ else if (mailsubject.contains("A user contacted you through us")) {
						leadsEntity = processSulekhaMailContent(mailContent, emailReceivedDate);
					} else if (mailsubject.contains("Quick Inquiry Details")) {
						leadsEntity = processRadicalMailContent(mailContent, emailReceivedDate);
					}
//				}
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (firstEmailTimeInMillis == 0) {
			firstEmailTimeInMillis = lastEmailTimeInMillis;
		}
		if (leadsEntity != null) {
			if (leadsEntity.getEmailId() != null) {
				if (leadsEntity.getCourse() != 0) {
					sendMail(leadsEntity.getEmailId(), "Welcome to Radical Technologies",
							"Dear User, Thanks For Contacting us");
				}
			}
			if (leadsEntity.getMobileNo() != null) {
				userService.sendSms("Dear User, Thanks For Contacting us", leadsEntity.getMobileNo());
			}
		}
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
			String[] nameArray = spiltContent[14].split(":");
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

	private LeadsEntity processJustDailMailConten(String mailContent, Date date) {
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

}
