package com.radical.lms.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.radical.lms.beans.LeadsEntityBean;
import com.radical.lms.entity.CourseCategeoryEntity;
import com.radical.lms.entity.LeadsEntity;
import com.radical.lms.entity.UsersEntity;
import com.radical.lms.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	private boolean getData = false;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String homepage(ModelMap map, HttpServletRequest request) {
		HttpSession session = request.getSession();
		UsersEntity user = (UsersEntity) session.getAttribute("userInfo");
		if (user != null) {
			switch (user.getRoleId()) {
			case 1:
				return "dashboard";
			default:
				return "login";
			}
		}
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginEmployee(@RequestParam("userName") String userName, @RequestParam("password") String password,
			ModelMap map, HttpServletRequest request) {
		UsersEntity user = userService.checkLoginDetails(userName, password);
		if (user != null) {
			HttpSession session = request.getSession();
			session.setAttribute("userInfo", user);
			switch (user.getRoleId()) {
			case 1:
				List count = this.userService.getCountByStatusType();
				long newCount = (Long) count.get(0);
				long openCount = (Long) count.get(1);
				long closeCount = (Long) count.get(2);
				List<LeadsEntityBean> leadsList = this.userService.getLeadsByStatus(1);
				map.addAttribute("newCount", newCount);
				map.addAttribute("openCount", openCount);
				map.addAttribute("closeCount", closeCount);
				map.addAttribute("allCount", ((int) newCount + (int) openCount + (int) closeCount));
				map.addAttribute("leadsList", leadsList);
				return "dashboard";
			}
		}
		return "loginfailure";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("userInfo");
		session.invalidate();
		return "login";
	}
}
