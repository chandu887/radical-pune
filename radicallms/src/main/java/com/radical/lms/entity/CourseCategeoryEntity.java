package com.radical.lms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "coursecategory")
public class CourseCategeoryEntity {

	@Column(name = "categoryname")
	private String categeoryName;

	public CourseCategeoryEntity() {

	}

	public CourseCategeoryEntity(String categeoryName) {
		this.categeoryName = categeoryName;
	}

	public String getCategeoryName() {
		return categeoryName;
	}

	public void setCategeoryName(String categeoryName) {
		this.categeoryName = categeoryName;
	}

}
