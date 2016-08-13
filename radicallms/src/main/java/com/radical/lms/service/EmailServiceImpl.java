package com.radical.lms.service;

import java.io.IOException;
import java.util.Date;
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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.radical.lms.dao.EmailDao;
import com.radical.lms.entity.EmailTimeEntity;
import com.radical.lms.entity.LeadsEntity;

public class EmailServiceImpl implements EmailService{
	
	final static Logger logger = Logger.getLogger(EmailServiceImpl.class);
	@Autowired
	private LeadService leadService;
	
	@Autowired
	private EmailDao emailDao;
	
	@Autowired
	private UserService userService;
	
	Store store = null;
	
	public void init() {
		setMailStore();
	}
	
	private void setMailStore() {
		Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        Session session = Session.getInstance(props, null);
        try {
        	Store store = session.getStore();
        	store.connect("imap.gmail.com", "emailtest887@gmail.com", "radical@123");
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
		try {
			inbox = getStore().getFolder("INBOX");
			inbox.open(Folder.READ_WRITE);
			
			Message[] messages = inbox.getMessages(1, inbox.getMessageCount());
			
			for (int i = messages.length-1; i > 0; i--) {
				Message message = messages[i];
				Address[] addressArray = message.getFrom();
				Date emailReceivedDate = message.getReceivedDate();
				if (firstEmailTimeInMillis == 0) {
					firstEmailTimeInMillis = emailReceivedDate.getTime();
				}
				if (lastEmailTimeInMillis >= emailReceivedDate.getTime()) {
					break;
				}
				if (addressArray[0].toString().equalsIgnoreCase("Chandrasekhar Mutta <chandrasekhar.mutta8@gmail.com>")) {
					
					String mailsubject = message.getSubject();
					Multipart multiPart = (Multipart) message.getContent();
					BodyPart bodyPart = multiPart.getBodyPart(0);
					String mailContent = (String) bodyPart.getContent();

					if (mailsubject.contains("Yet5.com")) {
						processYet5MailContent(mailContent, emailReceivedDate);
					} else if (mailsubject.contains("enquiry for you at")) {
						processJustDailMailConten(mailContent, emailReceivedDate);
					} /*else if (mailsubject.contains("UrbanPro-Customers")) {
						processUrbanProMailContent(mailContent, date);
					}*/ else if (mailsubject.contains("A user contacted you through us")) {
						processSulekhaMailContent(mailContent, emailReceivedDate);
					}
				}
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		if (firstEmailTimeInMillis == 0) {
			firstEmailTimeInMillis = lastEmailTimeInMillis;
		}
		EmailTimeEntity emailTimeEntity = new EmailTimeEntity();
		emailTimeEntity.setId(1);
		emailTimeEntity.setLastEmailTime(firstEmailTimeInMillis);
		emailDao.saveEmailTime(emailTimeEntity);
	}
	
	private void processYet5MailContent(String mailContent, Date date) {
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
			
			LeadsEntity leadsEntity = new LeadsEntity();
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
	
	private void processJustDailMailConten(String mailContent, Date date) {
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
			
			LeadsEntity leadsEntity = new LeadsEntity();
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
		
	}
	
	private void processSulekhaMailContent(String mailContent, Date date) {
		try {			
			String[] spiltContent = mailContent.split("\\n");
			String[] mobileNoArray = spiltContent[14].split(":");
			String mobileNo = mobileNoArray[1].replace("*", "").trim();
			LeadsEntity leadsEntity = new LeadsEntity();
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
	}
	
}
