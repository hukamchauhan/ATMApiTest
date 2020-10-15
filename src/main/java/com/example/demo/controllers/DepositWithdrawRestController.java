package com.example.demo.controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Map;

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
import com.example.demo.jparepositories.AccountTransactionsRepository;
import com.example.demo.model.ATMAccountDetails;
import com.example.demo.model.ATMPersonalDetails;
import com.example.demo.model.AccountTransactions;
import com.example.demo.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@PropertySource("classpath:application.properties")
@RequestMapping("/accountTransaction")
public class DepositWithdrawRestController {
	
	@Autowired
	AuthenticationService authenticationService;
	
	@Autowired
	ATMPersonalDetailsRepository personalDetailsRepository;
	
	@Autowired
	AccountTransactionsRepository accountTransactionRepository;
	
	@Autowired
    private Environment env;
	
	@RequestMapping(value = "/gettoken", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String authorize(@RequestBody ATMAccountDetails account) {
		String token = "";
		String apiUrl = "";
		ATMPersonalDetails personalDetails = null;
		try {
			
			if(account.getDebitCardNumber()==null || account.getDebitCardNumber().equals("")) {
				token = "Provide debit card details";
				return token;
			}
			
			if(account.getCode()==null || account.getCode().equals("")) {
				token = "Provide ATM code.";
				return token;
			}
			
			if(account.getBank()==null || account.getBank().equals("")) {
				token = "Provide valid bank.";
				return token;
			}
			
			token = authenticationService.generateToken(account);
			if(token.equals("Record Not Found")) {
				if(account.getBank().equals("SBI")) {
					String response = getSBIApi(account);
					ObjectMapper mapper = new ObjectMapper();
					personalDetails = mapper.readValue(response, ATMPersonalDetails.class);
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
							token = authenticationService.generateToken(account);
							return token;
						} else {
							if(!personalDetails.getAccountList().get(0).getCode().equals(account.getCode())) {
								token = "Invalid Pin";
								return token;
							}
							
							if(!personalDetails.getAccountStatus().equals("Active")) {
								token = "Account is deactivated.";
								return token;
							}
							
							if(!personalDetails.getAccountList().get(0).getCardStatus().equals("Active")){
								token = "Card is deactivated.";
								return token;
							}
						}
					} else {
						token = personalDetails.getMsg();
						return token;
					}
				} else {
					token = "Provide valid bank.";
					return token;
				}
			} 
		} catch(Exception e) {
			e.printStackTrace();
		}
        return token;
    }
	
	@RequestMapping("/withdraw/{token}/{debitcardnumber}/{amount}")
    public @ResponseBody String withdraw(@PathVariable(value = "token") String token, @PathVariable(value = "debitcardnumber") String debitcardnumber, @PathVariable(value = "amount") String amount) {
        try {
        	if(token==null || token.equals("")) {
        		return "Provide Token";
        	}
        	
        	if(debitcardnumber == null || debitcardnumber.equals("")) {
        		return "Provide debit card number";
        	}
        	
        	if(amount == null || amount.equals("") ) {
        		return "Provide amount";
        	}
        	
        	Long amount1;
        	try {
        		amount1 = new Long(amount);
        	} catch(NumberFormatException e) {
        		e.printStackTrace();
        		return "Please provide correct amount without decimals.";
        	}
        	
        	if(amount1>10000) {
        		return "You can withdraw only 10000 at once.";
        	}
        	
            if (authenticationService.verifyToken(debitcardnumber, token)) {
            	ATMAccountDetails account = new ATMAccountDetails();
            	account.setDebitCardNumber(debitcardnumber);
            	String response = getSBIApi(account);
            	ObjectMapper mapper = new ObjectMapper();
				ATMPersonalDetails personalDetails = mapper.readValue(response, ATMPersonalDetails.class);
				if(personalDetails.getAccountList().get(0).getCurrentBalanceList().get(0).getCurrent_balance().compareTo(BigDecimal.valueOf(amount1))>=0) {
					String status = withdraw(account, amount1);
					if(status.equals("success")) {
						AccountTransactions at = new AccountTransactions();
						at.setTransactionAmount(amount1.intValue());
						at.setTransactionStatus(status);
						at.setTransactionTime(new Date());
						at.setTypeOfTransaction("Debit");
						at.setAtmId("XXXXXXXXXX");
						at.setAccountTransactionId(personalDetails.getAccountList().get(0).getAccountId());
						accountTransactionRepository.save(at);
					}
					return status;
				} else {
					return "Insufficient fund";
				}
            } else {
                return "Invalid Token";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occured while updating your account";
        }
    }
	
	@RequestMapping("/deposit/{token}/{debitcardnumber}/{amount}")
    public @ResponseBody String deposit(@PathVariable(value = "token") String token, @PathVariable(value = "debitcardnumber") String debitcardnumber, @PathVariable(value = "amount") String amount) {
        try {
        	
        	if(token==null || token.equals("")) {
        		return "Provide Token";
        	}
        	
        	if(debitcardnumber == null || debitcardnumber.equals("")) {
        		return "Provide debit card number";
        	}
        	
        	if(amount == null || amount.equals("") ) {
        		return "Provide amount";
        	}
        	
        	Long amount1;
        	try {
        		amount1 = new Long(amount);
        	} catch(NumberFormatException e) {
        		e.printStackTrace();
        		return "Please provide correct amount without decimals.";
        	}
        	
        	if(amount1>50000) {
        		return "You can deposit only 50000 at once.";
        	}
        	
            if (authenticationService.verifyToken(debitcardnumber, token)) {
            	ATMAccountDetails account = new ATMAccountDetails();
            	account.setDebitCardNumber(debitcardnumber);
            	String response = getSBIApi(account);
            	ObjectMapper mapper = new ObjectMapper();
				ATMPersonalDetails personalDetails = mapper.readValue(response, ATMPersonalDetails.class);
				String status = deposit(account, amount1);
				if(status.equals("success")) {
					AccountTransactions at = new AccountTransactions();
					at.setTransactionAmount(amount1.intValue());
					at.setTransactionStatus(status);
					at.setTransactionTime(new Date());
					at.setTypeOfTransaction("Credit");
					at.setAtmId("XXXXXXXXXX");
					at.setAccountTransactionId(personalDetails.getAccountList().get(0).getAccountId());
					accountTransactionRepository.save(at);
				}
				return status;
            } else {
                return "Invalid Token";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occured while updating your account";
        }
    }
	
	@RequestMapping("/checkbalance/{token}/{debitcardnumber}")
    public @ResponseBody BigDecimal checkBalance(@PathVariable(value = "token") String token, @PathVariable(value = "debitcardnumber") String debitcardnumber) {
        try {
            if (authenticationService.verifyToken(debitcardnumber, token)) {
            	ATMAccountDetails account = new ATMAccountDetails();
            	account.setDebitCardNumber(debitcardnumber);
            	String response = getSBIApi(account);
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
	
	public String getSBIApi(ATMAccountDetails account) {
		String response = "";
		String apiUrl = "";
		try {
			apiUrl = env.getProperty("sbi.api");
			URL url = new URL(apiUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			//conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json; charset=utf8");
			String input = "{\"debitCardNumber\":\""+account.getDebitCardNumber()+"\"}";
			OutputStream os = conn.getOutputStream();
			os.write(input.toString().getBytes());
			os.flush();
			
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output="";
			while ((output = br.readLine()) != null) {
				response = output;
			}
			conn.disconnect();
		} catch(Exception e) {
			
		}
		return response;
	}
	
	public String withdraw(ATMAccountDetails account, Long amount) {
		String response = "";
		String apiUrl = "";
		try {
			apiUrl = env.getProperty("sbi.withdraw");
			URL url = new URL(apiUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=utf8");
			String input = "{\"debitCardNumber\":\""+account.getDebitCardNumber()+"\",\"amount\":\""+amount+"\"}";
			OutputStream os = conn.getOutputStream();
			os.write(input.toString().getBytes());
			os.flush();
			
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output="";
			while ((output = br.readLine()) != null) {
				response = output;
			}
			conn.disconnect();
		} catch(Exception e) {
			
		}
		return response;
	}
	
	public String deposit(ATMAccountDetails account, Long amount) {
		String response = "";
		String apiUrl = "";
		try {
			apiUrl = env.getProperty("sbi.deposit");
			URL url = new URL(apiUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			//conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json; charset=utf8");
			String input = "{\"debitCardNumber\":\""+account.getDebitCardNumber()+"\",\"amount\":\""+amount+"\"}";
			OutputStream os = conn.getOutputStream();
			os.write(input.toString().getBytes());
			os.flush();
			
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output="";
			while ((output = br.readLine()) != null) {
				response = output;
			}
			conn.disconnect();
		} catch(Exception e) {
			
		}
		return response;
	}

}
