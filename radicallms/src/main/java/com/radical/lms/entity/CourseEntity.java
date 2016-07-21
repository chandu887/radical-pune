package com.radical.lms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "courses")
public class CourseEntity {

	@Column(name = "categoryid")
	private int categeoryId;

	@Column(name = "coursename")
	private String courseName;

	public CourseEntity() {

	}

	public CourseEntity(int categeoryId, String courseName) {
		this.categeoryId = categeoryId;
		this.courseName = courseName;
	}

	public int getCategeoryId() {
		return categeoryId;
	}

	public void setCategeoryId(int categeoryId) {
		this.categeoryId = categeoryId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

}
