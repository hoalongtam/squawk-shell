/**
 * A class to get and reformat output from the Unix man utility.
 * 
 * Copyright (C) 2011   Hoa Long Tam, Joshua Evenson, Ryan Abrams, Jessica Pan.
 *
 * This file is part of Squawk-Shell.
 *
 * Squawk-Shell is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Squawk-Shell is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sqawk-Shell.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.IOException;
import java.io.InputStream;

public class FormatMan {
  
  String page;
  
  /**
   * Construct a new FormatMan object to handle formatting of the given |page|.
   * 
   * @param page
   *          The name of the manual page to format.
   */
  public FormatMan(String page) {
    this.page = page;
  }
  
  public String format() throws IOException, InterruptedException {
    Runtime r = Runtime.getRuntime();
    Process man = r.exec(String.format("man %s", page));
    InputStream man_output = man.getInputStream();
    InputStream man_error = man.getErrorStream();
    String formatted_output = "";
    
    int status = man.waitFor();
    if (status != 0) {
      int error_msg_size = man_error.available();
      for (int i = 0; i < error_msg_size; i += 1) {
        System.err.printf("%c", (char)man_error.read());
      }
      return "";
    }
    int output_size = man_output.available();
    byte[] buf = new byte[output_size];
    man_output.read(buf);
    
    byte[] text_buf = new byte[output_size];
    byte[] format_buf = new byte[output_size];
    
    int i = 0;
    int j = 0;
    byte PLAIN = 0;
    byte BOLD = 1;
    byte UNDERLINE = 2;
    while (i < output_size) {
      byte b = buf[i];
      if (b == 8) {
        j -= 1;
        i += 1;
        byte orig = text_buf[j];
        byte next = buf[i];
        if (orig == '_') {
          format_buf[j] |= UNDERLINE;
          text_buf[j] = next;
        } else if (next == orig) {
          format_buf[j] |= BOLD;
        } else {
          System.err.print("Invalid backspace overwrite format specifier: ");
          System.err.printf("%c^H%c\n", orig & 0xff, next & 0xff);
        }
      } else {
        text_buf[j] = b;
        format_buf[j] = PLAIN;
      }
      i += 1;
      j += 1;
    }
    
    StringBuffer html = new StringBuffer();
    html.append("<html><body><p style=\"font-family:menlo,monaco,courier,monospace\">");
    
    boolean underline = false;
    boolean bold = false;
    
    for (i = 0; i < j; i += 1) {
      byte letter = text_buf[i];
      byte format = format_buf[i];
      if (((format & BOLD) != 0) && !bold) {
        html.append("<b>");
        bold = true;
      } else if (((format & BOLD) == 0) && bold) {
        html.append("</b>");
        bold = false;
      }
      if (((format & UNDERLINE) != 0) && !underline) {
        html.append("<u>");
        underline = true;
      } else if (((format & UNDERLINE) == 0) && underline) {
        html.append("</u>");
        underline = false;
      }
      if (letter == '\n') {
        html.append("<br />\n");
      } else if (letter == ' ') {
        html.append("&nbsp;");
      } else {
        html.append((char)letter);
      }
    }
    
    html.append("</font></body></html>");
    
    return new String(html);
  }
  
  public static void main(String[] args) {
    FormatMan p = new FormatMan("man");
    try {
      System.out.println(p.format());
    } catch (Exception e) {}
  }
}
