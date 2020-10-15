package com.example.demo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Table(name = "account_current_balance")
public class AccountCurrentBalance implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1835080635549199266L;

	@Id
	@GeneratedValue
	@Column(name = "balance_id")
	@JsonProperty("balanceId")
	private Long balanceId;
	
	@JsonIgnoreProperties({"currentBalanceList"})
	@ManyToOne
	@JoinColumn(name="account_details_pk", nullable=false)
	@JsonProperty("accountId")
	private ATMAccountDetails accountId;
	
	@Column(name = "current_balance", nullable=false)
	@JsonProperty("current_balance")
	private BigDecimal current_balance;
	
	@Column(name = "balance_updated_date", nullable=false)
	@JsonProperty("updatedOn")
	private Date updatedOn;

	public Long getBalanceId() {
		return balanceId;
	}

	public void setBalanceId(Long balanceId) {
		this.balanceId = balanceId;
	}

	public ATMAccountDetails getAccountId() {
		return accountId;
	}

	public void setAccountId(ATMAccountDetails accountId) {
		this.accountId = accountId;
	}

	public BigDecimal getCurrent_balance() {
		return current_balance;
	}

	public void setCurrent_balance(BigDecimal current_balance) {
		this.current_balance = current_balance;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	
}
