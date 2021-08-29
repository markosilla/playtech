package com.playtech.hire.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;

import com.playtech.hire.auth.Accounts;
import com.playtech.hire.auth.IStorage;
import com.playtech.hire.auth.Storage;

public class CreateInitial implements Callable<Accounts> {

    @Override
    public Accounts call() throws Exception {
	return createInitial();
    }

    private static Accounts createInitial() throws IOException {
	IStorage s = Storage.createStorage();
	Accounts a = new Accounts(s);
	File initialFile = new File("initial.acc");

	try (InputStream f = new FileInputStream(initialFile)) {
	    s.read(f);
	    
	    if (a.size() > 0) {
		Assert.assertEquals(a.auth("xxs", "noPass0").getUid(), "xxs");
		return a;
	    }
	} catch (FileNotFoundException _skip) {
	}

	a.create("xxs", "noPass0");
	a.create("bin", "-txtBin");

	if (s == null)
	    return a;
	
	try (OutputStream out = new FileOutputStream(initialFile)) {
	    s.write(out);
	}
	return a;
    }
}
