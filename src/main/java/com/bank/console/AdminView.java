package com.bank.console;

import java.util.InputMismatchException;

import com.bank.controls.AdminControl;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.DepositFailedException;
import com.bank.exception.InsufficientBalanceException;
import com.bank.exception.InvalidDepositAmountException;
import com.bank.exception.InvalidWithdrawalAmountException;
import com.bank.exception.WithdrawalFailedException;
import com.bank.project.App;

public class AdminView {
    // ANSI escape codes for colors
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";

    public static void adminMenu() throws AccountNotFoundException, InputMismatchException, InvalidDepositAmountException, DepositFailedException, InvalidWithdrawalAmountException, InsufficientBalanceException, WithdrawalFailedException {
        while (true) {
            System.out.println(CYAN + "\n************* Admin Portal *************" + RESET);
            System.out.println(GREEN + "1. Add Manager" + RESET);
            System.out.println(GREEN + "2. Edit Manager" + RESET);
            System.out.println(GREEN + "3. Delete Manager" + RESET);
            System.out.println(GREEN + "4. Approve/Reject Manager" + RESET);
            System.out.println(GREEN + "5. View Transaction Reports" + RESET);
            System.out.println(GREEN + "6. Add branch" + RESET); 
            System.out.println(GREEN + "7. Edit branch" + RESET); 
            System.out.println(GREEN + "8. view branch" + RESET); 
            System.out.println(GREEN + "9. delete branch" + RESET); 
            System.out.println(RED + "10. Logout" + RESET);
            System.out.print(YELLOW + "Choose your option: " + RESET);

            int choice = App.scanner.nextInt();
            App.scanner.nextLine();

            switch (choice) {
                case 1:
                    AdminControl.addManager();
                    break;
                case 2:
                    AdminControl.editManager();
                    break;
                case 3:
                    AdminControl.deleteManager();
                    break;
                case 4:
                    AdminControl.approveOrRejectManager();
                    break;
                case 5:
                    AdminControl.viewTransactionReports();
                    break;
                case 6:
                    AdminControl.addBranch();
                    break;
                case 7:
                    AdminControl.editBranch();
                    break;
                case 8:
                    AdminControl.viewBranches();
                    break;
                case 9:
                    AdminControl.deleteBranch();
                    break;
                case 10:
                    System.out.println(RED + "Logging out..." + RESET);
                    MainMenu.show();
                    return;
                default:
                    System.out.println(PURPLE + "Invalid option. Try again." + RESET);
                    break;
            }
        }
    }
}
