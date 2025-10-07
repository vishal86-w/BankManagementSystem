package com.bank.project;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.bank.exception.AccountNotFoundException;
import com.bank.exception.DepositFailedException;
import com.bank.exception.InsufficientBalanceException;
import com.bank.exception.InvalidDepositAmountException;
import com.bank.exception.InvalidWithdrawalAmountException;
import com.bank.exception.WithdrawalFailedException;

public class App 
{
	public static final Scanner scanner = new Scanner(System.in);
    public static void main( String[] args ) throws InputMismatchException, AccountNotFoundException, InvalidDepositAmountException, DepositFailedException, InvalidWithdrawalAmountException, InsufficientBalanceException, WithdrawalFailedException
    {

			com.bank.console.MainMenu.show();
		
			scanner.close();
		
    }
}
