package com.bank.controls;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.bank.console.Manager;
import com.bank.dao.ConnectionProvider;
import com.bank.exception.AccountNotFoundException;
import com.bank.model.AdminModel;
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
	
	public static void editManager() throws AccountNotFoundException{
	    System.out.print("Enter Manager ID to edit: ");
	    String managerId = App.scanner.nextLine().trim();

	    try (Connection con = ConnectionProvider.getCon()) {
	        String checkSql = "SELECT * FROM manager WHERE manager_id = ?";
	        PreparedStatement check = con.prepareStatement(checkSql);
	        check.setString(1, managerId);
	        ResultSet rs = check.executeQuery();

	        if (!rs.next()) {
	            throw new AccountNotFoundException("Manager not found.");
	           
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
	public static int count = 0  ;
	public static void getBranchCount()
	{
		try (Connection con = ConnectionProvider.getCon()){
			String sql ="select count(*) from branch";
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
	public static void addBranch() {
		getBranchCount();
		AdminModel ad = new AdminModel();
		String getcount=Integer.toString(count);
		String Bid = "DBI097"+getcount;
		
		try(Connection con = ConnectionProvider.getCon()){
			String sql= "insert into branch(branch_id,branch_name,location) values (?,?,?)";
			ad.setBranchId(Bid);
			System.out.println("Enter Branch name: ");
			String bName = App.scanner.nextLine();
			System.out.println("Enter Branch location: ");
			String bLocation = App.scanner.nextLine();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, Bid);
			ps.setString(2, bName);
			ps.setString(3, bLocation);
			int i = ps.executeUpdate();
			if(i==1)
			{
				System.out.println("New Branch Added Successfully!!!");
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void editBranch() {
	    try (Connection con = ConnectionProvider.getCon()) {
	        System.out.print("Enter Branch ID to edit: ");
	        String branchId = App.scanner.nextLine();

	        System.out.print("Enter new Branch name: ");
	        String newName = App.scanner.nextLine();

	        System.out.print("Enter new Branch location: ");
	        String newLocation = App.scanner.nextLine();

	        String sql = "UPDATE branch SET branch_name = ?, location = ? WHERE branch_id = ?";
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setString(1, newName);
	        ps.setString(2, newLocation);
	        ps.setString(3, branchId);

	        int i = ps.executeUpdate();
	        if (i == 1) {
	            System.out.println("Branch updated successfully!");
	        } else {
	            System.out.println("Branch ID not found or update failed.");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public static void viewBranches() {
		getBranchCount();
	    try (Connection con = ConnectionProvider.getCon()) {
	        String sql = "SELECT branch_id, branch_name, location FROM branch";
	        PreparedStatement ps = con.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();

	        System.out.println("\n--- Branch List ---");
	        String getcount=Integer.toString(count);
            System.out.println("Branch count: "+ getcount);
	        while (rs.next()) {
	            String id = rs.getString("branch_id");
	            String name = rs.getString("branch_name");
	            String location = rs.getString("location");
	            
	            System.out.println("Branch ID     : " + id);
	            System.out.println("Branch Name   : " + name);
	            System.out.println("Location      : " + location);
	            System.out.println("---------------------------");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public static void deleteBranch() {
	    try (Connection con = ConnectionProvider.getCon()) {
	        System.out.print("Enter Branch ID to delete: ");
	        String branchId = App.scanner.nextLine();

	        String sql = "DELETE FROM branch WHERE branch_id = ?";
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setString(1, branchId);

	        int i = ps.executeUpdate();
	        if (i == 1) {
	            System.out.println("Branch deleted successfully!");
	        } else {
	            System.out.println("Branch ID not found or deletion failed.");
	        }

	    } catch (SQLException e) {
	        if (e.getErrorCode() == 1451) {
	            System.out.println("Cannot delete branch: It is referenced by other records.");
	        } else {
	            e.printStackTrace();
	        }
	    }
	}



}
