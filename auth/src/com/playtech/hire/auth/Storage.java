package com.playtech.hire.auth;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Storage {
  
  private final Accounts accounts;

  Storage(Accounts accounts){
    this.accounts = accounts;
  }
  
  public void read(InputStream in) throws IOException{
    Reader r = new InputStreamReader(in, StandardCharsets.UTF_8);
    Properties p = new Properties();
    p.load(r);
    
    p.forEach((k,v) -> accounts.addExistingEntry(k.toString(), v.toString()));       
  }
  
  public void write(OutputStream out) throws IOException{   
    Properties p = new Properties(){//ordered storage (alphabetically)
      private static final long serialVersionUID = 1L;
      @Override
      public synchronized Enumeration<Object> keys() {
        Object[] x = accounts.auth.keySet().toArray();
        return Collections.enumeration(Arrays.asList(x));
      }
      
    };
    
    p.putAll(accounts.auth);
    p.store(new OutputStreamWriter(out, StandardCharsets.UTF_8), "Accounts store as java properties");
    
    
  }
}
