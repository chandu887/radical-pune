package com.radical.lms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "emailcount")
public class EmailCountEntity {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "lastemailcount")
	private int lastEmailCount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLastEmailCount() {
		return lastEmailCount;
	}

	public void setLastEmailCount(int lastEmailCount) {
		this.lastEmailCount = lastEmailCount;
	}	
	
	
}
