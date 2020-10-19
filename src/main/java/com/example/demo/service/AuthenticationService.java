package com.example.demo.service;

import com.example.demo.model.ATMAccountDetails;;

public interface AuthenticationService {
	
	String generateToken(ATMAccountDetails account);

    boolean verifyToken(String accountNumber, String token);

}
