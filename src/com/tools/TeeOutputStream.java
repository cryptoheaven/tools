/**
 * Copyright 2001-2013 CryptoHeaven Corp. All Rights Reserved.
 *
 * This software is the confidential and proprietary information
 * of CryptoHeaven Corp. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with CryptoHeaven Corp.
 */
package com.tools;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Copyright 2001-2013 CryptoHeaven Corp. All Rights Reserved.
 *
 * @author  Marcin Kurzawa
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