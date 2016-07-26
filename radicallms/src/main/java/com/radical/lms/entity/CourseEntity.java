package com.radical.lms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "courses")
public class CourseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "courseid")
	private int courseId;

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

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
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
