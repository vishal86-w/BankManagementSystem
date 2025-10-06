package com.bank.console;

import com.bank.controls.ManagerControl;
import com.bank.project.App;

public class ManagerView {

    // ANSI escape codes for colors
    public static final String RESET = "\u001B[0m";
    public static final String CYAN = "\u001B[36m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";

    public static void managerMenu() {
        while (true) {
            System.out.println(CYAN + "\n=====  Manager Portal =====" + RESET);
            System.out.println(GREEN + "1. View All Customers" + RESET);
            System.out.println(GREEN + "2. Approve/Reject Customer Accounts" + RESET);
            System.out.println(GREEN + "3. View Transactions by Account" + RESET);
            System.out.println(GREEN + "4. View Branch Summary" + RESET);
            System.out.println(GREEN + "5. Create New Customer Account" + RESET);
            System.out.println(GREEN + "6. Update Customer Details" + RESET);
            System.out.println(RED + "7. Logout" + RESET);

            System.out.print(YELLOW + "Enter your choice: " + RESET);
            int choice = App.scanner.nextInt();
            App.scanner.nextLine();

            switch (choice) {
                case 1:
                    ManagerControl.viewAllCustomers();
                    break;
                case 2:
                    ManagerControl.approveOrRejectCustomer();
                    break;
                case 3:
                    ManagerControl.viewTransactionsByAccount();
                    break;
                case 4:
                    ManagerControl.viewBranchSummary();
                    break;
                case 5:
                    ManagerControl.createCustomerAccount();
                    break;
                case 6:
                    ManagerControl.updateCustomerDetails();
                    break;
                case 7:
                    System.out.println(BLUE + "Logging out..." + RESET);
                    MainMenu.show();
                    return;
                default:
                    System.out.println(RED + "Invalid choice. Please try again." + RESET);
                    managerMenu();
                    break;
            }
        }
    }
}
