package com.sfi.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class ClientTest {
	private final static Logger logger = LoggerFactory
			.getLogger(ClientTest.class);
	public ClientTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RestTemplate restTemplate = new RestTemplate();
		final String BASE_URL = "http://localhost:8088/";
		String Post_NTSNumber_URL = BASE_URL + "/saveAsset";
		/*//test for posting to product table 
		final String json4Product= "{"
				+ "\"product_name\":\"test\","
				+ "\"product_price\":4,"
			+ "}";*/
		final String json4Product= "{"
				+ "\"bid\":\"e8b490b63ab17f4963b901a44aa2c403\","
				+ "\"date_time\":\"04192018044850\","
				+ "\"managed_by\":\"Jason_Ni\","
				+ "\"temperature\":22.4,"
				+ "\"light\":0.4,"
				+ "\"is_moving\":true,"
				+ "\"transaction_type\":\"motion\","
				+ "\"location_x\":12.1,"
				+ "\"location_y\":10.2,"
				+ "\"pressure\":1003.4,"
			+ "}";
				HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(
				json4Product, headers);
		
		String output = restTemplate.postForObject(Post_NTSNumber_URL, request,String.class);
		logger.info(output);
	}

}
