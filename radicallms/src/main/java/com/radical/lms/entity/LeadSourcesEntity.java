package com.radical.lms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "leadsouces")
public class LeadSourcesEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "leadsourceid")
	private int leadsourceId;

	@Column(name = "leadsourcename")
	private String leadSourceName;

	@Column(name = "leadsourceemailid")
	private String leadSourceEmailId;

	public LeadSourcesEntity() {

	}
	
	public int getLeadsourceId() {
		return leadsourceId;
	}

	public void setLeadsourceId(int leadsourceId) {
		this.leadsourceId = leadsourceId;
	}

	public LeadSourcesEntity(String leadSourceName, String leadSourceEmailId) {
		this.leadSourceName = leadSourceName;
		this.leadSourceEmailId = leadSourceEmailId;
	}

	public String getLeadSourceName() {
		return leadSourceName;
	}

	public void setLeadSourceName(String leadSourceName) {
		this.leadSourceName = leadSourceName;
	}

	public String getLeadSourceEmailId() {
		return leadSourceEmailId;
	}

	public void setLeadSourceEmailId(String leadSourceEmailId) {
		this.leadSourceEmailId = leadSourceEmailId;
	}

}
