package com.bank.exception;

@SuppressWarnings("serial")
public class WithdrawalFailedException extends Exception {
    public WithdrawalFailedException(String message) {
        super(message);
    }
}