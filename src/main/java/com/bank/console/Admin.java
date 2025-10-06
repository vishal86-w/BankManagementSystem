package com.bank.console;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import com.bank.dao.ConnectionProvider;
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

	public static void AdminPortal()
	{
		
		System.out.println(BLUE+"*************Admin Login portal*************"+RESET);
		System.out.println(GREEN+"1.Login"+RESET);
		System.out.println(GREEN+"2.Return to menu"+RESET);
		System.out.println(GREEN+"Choose your option:"+RESET);
		int choice = App.scanner.nextInt();
		 App.scanner.nextLine();
		 
		switch (choice) {
		case 1:
			adminLogin();
			break;
		case 2:
			MainMenu.show();
			break;
		default:
			System.out.println(RED+"Invalid Option,try again..."+RESET);
			break;
		}
		
	}
	public static boolean verifyLogin(String inputUsername,String inputPassword)
	{

		 try (Connection con = ConnectionProvider.getCon()) {
	            String sql = "SELECT * FROM admin WHERE user_name = ? AND password = ?";
	            PreparedStatement stmt = con.prepareStatement(sql);
	            stmt.setString(1, inputUsername);
	            stmt.setString(2, inputPassword);

	            ResultSet rs = stmt.executeQuery();
	            return rs.next(); 

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
				System.out.println(YELLOW+"Login successful, welcome,"+RESET+inputUsername);
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
	

}
