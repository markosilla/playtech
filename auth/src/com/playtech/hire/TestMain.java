package com.playtech.hire;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;

import com.playtech.hire.test.ChangePasswordTest;
import com.playtech.hire.test.CreateInitial;
import com.playtech.hire.test.PersistentTest;
import com.playtech.hire.test.ValidityTest;

public class TestMain {
  public static void main(String[] args) throws Throwable{

    
    if (runTest() != 0){
      System.exit(1);
    }
    System.out.println("All tests completed successfully");
  }

  static int runTest(){
    Collection<Callable<?>> tests = Arrays.asList(
        new CreateInitial(),
        new ValidityTest(), 
        new ChangePasswordTest(),
        
        new PersistentTest(),//run it thrice
        new PersistentTest(),
        new PersistentTest()
    );
    
    int failures = 0;
    for (Callable<?> c : tests){
      try{
        c.call();
      }catch (Exception | Error e) {
        failures++;
        System.err.printf("FailedTest [%s]. Error/stack trace follows", c.getClass().getSimpleName());
        e.printStackTrace();
      }
    }
    return failures;
        
  }
  


}
