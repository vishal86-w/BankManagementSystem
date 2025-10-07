package com.bank.console;

import java.util.InputMismatchException;

import com.bank.exception.AccountNotFoundException;
import com.bank.exception.DepositFailedException;
import com.bank.exception.InsufficientBalanceException;
import com.bank.exception.InvalidDepositAmountException;
import com.bank.exception.InvalidWithdrawalAmountException;
import com.bank.exception.WithdrawalFailedException;
import com.bank.project.App;


public class MainMenu {
	//reset color 
	public static final String RESET = "\033[0m";

    // Regular Colors
    public static final String BLACK = "\033[0;30m";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String PURPLE = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";
    public static final String WHITE = "\033[0;37m";

    // Background
    public static final String RED_BACKGROUND = "\033[41m";
    public static final String GREEN_BACKGROUND = "\033[42m";
    public static final String YELLOW_BACKGROUND = "\033[43m";
    public static final String BLUE_BACKGROUND = "\033[44m";

    // Bright Colors
    public static final String RED_BRIGHT = "\033[0;91m";
    public static final String GREEN_BRIGHT = "\033[0;92m";
    public static final String YELLOW_BRIGHT = "\033[0;93m";
    public static final String BLUE_BRIGHT = "\033[0;94m";

	public static void show() throws InputMismatchException, AccountNotFoundException, InvalidDepositAmountException, DepositFailedException, InvalidWithdrawalAmountException, InsufficientBalanceException, WithdrawalFailedException
	{
		
		try {
			while(true)
			{
				System.out.println(CYAN+"***********Bank Management System***********"+RESET);
				System.out.println(GREEN+"1. Admin portal"+RESET);
			    System.out.println(YELLOW+"2. Manager portal"+RESET);
			    System.out.println(BLUE_BRIGHT+"3. Customer portal"+RESET);
			    System.out.println(RED_BRIGHT+"4. Exit"+RESET);
			    System.out.print(YELLOW_BRIGHT+"Choose your role: "+RESET);
			    
			    int choice = App.scanner.nextInt();
			    switch (choice) {
				case 1:
					// Admin login
					Admin.AdminPortal();
					break;
				case 2:
					//manager login
					Manager.ManagerPortal();
					break;
				case 3:
					//user login
					User.UserPortal();
					break;
				case 4:
					System.out.println("thank you for using our bank");
					System.exit(0);
					break;
				default:
					System.out.println("Invalid choice , Try Again!!!");
					show();
					break;
				}
			}
		} 
		catch(InputMismatchException e)
		{
			System.out.println("please a valid number to proceed...");
			App.scanner.nextLine();
			show();
		}
		
	}

}
