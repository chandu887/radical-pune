package com.radical.lms.thread;

import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.radical.lms.entity.CourseEntity;
import com.radical.lms.entity.SendEmailEntity;
import com.radical.lms.service.EmailService;
import com.radical.lms.service.UserService;

public class EmailThread implements Runnable {

	public void run() {  
		System.out.println("Emailthread calling");
		try {
			ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(ContextLoaderListener.getCurrentWebApplicationContext().getServletContext());
			System.out.println("Emailthread executing");
			EmailService emailService = (EmailService) springContext.getBean("emailService");
			UserService userService = (UserService) springContext.getBean("userService");

			emailService.readMailInbox();
			
			/*List<SendEmailEntity> emailEntries = emailService.getEmailEntries();
			
			for (SendEmailEntity sendEmailEntity : emailEntries) {
				CourseEntity courseEntity = userService.getCourseListBasedOnCourseId(sendEmailEntity.getCourseId());
				
				boolean emailStatus = emailService.sendMail(sendEmailEntity.getReceiverMailId(), courseEntity.getSubject(), courseEntity.getMessagebody());
				if (emailStatus) {
					sendEmailEntity.setStatus(1);
				} else {
					sendEmailEntity.setStatus(2);
				}
				sendEmailEntity.setEmailSendTime(new Date());
				emailService.updateEmailEntries(sendEmailEntity);
			}*/
			
			Thread.currentThread().sleep(900000);
			new Thread(new EmailThread()).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}  
	
}
