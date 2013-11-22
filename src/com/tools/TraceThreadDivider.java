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

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Copyright 2001-2013 CryptoHeaven Corp. All Rights Reserved.
 *
 * @author  Marcin Kurzawa
 */
public class TraceThreadDivider extends Object {

  /** Creates new TraceThreadDivider */
  public TraceThreadDivider() {
  }

  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("Usage: <program> [Trace-file-name] [Trace-output-file-name-prefix]");
    }

    FileInputStream fIn = null;
    Hashtable threadsHT = null;

    try {
      String traceFileName = args[0];
      String traceFileOutPre = args[1];

      threadsHT = new Hashtable();
      fIn = new FileInputStream(traceFileName);
      BufferedReader reader = new BufferedReader(new InputStreamReader(fIn));

      String line = null;
      boolean newBlockOfLines = false;
      boolean suppressThreadLine = false;
      boolean suppressBlankLine = false;
      boolean isNewThreadFile = false;
      BufferedWriter writer = null;
      while ( (line = reader.readLine()) != null ) {
        isNewThreadFile = false;
        if (newBlockOfLines) {
          if (line.startsWith("Thread -- ")) {
            int start = line.indexOf("ID=") + 3;
            int end = line.indexOf(' ', start);
            String idS = line.substring(start, end);
            writer = (BufferedWriter) threadsHT.get(idS);
            if (writer == null) {
              isNewThreadFile = true;
              FileOutputStream fOut = new FileOutputStream(traceFileOutPre + "_" + idS);
              writer = new BufferedWriter(new OutputStreamWriter(fOut));
              threadsHT.put(idS, writer);
            } else {
              suppressThreadLine = true;
            }
          }
        }

        if (writer != null) {
          if (suppressThreadLine) {
            suppressThreadLine = false;
            suppressBlankLine = true;
          } else {
            if (suppressBlankLine && line.length() == 0) {
              // no-op
            } else if (line.length() == 0 && !newBlockOfLines) {
              // no-op
            } else {
              if (newBlockOfLines) {
                int firstDash = line.indexOf('|');
                if (firstDash >= 0) {
                  line = line.substring(0, firstDash) + "+" + line.substring(firstDash+1);
                }
              }
              writer.write(line);
              writer.newLine();
              if (isNewThreadFile) {
                writer.newLine();
              }
            }
            suppressBlankLine = false;
          }
        }


        if (line.length() == 0) {
          newBlockOfLines = true;
        } else {
          newBlockOfLines = false;
        }
      }
    } catch (Throwable t) {
    }

    try {
      if (fIn != null)
        fIn.close();
    } catch (Throwable t) {
    }

    try {
      Enumeration enm = threadsHT.elements();
      while (enm.hasMoreElements()) {
        BufferedWriter writer = (BufferedWriter) enm.nextElement();
        writer.close();
      }

    } catch (Throwable t) {
    }

  }

}