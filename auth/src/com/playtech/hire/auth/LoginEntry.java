package com.playtech.hire.auth;

import java.util.Map;

public class LoginEntry {
  enum AuthType{
    PLAIN_TEXT;//future use
  }

  final String uid;
  final String auth;

  AuthType type;


  LoginEntry(String uid, String auth) {
    this(uid, auth, AuthType.PLAIN_TEXT);
  }

  LoginEntry(String uid, String auth, AuthType type) {
    this.uid = uid;    
    this.auth = auth;
    this.type = type;
  }


  
  public String getUid() {
    return uid;
  }



  void addTo(Map<String, String> accounts){    
    if (null != accounts.putIfAbsent(uid, auth)){
      throw new IllegalOperation("Account [%s] exists already", uid);
    }
  }

  void replaceExisting(Map<String, String> accounts){        
    if (null == accounts.replace(uid, auth)){
      throw new IllegalOperation("Account [%s] not found", uid);
    }
  }
  
  public boolean matchesPassword(String plainTextPassword) {   
    return isEqual(auth, plainTextPassword);
  }

  public static boolean isEqual(String digesta, String digestb) {//constant time equals 
    if (digesta == digestb) return true;
    if (digesta == null || digestb == null) {
      return false;
    }


    int result = 0;
    for (int i = 0, len = Math.min(digesta.length(), digestb.length()); i < len; i++) {
      result |= digesta.charAt(i) ^ digestb.charAt(i);
    }
    return result + digesta.length() - digestb.length()== 0 ;
  }

  @Override
  public String toString() {
    return String.format("LoginEntry [uid=%s, auth=%s, type=%s]", uid, "auth", type);
  }
  
  
  

}
