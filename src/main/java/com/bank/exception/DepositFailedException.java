package com.bank.exception;

@SuppressWarnings("serial")
public class DepositFailedException extends Exception{
	public DepositFailedException(String message) {
        super(message);
    }
}
