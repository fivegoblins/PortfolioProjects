package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.OutgoingTransfer;

public interface AccountDAO {
	
	int updateUserBalanceAfterSentTransfer(long userId, OutgoingTransfer transfer);

	int updateReceiversBalanceAfterTransfer(long userId, OutgoingTransfer transfer);

}
