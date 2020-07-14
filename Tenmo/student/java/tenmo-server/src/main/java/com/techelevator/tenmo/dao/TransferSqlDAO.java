package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.tenmo.model.OutgoingTransfer;
import com.techelevator.tenmo.model.Transfer;

public class TransferSqlDAO implements TransferDAO {
	private static final double STARTING_BALANCE = 1000;
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public List<Transfer> retrieveTransfersByUserId(long id) {
    	ArrayList<Transfer> transfers = new ArrayList<>();
    	String sqlGetTransfersByUserId = "SELECT t.transfer_id, tt.transfer_type_desc, ts.transfer_status_desc, u.username AS \"from\", "
    									+ "us.username AS \"to\", t.amount " 
    									+ "FROM transfers t JOIN transfer_types tt ON t.transfer_type_id = tt.transfer_type_id " 
    									+ "JOIN transfer_statuses ts ON t.transfer_status_id = ts.transfer_status_id "
    									+ "JOIN accounts a ON t.account_to = a.account_id "
    									+ "JOIN users us ON a.user_id = us.user_id "
    									+ "JOIN accounts ac ON t.account_from = ac.account_id "
    									+ "JOIN users u ON ac.user_id = u.user_id "
    									+ "WHERE t.account_from = ? OR t.account_to = ?";
    	
    	SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetTransfersByUserId, id, id);
    	
    	while(results.next()) {
    		Transfer transferInfo = mapRowToTransfer(results);
    		transfers.add(transferInfo);
    	}
    	
    	return transfers;
    }
    
    @Override
    	public List<Transfer> getAllTransfersSentByUser(long accountFrom) {
    	ArrayList<Transfer> transfers = new ArrayList<>();
    	String sqlGetTransfer = "SELECT * FROM transfers WHERE account_from = ?";
    	
    	SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetTransfer, accountFrom);
    	
    	while(results.next()) {
    		Transfer sentTransfers = mapRowToTransfer(results);
    		transfers.add(sentTransfers);
    	}
    	return transfers;
    }
    
    @Override
	public List<Transfer> getAllTransfersReceivedByUser(long accountTo) {
	ArrayList<Transfer> transfers = new ArrayList<>();
	String sqlGetTransfer = "SELECT * FROM transfers WHERE account_to = ?";
	
	SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetTransfer, accountTo);
	
	while(results.next()) {
		Transfer receivedTransfers = mapRowToTransfer(results);
		transfers.add(receivedTransfers);
	}
	return transfers;
}
    
    @Override
    public Transfer getTransferDetailsWithTransferId(long id) {
    	String sqlGetTransfer = "SELECT t.transfer_id, tt.transfer_type_desc, ts.transfer_status_desc, u.username AS \"from\", "
				+ "us.username AS \"to\", t.amount " 
				+ "FROM transfers t JOIN transfer_types tt ON t.transfer_type_id = tt.transfer_type_id " 
				+ "JOIN transfer_statuses ts ON t.transfer_status_id = ts.transfer_status_id "
				+ "JOIN accounts a ON t.account_to = a.account_id "
				+ "JOIN users us ON a.user_id = us.user_id "
				+ "JOIN accounts ac ON t.account_from = ac.account_id "
				+ "JOIN users u ON ac.user_id = u.user_id "
				+ "WHERE t.transfer_id = ? ";
    	SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetTransfer, id);
    	
    	if(results.next()) {
    		return mapRowToTransfer(results);
    	}
    		return null;
    }
    
    @Override
    public int sendTransferToUser(long accountFrom, OutgoingTransfer transfer) {
    	String sqlsendTransfer = "INSERT INTO transfers (transfer_type_id, transfer_status_id, "
    							+ "account_from, account_to, amount) "
    							+ "VALUES (2, 2, ?, ?, ?)";
    	int results = jdbcTemplate.update(sqlsendTransfer, accountFrom, transfer.getAccountTo(), transfer.getAmount());
    	
    	return results;
    }
    
    @Override
    public int sendRequestToUser(long accountFrom, OutgoingTransfer transfer) {
    	String sqlSendRequest = "INSERT INTO transfers (transfer_type_id, transfer_status_id, "
				+ "account_from, account_to, amount) "
				+ "VALUES (1, 1, ?, ?, ?)";
    	return jdbcTemplate.update(sqlSendRequest, accountFrom, transfer.getAccountTo(), transfer.getAmount());
    }
    
    private Transfer mapRowToTransfer(SqlRowSet rs) {
    	Transfer transfer = new Transfer();
    	transfer.setId(rs.getLong("transfer_id"));
    	transfer.setTransferType(rs.getString("transfer_type_desc"));
    	transfer.setTransferStatus(rs.getString("transfer_status_desc"));
    	transfer.setAccountFrom(rs.getString("from"));
    	transfer.setAccountTo(rs.getString("to"));
    	transfer.setAmount(rs.getBigDecimal("amount"));
    	
    	return transfer;
    }

}
