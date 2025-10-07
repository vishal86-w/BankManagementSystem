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
import com.bank.model.ManagerModel;
import com.bank.project.App;

public class Manager {
	public static void ManagerPortal() throws AccountNotFoundException, InvalidDepositAmountException, DepositFailedException, InvalidWithdrawalAmountException, InsufficientBalanceException, WithdrawalFailedException
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

	    ManagerModel mg = new ManagerModel();

	    try (Connection con = ConnectionProvider.getCon()) {
	        mg.setManagerId(managerId);

	        System.out.println("Enter Name: ");
	        mg.setManagerName(App.scanner.nextLine());

	        System.out.println("Enter Email: ");
	        mg.setManagerEmail(App.scanner.nextLine());

	        System.out.println("Enter Password: ");
	        String plainPassword = App.scanner.nextLine();
	        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
	        mg.setManagerPassword(hashedPassword);

	        System.out.println("Enter Branch ID: ");
	        mg.setBranchId(App.scanner.nextLine());

	        String sql = "INSERT INTO manager (manager_id, manager_name, manager_email, manager_password, branch_id) VALUES (?, ?, ?, ?, ?)";
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setString(1, managerId);
	        ps.setString(2, mg.getManagerName());
	        ps.setString(3, mg.getManagerEmail());
	        ps.setString(4, mg.getManagerPassword());
	        ps.setString(5, mg.getBranchId());

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

	
	public static void loginManager() throws AccountNotFoundException, InvalidDepositAmountException, DepositFailedException, InvalidWithdrawalAmountException, InsufficientBalanceException, WithdrawalFailedException {
	    App.scanner.nextLine();

	    System.out.println("Enter Manager ID: ");
	    String managerId = App.scanner.nextLine().trim();

	    System.out.println("Enter Email: ");
	    String email = App.scanner.nextLine().trim();

	    System.out.println("Enter Password: ");
	    String inputPassword = App.scanner.nextLine().trim();

	    try (Connection con = ConnectionProvider.getCon()) {
	        String sql = "SELECT manager_name, manager_password, status FROM manager WHERE manager_id = ? AND manager_email = ?";
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setString(1, managerId);
	        ps.setString(2, email);

	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            String storedHash = rs.getString("manager_password");
	            boolean isApproved = rs.getBoolean("status");

	            if (BCrypt.checkpw(inputPassword, storedHash)) {
	                if (isApproved) {
	                    System.out.println("Login successful. Welcome, " + rs.getString("manager_name") + "!");
	                    ManagerView.managerMenu();
	                } else {
	                    System.out.println("Your account is pending approval by the admin.");
	                }
	            } else {
	                System.out.println("Invalid credentials. Please try again.");
	            }
	        } else {
	            System.out.println("Invalid credentials. Please try again.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}



}
