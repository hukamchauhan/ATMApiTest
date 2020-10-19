package com.example.demo.service;

public interface AccountUpdateService {
	
	String withdraw(String accountNumber, Long amount);

	String deposit(String accountNumber, Long amount);

}
