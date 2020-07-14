package com.techelevator.tenmo.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.tenmo.model.OutgoingTransfer;

public class AccountSqlDAO implements AccountDAO {
    private static final double STARTING_BALANCE = 1000;
	private JdbcTemplate jdbcTemplate;

	 @Override
	    public int updateUserBalanceAfterSentTransfer(long userId, OutgoingTransfer transfer) {
	    	String sqlUpdateBalance = "UPDATE accounts SET balance = balance - ? "
	    							+ "WHERE user_id = ? ";
	    	
	    	int results = jdbcTemplate.update(sqlUpdateBalance, transfer.getAmount(), userId);
	    	
	    	return results;
	    }
	    
    @Override
    public int updateReceiversBalanceAfterTransfer(long userId, OutgoingTransfer transfer) {
    	String sqlUpdateBalance = "UPDATE accounts SET balance = balance + ? "
    							+ "WHERE user_id = ? ";
    	
    	int results =jdbcTemplate.update(sqlUpdateBalance, transfer.getAmount(), userId);
    	
    	return results;
    }
    
}
