package com.playtech.hire.test;

import static com.playtech.hire.test.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

import com.playtech.hire.auth.Accounts;
import com.playtech.hire.auth.IllegalOperation;
import com.playtech.hire.auth.LoginEntry;
public class PersistentTest  implements Callable<Void>{

  @Override
  public Void call() throws Exception {
    tryGuess();
    return null;
  }

  void tryGuess() throws Exception {

    Accounts a = new CreateInitial().call();

    File file = new File("accounts.acc");
    if (file.canRead()){
      try(InputStream in = new FileInputStream(file)){
        a.getStorage().read(in);
      }catch (IOException e) {
        throw new AssertionError(e);
      }
    }

    String[] uids = {"rex", "dana", "blox"};
    createIfMissing(a, uids); 
    
    for (String uid : uids){
      String pwd = guess(a, uid);
      if (pwd == null){
        fail("Failed to guess password for account: %s", uid);
      }
      String newPwd = randomPasswordForAccount(uid);
      a.changePassword(uid, pwd, newPwd);
      
      if (a.auth(uid, newPwd) == null){
        fail("Failed to changed password for account: %s", uid);
      }      
    }
    

    try(OutputStream out = new FileOutputStream("accounts.acc")){
      a.getStorage().write(out);
    };    
  }
  
  String guess(Accounts a, String uid){
    for (int i = 0; i < 997 * 997; i++){
      String pwd = randomPasswordForAccount(uid);
      LoginEntry e = a.auth(uid, pwd);
      if (e!=null)
        return pwd;
    }
    return null;
  }

  private static String randomPasswordForAccount(String uid) {
    return randomPassword(uid.substring(0,1));
  }
  
  private static void createIfMissing(Accounts a, String[] uids) {
    for (String uid : uids){
      try{
        a.create(uid, randomPasswordForAccount(uid));
      }catch (IllegalOperation alreadExisting) {
      }
    }
   
  }

  static String randomPassword(String prefix){
    return String.join(":", prefix, "rngPass", Integer.toString(ThreadLocalRandom.current().nextInt(997), Character.MAX_RADIX));
  }
}