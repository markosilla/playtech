package com.playtech.hire.auth;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

import com.playtech.hire.auth.LoginEntry.AuthType;

public class Accounts {
    final SortedMap<String, String> auth = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    final PasswordEncryptionService pes = new PasswordEncryptionService();

    void addExistingEntry(String uid, String storedPassword) {
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
	String pwd = auth.get(uid);
	if (pwd == null) {
	    return null;
	}

	LoginEntry e = new LoginEntry(uid, pwd);

	if (e.matchesPassword(plainTextPassword)) {
	    try {
		pwd = pes.getEncryptedPasswordWithSalt(plainTextPassword);
	    } catch (NoSuchAlgorithmException e1) {
		e1.printStackTrace();
	    } catch (InvalidKeySpecException e1) {
		e1.printStackTrace();
	    }

	    return new LoginEntry(auth.tailMap(uid).firstKey(), pwd, AuthType.PBKDF2); // exact matching
	} else {
	    try {
		if (pes.authenticate(plainTextPassword, pwd) == true) {
		    return new LoginEntry(auth.tailMap(uid).firstKey(), pwd, AuthType.PBKDF2);
		}
	    } catch (NoSuchAlgorithmException | InvalidKeySpecException e1) {
		e1.printStackTrace();
		return null;
	    }
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
	LoginEntry e = newLoginEntry(uid, plainTextPassword);
	e.addTo(auth);
	return e;
    }

    private static String validateUid(String uid) {
	String banned = "[]{}<>|*?&";
	for (char c : uid.toCharArray()) {// null check as well
	    if (Character.isWhitespace(c)) {
		throw new IllegalOperation("A white space character in uid");
	    }
	    if (Character.isISOControl(c) || Character.isMirrored(c) || Character.isLowSurrogate(c)
		    || Character.isHighSurrogate(c)) {
		throw new IllegalOperation("Unsupported character");
	    }
	    if (banned.indexOf(c) >= 0) {
		throw new IllegalOperation("Unsupported character in uid: %s", c);
	    }
	}
	return uid;
    }

    private LoginEntry newLoginEntry(String uid, String plainTextPassword) {
	String securePassword = null;
	
	if (plainTextPassword.isEmpty()) {
	    throw new IllegalOperation("Empty password");
	}

	if (plainTextPassword.trim() != plainTextPassword) {
	    throw new IllegalOperation("Whitespaces at the begging/end of password");
	}
	
	try {
	    securePassword = pes.getEncryptedPasswordWithSalt(plainTextPassword);
	} catch (NoSuchAlgorithmException | InvalidKeySpecException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	    return null;
	}

	
	LoginEntry e = new LoginEntry(validateUid(uid), securePassword, AuthType.PBKDF2);
	return e;
    }

    public LoginEntry changePassword(String uid, String oldPassword, String newPassword) {
	LoginEntry existing = auth(uid, oldPassword);
	if (existing == null) {
	    throw new IllegalOperation("Account [%s] not found", uid);
	}
	LoginEntry replacement = newLoginEntry(existing.getUid(), newPassword);
	replacement.replaceExisting(auth);
	return replacement;
    }

    public Storage getStorage() {
	return new Storage(this);
    }

    public int size() {
	return auth.size();
    }

}
