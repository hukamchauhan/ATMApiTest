package com.example.demo.service;

public interface AccountUpdateService {
	
	double checkBalance(long accountNumber);

	boolean withdraw(long accountNumber, int amount);

	boolean deposit(long accountNumber, int amount);

}
