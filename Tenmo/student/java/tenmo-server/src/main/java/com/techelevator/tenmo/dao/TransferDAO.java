package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.OutgoingTransfer;
import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO {

	Transfer getTransferDetailsWithTransferId(long id);

	List<Transfer> getAllTransfersSentByUser(long accountFrom);

	List<Transfer> getAllTransfersReceivedByUser(long accountTo);
	
	List<Transfer> retrieveTransfersByUserId(long id);
	
	public int sendTransferToUser(long accountFrom, OutgoingTransfer transfer);
	
	public int sendRequestToUser(long accountFrom, OutgoingTransfer transfer);

}
