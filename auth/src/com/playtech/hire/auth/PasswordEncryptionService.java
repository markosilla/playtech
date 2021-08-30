package com.playtech.hire.auth;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/*
 * References:
 * - https://www.javacodegeeks.com/2012/05/secure-password-storage-donts-dos-and.html
 * - https://stackoverflow.com/questions/19348501/pbkdf2withhmacsha512-vs-pbkdf2withhmacsha1
 * - https://betterprogramming.pub/how-to-correctly-store-passwords-in-a-database-5d261f49f605
 * 
 */
public class PasswordEncryptionService {

    private static final int SALT_LEN = 8;
    private static final int ITERATIONS = 1; /* Choose and see NIST recommendation below */

    public boolean authenticate(String attemptedPassword, String passwordWithSalt)
	    throws NoSuchAlgorithmException, InvalidKeySpecException {
	byte[] passwordWithSaltInBytes = null;
	byte[] salt = new byte[SALT_LEN];
	byte[] encryptedPass = null;

	try {
	    passwordWithSaltInBytes = Base64.getDecoder().decode(passwordWithSalt);
	} catch (IllegalArgumentException iae) {
	    return false;
	}
	
	if (passwordWithSaltInBytes.length <= SALT_LEN)
	    throw new InvalidKeySpecException("Invalid hash key");

	encryptedPass = new byte[passwordWithSaltInBytes.length - SALT_LEN];

	System.arraycopy(passwordWithSaltInBytes, 0, salt, 0, SALT_LEN);
	System.arraycopy(passwordWithSaltInBytes, SALT_LEN, encryptedPass, 0,
		(passwordWithSaltInBytes.length - SALT_LEN));

	return authenticate(attemptedPassword, encryptedPass, salt);
    }

    public String getEncryptedPasswordWithSalt(String plainTextPassword)
	    throws NoSuchAlgorithmException, InvalidKeySpecException {
	byte[] randomSalt = generateSalt();
	byte[] encryptedPass = getEncryptedPassword(plainTextPassword, randomSalt);
	byte[] passwordWithSaltInBytes = new byte[randomSalt.length + encryptedPass.length];

	/* saltedPassword = salt + password */
	System.arraycopy(randomSalt, 0, passwordWithSaltInBytes, 0, SALT_LEN);
	System.arraycopy(encryptedPass, 0, passwordWithSaltInBytes, SALT_LEN, encryptedPass.length);

	return Base64.getEncoder().encodeToString(passwordWithSaltInBytes);
    }
    
    private boolean authenticate(String attemptedPassword, byte[] encryptedPassword, byte[] salt)
	    throws NoSuchAlgorithmException, InvalidKeySpecException {
	// Encrypt the clear-text password using the same salt that was used to
	// encrypt the original password
	byte[] encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword, salt);

	// Authentication succeeds if encrypted password that the user entered
	// is equal to the stored hash
	return Arrays.equals(encryptedPassword, encryptedAttemptedPassword);
    }

    private byte[] getEncryptedPassword(String password, byte[] salt)
	    throws NoSuchAlgorithmException, InvalidKeySpecException {
	// PBKDF2 with SHA-1 as the hashing algorithm. Note that the NIST
	// specifically names SHA-1 as an acceptable hashing algorithm for PBKDF2
	String algorithm = "PBKDF2WithHmacSHA1";
	// SHA-1 generates 160 bit hashes, so that's what makes sense here
	int derivedKeyLength = 160;
	// Pick an iteration count that works for you. The NIST recommends at
	// least 1,000 iterations:
	// http://csrc.nist.gov/publications/nistpubs/800-132/nist-sp800-132.pdf
	// iOS 4.x reportedly uses 10,000:
	// http://blog.crackpassword.com/2010/09/smartphone-forensics-cracking-blackberry-backup-passwords/
	KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, derivedKeyLength);
	SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);

	return f.generateSecret(spec).getEncoded();
    }

    private byte[] generateSalt() throws NoSuchAlgorithmException {
	// VERY important to use SecureRandom instead of just Random
	SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

	// Generate a 8 byte (64 bit) salt as recommended by RSA PKCS5
	byte[] salt = new byte[SALT_LEN];
	random.nextBytes(salt);

	return salt;
    }
}
