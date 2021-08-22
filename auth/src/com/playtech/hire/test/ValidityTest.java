package com.playtech.hire.test;

import static com.playtech.hire.test.Assert.*;

import java.util.concurrent.Callable;

import com.playtech.hire.auth.Accounts;
import com.playtech.hire.auth.IllegalOperation;
public class ValidityTest implements Callable<Void>{

  @Override
  public Void call() throws Exception {
    final Accounts a = new Accounts();
    
    createAndAuth(a);    
    invalidCharacters(a);
    invalidPassword(a);
    nullPassword(a);
    
    
    return null;
  }

  private void createAndAuth(final Accounts a) {
    {
      String pwd = "any longer[]\"pass";
      a.create("n1", pwd);
      assertEquals(a.auth("n1", pwd).getUid(), "n1");
      assertEquals(a.auth("N1", pwd).getUid(), "n1");
    }
    
    {//case insensitive match
      String pwd = "empty''";
      a.create("account2", pwd);
      assertEquals(a.size(), 2);
      assertEquals(a.auth("ACcount2", pwd).getUid(), "account2");
    }
    
    {//no match
      assertEquals(a.auth("n1", "xxx"), null);
    }
  }

  private void invalidCharacters(final Accounts a) {
    try{
      a.create("<xml/>", "xxx");
      fail("invalid chracters accepted");            
    }catch(IllegalOperation _skip){}
  }


  private void invalidPassword(final Accounts a) {
    try{
      a.create("goodman", " abx--");
      fail("white space accepted");            
    }catch(IllegalOperation _skip){}   
  }
  
  private void nullPassword(final Accounts a) {
    try{
      a.create("goodman", null);
      fail("null password accepted");            
    }catch(NullPointerException _skip){}   
  }
}
