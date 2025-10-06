package com.bank.controls;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bank.console.Manager;
import com.bank.dao.ConnectionProvider;
import com.bank.project.App;

public class AdminControl {
	
	public static void addManager() {
		int mCount = Manager.getManagerCount();
	    String managerId = "MGR" + String.format("%04d", mCount + 1);
	    System.out.println("Generated Manager ID: " + managerId);

	    System.out.print("Enter Name: ");
	    String name = App.scanner.nextLine().trim();

	    System.out.print("Enter Email: ");
	    String email = App.scanner.nextLine().trim();

	    System.out.print("Enter Password: ");
	    String password = App.scanner.nextLine().trim();

	    System.out.print("Enter Branch ID: ");
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
	        System.out.println(rows > 0 ? "Manager added successfully." : " Failed to add manager.");
	    } catch (SQLException e) {
	        if (e.getMessage().contains("Duplicate entry")) {
	            System.out.println("Email or Manager ID already exists.");
	        } else {
	            e.printStackTrace();
	        }
	    }
	}
	
	public static void editManager() {
	    System.out.print("Enter Manager ID to edit: ");
	    String managerId = App.scanner.nextLine().trim();

	    try (Connection con = ConnectionProvider.getCon()) {
	        String checkSql = "SELECT * FROM manager WHERE manager_id = ?";
	        PreparedStatement check = con.prepareStatement(checkSql);
	        check.setString(1, managerId);
	        ResultSet rs = check.executeQuery();

	        if (!rs.next()) {
	            System.out.println("❌ Manager not found.");
	            return;
	        }

	        System.out.print("Enter new name (leave blank to keep current): ");
	        String name = App.scanner.nextLine().trim();
	        if (name.isEmpty()) name = rs.getString("manager_name");

	        System.out.print("Enter new email (leave blank to keep current): ");
	        String email = App.scanner.nextLine().trim();
	        if (email.isEmpty()) email = rs.getString("manager_email");

	        System.out.print("Enter new password (leave blank to keep current): ");
	        String password = App.scanner.nextLine().trim();
	        if (password.isEmpty()) password = rs.getString("manager_password");

	        System.out.print("Enter new branch ID (leave blank to keep current): ");
	        String branchId = App.scanner.nextLine().trim();
	        if (branchId.isEmpty()) branchId = rs.getString("branch_id");

	        String updateSql = "UPDATE manager SET manager_name = ?, manager_email = ?, manager_password = ?, branch_id = ? WHERE manager_id = ?";
	        PreparedStatement ps = con.prepareStatement(updateSql);
	        ps.setString(1, name);
	        ps.setString(2, email);
	        ps.setString(3, password);
	        ps.setString(4, branchId);
	        ps.setString(5, managerId);

	        int rows = ps.executeUpdate();
	        System.out.println(rows > 0 ? "Manager updated successfully." : "Update failed.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void deleteManager() {
	    System.out.print("Enter Manager ID to delete: ");
	    String managerId = App.scanner.nextLine().trim();

	    try (Connection con = ConnectionProvider.getCon()) {
	        String sql = "DELETE FROM manager WHERE manager_id = ?";
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setString(1, managerId);

	        int rows = ps.executeUpdate();
	        System.out.println(rows > 0 ? " Manager deleted." : "Manager not found.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void approveOrRejectManager() {
	    try (Connection con = ConnectionProvider.getCon()) {
	        String sql = "SELECT manager_id, manager_name, manager_email, branch_id FROM manager WHERE status = false";
	        PreparedStatement ps = con.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();

	        System.out.println(" Pending Manager Approvals:");
	        while (rs.next()) {
	            System.out.println("----------------------------------");
	            System.out.println("Manager ID   : " + rs.getString("manager_id"));
	            System.out.println("Name         : " + rs.getString("manager_name"));
	            System.out.println("Email        : " + rs.getString("manager_email"));
	            System.out.println("Branch ID    : " + rs.getString("branch_id"));
	        }

	        System.out.println(" Enter Manager ID to approve/reject: ");
	        String managerId = App.scanner.nextLine().trim();

	        System.out.println("1. Approve");
	        System.out.println("2. Reject");
	        System.out.print("Choose action: ");
	        int choice = App.scanner.nextInt();
	        App.scanner.nextLine();

	        boolean newStatus = (choice == 1);

	        String updateSql = "UPDATE manager SET status = ? WHERE manager_id = ?";
	        PreparedStatement update = con.prepareStatement(updateSql);
	        update.setBoolean(1, newStatus);
	        update.setString(2, managerId);

	        int rows = update.executeUpdate();
	        System.out.println(rows > 0 ? " Manager " + (newStatus ? "approved." : "rejected.") : " Update failed.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public static void viewTransactionReports() {
	    try (Connection con = ConnectionProvider.getCon()) {
	        String sql = "SELECT transaction_id, account_holder_number, transaction_type, amount, transaction_date FROM transaction ORDER BY transaction_date DESC";
	        PreparedStatement ps = con.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();

	        System.out.println("\n Transaction Report:");
	        while (rs.next()) {
	            System.out.println("----------------------------------");
	            System.out.println("Transaction ID   : " + rs.getString("transaction_id"));
	            System.out.println("Account ID       : " + rs.getString("account_holder_number"));
	            System.out.println("Type             : " + rs.getString("transaction_type"));
	            System.out.println("Amount           : ₹" + rs.getDouble("amount"));
	            System.out.println("Date             : " + rs.getTimestamp("transaction_date"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}



}
