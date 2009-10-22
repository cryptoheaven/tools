/*
 * TeeInputStream.java
 *
 * Created on November 16, 2001, 12:21 AM
 */

package com.tools;

import java.io.*;
import java.util.*;

/**
 *
 * @author  marcin
 * @version 
 */
public class TeeInputStream extends InputStream {

  private InputStream in;

  boolean evasedrop = false;
  private FileWriter evaseWriter;
  private Date lastDate;
  private boolean lastDir;
  private int lineLength;
  private char[] hex = new char[] { '0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f' };

  /** Creates new TeeInputStream */
  public TeeInputStream(InputStream in, String fileName) {
    this.in = in;
    setEvasedrop(fileName);
  }

  public void setEvasedrop(String fileName) {
    try {
      evaseWriter = new FileWriter(fileName);
      evasedrop = true;
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  public void pause() {
    evasedrop = false;
  }
  public void resume() {
    evasedrop = true;
  }

  public synchronized void tee(int param, boolean isInput) {
    if (evasedrop) {
      try {
        if (lastDate == null || lastDir != isInput || lineLength >= 48 || (System.currentTimeMillis() - lastDate.getTime()) >= 1L) {
          lastDate = new Date();
          lastDir = isInput;
          lineLength = 0;
          evaseWriter.write("\r\n" + new java.text.SimpleDateFormat("mm:ss.SSS").format(lastDate) + " " + (isInput ? "input : ":"output: "));
        }
        lineLength ++;
        evaseWriter.write(new char[] { hex[(param >> 4) & 0x000F], hex[(param) & 0x000F] });
        evaseWriter.flush();
      } catch (Throwable t) {
        t.printStackTrace();
      }
    }
  }
  
  public int read() throws java.io.IOException {
    int p = in.read();
    int param = p;
    if (param > -1) {
      tee(param, true);
    }
    return p;
  }

  public int available() throws IOException {
    return in.available();
  }
  public void close() throws IOException {
    in.close();
  }
  public void mark(int readlimit) {
    in.mark(readlimit);
  }
  public boolean markSupported() {
    return in.markSupported();
  }
  public void reset() throws IOException {
    in.reset();
  }
  public long skip(long n) throws IOException {
    return in.skip(n);
  }
  public int read(byte[] b) throws IOException {
    return read(b, 0, 1);
  }
  public int read(byte[] b, int off, int len) throws IOException {
    if (len > 0) {
      int p = read();
      if (p > -1) {
        b[off] = (byte) p;
        return 1;
      }
      else
        return -1;
    }
    return 0;
  }
}