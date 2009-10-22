/*
 * TeeOutputStream.java
 *
 * Created on November 16, 2001, 12:22 AM
 */

package com.tools;

import java.io.*;
import java.util.*;

/**
 *
 * @author  marcin
 * @version 
 */
public class TeeOutputStream extends OutputStream {

  private OutputStream out;
  private TeeInputStream teeIn;

  /** Creates new TeeOutputStream */
  public TeeOutputStream(OutputStream out, TeeInputStream teeIn) {
    this.out = out;
    this.teeIn = teeIn;
  }

  public void write(int param) throws java.io.IOException {
    int p = param;
    teeIn.tee(param, false);
    out.write(p);
  }

  public void close() throws IOException {
    out.close();
  }
  public void flush() throws IOException {
    out.flush();
  }
/*
  public void write(byte[] bytes) throws IOException {
    write(bytes, 0, bytes.length);
  }
  public void write(byte[] bytes, int off, int len) throws IOException {
    if (bytes != null && len > 0) {
      for (int i=0; i<bytes.length; i++) {
        write(bytes[i]);
      }
    }
  }
   */
}