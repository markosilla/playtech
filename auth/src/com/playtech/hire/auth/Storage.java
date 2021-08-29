package com.playtech.hire.auth;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Storage implements IStorage {

    private final SortedMap<String, String> auth = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    
    public static IStorage createStorage() {
	return new Storage();
    }

    private Storage() {
	
    }
    
    public int getSize() {
	return auth.size();
    }

    @Override
    public void read(InputStream in) throws IOException {
	Reader r = new InputStreamReader(in, StandardCharsets.UTF_8);
	Properties p = new Properties();
	
	p.load(r);
	p.forEach((k, v) -> auth.putIfAbsent(validateUserId(k.toString()), Objects.requireNonNull(v.toString())));
    }

    @Override
    public void write(OutputStream out) throws IOException {
	Properties p = new Properties() {// ordered storage (alphabetically)
	    private static final long serialVersionUID = 1L;

	    @Override
	    public synchronized Enumeration<Object> keys() {
		Object[] x = auth.keySet().toArray();
		return Collections.enumeration(Arrays.asList(x));
	    }
	};

	p.putAll(auth);
	p.store(new OutputStreamWriter(out, StandardCharsets.UTF_8), "Accounts store as java properties");
    }

    
    /**
     * 
     * Validation utility function, can be separate
     * 
     * @param uid
     * @return
     */
    public static String validateUserId(String uid) {
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
}
