package com.org.controller;

import com.org.model.Bank;
import com.org.service.Service;
import com.org.service.ServiceImpl;
import java.sql.SQLException;
import java.util.Scanner;



public class BankController {

	private static Scanner sn = new Scanner(System.in);
	
	public static void main(String[] args) {
		
		Bank n = new Bank();
		
		try {
		
		Service sv = new ServiceImpl();
		n.db_connect();
		
		boolean exe = true;
		
		System.out.println("connection successfull..");
		while(exe) {
			System.out.println(" 1.create account \n 2.view account \n 3.update account info \n 4.deposite amount \n 5.withdraw amount \n 6.transaction amount \n 7.view transaction \n 8.exit");
			int input = sn.nextInt();
			
			switch(input) {
			case 1:
				sv.createaccount();
				System.out.println(" 1 ");
				break;
				
			case 2:
				sv.viewaccount();
				System.out.println(" 2 ");
				break;
				
			case 3:
				sv.updateaccount();
				System.out.println(" 3 ");
				break;
				
			case 4:
				sv.deposit();
				System.out.println(" 4 ");
				break;
				
			case 5:
				sv.withdraw();
				System.out.println(" 5 ");
				break;
				
			case 6:
				sv.amount_trans();
				System.out.println(" 6 ");
				break;
				
			case 7:
				sv.viewtrans();
				System.out.println(" 7 ");
				break;
				
			case 8:
				exe = false;
				System.out.println(" 8 ");
				break;
				
			default:
				System.out.println(" Invalid choice.. ");
				
			}
		}
		
		
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

}


