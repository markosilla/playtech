README
This is a test assignment intended for the hiring process at Playtech Estonia.

The provided Java classes contain a simple account/authentication manager and testing utility to ensure correctness. The tests are located at: 
com.playtech.hire.test and the implementation of the authentication manager can be found at com.playtech.hire.auth
While functional, the account manager exhibits a major downside of being security-unsound, storing password in plaintext. In this regard the project is considered unfinished.

The goal of the assignment is fixing the security issues within the account/authentication manager. It's important to note that the improvements have to be done in a backwards-compatible manner, 
e.g. the existing files should be readable and all tests have to pass. To ease the migration process, existing password should be converted to a new, secure form upon user (auth() method).    

As mentioned the code is unfinished, any comments and remarks on the code's quality and structure would be welcome.

Running the assignment from command line (linux & Windows):
    mkdir bin
    javac -d bin -sourcepath src src/com/playtech/hire/TestMain.java
    java -cp bin com.playtech.hire.TestMain
    
Alternatively import the code in your favorite IDE.


Good luck!
