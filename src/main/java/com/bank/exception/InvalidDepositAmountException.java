package com.bank.exception;

@SuppressWarnings("serial")
public class InvalidDepositAmountException extends Exception{
	public InvalidDepositAmountException(String message) {
        super(message);
    }
}
