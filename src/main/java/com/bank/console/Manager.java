package com.bank.console;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.bank.dao.ConnectionProvider;
import com.bank.project.App;

public class Manager {
	public static void ManagerPortal()
	{
		
		System.out.println("*************Manager portal*************");
		System.out.println("1.Login");
		System.out.println("2.Register");
		System.out.println("3.Return to menu");
		System.out.println("Choose your option:");
		int choice = App.scanner.nextInt();
		switch (choice) {
		case 1:
			loginManager();
			break;
		case 2:
			registerManager();
			break;
		case 3:
			MainMenu.show();
			break;
		default:
			System.out.println("Invalid Option,try again...");
			break;
		}
		
	}
	
	public static int getManagerCount() {
	    int count = 0;
	    try (Connection con = ConnectionProvider.getCon()) {
	        String sql = "SELECT COUNT(*) FROM manager";
	        Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(sql);
	        if (rs.next()) {
	            count = rs.getInt(1);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return count;
	}

	
	public static void registerManager() {
		String managerId = "MGR" + String.format("%04d", getManagerCount() + 1);
		System.out.println("Generated Manager ID: " + managerId);
		App.scanner.nextLine();

	    System.out.println("Enter Name: ");
	    String name = App.scanner.nextLine().trim();

	    System.out.println("Enter Email: ");
	    String email = App.scanner.nextLine().trim();

	    System.out.println("Enter Password: ");
	    String password = App.scanner.nextLine().trim();

	    System.out.println("Enter Branch ID: ");
	    String branchId = App.scanner.nextLine().trim();

	    try (Connection con = ConnectionProvider.getCon()) {
	        String sql = "INSERT INTO manager (manager_id, manager_name, manager_email, manager_password, branch_id) VALUES (?, ?, ?, ?, ?)";
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setString(1, managerId);
	        ps.setString(2, name);
	        ps.setString(3, email);
	        ps.setString(4, password);
	        ps.setString(5, branchId);

	        int rows = ps.executeUpdate();
	        if (rows > 0) {
	            System.out.println("Manager registered successfully.");
	        } else {
	            System.out.println("Registration failed. Please try again.");
	        }
	    } catch (SQLException e) {
	        if (e.getMessage().contains("Duplicate entry")) {
	            System.out.println("Email or Manager ID already exists.");
	        } else {
	            e.printStackTrace();
	        }
	    }
	}
	
	public static void loginManager() {
	    App.scanner.nextLine(); 

	    System.out.println("Enter Manager ID: ");
	    String managerId = App.scanner.nextLine().trim();

	    System.out.println("Enter Email: ");
	    String email = App.scanner.nextLine().trim();

	    System.out.println("Enter Password: ");
	    String password = App.scanner.nextLine().trim();

	    try (Connection con = ConnectionProvider.getCon()) {
	        String sql = "SELECT manager_name, status FROM manager WHERE manager_id = ? AND manager_email = ? AND manager_password = ?";
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setString(1, managerId);
	        ps.setString(2, email);
	        ps.setString(3, password);

	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            boolean isApproved = rs.getBoolean("status");
	            if (isApproved) {
	                System.out.println(" Login successful. Welcome, " + rs.getString("manager_name") + "!");
	                ManagerView.managerMenu();
	            } else {
	                System.out.println(" Your account is pending approval by the admin. ");
	            }
	        } else {
	            System.out.println(" Invalid credentials. Please try again. ");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}




}
