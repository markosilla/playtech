package com.playtech.hire.test;

import java.util.Objects;

public class Assert {
  private Assert(){};
  
  public static void assertEquals(Object a, Object b) {
    if (Objects.equals(a, b) && Objects.equals(b, a)){//test transitive
      return;
    }
    throw new AssertionError(String.format("[%s] should equal [%s]", a, b));
  }
  
  public static void fail(String error, Object... args){
    throw new AssertionError(String.format(error, args));
  }
}
