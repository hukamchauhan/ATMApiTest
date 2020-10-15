package com.example.demo.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "atm_personal_details")
public class ATMPersonalDetails {
	
	@Id
	@GeneratedValue
	@Column(name = "atm_person_id")
	@JsonProperty("personId")
	private Long personId;
	
	@Column(name = "name", nullable=false)
	@JsonProperty("name")
	private String name;
	
	@Column(name = "contact_number", nullable=false)
	@JsonProperty("contactNumber")
	private String contactNumber;
	
	@Column(name = "pan_number", nullable=false)
	@JsonProperty("panNumber")
	private String panNumber;
	
	@Column(name = "aadhar_number", nullable=false)
	@JsonProperty("aadharNumber")
	private String aadharNumber;
	
	@Column(name = "address", nullable=false)
	@JsonProperty("address")
	private String address;
	
	@Column(name = "account_status", nullable=false)
	@JsonProperty("accountStatus")
	private String accountStatus;
	
	@OneToMany(mappedBy = "personalDetails", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JsonProperty("accountList")
	private List<ATMAccountDetails> accountList;
	
	@Transient
	private String msg;
	
	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<ATMAccountDetails> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<ATMAccountDetails> accountList) {
		this.accountList = accountList;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	

}
