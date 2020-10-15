package com.example.demo;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.model.ATMAccountDetails;

public class DepositWithdrawRestControllerTest extends AbstractTest {

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	public void authorizeAPIHitSuccess() throws Exception {
		String uri = "/accountTransaction/gettoken";
		ATMAccountDetails account = new ATMAccountDetails();
		account.setDebitCardNumber("");
		account.setCode("");
		account.setBank("");
		
		String inputJson = super.mapToJson(account);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
		         .contentType(MediaType.APPLICATION_JSON_VALUE)
		         .content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	}
	
	@Test
	public void authorizeAPIwithoutDebitCard() throws Exception {
		String uri = "/accountTransaction/gettoken";
		ATMAccountDetails account = new ATMAccountDetails();
		account.setDebitCardNumber("");
		account.setCode("");
		account.setBank("");
		
		String inputJson = super.mapToJson(account);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
		         .contentType(MediaType.APPLICATION_JSON_VALUE)
		         .content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    
	    String content = mvcResult.getResponse().getContentAsString();
	    assertEquals(content, "Provide debit card details");
	    
	}
	
	@Test
	public void authorizeAPIwithoutCode() throws Exception {
		String uri = "/accountTransaction/gettoken";
		ATMAccountDetails account = new ATMAccountDetails();
		account.setDebitCardNumber("112233445566");
		account.setCode("");
		account.setBank("");
		
		String inputJson = super.mapToJson(account);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
		         .contentType(MediaType.APPLICATION_JSON_VALUE)
		         .content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    
	    String content = mvcResult.getResponse().getContentAsString();
	    assertEquals(content, "Provide ATM code.");
	    
	}
	
	@Test
	public void authorizeAPIwithoutBank() throws Exception {
		String uri = "/accountTransaction/gettoken";
		ATMAccountDetails account = new ATMAccountDetails();
		account.setDebitCardNumber("112233445566");
		account.setCode("5569");
		account.setBank("");
		
		String inputJson = super.mapToJson(account);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
		         .contentType(MediaType.APPLICATION_JSON_VALUE)
		         .content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    
	    String content = mvcResult.getResponse().getContentAsString();
	    assertEquals(content, "Provide valid bank.");
	    
	}
	
	@Test
	public void authorizeAPIInvalidDebitCard() throws Exception {
		String uri = "/accountTransaction/gettoken";
		ATMAccountDetails account = new ATMAccountDetails();
		account.setDebitCardNumber("112233445577");
		account.setCode("5569");
		account.setBank("SBI");
		
		String inputJson = super.mapToJson(account);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
		         .contentType(MediaType.APPLICATION_JSON_VALUE)
		         .content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    
	    String content = mvcResult.getResponse().getContentAsString();
	    assertEquals(content, "No Record Found");
	    
	}
	
	@Test
	public void authorizeAPIInvalidPin() throws Exception {
		String uri = "/accountTransaction/gettoken";
		ATMAccountDetails account = new ATMAccountDetails();
		account.setDebitCardNumber("112233445566");
		account.setCode("5560");
		account.setBank("SBI");
		
		String inputJson = super.mapToJson(account);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
		         .contentType(MediaType.APPLICATION_JSON_VALUE)
		         .content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    
	    String content = mvcResult.getResponse().getContentAsString();
	    assertEquals(content, "Invalid Pin");
	    
	}
	
	@Test
	public void authorizeAPIInvalidBank() throws Exception {
		String uri = "/accountTransaction/gettoken";
		ATMAccountDetails account = new ATMAccountDetails();
		account.setDebitCardNumber("112233445577");
		account.setCode("5560");
		account.setBank("SBII");
		
		String inputJson = super.mapToJson(account);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
		         .contentType(MediaType.APPLICATION_JSON_VALUE)
		         .content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    
	    String content = mvcResult.getResponse().getContentAsString();
	    assertEquals(content, "Provide valid bank.");
	    
	}
	
	@Test
	public void authorizeAPITokenGenerated() throws Exception {
		String uri = "/accountTransaction/gettoken";
		ATMAccountDetails account = new ATMAccountDetails();
		account.setDebitCardNumber("112233445566");
		account.setCode("5569");
		account.setBank("SBI");
		
		String inputJson = super.mapToJson(account);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
		         .contentType(MediaType.APPLICATION_JSON_VALUE)
		         .content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    
	    String content = mvcResult.getResponse().getContentAsString();
	    System.out.println("Token Generated :"+content  );
	}
	
	@Test
	public void authorizeAPIInactiveAccount() throws Exception {
		String uri = "/accountTransaction/gettoken";
		ATMAccountDetails account = new ATMAccountDetails();
		account.setDebitCardNumber("556677889911");
		account.setCode("4567");
		account.setBank("SBI");
		
		String inputJson = super.mapToJson(account);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
		         .contentType(MediaType.APPLICATION_JSON_VALUE)
		         .content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    
	    String content = mvcResult.getResponse().getContentAsString();
	    assertEquals(content, "Account is deactivated.");
	}
	
	@Test
	public void authorizeAPIInactiveATM() throws Exception {
		String uri = "/accountTransaction/gettoken";
		ATMAccountDetails account = new ATMAccountDetails();
		account.setDebitCardNumber("445566778888");
		account.setCode("3456");
		account.setBank("SBI");
		
		String inputJson = super.mapToJson(account);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
		         .contentType(MediaType.APPLICATION_JSON_VALUE)
		         .content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    
	    String content = mvcResult.getResponse().getContentAsString();
	    assertEquals(content, "Card is deactivated.");
	}
	
	@Test
	public void balanceEnquiry() throws Exception {
		String uri = "/accountTransaction/gettoken";
		ATMAccountDetails account = new ATMAccountDetails();
		account.setDebitCardNumber("334455667788");
		account.setCode("2345");
		account.setBank("SBI");
		
		String inputJson = super.mapToJson(account);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
		         .contentType(MediaType.APPLICATION_JSON_VALUE)
		         .content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    
	    String content = mvcResult.getResponse().getContentAsString();
	    String uri1 = "/accountTransaction/checkbalance/"+content+"/"+account.getDebitCardNumber();
	    MvcResult mvcResult1 = mvc.perform(MockMvcRequestBuilders.get(uri1)
	            .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
	    int status1 = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    String content1 = mvcResult1.getResponse().getContentAsString();
	    assertEquals("500000.00", content1);
	}
	
	@Test
	public void depositSuccessful() throws Exception {
		String uri = "/accountTransaction/gettoken";
		ATMAccountDetails account = new ATMAccountDetails();
		account.setDebitCardNumber("112233445566");
		account.setCode("5569");
		account.setBank("SBI");
		account.setAmount(50000);
		
		String inputJson = super.mapToJson(account);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
		         .contentType(MediaType.APPLICATION_JSON_VALUE)
		         .content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    
	    String content = mvcResult.getResponse().getContentAsString();
	    String uri1 = "/accountTransaction/deposit/"+content+"/"+account.getDebitCardNumber()+"/"+account.getAmount();
	    MvcResult mvcResult1 = mvc.perform(MockMvcRequestBuilders.get(uri1)
	            .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
	    int status1 = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    String content1 = mvcResult1.getResponse().getContentAsString();
	    assertEquals("success", content1);
	}
	
	@Test
	public void withdrawInsufficientBalance() throws Exception {
		String uri = "/accountTransaction/gettoken";
		ATMAccountDetails account = new ATMAccountDetails();
		account.setDebitCardNumber("223344556677");
		account.setCode("1234");
		account.setBank("SBI");
		account.setAmount(10000);
		
		String inputJson = super.mapToJson(account);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
		         .contentType(MediaType.APPLICATION_JSON_VALUE)
		         .content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    
	    String content = mvcResult.getResponse().getContentAsString();
	    String uri1 = "/accountTransaction/withdraw/"+content+"/"+account.getDebitCardNumber()+"/"+account.getAmount();
	    MvcResult mvcResult1 = mvc.perform(MockMvcRequestBuilders.get(uri1)
	            .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
	    int status1 = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    String content1 = mvcResult1.getResponse().getContentAsString();
	    assertEquals("Insufficient fund", content1);
	}
	
	@Test
	public void withdrawSuccessful() throws Exception {
		String uri = "/accountTransaction/gettoken";
		ATMAccountDetails account = new ATMAccountDetails();
		account.setDebitCardNumber("112233445566");
		account.setCode("5569");
		account.setBank("SBI");
		account.setAmount(5000);
		
		String inputJson = super.mapToJson(account);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
		         .contentType(MediaType.APPLICATION_JSON_VALUE)
		         .content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    
	    String content = mvcResult.getResponse().getContentAsString();
	    String uri1 = "/accountTransaction/withdraw/"+content+"/"+account.getDebitCardNumber()+"/"+account.getAmount();
	    MvcResult mvcResult1 = mvc.perform(MockMvcRequestBuilders.get(uri1)
	            .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
	    int status1 = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    String content1 = mvcResult1.getResponse().getContentAsString();
	    assertEquals("success", content1);
	}
	
	@Test
	public void withdrawMoreThan10000Error() throws Exception {
		String uri = "/accountTransaction/gettoken";
		ATMAccountDetails account = new ATMAccountDetails();
		account.setDebitCardNumber("112233445566");
		account.setCode("5569");
		account.setBank("SBI");
		account.setAmount(13000);
		
		String inputJson = super.mapToJson(account);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
		         .contentType(MediaType.APPLICATION_JSON_VALUE)
		         .content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    
	    String content = mvcResult.getResponse().getContentAsString();
	    String uri1 = "/accountTransaction/withdraw/"+content+"/"+account.getDebitCardNumber()+"/"+account.getAmount();
	    MvcResult mvcResult1 = mvc.perform(MockMvcRequestBuilders.get(uri1)
	            .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
	    int status1 = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    String content1 = mvcResult1.getResponse().getContentAsString();
	    assertEquals("You can deposit only 10000 at once.", content1);
	}
	
	@Test
	public void depositMoreThan50000Error() throws Exception {
		String uri = "/accountTransaction/gettoken";
		ATMAccountDetails account = new ATMAccountDetails();
		account.setDebitCardNumber("112233445566");
		account.setCode("5569");
		account.setBank("SBI");
		account.setAmount(50000);
		
		String inputJson = super.mapToJson(account);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
		         .contentType(MediaType.APPLICATION_JSON_VALUE)
		         .content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    
	    String content = mvcResult.getResponse().getContentAsString();
	    String uri1 = "/accountTransaction/deposit/"+content+"/"+account.getDebitCardNumber()+"/"+account.getAmount();
	    MvcResult mvcResult1 = mvc.perform(MockMvcRequestBuilders.get(uri1)
	            .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
	    int status1 = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    String content1 = mvcResult1.getResponse().getContentAsString();
	    assertEquals("success", content1);
	}

}
