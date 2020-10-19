package com.example.demo.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.example.demo.model.ATMAccountDetails;

public class ServiceUtility {
	
	
	public static String getSBIApi(ATMAccountDetails account, String apiUrl) {
		String response = "";
		try {
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
			throw new RuntimeException("Error reading the data.");
		}
		return response;
	}
	
	public static String withdraw(ATMAccountDetails account, Long amount, String apiUrl) {
		String response = "";
		try {
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
			throw new RuntimeException("Error reading the data.");
		}
		return response;
	}
	
	public static String deposit(ATMAccountDetails account, Long amount, String apiUrl) {
		String response = "";
		try {
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
			throw new RuntimeException("Error reading the data.");
		}
		return response;
	}

}
