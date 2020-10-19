package com.example.demo.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.example.demo.jparepositories.ATMAccountDetailsRepository;
import com.example.demo.jparepositories.ATMPersonalDetailsRepository;
import com.example.demo.model.ATMAccountDetails;
import com.example.demo.model.ATMPersonalDetails;
import com.example.demo.service.AuthenticationService;
import com.example.demo.util.ServiceUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
	
	@Autowired
	ATMAccountDetailsRepository accountDetailsRepository;
	
	@Autowired
	ATMPersonalDetailsRepository personalDetailsRepository;
	
	@Autowired
    private Environment env;

	Map<String, String> authDetails = new HashMap<String, String>();
	
	@Transactional
	public String generateToken(ATMAccountDetails account) {
		String token = "";
		ATMPersonalDetails personalDetails = null;
		int count = accountDetailsRepository.getAccountDetails(account.getDebitCardNumber(), account.getCode(), account.getBank());
		if(count>0) {
			token = UUID.randomUUID().toString();
            authDetails.put(account.getDebitCardNumber(), token);
		} else {
			if(account.getBank().equals("SBI")) {
				String response = ServiceUtility.getSBIApi(account,env.getProperty("sbi.api"));
				ObjectMapper mapper = new ObjectMapper();
				try {
					personalDetails = mapper.readValue(response, ATMPersonalDetails.class);
				} catch (JsonProcessingException e) {
					throw new RuntimeException("Internal Error.");
				}
				if(personalDetails.getMsg()==null || personalDetails.getMsg().equals("")){
					personalDetails.setPersonId(null);
					personalDetails.getAccountList().get(0).setAccountId(null);
					personalDetails.getAccountList().get(0).setBank("SBI");
					personalDetails.getAccountList().get(0).setPersonalDetails(personalDetails);
					personalDetails.getAccountList().get(0).getCurrentBalanceList().get(0).setBalanceId(null);
					personalDetails.getAccountList().get(0).getCurrentBalanceList().get(0).setAccountId(personalDetails.getAccountList().get(0));
					personalDetailsRepository.saveAndFlush(personalDetails);
					if(personalDetails.getAccountList().get(0).getDebitCardNumber().equals(account.getDebitCardNumber()) 
							&& personalDetails.getAccountList().get(0).getCode().equals(account.getCode())
							&& personalDetails.getAccountStatus().equals("Active") 
							&& personalDetails.getAccountList().get(0).getCardStatus().equals("Active")) {
						token = UUID.randomUUID().toString();
						authDetails.put(account.getDebitCardNumber(), token);
						return token;
					} else {
						if(!personalDetails.getAccountList().get(0).getCode().equals(account.getCode())) {
							throw new RuntimeException("Invalid Pin");
						}
						
						if(!personalDetails.getAccountStatus().equals("Active")) {
							throw new RuntimeException("Account is deactivated.");
						}
						
						if(!personalDetails.getAccountList().get(0).getCardStatus().equals("Active")){
							throw new RuntimeException("Card is deactivated.");
						}
					}
				} else {
					throw new RuntimeException(personalDetails.getMsg());
				}
			} else {
				throw new RuntimeException("Provide valid bank.");
			}
		}
		return token;
	}

	@Override
	public boolean verifyToken(String debitCardNumber, String token) {
		String existingToken = authDetails.get(debitCardNumber);

        if (existingToken != null && existingToken.equals(token)) {
            authDetails.remove(debitCardNumber);
            return true;
        } else {
            return false;
        }
	}

}
