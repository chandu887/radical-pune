package com.radical.lms.beans;


public class CourseBean {

	private int courseId;

	private String courseName;
	
	private String categeoryName;
	
	private int isActive;

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCategeoryName() {
		return categeoryName;
	}

	public void setCategeoryName(String categeoryName) {
		this.categeoryName = categeoryName;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	
	
}
