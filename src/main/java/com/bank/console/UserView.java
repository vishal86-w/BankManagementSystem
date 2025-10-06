package com.bank.console;

import com.bank.controls.UserControl;
import com.bank.controls.UserEmailControl;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.DepositFailedException;
import com.bank.exception.InsufficientBalanceException;
import com.bank.exception.InvalidDepositAmountException;
import com.bank.exception.InvalidWithdrawalAmountException;
import com.bank.exception.WithdrawalFailedException;
import com.bank.project.App;

public class UserView {

    // ANSI escape codes for colors
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";

    public static void userAction() throws AccountNotFoundException, InvalidDepositAmountException, DepositFailedException, InvalidWithdrawalAmountException, InsufficientBalanceException, WithdrawalFailedException {
        System.out.println(BLUE + "*********** User Menu ***********" + RESET);

        System.out.println(GREEN + "1. Deposit money" + RESET);
        System.out.println(GREEN + "2. Withdraw money" + RESET);
        System.out.println(GREEN + "3. View profile" + RESET);
        System.out.println(GREEN + "4. Transaction history" + RESET);
        System.out.println(GREEN + "5. Transfer money" + RESET);
        System.out.println(GREEN + "6. Check balance" + RESET);
        System.out.println(GREEN + "7. Mini statement" + RESET);
        System.out.println(RED + "8. Log out" + RESET);

        System.out.print(YELLOW + "Enter your choice: " + RESET);
        int option = App.scanner.nextInt();
        App.scanner.nextLine();

        switch (option) {
            case 1:
                UserControl.deposit();
                break;
            case 2:
                UserControl.withdraw();
                break;
            case 3:
                UserControl.viewProfile();
                break;
            case 4:
                UserControl.viewTransactions();
                break;
            case 5:
                UserControl.transfer();
                break;
            case 6:
                UserControl.checkBalance();
                break;
            case 7:
                System.out.print(YELLOW + "Enter your Account ID: " + RESET);
                String accId = App.scanner.nextLine().trim();
                UserEmailControl.generateAndSendReport(accId);
                break;
            case 8:
                UserControl.logout();
                break;
            default:
                System.out.println(RED + "Invalid choice, Try Again!!!" + RESET);
                userAction();
                break;
        }
    }
}
