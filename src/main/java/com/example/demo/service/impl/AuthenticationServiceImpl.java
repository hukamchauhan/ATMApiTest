package com.example.demo.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.jparepositories.ATMAccountDetailsRepository;
import com.example.demo.model.ATMAccountDetails;
import com.example.demo.service.AuthenticationService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
	
	@Autowired
	ATMAccountDetailsRepository accountDetailsRepository;

	Map<String, String> authDetails = new HashMap<String, String>();
	
	@Override
	public String generateToken(ATMAccountDetails account) {
		String token = "";
		int count = accountDetailsRepository.getAccountDetails(account.getDebitCardNumber(), account.getCode(), account.getBank());
		if(count>0) {
			token = UUID.randomUUID().toString();
            authDetails.put(account.getDebitCardNumber(), token);
		} else {
			token = "Record Not Found";
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

	@Override
	public void getAccountNumber() {
		
	}

}
