package com.playtech.hire.auth;

public class IllegalOperation extends RuntimeException{
  private static final long serialVersionUID = 1L;

  public IllegalOperation(String message, Throwable cause) {
    super(message, cause);
  }

  public IllegalOperation(String message, Object... args) {
    super(String.format(message, args));
  }  
}
