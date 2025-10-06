package com.bank.controls;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.bank.console.User;
import com.bank.dao.ConnectionProvider;
import com.bank.model.UserModel;
import com.bank.project.App;

public class ManagerControl {
	public static void viewAllCustomers() {
	    try (Connection con = ConnectionProvider.getCon()) {
	        String sql = "SELECT account_holder_number, account_holder_name, account_holder_email, branch_id, account_type, balance, status FROM customer";
	        PreparedStatement ps = con.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();

	        System.out.println("\n=====  All Customers =====");
	        boolean hasCustomers = false;

	        while (rs.next()) {
	            hasCustomers = true;
	            System.out.println("--------------------------------------------");
	            System.out.println("Account ID     : " + rs.getString("account_holder_number"));
	            System.out.println("Name           : " + rs.getString("account_holder_name"));
	            System.out.println("Email          : " + rs.getString("account_holder_email"));
	            System.out.println("Branch ID      : " + rs.getString("branch_id"));
	            System.out.println("Account Type   : " + rs.getString("account_type"));
	            System.out.println("Balance        : ₹." + rs.getDouble("balance"));
	            System.out.println("Status         : " + (rs.getBoolean("status") ? "Active" : "Inactive"));
	        }

	        if (!hasCustomers) {
	            System.out.println("No customers found in the system.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void approveOrRejectCustomer() {
	    System.out.println("Enter Customer Account ID to review: ");
	    String accId = App.scanner.nextLine().trim();

	    System.out.println("Choose action:");
	    System.out.println("1. Approve (Activate)");
	    System.out.println("2. Reject (Deactivate)");
	    System.out.print("Enter your choice: ");
	    int choice = App.scanner.nextInt();
	    App.scanner.nextLine(); // consume newline

	    boolean newStatus;
	    if (choice == 1) {
	        newStatus = true;
	    } else if (choice == 2) {
	        newStatus = false;
	    } else {
	        System.out.println(" Invalid choice. Operation cancelled.");
	        return;
	    }

	    try (Connection con = ConnectionProvider.getCon()) {
	        String sql = "UPDATE customer SET status = ? WHERE account_holder_number = ?";
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setBoolean(1, newStatus);
	        ps.setString(2, accId);

	        int rows = ps.executeUpdate();
	        if (rows > 0) {
	            System.out.println(" Customer account " + (newStatus ? "approved" : "rejected") + " successfully.");
	        } else {
	            System.out.println(" Account not found or update failed.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void viewTransactionsByAccount() {
	    System.out.println("Enter Customer Account ID: ");
	    String accId = App.scanner.nextLine().trim();

	    try (Connection con = ConnectionProvider.getCon()) {
	        String sql = "SELECT transaction_id, transaction_type, amount, transaction_date FROM transaction WHERE account_holder_number = ? ORDER BY transaction_date DESC";
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setString(1, accId);

	        ResultSet rs = ps.executeQuery();

	        System.out.println("\n Transactions for Account ID: " + accId);
	        boolean hasTransactions = false;

	        while (rs.next()) {
	            hasTransactions = true;
	            System.out.println("--------------------------------------------------");
	            System.out.println("Transaction ID   : " + rs.getString("transaction_id"));
	            System.out.println("Type             : " + rs.getString("transaction_type"));
	            System.out.println("Amount           : ₹" + rs.getDouble("amount"));
	            System.out.println("Date             : " + rs.getTimestamp("transaction_date"));
	        }

	        if (!hasTransactions) {
	            System.out.println("No transactions found for this account.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public static void viewBranchSummary() {
	    try (Connection con = ConnectionProvider.getCon()) {
	        String sql = """
	            SELECT 
	                b.branch_id,
	                b.branch_name,
	                COUNT(c.account_holder_number) AS total_customers,
	                SUM(c.balance) AS total_balance
	            FROM branch b
	            LEFT JOIN customer c ON b.branch_id = c.branch_id
	            GROUP BY b.branch_id, b.branch_name
	            ORDER BY b.branch_id
	        """;

	        PreparedStatement ps = con.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();

	        System.out.println("\n=====  Branch Summary =====");
	        while (rs.next()) {
	            System.out.println("--------------------------------------------");
	            System.out.println("Branch ID        : " + rs.getString("branch_id"));
	            System.out.println("Branch Name      : " + rs.getString("branch_name"));
	            System.out.println("Total Customers  : " + rs.getInt("total_customers"));
	            System.out.println("Total Balance    : ₹" + rs.getDouble("total_balance"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void createCustomerAccount() {
	   int count = User.count;
		User.getCustomerCount();
	    try (Connection con = ConnectionProvider.getCon()) {
	        String sql = "SELECT COUNT(*) FROM customer";
	        Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(sql);
	        if (rs.next()) {
	            count = rs.getInt(1);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    String branchId = "SIB090797";
	    String baseAccNum = "90808766600";
	    String accountNum = baseAccNum + count;

	    UserModel user = new UserModel();
	    user.setAccountHolderNumber(accountNum);
	    user.setBranchId(branchId);

	    // Collect customer details
	    System.out.println("Enter Customer Name (as per Aadhaar): ");
	    user.setAccountHolderName(App.scanner.nextLine());

	    System.out.println("Enter Branch Name: ");
	    user.setBranchName(App.scanner.nextLine());

	    System.out.println("Enter Email: ");
	    user.setAccountHolderEmail(App.scanner.nextLine());

	    System.out.println("Enter Password: ");
	    user.setAccPassword(App.scanner.nextLine());

	    System.out.println("Enter Account Type [1 = Saving, 2 = Current]: ");
	    int accType = App.scanner.nextInt();
	    App.scanner.nextLine(); // consume newline

	    switch (accType) {
	        case 1: user.setAccountType("saving"); break;
	        case 2: user.setAccountType("current"); break;
	        default:
	            System.out.println("Invalid account type. Try again.");
	            return;
	    }

	    try (Connection con = ConnectionProvider.getCon()) {
	        String sql = "INSERT INTO customer (account_holder_number, branch_id, branch_name, account_holder_name, account_holder_email, acc_password, account_type, balance, amount, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        PreparedStatement ps = con.prepareStatement(sql);

	        ps.setString(1, user.getAccountHolderNumber());
	        ps.setString(2, user.getBranchId());
	        ps.setString(3, user.getBranchName());
	        ps.setString(4, user.getAccountHolderName());
	        ps.setString(5, user.getAccountHolderEmail());
	        ps.setString(6, user.getAccPassword());
	        ps.setString(7, user.getAccountType());
	        ps.setDouble(8, 0.0); // Initial balance
	        ps.setDouble(9, 0.0); // Initial amount
	        ps.setBoolean(10, true); //  Approved by manager

	        int i = ps.executeUpdate();
	        if (i == 1) {
	            System.out.println("Customer account created and approved successfully!");
	            System.out.println("Assigned Account Number: " + user.getAccountHolderNumber());
	        } else {
	            System.out.println("Account creation failed. Please check input.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public static void updateCustomerDetails() {
	    System.out.println("Enter Customer Account ID to update: ");
	    String accId = App.scanner.nextLine().trim();

	    try (Connection con = ConnectionProvider.getCon()) {
	        // Check if customer exists
	        String checkSql = "SELECT * FROM customer WHERE account_holder_number = ?";
	        PreparedStatement checkStmt = con.prepareStatement(checkSql);
	        checkStmt.setString(1, accId);
	        ResultSet rs = checkStmt.executeQuery();

	        if (!rs.next()) {
	            System.out.println("Customer not found.");
	            return;
	        }

	        // Prompt for new details
	        System.out.println("Enter new name (leave blank to keep current): ");
	        String name = App.scanner.nextLine().trim();
	        if (name.isEmpty()) name = rs.getString("account_holder_name");

	        System.out.println("Enter new email (leave blank to keep current): ");
	        String email = App.scanner.nextLine().trim();
	        if (email.isEmpty()) email = rs.getString("account_holder_email");

	        System.out.println("Enter new branch name (leave blank to keep current): ");
	        String branchName = App.scanner.nextLine().trim();
	        if (branchName.isEmpty()) branchName = rs.getString("branch_name");

	        System.out.println("Enter new account type [1 = Saving, 2 = Current, 0 = Keep current]: ");
	        int accTypeChoice = App.scanner.nextInt();
	        App.scanner.nextLine(); 
	        
	        String accountType = rs.getString("account_type");
	        if (accTypeChoice == 1) accountType = "saving";
	        else if (accTypeChoice == 2) accountType = "current";

	        // Update customer record
	        String updateSql = "UPDATE customer SET account_holder_name = ?, account_holder_email = ?, branch_name = ?, account_type = ? WHERE account_holder_number = ?";
	        PreparedStatement updateStmt = con.prepareStatement(updateSql);
	        updateStmt.setString(1, name);
	        updateStmt.setString(2, email);
	        updateStmt.setString(3, branchName);
	        updateStmt.setString(4, accountType);
	        updateStmt.setString(5, accId);

	        int rows = updateStmt.executeUpdate();
	        if (rows > 0) {
	            System.out.println("Customer details updated successfully.");
	        } else {
	            System.out.println("Update failed. Please try again.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}




}
