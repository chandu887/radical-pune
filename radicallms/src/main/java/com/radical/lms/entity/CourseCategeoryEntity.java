package com.radical.lms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "coursecategory")
public class CourseCategeoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "categoryid")
	private int categoryId;

	@Column(name = "categoryname")
	private String categeoryName;

	public CourseCategeoryEntity() {

	}

	public CourseCategeoryEntity( String categeoryName) {
		this.categeoryName = categeoryName;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategeoryName() {
		return categeoryName;
	}

	public void setCategeoryName(String categeoryName) {
		this.categeoryName = categeoryName;
	}

}
