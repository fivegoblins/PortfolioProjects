package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.OutgoingTransfer;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

@RestController
@PreAuthorize("isAuthenticated()")
public class TenmoController {
	private UserDAO userDAO;

	
	public TenmoController(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	@RequestMapping(path = "/users", method = RequestMethod.GET)
	public List<User> getAllUsers() {
		return userDAO.findAll();	
	}
	
	@RequestMapping(path = "/users/{username}", method = RequestMethod.GET)
	public User getUserByUsername(@PathVariable String username) {
		return userDAO.findByUsername(username);
	}

	@RequestMapping(path = "/users/{id}/balance", method = RequestMethod.GET)
	public BigDecimal getAccountBalance(@PathVariable long id, Principal principal) {
		return userDAO.getBalanceByUserid(id);
	}
	
	@RequestMapping(path = "/users/{id}/transfers", method = RequestMethod.GET)
	public List<Transfer> getTransferHistory(@PathVariable long id, Principal principal) {
		List<Transfer> transfers = new ArrayList<>();
		transfers = userDAO.retrieveTransfersByUserId(id);
		return transfers;
	}

	@RequestMapping(path = "/users/{id}/transfers/{transferId}", method = RequestMethod.GET)
	public Transfer getTransferById(@PathVariable long id, @PathVariable long transferId) {
		Transfer transfer = new Transfer();
		transfer = userDAO.getTransferDetailsWithTransferId(transferId);
		return transfer;
	}
	
	@RequestMapping(path = "/users/{id}/balance/withdraw", method = RequestMethod.PUT)
	public void updateUserAccountBalance(@PathVariable long id, @RequestBody OutgoingTransfer transfer) {
		userDAO.updateUserBalanceAfterSentTransfer(id, transfer);
	}
	
	@RequestMapping(path = "/users/{id}/balance/deposit", method = RequestMethod.PUT)
	public void updateReceiverAccountBalance(@PathVariable long id, @RequestBody OutgoingTransfer transfer) {
		userDAO.updateReceiversBalanceAfterTransfer(id, transfer);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "/users/{id}/transfers/send", method = RequestMethod.POST)
	public int sendTenmoBucks(@PathVariable long id, @RequestBody OutgoingTransfer transfer) {
		int results = userDAO.sendTransferToUser(id, transfer);
		return results;
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "/users/{id}/transfers/request", method = RequestMethod.POST)
	public int requestTenmoBucks(@PathVariable long id, @RequestBody OutgoingTransfer transfer) {
		int results = userDAO.requestTransferFromUser(id, transfer);
		return results;
	}
	
	@RequestMapping(path = "/users/{id}/transfers/pending", method = RequestMethod.GET)
	public List<Transfer> getPendingRequests(@PathVariable long id) {
		return userDAO.retrievePendingRequests(id);
	}
	
	@RequestMapping(path = "/users/{id}/transfers/{transferId}", method = RequestMethod.PUT)
	public void updateTransferStatus(@PathVariable long id, @PathVariable long transferId, @RequestBody OutgoingTransfer transfer) {
		userDAO.acceptOrRejectRequest(transferId, transfer);
	}
}
