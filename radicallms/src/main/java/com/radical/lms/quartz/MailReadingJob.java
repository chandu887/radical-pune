package com.radical.lms.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.radical.lms.service.EmailService;

public class MailReadingJob extends QuartzJobBean{
	
	/*@Autowired
	EmailService emailService;
	
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}*/


	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(ContextLoaderListener .getCurrentWebApplicationContext().getServletContext());

		EmailService emailService = (EmailService) springContext.getBean("emailService");

		emailService.readMailInbox();
	}
}
