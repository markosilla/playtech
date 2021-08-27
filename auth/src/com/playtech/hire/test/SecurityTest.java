package com.playtech.hire.test;

import static com.playtech.hire.test.Assert.fail;

import java.util.concurrent.Callable;

import com.playtech.hire.auth.PasswordEncryptionService;

public class SecurityTest implements Callable<Void> {

    @Override
    public Void call() throws Exception {
	testEncryptionFlow();
	return null;
    }

    void testEncryptionFlow() throws Exception {
	PasswordEncryptionService pes = new PasswordEncryptionService();
	byte[] salt = pes.generateSalt();
	byte[] otherSalt = pes.generateSalt();
	byte[] encryptedPass = pes.getEncryptedPassword("secretPassword", salt);
	boolean authRes;

	authRes = pes.authenticate("secretPassword", encryptedPass, salt);
	if (authRes != true)
	    fail("Failed to authenicate valid password and username");

	authRes = pes.authenticate("secretPassword", encryptedPass, otherSalt);
	if (authRes != false)
	    fail("Failed to reject authenication with invalid salt");

	authRes = pes.authenticate("incorrectPassword", encryptedPass, salt);
	if (authRes != false)
	    fail("Failed to reject authenication with invalid password");

	try {
	    authRes = pes.authenticate("incorrectPassword", encryptedPass, null);
	    fail("javax null salt not accepted?");
	} catch (NullPointerException npe) {
	}
    }
}