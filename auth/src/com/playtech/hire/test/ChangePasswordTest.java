package com.playtech.hire.test;

import java.util.concurrent.Callable;

import com.playtech.hire.auth.Accounts;
import com.playtech.hire.auth.IllegalOperation;

public class ChangePasswordTest implements Callable<Void>{

  @Override
  public Void call() throws Exception {
    Accounts a = new CreateInitial().call();
    a.changePassword("bin", "-txtBin", "sub2bin+");
    try{
      a.changePassword("bin", "-txtBin", "sub2bin+");
      Assert.fail("password unchanged?");
    }catch (IllegalOperation e) {
    }

    return null;
  }

}
