package com.org.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.org.model.Bank;

public class ServiceImpl implements Service {
    private static Scanner sn = new Scanner(System.in);

	@Override
	public void createaccount() {
		
		try {
			Connection conn = Bank.db_connect();
			
			System.out.println("Enter customer_id:");
			int cid = sn.nextInt();
			
			System.out.println("Enter account type (1.savings  2. current)");
			int id = sn.nextInt();
			String type ;
			if(id == 1) {
				type = "savings";
			}else {
				type = "current";
			}
			
			
			System.out.println("Enter amount:");
			Double balance = sn.nextDouble();
			
			System.out.println("Enter address:");
			String address = sn.next();
			
			System.out.println("Enter mobileNo:");
			String mobile = sn.next();
			
			
			String sql = "INSERT INTO account(customer_id , account_type , balance , address ,mobile ) VALUES (?,?,?,?,?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cid);
			pstmt.setString(2, type);
			pstmt.setDouble(3,balance);
			pstmt.setString(4, address);
			pstmt.setString(5, mobile);
			pstmt.executeUpdate();
			
			
			
			String query = "SELECT account_id FROM account WHERE customer_id = ? AND account_type =?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, cid);
			pstmt.setString(2, type);
			ResultSet rs = pstmt.executeQuery();
			int accountID = rs.next() ? rs.getInt("account_id"): 0;
			
			
			if(type.equals("savings")){
				String query2 = "INSERT INTO savings_account(account_id,interest_rate) VALUES (?,?) ";
				pstmt = conn.prepareStatement(query2);
				pstmt.setInt(1, accountID);
				pstmt.setDouble(2, 0.05); // Default interest rate
				pstmt.executeUpdate();
 			}else {
 				String query3 = "INSERT INTO current_account(account_id,overdraft_limit) values (?,?)";
 				pstmt = conn.prepareStatement(query3);
 				pstmt.setInt(1, accountID);
 				pstmt.setDouble(2, 1000.0);
 				pstmt.executeUpdate();
 				
 			}
			
			System.out.println("data insert successfully");
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}

	@Override
	public void viewaccount() {
		try {
			System.out.println("Enter account id:");
			int aid = sn.nextInt();
			Connection conn = Bank.db_connect();
			String sql = "select * from account where account_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, aid);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				System.out.println("account id: "+rs.getInt("account_id"));
				System.out.println("customer id: "+rs.getInt("customer_id"));
				System.out.println("balance: "+rs.getDouble("balance"));
				System.out.println("account_type: "+rs.getString("account_type"));
						

			}
			
		}catch(SQLException e) {
			
			e.printStackTrace();
			
		}
	}

	@Override
	public void updateaccount() {
		try {
			System.out.println("Enter account id:");
			int aid = sn.nextInt();
			
			System.out.println("Enter address:");
			String address = sn.next();
			
			System.out.println("Enter mobileNo:");
			String mobile = sn.next();
			
			Connection conn = Bank.db_connect();
			String sql = "UPDATE account SET address = ? , mobile = ? WHERE account_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, address);
			pstmt.setString(2, mobile);
			pstmt.setInt(3, aid);
			pstmt.executeUpdate();

			System.out.println("Account info updated successfully..");
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void withdraw() {
		try {
			Connection conn = Bank.db_connect();
			System.out.println("Enter account id: ");
			int aid = sn.nextInt();
			
			System.out.println("Enter withdraw amount:");
			double amount = sn.nextDouble();
			
			String query = "SELECT * FROM account WHERE account_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, aid);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {

				String account = rs.getString("account_type");
				
				if(!account.equals("Current")) {
					System.out.println(account);
					double balance = rs.getDouble("balance");
					if(balance >= amount) {
						query = "INSERT INTO transaction(account_id,transaction_type,amount) VALUES (?,?,?)";
						pstmt = conn.prepareStatement(query);
						pstmt.setInt(1, aid);
						pstmt.setString(2, "Withdraw");
						pstmt.setDouble(3, amount);
						pstmt.executeUpdate();
						
						query = "update account set balance = balance - ? where account_id = ?";
						pstmt = conn.prepareStatement(query);
						pstmt.setDouble(1, amount);
						pstmt.setInt(2, aid);
						pstmt.executeUpdate();
						
						System.out.println("Withdrawal auccessfull..");
					}else {
						System.out.println("Insufficient balance!");
					}
					rs.close();
					}
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deposit() {
		try {
			
			Connection conn = Bank.db_connect();
			System.out.println("Enter account id:");
			int aid = sn.nextInt();
			
			System.out.println("Enter amount to deposit:");
			double amount = sn.nextDouble();
			
			String query = "INSERT INTO transaction(account_id,transaction_type,amount) VALUES (?,?,?)";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, aid);
			pstmt.setString(2, "Deposit");
			pstmt.setDouble(3, amount);
			pstmt.executeUpdate();
			
			query = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setDouble(1, amount);
			pstmt.setInt(2, aid);
			pstmt.executeUpdate();
			
			System.out.println("Deposit successfull..");
		
		}catch(SQLException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void amount_trans() {
		
		 try {
	            Connection con = Bank.db_connect();
	            
	            System.out.println("Enter source account id:");
	            int sid = sn.nextInt();
	            
	            System.out.println("Enter destination account id:");
	            int did = sn.nextInt();
	            
	            System.out.println("Enter transfer amount:");
	            double amount = sn.nextDouble();

	            String query = "SELECT balance FROM account WHERE account_id = ?";
	            PreparedStatement pstmt = con.prepareStatement(query);
	            pstmt.setInt(1, sid);
	            ResultSet rs = pstmt.executeQuery();
	            
	            if (rs.next()) {
	                double balance = rs.getDouble("balance");
	                
	                if (balance >= amount) {
	                    
	                    String updateSource = "UPDATE account SET balance = balance - ? WHERE account_id = ?";
	                    pstmt = con.prepareStatement(updateSource);
	                    pstmt.setDouble(1, amount);
	                    pstmt.setInt(2, sid);
	                    pstmt.executeUpdate();
	                   
	                    String updateDestination = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
	                    pstmt = con.prepareStatement(updateDestination);
	                    pstmt.setDouble(1, amount);
	                    pstmt.setInt(2, did);
	                    pstmt.executeUpdate();
	                   
	                    String insertSourceTransaction = "INSERT INTO transaction (account_id, transaction_type, amount, transaction_date) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
	                    pstmt = con.prepareStatement(insertSourceTransaction);
	                    pstmt.setInt(1, sid);
	                    pstmt.setString(2, "Debit");
	                    pstmt.setDouble(3, amount);
	                    pstmt.executeUpdate();
	          
	                    String insertDestinationTransaction = "INSERT INTO transaction (account_id, transaction_type, amount, transaction_date) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
	                    pstmt = con.prepareStatement(insertDestinationTransaction);
	                    pstmt.setInt(1, did);
	                    pstmt.setString(2, "Credit");
	                    pstmt.setDouble(3, amount);
	                    pstmt.executeUpdate();
	                    
	                    System.out.println("Transaction completed successfully.");
	                    
	                } else {
	                    System.out.println("Insufficient balance in the source account.");
	                }
	            } else {
	                System.out.println("Source account not found.");
	            }
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    

		
	}		

	@Override
	public void viewtrans() {
		try {

			Connection conn = Bank.db_connect();
			System.out.println("Enter account id:");
			int aid = sn.nextInt();
			
			String query = "select * from transaction where account_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, aid);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				System.out.println("Transaction id: "+ rs.getInt("transaction_id"));
				System.out.println("Account id: "+ rs.getInt("account_id"));
				System.out.println("Transaction type: "+ rs.getString("transaction_type"));
				System.out.println("Amount: "+ rs.getDouble("amount"));

			}

			
		}catch(SQLException e) {
			e.printStackTrace();
		}	}
    
    
}
