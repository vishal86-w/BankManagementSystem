package com.bank.console;

import java.util.Scanner;

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

	public static void show()
	{
		Scanner din = new Scanner(System.in);
		try {
			while(true)
			{
				System.out.println(CYAN+"***********Bank Management System***********"+RESET);
				System.out.println(GREEN+"1. Admin Login"+RESET);
			    System.out.println(YELLOW+"2. Manager Login"+RESET);
			    System.out.println(BLUE_BRIGHT+"3. User Login"+RESET);
			    System.out.println(RED_BRIGHT+"4. Exit"+RESET);
			    System.out.print("Choose your role: "+RESET);
			    
			    int choice = din.nextInt();
			    switch (choice) {
				case 1:
					//admin login
					break;
				case 2:
					//manager login
					break;
				case 3:
					//user login
					break;
				case 4:
					System.out.println("thank you for using our bank");
					System.exit(0);
					break;
				default:
					System.out.println("Invalid choice , Try Again!!!");
					break;
				}
			}
		} 
		finally {
			din.close();
		}
	}

}
