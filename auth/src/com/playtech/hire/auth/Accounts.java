package com.playtech.hire.auth;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

public class Accounts {
    
    private IStorage storage;
    
    public Accounts (IStorage storage) {
	if (storage == null)
	    throw new IllegalArgumentException();
	
	this.storage = storage;
    }

    public int size() {
	return storage.getSize();
    } 
    
    public boolean authUsersFromFile(File file) {
	if (file.canRead()) {
	    try (InputStream in = new FileInputStream(file)) {
		storage.read(in);
	    } catch (IOException e) {
		throw new AssertionError(e);
	    }
	}
	
	
	
	return true;
    }
    
    
    void addExistingEntry(String uid, SecurityToken storedPassword) {
	auth.putIfAbsent(validateUid(uid), Objects.requireNonNull(storedPassword));
    }

    /**
     * 
     * @param uid               user identity
     * @param plainTextPassword plaintext password of the uid
     * @return exact uid of the account when authentication succeeds, null when no
     *         account found
     */
    public LoginEntry auth(String uid, String plainTextPassword) {
	SecurityToken pwd = auth.get(uid);
	if (pwd == null) {
	    return null;
	}
	LoginEntry e = new LoginEntry(uid, pwd);
	
	if (e.matchesPassword(pwd)) {
	    return new LoginEntry(auth.tailMap(uid).firstKey(), pwd); // exact matching
	}

	return null;
    }

    /**
     * 
     * @param uid
     * @param plainTextPassword
     * @return a newly create account
     * @throws IllegalOperation if the account exist already or the input is illegal
     */
    public LoginEntry create(String uid, String plainTextPassword) {
	PasswordEncryptionService pes = new PasswordEncryptionService();
	
	SecurityToken st = new SecurityToken("");
	
	
	
	LoginEntry e = newLoginEntry(uid, st);
	e.addTo(auth);
	return e;
    }

    private LoginEntry newLoginEntry(String uid, SecurityToken storedPassword) {
//	if (plainTextPassword.isEmpty()) {
//	    throw new IllegalOperation("Empty password");
//	}
//
//	if (plainTextPassword.trim() != plainTextPassword) {
//	    throw new IllegalOperation("Whitespaces at the begging/end of password");
//	}
	if (storedPassword.isValidToken())
	    throw new IllegalOperation("Illegal password");
	
	LoginEntry e = new LoginEntry(validateUid(uid), storedPassword);
	return e;
    }

    public LoginEntry changePassword(String uid, String oldPassword, String newPassword) {
	LoginEntry existing = auth(uid, oldPassword);
	if (existing == null) {
	    throw new IllegalOperation("Account [%s] not found", uid);
	}
	
	
	PasswordEncryptionService pes = new PasswordEncryptionService();
	
	SecurityToken newSecurityToken = new SecurityToken(newPassword);
	
	
	LoginEntry replacement = newLoginEntry(existing.getUid(), newSecurityToken);
	replacement.replaceExisting(auth);
	return replacement;
    }



}
