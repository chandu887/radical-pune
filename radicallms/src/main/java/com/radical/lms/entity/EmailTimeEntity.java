package com.radical.lms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "emailtime")
public class EmailTimeEntity {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "lastemailtime")
	private long lastEmailTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getLastEmailTime() {
		return lastEmailTime;
	}

	public void setLastEmailTime(long lastEmailTime) {
		this.lastEmailTime = lastEmailTime;
	}

}
