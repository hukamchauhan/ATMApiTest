package com.example.demo.controllers;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.jparepositories.ATMPersonalDetailsRepository;
import com.example.demo.model.ATMAccountDetails;
import com.example.demo.model.ATMPersonalDetails;
import com.example.demo.service.AccountUpdateService;
import com.example.demo.service.AuthenticationService;
import com.example.demo.util.ServiceUtility;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@PropertySource("classpath:application.properties")
@RequestMapping("/accountTransaction")
public class DepositWithdrawRestController {

	@Autowired
	AuthenticationService authenticationService;

	@Autowired
	AccountUpdateService accountUpdateService;

	@Autowired
	ATMPersonalDetailsRepository personalDetailsRepository;
	
	@Autowired
    private Environment env;

	@RequestMapping(value = "/gettoken", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String authorize(@RequestBody ATMAccountDetails account) {
		String token = "";
		try {

			if (account.getDebitCardNumber() == null || account.getDebitCardNumber().equals("")) {
				token = "Provide debit card details";
				return token;
			}

			if (account.getCode() == null || account.getCode().equals("")) {
				token = "Provide ATM code.";
				return token;
			}

			if (account.getBank() == null || account.getBank().equals("")) {
				token = "Provide valid bank.";
				return token;
			}

			token = authenticationService.generateToken(account);

		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return token;
	}

	@RequestMapping("/withdraw/{token}/{debitcardnumber}/{amount}")
	public @ResponseBody String withdraw(@PathVariable(value = "token") String token,
			@PathVariable(value = "debitcardnumber") String debitcardnumber,
			@PathVariable(value = "amount") String amount) {
		try {

			Long amount1;
			try {
				amount1 = new Long(amount);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return "Please provide correct amount without decimals.";
			}

			if (amount1 > 10000) {
				return "You can withdraw only 10000 at once.";
			}

			if (authenticationService.verifyToken(debitcardnumber, token)) {
				return accountUpdateService.withdraw(debitcardnumber, amount1);
			} else {
				return "Invalid Token";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@RequestMapping("/deposit/{token}/{debitcardnumber}/{amount}")
	public @ResponseBody String deposit(@PathVariable(value = "token") String token,
			@PathVariable(value = "debitcardnumber") String debitcardnumber,
			@PathVariable(value = "amount") String amount) {
		try {

			Long amount1;
			try {
				amount1 = new Long(amount);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return "Please provide correct amount without decimals.";
			}

			if (amount1 > 50000) {
				return "You can deposit only 50000 at once.";
			}

			if (authenticationService.verifyToken(debitcardnumber, token)) {
				return accountUpdateService.deposit(debitcardnumber, amount1);
			} else {
				return "Invalid Token";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@RequestMapping("/checkbalance/{token}/{debitcardnumber}")
	public @ResponseBody BigDecimal checkBalance(@PathVariable(value = "token") String token,
			@PathVariable(value = "debitcardnumber") String debitcardnumber) {
		try {
			if (authenticationService.verifyToken(debitcardnumber, token)) {
				ATMAccountDetails account = new ATMAccountDetails();
				account.setDebitCardNumber(debitcardnumber);
				String response = ServiceUtility.getSBIApi(account,env.getProperty("sbi.api"));
				ObjectMapper mapper = new ObjectMapper();
				ATMPersonalDetails personalDetails = mapper.readValue(response, ATMPersonalDetails.class);
				return personalDetails.getAccountList().get(0).getCurrentBalanceList().get(0).getCurrent_balance();
			} else {
				return BigDecimal.valueOf(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return BigDecimal.valueOf(0);
		}
	}

}
