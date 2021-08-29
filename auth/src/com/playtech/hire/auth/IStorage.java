package com.playtech.hire.auth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IStorage {

    void read(InputStream in) throws IOException;

    void write(OutputStream out) throws IOException;
    
    int getSize();
}