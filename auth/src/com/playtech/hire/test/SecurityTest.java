package com.playtech.hire.test;

import static com.playtech.hire.test.Assert.fail;

import java.util.concurrent.Callable;

import com.playtech.hire.auth.PasswordEncryptionService;

public class SecurityTest implements Callable<Void> {

    @Override
    public Void call() throws Exception {
	testPasswordEncryptionService();
	return null;
    }

    void testPasswordEncryptionService() throws Exception {
	PasswordEncryptionService pes = new PasswordEncryptionService();
	String passwordWithSalt = pes.getEncryptedPasswordWithSalt("secretPassword");
	boolean authRes;

	authRes = pes.authenticate("secretPassword", passwordWithSalt);
	if (authRes != true)
	    fail("Failed to authenicate valid password and username");

	authRes = pes.authenticate("incorrectPassword", passwordWithSalt);
	if (authRes != false)
		  fail("Failed to reject authenication with invalid salt");
    }
}