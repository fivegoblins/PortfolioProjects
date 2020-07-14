package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.techelevator.tenmo.model.OutgoingTransfer;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

@Service
public class UserSqlDAO implements UserDAO {

    private static final double STARTING_BALANCE = 1000;
    private JdbcTemplate jdbcTemplate;

    public UserSqlDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int findIdByUsername(String username) {
        return jdbcTemplate.queryForObject("select user_id from users where username = ?", int.class, username);
    }
    
    @Override
    public BigDecimal getBalanceByUserid(long id) {
    	return jdbcTemplate.queryForObject("SELECT balance FROM accounts WHERE user_id = ?", BigDecimal.class, id);
    }
    
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
    public List<Transfer> retrievePendingRequests(long id) {
    	ArrayList<Transfer> transfers = new ArrayList<>();
    	String sqlGetTransfersByUserId = "SELECT t.transfer_id, tt.transfer_type_desc, ts.transfer_status_desc, u.username AS \"from\", "
    									+ "us.username AS \"to\", t.amount " 
    									+ "FROM transfers t JOIN transfer_types tt ON t.transfer_type_id = tt.transfer_type_id " 
    									+ "JOIN transfer_statuses ts ON t.transfer_status_id = ts.transfer_status_id "
    									+ "JOIN accounts a ON t.account_to = a.account_id "
    									+ "JOIN users us ON a.user_id = us.user_id "
    									+ "JOIN accounts ac ON t.account_from = ac.account_id "
    									+ "JOIN users u ON ac.user_id = u.user_id "
    									+ "WHERE t.account_to = ? AND tt.transfer_type_id = 1 AND ts.transfer_status_id = 1";
    	
    	SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetTransfersByUserId, id);
    	
    	while(results.next()) {
    		Transfer transferInfo = mapRowToTransfer(results);
    		transfers.add(transferInfo);
    	}
    	
    	return transfers;
    }
    
    @Override
    public int acceptOrRejectRequest(long transferId, OutgoingTransfer transfer) {
    	String sqlSetTransferStatus = "UPDATE transfers SET transfer_status_id = ? WHERE transfer_id = ?";
    	return jdbcTemplate.update(sqlSetTransferStatus, transfer.getTransferStatus(), transferId);
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
    public User getLoggedInUser(String username) {
    	User user = new User();
    	String sqlGetLoggedInUser = "SELECT * FROM users WHERE username = ?";
    	SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetLoggedInUser, username);
    	
    	if (results.next()) {
    		user = mapRowToUser(results);
    	}
    	
    	return user;
    }
    
    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "select * from users";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            User user = mapRowToUser(results);
            users.add(user);
        }

        return users;
    }

    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {
        for (User user : this.findAll()) {
            if( user.getUsername().toLowerCase().equals(username.toLowerCase())) {
                return user;
            }
        }
        throw new UsernameNotFoundException("User " + username + " was not found.");
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
    public int requestTransferFromUser(long accountFrom, OutgoingTransfer transfer) {
    	String sqlRequestTransfer = "INSERT INTO transfers (transfer_type_id, transfer_status_id, "
				+ "account_from, account_to, amount) "
				+ "VALUES (1, 1, ?, ?, ?)";
    	int results = jdbcTemplate.update(sqlRequestTransfer, accountFrom, transfer.getAccountTo(), transfer.getAmount());
    	return results;
    }
    
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
    
    @Override
    public boolean create(String username, String password) {
        boolean userCreated = false;
        boolean accountCreated = false;

        // create user
        String insertUser = "insert into users (username,password_hash) values(?,?)";
        String password_hash = new BCryptPasswordEncoder().encode(password);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String id_column = "user_id";
        userCreated = jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(insertUser, new String[]{id_column});
                    ps.setString(1, username);
                    ps.setString(2,password_hash);
                    return ps;
                }
                , keyHolder) == 1;
        int newUserId = (int) keyHolder.getKeys().get(id_column);

        // create account
        String insertAccount = "insert into accounts (user_id,balance) values(?,?)";
        accountCreated = jdbcTemplate.update(insertAccount,newUserId,STARTING_BALANCE) == 1;

        return userCreated && accountCreated;
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

    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setActivated(true);
        user.setAuthorities("ROLE_USER");
        return user;
    }
    
}
