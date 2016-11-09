package com.radical.lms.quartz;

import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.radical.lms.entity.CourseEntity;
import com.radical.lms.entity.SendEmailEntity;
import com.radical.lms.service.EmailService;
import com.radical.lms.service.UserService;

public class MailSendingJob extends QuartzJobBean {

	@Override
	public void executeInternal(JobExecutionContext context) throws JobExecutionException {
		ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(ContextLoaderListener.getCurrentWebApplicationContext().getServletContext());
		EmailService emailService = (EmailService) springContext.getBean("emailService");
		UserService userService = (UserService) springContext.getBean("userService");
				
		List<SendEmailEntity> emailEntries = emailService.getEmailEntries();
		
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
		}		
	}

}
