package com.bank.controls;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.UUID;

import com.bank.console.MainMenu;
import com.bank.console.UserView;
import com.bank.dao.ConnectionProvider;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.DepositFailedException;
import com.bank.exception.InsufficientBalanceException;
import com.bank.exception.InvalidDepositAmountException;
import com.bank.exception.InvalidWithdrawalAmountException;
import com.bank.exception.WithdrawalFailedException;
import com.bank.project.App;


public class UserControl {

	
	public static void deposit() throws AccountNotFoundException ,InvalidDepositAmountException , DepositFailedException, InvalidWithdrawalAmountException, InsufficientBalanceException, WithdrawalFailedException {
		System.out.println("Enter your Account Id: ");
	    String accId = App.scanner.nextLine();

	    System.out.println("Enter the amount you want to deposit : ");
	    double deposit = App.scanner.nextDouble();
	    App.scanner.nextLine(); 

	    try {
	        if (deposit <= 0) {
	            throw new InvalidDepositAmountException("Deposit amount must be greater than zero.");
	        }

	        try (Connection con = ConnectionProvider.getCon()) {
	            String checkSql = "SELECT balance FROM customer WHERE account_holder_number = ?";
	            PreparedStatement checkStmt = con.prepareStatement(checkSql);
	            checkStmt.setString(1, accId.trim());
	            ResultSet rs = checkStmt.executeQuery();

	            if (!rs.next()) {
	                throw new AccountNotFoundException("Account ID " + accId + " not found.");
	            }

	            double currentBalance = rs.getDouble("balance");
	            double newBalance = currentBalance + deposit;

	            String updateSql = "UPDATE customer SET balance = ? WHERE account_holder_number = ?";
	            PreparedStatement updateStmt = con.prepareStatement(updateSql);
	            updateStmt.setDouble(1, newBalance);
	            updateStmt.setString(2, accId);

	            int rows = updateStmt.executeUpdate();
	            if (rows <= 0) {
	                throw new DepositFailedException("Unable to update balance. Please try again.");
	            }

	            String insertTxn = "INSERT INTO transaction (transaction_id, account_holder_number, transaction_type, amount, transaction_date) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
	            PreparedStatement txnStmt = con.prepareStatement(insertTxn);
	            txnStmt.setString(1, UUID.randomUUID().toString().substring(0, 20));
	            txnStmt.setString(2, accId);
	            txnStmt.setString(3, "deposit");
	            txnStmt.setDouble(4, deposit);
	            txnStmt.executeUpdate();

	            System.out.println("Deposit successful! New balance: ₹" + newBalance);
	        }
	    } catch (InvalidDepositAmountException | AccountNotFoundException | DepositFailedException e) {
	        System.out.println("Error: " + e.getMessage());
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    UserView.userAction();
	}


	public static void withdraw() throws InvalidWithdrawalAmountException ,AccountNotFoundException , InsufficientBalanceException ,WithdrawalFailedException, InvalidDepositAmountException, DepositFailedException{
	    System.out.println("Enter your Account ID: ");
	    String accId = App.scanner.nextLine().trim();

	    System.out.println("Enter amount to withdraw: ");
	    double amount = App.scanner.nextDouble();
	    App.scanner.nextLine(); 

	    try {
	        if (amount <= 0) {
	            throw new InvalidWithdrawalAmountException("Withdrawal amount must be greater than zero.");
	        }

	        try (Connection con = ConnectionProvider.getCon()) {
	            String sql = "SELECT balance FROM customer WHERE account_holder_number = ?";
	            PreparedStatement ps = con.prepareStatement(sql);
	            ps.setString(1, accId);
	            ResultSet rs = ps.executeQuery();

	            if (!rs.next()) {
	                throw new AccountNotFoundException("Account ID " + accId + " not found.");
	            }

	            double currentBalance = rs.getDouble("balance");

	            if (currentBalance < amount) {
	                throw new InsufficientBalanceException("Insufficient balance. Available: ₹" + currentBalance);
	            }

	            double newBalance = currentBalance - amount;

	            String updateSql = "UPDATE customer SET balance = ? WHERE account_holder_number = ?";
	            PreparedStatement updateStmt = con.prepareStatement(updateSql);
	            updateStmt.setDouble(1, newBalance);
	            updateStmt.setString(2, accId);

	            int rows = updateStmt.executeUpdate();
	            if (rows <= 0) {
	                throw new WithdrawalFailedException("Unable to update balance. Please try again.");
	            }

	            String insertTxn = "INSERT INTO transaction (transaction_id, account_holder_number, transaction_type, amount, transaction_date) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
	            PreparedStatement txnStmt = con.prepareStatement(insertTxn);
	            txnStmt.setString(1, UUID.randomUUID().toString().substring(0, 36));
	            txnStmt.setString(2, accId);
	            txnStmt.setString(3, "withdrawal");
	            txnStmt.setDouble(4, amount);
	            txnStmt.executeUpdate();

	            System.out.println("Withdrawal successful! New balance: ₹" + newBalance);
	        }
	    } catch (InvalidWithdrawalAmountException | AccountNotFoundException | InsufficientBalanceException | WithdrawalFailedException e) {
	        System.out.println("Error: " + e.getMessage());
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    UserView.userAction();
	}

public static void viewTransactions() throws AccountNotFoundException, InvalidDepositAmountException, DepositFailedException, InvalidWithdrawalAmountException, InsufficientBalanceException, WithdrawalFailedException  {
    System.out.println("Enter your Account ID: ");
    String accId = App.scanner.nextLine().trim();

    try (Connection con = ConnectionProvider.getCon()) {
        String sql = "SELECT transaction_id, transaction_type, amount, transaction_date FROM transaction WHERE account_holder_number = ? ORDER BY transaction_date DESC";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, accId);

        ResultSet rs = ps.executeQuery();

        System.out.println("\n Transaction History:");
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

    UserView.userAction();
}


public static void viewProfile() throws AccountNotFoundException, InvalidDepositAmountException, DepositFailedException, InvalidWithdrawalAmountException, InsufficientBalanceException, WithdrawalFailedException
{
	System.out.println("Enter your Account ID: ");
    String accId = App.scanner.nextLine().trim();

    try (Connection con = ConnectionProvider.getCon()) {
        String sql = "SELECT account_holder_name, account_holder_email, branch_id, branch_name, account_type, balance, status FROM customer WHERE account_holder_number = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, accId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            System.out.println("\n Profile Details:");
            System.out.println("Name           : " + rs.getString("account_holder_name"));
            System.out.println("Email          : " + rs.getString("account_holder_email"));
            System.out.println("Branch ID      : " + rs.getString("branch_id"));
            System.out.println("Branch Name    : " + rs.getString("branch_name"));
            System.out.println("Account Type   : " + rs.getString("account_type"));
            System.out.println("Balance        : ₹." + rs.getDouble("balance"));
            System.out.println("Status         : " + (rs.getBoolean("status") ? "Active" : "Inactive"));
        } else {
            throw new AccountNotFoundException("Account not found. Please check your Account ID.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    UserView.userAction();
}
public static void checkBalance() throws AccountNotFoundException, InvalidDepositAmountException, DepositFailedException, InvalidWithdrawalAmountException, InsufficientBalanceException, WithdrawalFailedException{
	System.out.println("Enter your Account ID: ");
    String accId = App.scanner.nextLine().trim();

    try (Connection con = ConnectionProvider.getCon()) {
        String sql = "SELECT account_holder_name, balance FROM customer WHERE account_holder_number = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, accId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String name = rs.getString("account_holder_name");
            double balance = rs.getDouble("balance");
            System.out.println("Hello " + name + ", your current balance is ₹" + balance);
        } else {
        	 throw new AccountNotFoundException("Account not found. Please check your Account ID.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    UserView.userAction();
}
public static void transfer() throws AccountNotFoundException, InvalidDepositAmountException, DepositFailedException, InvalidWithdrawalAmountException, InsufficientBalanceException, WithdrawalFailedException
{
	System.out.println("Enter your Account ID (Sender): ");
    String senderId = App.scanner.nextLine().trim();

    System.out.println("Enter Recipient Account ID: ");
    String receiverId = App.scanner.nextLine().trim();

    System.out.println("Enter amount to transfer: ");
    double amount = App.scanner.nextDouble();
    App.scanner.nextLine(); // consume newline

    if (amount <= 0) {
        System.out.println("Invalid amount. Must be greater than zero.");
        return;
    }

    try (Connection con = ConnectionProvider.getCon()) {
        con.setAutoCommit(false); // Start transaction

        // Check sender balance
        String senderSql = "SELECT balance FROM customer WHERE account_holder_number = ?";
        PreparedStatement senderStmt = con.prepareStatement(senderSql);
        senderStmt.setString(1, senderId);
        ResultSet senderRs = senderStmt.executeQuery();

        if (!senderRs.next()) {
            System.out.println("Sender account not found.");
            return;
        }

        double senderBalance = senderRs.getDouble("balance");
        if (senderBalance < amount) {
            System.out.println("Insufficient balance.");
            return;
        }

        // Check receiver account
        String receiverSql = "SELECT balance FROM customer WHERE account_holder_number = ?";
        PreparedStatement receiverStmt = con.prepareStatement(receiverSql);
        receiverStmt.setString(1, receiverId);
        ResultSet receiverRs = receiverStmt.executeQuery();

        if (!receiverRs.next()) {
            System.out.println("Receiver account not found.");
            return;
        }

        double receiverBalance = receiverRs.getDouble("balance");

        // Update sender balance
        String updateSender = "UPDATE customer SET balance = ? WHERE account_holder_number = ?";
        PreparedStatement updateSenderStmt = con.prepareStatement(updateSender);
        updateSenderStmt.setDouble(1, senderBalance - amount);
        updateSenderStmt.setString(2, senderId);
        updateSenderStmt.executeUpdate();

        // Update receiver balance
        String updateReceiver = "UPDATE customer SET balance = ? WHERE account_holder_number = ?";
        PreparedStatement updateReceiverStmt = con.prepareStatement(updateReceiver);
        updateReceiverStmt.setDouble(1, receiverBalance + amount);
        updateReceiverStmt.setString(2, receiverId);
        updateReceiverStmt.executeUpdate();

        con.commit(); // Commit transaction
        System.out.println(" ₹" + amount + " transferred successfully from " + senderId + " to " + receiverId);
    } catch (SQLException e) {
        e.printStackTrace();
    }
    UserView.userAction();
}



public static void logout() throws InputMismatchException, AccountNotFoundException, InvalidDepositAmountException, DepositFailedException, InvalidWithdrawalAmountException, InsufficientBalanceException, WithdrawalFailedException
{
	System.out.println("Thanks for using our bank");
	MainMenu.show();
}

}
