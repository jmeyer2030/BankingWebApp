package com.jmeyer2030.banking_backend.exception;

public class UserDoesNotExistException extends RuntimeException {

    public enum Context { SIGN_IN, TRANSACTION };

    public Context context;

    public UserDoesNotExistException(Context context) {
        super(context == Context.SIGN_IN ? "Username is incorrect" : "Recipient doesn't exist.");
        this.context = context;
    }

    public UserDoesNotExistException(Context context, String message) {
        super(message);
        this.context = context;
    }
}
