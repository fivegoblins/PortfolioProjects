package com.techelevator.tenmo.services;

import java.math.BigDecimal;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;


import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.OutgoingTransfer;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

public class TenmoService {
	private static String AUTH_TOKEN = "";
	private final String API_BASE_URL;
	public RestTemplate restTemplate = new RestTemplate();
	
	private AuthenticatedUser currentUser;
	
	public TenmoService(String url) {
		API_BASE_URL = url;
	}
	
	
	public Transfer[] getTransferHistory(AuthenticatedUser currentUser) throws TenmoServiceException {
		Transfer[] transfers = null;
		AUTH_TOKEN = currentUser.getToken();
		
		try {
			transfers = restTemplate.exchange(API_BASE_URL + "users/" + (long)currentUser.getUser().getId() + "/transfers",
					HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();	
		} catch (RestClientResponseException e) {
			throw new TenmoServiceException(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
		}
				
		return transfers;
	}
	
	public Transfer getTransferByTransferId(AuthenticatedUser currentUser, long transferId) throws TenmoServiceException {
		Transfer transfer = new Transfer();
		AUTH_TOKEN = currentUser.getToken();
		
		try {
			transfer = restTemplate.exchange(API_BASE_URL + "users/" + (long)currentUser.getUser().getId() + "/transfers/" 
					+ transferId, HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();
		} catch (RestClientResponseException e) {
			throw new TenmoServiceException(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
		}
		return transfer;
	}
	
	public Transfer[] getPendingRequests(AuthenticatedUser currentUser) throws TenmoServiceException {
		Transfer[] transfers = null;
		AUTH_TOKEN = currentUser.getToken();
		
		try {
			transfers = restTemplate.exchange(API_BASE_URL + "users/" + (long)currentUser.getUser().getId() + "/transfers/pending", 
					HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
		} catch (RestClientResponseException e) {
			throw new TenmoServiceException(e.getRawStatusCode() + ": " + e.getResponseBodyAsString());
		}
		
		return transfers;
	}
	
	public BigDecimal getAccountBalance(AuthenticatedUser currentUser) throws TenmoServiceException {
		BigDecimal currentBalance;
		AUTH_TOKEN = currentUser.getToken();
		try {
			currentBalance = restTemplate.exchange(API_BASE_URL + "users/" + (long)currentUser.getUser().getId() + "/balance",
					HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
		} catch (RestClientResponseException e) {
			throw new TenmoServiceException(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
		}
		
		return currentBalance;
	}
	
	public User[] listAllUsers(AuthenticatedUser currentUser) throws TenmoServiceException {
		User[] users = null;
		AUTH_TOKEN = currentUser.getToken();
		try {
			users = restTemplate.exchange(API_BASE_URL + "users", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
		} catch (RestClientResponseException e) {
			throw new TenmoServiceException(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
		}
		return users;
	}
	
	 public OutgoingTransfer addTransfer(AuthenticatedUser currentUser, long accountTo, BigDecimal amount) throws TenmoServiceException {
	        OutgoingTransfer transfer = makeOutgoingTransfer(currentUser, accountTo, amount);
	        
	        try {
	            transfer = restTemplate.postForObject(API_BASE_URL + "users/" + transfer.getAccountFrom() + "/transfers/send", 
	            		makeTransferEntity(transfer), OutgoingTransfer.class);
	        } catch (RestClientResponseException ex) {
	            throw new TenmoServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
	        }
	        return transfer;
	    }
	 
	 public OutgoingTransfer addRequest(AuthenticatedUser currentUser, long accountTo, BigDecimal amount) throws TenmoServiceException {
		 OutgoingTransfer transfer = makeOutgoingTransfer(currentUser, accountTo, amount);
		 try {
			 transfer = restTemplate.postForObject(API_BASE_URL + "users/" + transfer.getAccountFrom() + "/transfers/request", 
	            		makeTransferEntity(transfer), OutgoingTransfer.class);
		 } catch (RestClientResponseException e) {
			 throw new TenmoServiceException(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
		 }
		 return transfer;
	 }
	 
	 
	 public OutgoingTransfer update(AuthenticatedUser currentUser, long accountTo, BigDecimal amount) throws TenmoServiceException {
	        OutgoingTransfer transfer = makeOutgoingTransfer(currentUser, accountTo, amount);
	        try {
	            restTemplate.put(API_BASE_URL + "users/" + currentUser.getUser().getId() + "/balance/withdraw", makeTransferEntity(transfer));
	        } catch (RestClientResponseException ex) {
	            throw new TenmoServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
	        }
	        return transfer;
	    }
	
	 public OutgoingTransfer updateReceiver(AuthenticatedUser currentUser, long accountTo, BigDecimal amount) throws TenmoServiceException {
	        OutgoingTransfer transfer = makeOutgoingTransfer(currentUser, accountTo, amount);
	        try {
	            restTemplate.put(API_BASE_URL + "users/" + accountTo + "/balance/deposit", makeTransferEntity(transfer));
	        } catch (RestClientResponseException ex) {
	            throw new TenmoServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
	        }
	        return transfer;
	    }
	 
	 public OutgoingTransfer updateStatus(AuthenticatedUser currentUser, long transferId, long status) {
		 OutgoingTransfer transfer = new OutgoingTransfer();
		 transfer.setTransferStatus(status);
		 
		 restTemplate.put(API_BASE_URL  + "users/" + (long)currentUser.getUser().getId() + "/transfers/" + transferId, makeTransferEntity(transfer));
		 return transfer;
	 }
	 
	public OutgoingTransfer updateRequestBalance(long userId, BigDecimal amount) { 
		OutgoingTransfer transfer = new OutgoingTransfer();
		transfer.setAccountFrom(userId);
		transfer.setAmount(amount);
		restTemplate.put(API_BASE_URL + "users/" + userId + "/balance/deposit", makeTransferEntity(transfer));
		
		return transfer;
	}
	 
	 
	private OutgoingTransfer makeOutgoingTransfer(AuthenticatedUser currentUser, long accountTo, BigDecimal amount) {
		OutgoingTransfer transfer = new OutgoingTransfer();
		transfer.setAccountTo(accountTo);
		transfer.setAccountFrom((long)currentUser.getUser().getId());
		transfer.setAmount(amount);
		
		return transfer;
	}
	
	 private HttpEntity<OutgoingTransfer> makeTransferEntity(OutgoingTransfer transfer) {
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.setBearerAuth(AUTH_TOKEN);
	        HttpEntity<OutgoingTransfer> entity = new HttpEntity<>(transfer, headers);
	        return entity;
	    }
	
	private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }
}
