package com.playtech.hire.auth;

public class SecurityToken {
    private byte[] password;
    private byte[] salt;
    
    
    public SecurityToken(String string) {
	// TODO Auto-generated constructor stub
    }

    public boolean isValidToken() {
	return true;
    }
    
    @Override
    public String toString() {
	return String.format("");
    }

    @Override
    public boolean equals(Object o) {
	return true;
    }   
}
