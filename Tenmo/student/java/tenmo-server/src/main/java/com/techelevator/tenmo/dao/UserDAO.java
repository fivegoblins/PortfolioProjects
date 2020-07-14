package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.OutgoingTransfer;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

public interface UserDAO {

    List<User> findAll();

    User findByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password);

	BigDecimal getBalanceByUserid(long id);

	List<Transfer> retrieveTransfersByUserId(long id);
	
	List<Transfer> retrievePendingRequests(long id);
	
	User getLoggedInUser(String username);

	Transfer getTransferDetailsWithTransferId(long id);

	List<Transfer> getAllTransfersSentByUser(long accountFrom);

	List<Transfer> getAllTransfersReceivedByUser(long accountTo);

	public int sendTransferToUser(long accountFrom, OutgoingTransfer transfer);
	
	public int requestTransferFromUser(long accountFrom, OutgoingTransfer transfer);
	
	int updateUserBalanceAfterSentTransfer(long userId, OutgoingTransfer transfer);

	int updateReceiversBalanceAfterTransfer(long userId, OutgoingTransfer transfer);
	
	int acceptOrRejectRequest(long transferId, OutgoingTransfer transfer);
	
}
