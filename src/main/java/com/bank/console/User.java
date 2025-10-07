package com.bank.console;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.mindrot.jbcrypt.BCrypt;

import com.bank.dao.ConnectionProvider;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.DepositFailedException;
import com.bank.exception.InsufficientBalanceException;
import com.bank.exception.InvalidDepositAmountException;
import com.bank.exception.InvalidWithdrawalAmountException;
import com.bank.exception.WithdrawalFailedException;
import com.bank.model.UserModel;
import com.bank.project.App;

public class User {
	 public static final String RESET = "\u001B[0m";

	    public static final String BLACK = "\u001B[30m";
	    public static final String RED = "\u001B[31m";
	    public static final String GREEN = "\u001B[32m";
	    public static final String YELLOW = "\u001B[33m";
	    public static final String BLUE = "\u001B[34m";
	    public static final String PURPLE = "\u001B[35m";
	    public static final String CYAN = "\u001B[36m";
	    public static final String WHITE = "\u001B[37m";

	    public static final String BOLD = "\033[1m";
	public static void UserPortal()
	{
		
		try {
			
			System.out.println(BLUE+"*************User portal*************"+RESET);
			System.out.println(GREEN+"1.Login"+RESET);
			System.out.println(GREEN+"2.Register"+RESET);
			System.out.println(GREEN+"3.Return to menu"+RESET);
			System.out.println(GREEN+"Choose your option:"+RESET);
			int choice = App.scanner.nextInt();
			App.scanner.nextLine();
			switch (choice) {
			case 1:
				// user login
				userLogin();
				break;
			case 2:
				//user register
				userRegister();
				break;
			case 3:
				// return to menu
				MainMenu.show();
				break;
			default:
				System.out.println(RED+"Invalid Option,try again..."+RESET);
				UserPortal();
				break;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
public static int count = 0  ;
public static void getCustomerCount()
	{
		try (Connection con = ConnectionProvider.getCon()){
			String sql ="select count(*) from customer";
			Statement smt = con.createStatement();
			ResultSet rs=smt.executeQuery(sql);
			if(rs.next())
			{
				String data = rs.getString(1);
				count = Integer.parseInt(data);
				//System.out.println(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
public static void userRegister()
	{
		getCustomerCount();
		
		String accNum = "90808766600";
		
		String getcount=Integer.toString(count);
		String AccountNum = accNum+""+getcount;
		
	
		UserModel user = new UserModel();
		
		
		try(Connection con = ConnectionProvider.getCon()) {
			
			user.setAccountHolderNumber(AccountNum);
			
			System.out.println(CYAN+"Enter your name (name should be same as aadhar) : "+RESET);
			user.setAccountHolderName(App.scanner.nextLine());
			System.out.println(CYAN+"Enter the branch id:"+RESET);
			user.setBranchId(App.scanner.nextLine());
			System.out.println(CYAN+"Enter the branch name: "+RESET);
			user.setBranchName(App.scanner.nextLine());
			System.out.println(CYAN+"Enter your email : "+RESET);
			user.setAccountHolderEmail(App.scanner.nextLine());
			if (!user.getAccountHolderEmail().matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
			    System.out.println(RED + "Invalid email format.Try again..." + RESET);
			    userRegister();
			}

			System.out.println(CYAN+"Enter your password"+RESET);
			String plainPassword = App.scanner.nextLine();
			String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
			
			user.setAccPassword(hashedPassword);

			System.out.println(CYAN+"Enter the account type [1 = Saving , 2 = Current]:"+RESET);
			int acctype = App.scanner.nextInt();
			App.scanner.nextLine();
			switch (acctype) {
			case 1:
				user.setAccountType("saving");
				break;
			case 2:
				user.setAccountType("current");
				break;
			default:
				System.out.println(RED+"Invalid option,try again..."+RESET);
				userRegister();
				break;
			}
			String sql = "INSERT INTO customer (account_holder_number, branch_id, branch_name, account_holder_name, account_holder_email, acc_password, account_type, balance, status) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		    PreparedStatement ps = con.prepareStatement(sql);

		    ps.setString(1, user.getAccountHolderNumber());
		    ps.setString(2, user.getBranchId());
		    ps.setString(3, user.getBranchName());
		    ps.setString(4, user.getAccountHolderName());
		    ps.setString(5, user.getAccountHolderEmail());
		    ps.setString(6, user.getAccPassword());
		    ps.setString(7, user.getAccountType());
		    ps.setDouble(8, 0.0); // Initial balance
		    ps.setBoolean(9, false); // Active status
		    int i = ps.executeUpdate();
			if(i==1)
			{
				System.out.println(GREEN+"Registered Successfully,please wait for the approval"+RESET);
			}
			else
			{
				System.out.println(RED+"Registeration failed,Invalid input"+RESET);
			}

		} 
		catch(SQLException e) {
			e.printStackTrace();
		}
				
	}

public static void VerifyUser(String ACC_ID, String password) throws AccountNotFoundException, InvalidDepositAmountException, DepositFailedException, InvalidWithdrawalAmountException, InsufficientBalanceException, WithdrawalFailedException {
	    try (Connection con = ConnectionProvider.getCon()) {
	    	String sql = "SELECT * FROM customer WHERE account_holder_number = ?";
	    	PreparedStatement ps = con.prepareStatement(sql);
	    	ps.setString(1, ACC_ID);

	    	ResultSet rs = ps.executeQuery();

	    	if (rs.next()) {
	    	    String storedHash = rs.getString("acc_password");
	    	    if (BCrypt.checkpw(password, storedHash)) {
	    	        System.out.println("Login successful, welcome " + rs.getString("account_holder_name") +
	    	                           " and your account number is " + rs.getString("account_holder_number"));
	    	        if(rs.getBoolean("status")==true) {
	   	    		 UserView.userAction();
	    	        }
	    	        else {
	    	        	System.out.println(RED+"your account is not yet approved..."+RESET);
	    	        }
	    	    } else {
	    	        throw new AccountNotFoundException(RED + "Invalid account ID or password." + RESET);
	    	    }
	    	
	    	
	    	} else {
	    	    throw new AccountNotFoundException(RED + "Invalid account ID or password." + RESET);
	    	}

	    } catch (SQLException e) {
	        e.printStackTrace(); 
	    }
	}

	
public static void userLogin() throws InvalidDepositAmountException, DepositFailedException, InvalidWithdrawalAmountException, InsufficientBalanceException, WithdrawalFailedException {
	    System.out.println(CYAN+"Enter your Account ID: "+RESET);
	    String ACC_ID = App.scanner.nextLine();

	    System.out.println(CYAN+"Enter your password: "+RESET);
	    String password = App.scanner.nextLine();

	    try {
	        VerifyUser(ACC_ID, password);
	    } catch (AccountNotFoundException e) {
	        System.out.println(RED+"Login failed: " +RESET+ e.getMessage());
	        
	    }
	}


}
