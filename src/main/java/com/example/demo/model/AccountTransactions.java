package com.example.demo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "atm_transaction_details")
public class AccountTransactions {

	@Id
	@GeneratedValue
	@Column(name = "transaction_id")
	private Long transactionId;
	
	@Column(name = "account_details_pk", nullable=false)
	private Long accountTransactionId;
	
	@Column(name = "transaction_type", nullable=false)
	private String typeOfTransaction;
	
	@Column(name = "transaction_time", nullable=false)
	private Date transactionTime;
	
	@Column(name = "transaction_amount", nullable=false)
	private Integer transactionAmount;
	
	@Column(name="atm_id", nullable=false)
	private String atmId;
	
	@Column(name="transaction_status", nullable=false)
	private String transactionStatus;

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public String getTypeOfTransaction() {
		return typeOfTransaction;
	}

	public void setTypeOfTransaction(String typeOfTransaction) {
		this.typeOfTransaction = typeOfTransaction;
	}

	public Date getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(Date transactionTime) {
		this.transactionTime = transactionTime;
	}

	public Integer getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Integer transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getAtmId() {
		return atmId;
	}

	public void setAtmId(String atmId) {
		this.atmId = atmId;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public Long getAccountTransactionId() {
		return accountTransactionId;
	}

	public void setAccountTransactionId(Long accountTransactionId) {
		this.accountTransactionId = accountTransactionId;
	}

	
	
}
