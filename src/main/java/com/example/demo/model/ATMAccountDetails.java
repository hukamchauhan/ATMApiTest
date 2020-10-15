package com.example.demo.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "atm_account_details")
public class ATMAccountDetails {

	@Id
	@GeneratedValue
	@Column(name = "account_id")
	@JsonProperty("accountId")
	private Long accountId;
	
	@Column(name = "account_number", nullable=false)
	@JsonProperty("accountNumber")
	private String accountNumber;
	
	@Column(name = "card_type", nullable=false)
	@JsonProperty("cardType")
	private String cardType;
	
	@Column(name = "card_number", nullable=false)
	@JsonProperty("debitCardNumber")
	private String debitCardNumber;
	
	@Column(name = "code", nullable=false)
	@JsonProperty("code")
	private String code;
	
	@Column(name = "bank", nullable=false)
	@JsonProperty("bank")
	private String bank;
	
	@Column(name = "card_status", nullable=false)
	@JsonProperty("cardStatus")
	private String cardStatus;
	
	@ManyToOne
	@JoinColumn(name="atm_personal_details_pk", nullable=false)
	@JsonProperty("personalDetails")
	private ATMPersonalDetails personalDetails;
	
	@JsonIgnoreProperties({"accountId"})
	@OneToMany(mappedBy = "accountId", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	private List<AccountCurrentBalance> currentBalanceList;
	
	@Transient
	@JsonProperty("amount")
	int amount;
	
	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getDebitCardNumber() {
		return debitCardNumber;
	}

	public void setDebitCardNumber(String debitCardNumber) {
		this.debitCardNumber = debitCardNumber;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ATMPersonalDetails getPersonalDetails() {
		return personalDetails;
	}

	public void setPersonalDetails(ATMPersonalDetails personalDetails) {
		this.personalDetails = personalDetails;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public List<AccountCurrentBalance> getCurrentBalanceList() {
		return currentBalanceList;
	}

	public void setCurrentBalanceList(List<AccountCurrentBalance> currentBalanceList) {
		this.currentBalanceList = currentBalanceList;
	}

	public String getCardStatus() {
		return cardStatus;
	}

	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}


}
