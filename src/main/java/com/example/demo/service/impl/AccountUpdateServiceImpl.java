package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.example.demo.jparepositories.AccountTransactionsRepository;
import com.example.demo.model.ATMAccountDetails;
import com.example.demo.model.ATMPersonalDetails;
import com.example.demo.model.AccountTransactions;
import com.example.demo.service.AccountUpdateService;
import com.example.demo.util.ServiceUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AccountUpdateServiceImpl implements AccountUpdateService {
	
	@Autowired
	AccountTransactionsRepository accountTransactionRepository;
	
	@Autowired
    private Environment env;

	@Transactional
	public String withdraw(String debitCardNumber, Long amount) {
		
		ATMAccountDetails account = new ATMAccountDetails();
    	account.setDebitCardNumber(debitCardNumber);
    	String response = null;
    	try {
    		response = ServiceUtility.getSBIApi(account,env.getProperty("sbi.api"));
    	} catch(Exception e) {
    		throw new RuntimeException(e.getMessage());
    	}
    	
    	ObjectMapper mapper = new ObjectMapper();
		ATMPersonalDetails personalDetails = null;
		try {
			personalDetails = mapper.readValue(response, ATMPersonalDetails.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("Internal Error.");
		}
		
		if(personalDetails.getAccountList().get(0).getCurrentBalanceList().get(0).getCurrent_balance().compareTo(BigDecimal.valueOf(amount))>=0) {
			String status = null;
			try {
				status = ServiceUtility.withdraw(account, amount,env.getProperty("sbi.withdraw"));
			} catch(Exception e) {
				throw new RuntimeException(e.getMessage());
			}
			if(status.equals("success")) {
				AccountTransactions at = new AccountTransactions();
				at.setTransactionAmount(amount.intValue());
				at.setTransactionStatus(status);
				at.setTransactionTime(new Date());
				at.setTypeOfTransaction("Debit");
				at.setAtmId("XXXXXXXXXX");
				at.setAccountTransactionId(personalDetails.getAccountList().get(0).getAccountId());
				accountTransactionRepository.save(at);
			}
			return status;
		} else {
			return "Insufficient fund.";
		}
	}

	@Transactional
	public String deposit(String debitcardnumber, Long amount) {
		
		ATMAccountDetails account = new ATMAccountDetails();
		account.setDebitCardNumber(debitcardnumber);
		
		String response = null;
		try {
			response =ServiceUtility.getSBIApi(account,env.getProperty("sbi.api"));
		} catch(Exception e) {
    		throw new RuntimeException(e.getMessage());
		}
		
		ObjectMapper mapper = new ObjectMapper();
		ATMPersonalDetails personalDetails = null;
		try {
			personalDetails = mapper.readValue(response, ATMPersonalDetails.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("Internal Error.");
		}
		
		String status = "" ;
		try {
			status = ServiceUtility.deposit(account, amount, env.getProperty("sbi.deposit"));
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
		if (status.equals("success")) {
			AccountTransactions at = new AccountTransactions();
			at.setTransactionAmount(amount.intValue());
			at.setTransactionStatus(status);
			at.setTransactionTime(new Date());
			at.setTypeOfTransaction("Credit");
			at.setAtmId("XXXXXXXXXX");
			at.setAccountTransactionId(personalDetails.getAccountList().get(0).getAccountId());
			accountTransactionRepository.save(at);
		}
		return status;
	}

}
