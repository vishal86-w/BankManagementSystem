package com.bank.console;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;

import org.mindrot.jbcrypt.BCrypt;

import com.bank.dao.ConnectionProvider;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.DepositFailedException;
import com.bank.exception.InsufficientBalanceException;
import com.bank.exception.InvalidDepositAmountException;
import com.bank.exception.InvalidWithdrawalAmountException;
import com.bank.exception.WithdrawalFailedException;
import com.bank.model.AdminModel;
import com.bank.project.App;

public class Admin {
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

	public static void AdminPortal() throws InputMismatchException, AccountNotFoundException, InvalidDepositAmountException, DepositFailedException, InvalidWithdrawalAmountException, InsufficientBalanceException, WithdrawalFailedException
	{
		
		System.out.println(BLUE+"*************Admin Login portal*************"+RESET);
		System.out.println(GREEN+"1.Login"+RESET);
		System.out.println(GREEN+"2.Register"+RESET);
		System.out.println(GREEN+"3.Return to menu"+RESET);
		System.out.println(GREEN+"Choose your option:"+RESET);
		int choice = App.scanner.nextInt();
		 App.scanner.nextLine();
		 
		switch (choice) {
		case 1:
			adminLogin();
			break;
		case 2:
			adminRegister();
			break;
		case 3:
			MainMenu.show();;
			break;
		default:
			System.out.println(RED+"Invalid Option,try again..."+RESET);
			break;
		}
		
	}
	public static boolean verifyLogin(String inputUsername,String inputPassword) throws AccountNotFoundException, InputMismatchException, InvalidDepositAmountException, DepositFailedException, InvalidWithdrawalAmountException, InsufficientBalanceException, WithdrawalFailedException
	{
		  try (Connection con = ConnectionProvider.getCon()) {
		    	String sql = "SELECT * FROM admin WHERE user_name = ?";
		    	PreparedStatement ps = con.prepareStatement(sql);
		    	ps.setString(1, inputUsername);

		    	ResultSet rs = ps.executeQuery();

		    	if (rs.next()) {
		    	    String storedHash = rs.getString("password");
		    	    if (BCrypt.checkpw(inputPassword, storedHash)) {
		    	    	System.out.println(YELLOW+"Login successful, welcome,"+RESET+GREEN+inputUsername+RESET);
		    	      AdminView.adminMenu();
		    	      return true;
		    	    } 
		    	} else {
		    	    throw new AccountNotFoundException(RED + "Invalid account ID or password." + RESET);
		    	}

		    } catch (SQLException e) {
		        e.printStackTrace(); 
		    }
		  return false;
	}
	public static void adminLogin()
	{
		
		try {
			System.out.println(CYAN+"Enter your user name: "+RESET);
			String inputUsername = App.scanner.nextLine();
			System.out.println(CYAN+"Enter your password"+RESET);
			String inputPassword = App.scanner.nextLine();
			
			if(verifyLogin(inputUsername, inputPassword))
			{
				
				AdminView.adminMenu();
				
			}
			else
			{
				System.out.println(RED+"Invalid credentials!!!"+RESET);
				
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
	}
	public static int count=0;
	public static void getAdminCount()
	{
		try (Connection con = ConnectionProvider.getCon()){
			String sql ="select count(*) from admin";
			Statement smt = con.createStatement();
			ResultSet rs=smt.executeQuery(sql);
			if(rs.next())
			{
				String data = rs.getString(1);
				count = Integer.parseInt(data);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void adminRegister() {
	    getAdminCount();
	    String Admname = "adm00";
	    String getcount = Integer.toString(count);
	    String Accountadm = Admname + getcount;

	    AdminModel ad = new AdminModel();

	    try (Connection con = ConnectionProvider.getCon()) {
	        ad.setAdmin_id(Accountadm);

	        System.out.println(CYAN + "Enter your username" + RESET);
	        ad.setUser_name(App.scanner.nextLine());

	        System.out.println(CYAN + "Enter your password" + RESET);
	        String plainPassword = App.scanner.nextLine();

	        
	        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
	        ad.setPassword(hashedPassword);

	        String sql = "INSERT INTO admin(admin_id, user_name, password) VALUES (?, ?, ?)";
	        PreparedStatement ps = con.prepareStatement(sql);

	        ps.setString(1, ad.getAdmin_id());
	        ps.setString(2, ad.getUser_name());
	        ps.setString(3, ad.getPassword());

	        int i = ps.executeUpdate();
	        if (i == 1) {
	            System.out.println(GREEN + "Registered Successfully" + RESET);
	        } else {
	            System.out.println(RED + "Registration failed, Invalid input" + RESET);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	

}
