package com.radical.lms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class UsersEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "userid")
	private int userId;
	
	@Column(name = "username")
	private String userName;

	@Column(name = "password")
	private String password;

	@Column(name = "emailid")
	private String email;

	@Column(name = "createdtime")
	private Date createdTime;

	@Column(name = "lastlogindate")
	private Date lastLoginTime;

	@Column(name = "roleid")
	private int roleId;

	public UsersEntity() {
	}

	public UsersEntity(String userName, String password, String email, Date createdTime, Date lastLoginTime,
			int roleId) {
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.createdTime = createdTime;
		this.lastLoginTime = lastLoginTime;
		this.roleId = roleId;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

}
