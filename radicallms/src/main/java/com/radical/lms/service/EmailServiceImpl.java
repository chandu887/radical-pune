package com.radical.lms.service;

import java.io.IOException;
import java.util.Date;
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

import org.springframework.beans.factory.annotation.Autowired;

import com.radical.lms.dao.EmailDao;
import com.radical.lms.entity.EmailCountEntity;
import com.radical.lms.entity.LeadsEntity;

public class EmailServiceImpl implements EmailService{
	
	@Autowired
	private LeadService leadService;
	
	@Autowired
	private EmailDao emailDao;
	
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
        	store.connect("imap.gmail.com", "ganeshkumar.gara@gmail.com", "ganeshkumargara");
        	setStore(store);
			//store.connect("imap.gmail.com", "chandrasekhar.mutta8@gmail.com", "9247271182");
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
		int currentEmailCount = 0;
		try {
			inbox = getStore().getFolder("INBOX");
			inbox.open(Folder.READ_WRITE);
			currentEmailCount = inbox.getMessageCount();
			int lastEmailCount = emailDao.getLastEmailCount();
			Message[] messages = inbox.getMessages(lastEmailCount, currentEmailCount);
			for (Message message : messages) {
				Address[] addressArray = message.getFrom();
				if (addressArray[0].toString().equalsIgnoreCase("Ravi Shashtri <nagunuriravi@gmail.com>")) {
					String mailsubject = message.getSubject();
					Date date = message.getSentDate();
					Multipart multiPart = (Multipart) message.getContent();
					BodyPart bodyPart = multiPart.getBodyPart(0);
					String mailContent = (String) bodyPart.getContent();

					if (mailsubject.contains("Yet5.com")) {
						processYet5MailContent(mailContent, date);
					} else if (mailsubject.contains("enquiry for you at")) {
						processJustDailMailConten(mailContent, date);
					} else if (mailsubject.contains("UrbanPro-Customers")) {
						processUrbanProMailContent(mailContent, date);
					} else if (mailsubject.contains("A user contacted you through us")) {
						processSulekhaMailContent(mailContent, date);
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
		EmailCountEntity emailCountEntity = new EmailCountEntity();
		emailCountEntity.setId(1);
		emailCountEntity.setLastEmailCount(currentEmailCount);
		emailDao.saveEmailCount(emailCountEntity);
		
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
			leadService.saveLead(leadsEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void processUrbanProMailContent(String mailContent, Date date) {
		System.out.println(mailContent);
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
			leadService.saveLead(leadsEntity);
			
		} catch (Exception e) {
			e.printStackTrace();
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
			leadService.saveLead(leadsEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
