/**
 * The main GUI handler for Squawk-Shell.
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
 * along with Squawk-Shell.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.StringTokenizer;

import javax.swing.GroupLayout;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.ListView;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * GUI.java
 *
 * Created on Apr 2, 2011, 8:51:42 PM
 */

/**
 * 
 * @author JP
 */
public class GUI extends javax.swing.JFrame {
  
  private CommandInterface ci;
  private ListView folderView;
  boolean directory_change_intentional;
  String[] defaultCommandList = null;
  
  /** Creates new form GUI */
  public GUI() {
    initComponents();
    shellChooser.setDragEnabled(true);
    try {
      ci = new CommandInterface(this);
      ci.apropos();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    
    shellCommand.requestFocus();
    try {
      ci.sendCommand("cd $HOME");
    } catch (IOException ex) {
      System.err.println("IOException of the second kind.");
    }
    
  }
  
  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">
  // GEN-BEGIN:initComponents
      private
      void initComponents() {
    
    jFrame1 = new javax.swing.JFrame();
    menuBar1 = new java.awt.MenuBar();
    menu1 = new java.awt.Menu();
    panelInfo = new javax.swing.JPanel();
    jScrollPane1 = new javax.swing.JScrollPane();
    manPageList = new javax.swing.JList();
    jTextField1 = new javax.swing.JTextField();
    fileViewerFrame = new javax.swing.JPanel();
    shellChooser = new javax.swing.JFileChooser();
    shellChooser.setControlButtonsAreShown(false);
    shellChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    shellChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(evt.getPropertyName())) {
          try {
            if (!directory_change_intentional) {
              String new_dir = shellChooser.getCurrentDirectory().getAbsolutePath();
              String relative = new File(old_dir).toURI()
                                                 .relativize(new File(new_dir).toURI())
                                                 .getPath();
              ci.sendCommand("cd " + relative);
              old_dir = new_dir;
            }
          } catch (IOException e) {}
        }
      }
    });
    shellBody = new javax.swing.JPanel();
    shellCommand = new javax.swing.JTextField();
    shellText = new java.awt.TextArea();
    label1 = new java.awt.Label();
    
    GroupLayout jFrame1Layout = new GroupLayout(jFrame1.getContentPane());
    jFrame1.getContentPane().setLayout(jFrame1Layout);
    jFrame1Layout.setHorizontalGroup(jFrame1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                  .addGap(0, 400, Short.MAX_VALUE));
    jFrame1Layout.setVerticalGroup(jFrame1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addGap(0, 300, Short.MAX_VALUE));
    
    menu1.setLabel("File");
    menuBar1.add(menu1);
    
    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    
    panelInfo.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
    
    manPageList.setFixedCellWidth(20);
    manPageList.setFont(new java.awt.Font("Monaco", 0, 12)); // NOI18N
    manPageList.setModel(new Model());
    
    manPageList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    manPageList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    manPageList.addMouseListener(new java.awt.event.MouseAdapter() {
      @Override
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
          int i = manPageList.locationToIndex(evt.getPoint());
          StringTokenizer st = new StringTokenizer(
                                                   (String)manPageList.getSelectedValue());
          popupManPage(st.nextToken());
        }
      }
    });
    
    jScrollPane1.setViewportView(manPageList);
    
    jTextField1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
    jTextField1.setText("Common Commands");
    jTextField1.addActionListener(new java.awt.event.ActionListener() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jTextField1ActionPerformed(evt);
      }
    });
    
    javax.swing.GroupLayout panelInfoLayout = new javax.swing.GroupLayout(panelInfo);
    panelInfo.setLayout(panelInfoLayout);
    panelInfoLayout.setHorizontalGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                      .addComponent(jScrollPane1,
                                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                    275,
                                                                    Short.MAX_VALUE)
                                                      .addComponent(jTextField1,
                                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                    275,
                                                                    Short.MAX_VALUE));
    panelInfoLayout.setVerticalGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(panelInfoLayout.createSequentialGroup()
                                                                             .addContainerGap()
                                                                             .addComponent(jTextField1,
                                                                                           javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                           30,
                                                                                           javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                             .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                             .addComponent(jScrollPane1,
                                                                                           javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                           683,
                                                                                           Short.MAX_VALUE)));
    
    fileViewerFrame.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
    
    shellChooser.addActionListener(new java.awt.event.ActionListener() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        shellChooserActionPerformed(evt);
      }
    });
    
    javax.swing.GroupLayout fileViewerFrameLayout = new javax.swing.GroupLayout(
                                                                                fileViewerFrame);
    fileViewerFrame.setLayout(fileViewerFrameLayout);
    fileViewerFrameLayout.setHorizontalGroup(fileViewerFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                  .addGroup(fileViewerFrameLayout.createSequentialGroup()
                                                                                                 .addContainerGap()
                                                                                                 .addComponent(shellChooser,
                                                                                                               javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                               571,
                                                                                                               Short.MAX_VALUE)
                                                                                                 .addContainerGap()));
    fileViewerFrameLayout.setVerticalGroup(fileViewerFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                          fileViewerFrameLayout.createSequentialGroup()
                                                                                               .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                Short.MAX_VALUE)
                                                                                               .addComponent(shellChooser,
                                                                                                             javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                             javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                             javax.swing.GroupLayout.PREFERRED_SIZE)));
    
    shellBody.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
    
    shellCommand.addActionListener(new java.awt.event.ActionListener() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        shellCommandActionPerformed(evt);
      }
    });
    shellCommand.addKeyListener(new java.awt.event.KeyAdapter() {
      @Override
      public void keyPressed(java.awt.event.KeyEvent evt) {
        shellCommandKeyPressed(evt);
      }
      
      @Override
      public void keyReleased(java.awt.event.KeyEvent evt) {
        // Apropos Man Page Command Checker
        StringTokenizer st = new StringTokenizer(shellCommand.getText());
        if (st.hasMoreTokens()) {
          String[] items = ci.searchBuffer(st.nextToken());
          manPageList.setModel(new Model(items));
          manPageList.repaint();
        } else {
          manPageList.setModel(new Model(defaultCommandList));
          manPageList.repaint();
        }
      }
    });
    
    shellText.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
    shellText.setEditable(false);
    shellText.setFont(new java.awt.Font("Monaco", 0, 12));
    javax.swing.GroupLayout shellBodyLayout = new javax.swing.GroupLayout(shellBody);
    shellBody.setLayout(shellBodyLayout);
    shellBodyLayout.setHorizontalGroup(shellBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                shellBodyLayout.createSequentialGroup()
                                                                               .addContainerGap()
                                                                               .addGroup(shellBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                        .addComponent(shellText,
                                                                                                                      javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                      javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                      571,
                                                                                                                      Short.MAX_VALUE)
                                                                                                        .addComponent(shellCommand,
                                                                                                                      javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                      javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                      571,
                                                                                                                      Short.MAX_VALUE))
                                                                               .addContainerGap()));
    shellBodyLayout.setVerticalGroup(shellBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                              shellBodyLayout.createSequentialGroup()
                                                                             .addContainerGap()
                                                                             .addComponent(shellText,
                                                                                           javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                           261,
                                                                                           Short.MAX_VALUE)
                                                                             .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                             .addComponent(shellCommand,
                                                                                           javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                           javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                           javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                             .addContainerGap()));
    
    label1.setText("label1");
    
    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                                    .addContainerGap()
                                                    .addComponent(panelInfo,
                                                                  javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                  javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                  javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                    .addComponent(shellBody,
                                                                                  javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                  javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                  Short.MAX_VALUE)
                                                                    .addComponent(fileViewerFrame,
                                                                                  javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                  javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                  Short.MAX_VALUE))
                                                    .addContainerGap()));
    layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                  .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                            layout.createSequentialGroup()
                                                  .addContainerGap()
                                                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                  .addComponent(panelInfo,
                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                  .addGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                            layout.createSequentialGroup()
                                                                                  .addComponent(fileViewerFrame,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                  .addComponent(shellBody,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)))
                                                  .addContainerGap()));
    
    pack();
  }// </editor-fold>//GEN-END:initComponents
  
  private void shellCommandActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_shellCommandActionPerformed
    // TODO add your handling code here:
  }// GEN-LAST:event_shellCommandActionPerformed
  
  private void shellCommandKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_shellCommandKeyPressed
    // GEN-FIRST:event_shellCommandKeyPressed
    if (evt.getKeyCode() == evt.VK_C) {
      if (evt.isControlDown()) {
        ci.terminate();
        shellCommand.setText("");
        return;
      }
      
    }
    
    if (evt.getKeyCode() == evt.VK_ENTER) {
      String s = shellCommand.getText();
      // shellText.append("> " + s + "\n");
      try {
        ci.sendCommand(s);
      } catch (java.io.IOException e) {}
      shellCommand.setText("");
    }
  }
  
  public void setHelpModel(String[] s) {
    manPageList.setModel(new Model(s));
    manPageList.repaint();
  }
  
  public void setDCL(String[] s) {
    defaultCommandList = s;
  }
  
  // GEN-LAST:event_shellCommandKeyPressed
  public void receiveResponse(String s) {
    shellText.append(s + "\n");
    shellChooser.rescanCurrentDirectory();
  }// GEN-LAST:event_shellCommandKeyPressed
  
  private final JFrame manPopup = new JFrame("");
  JEditorPane manJep = new JEditorPane();;
  JScrollPane manJsp = new JScrollPane(manJep);;
  
  public void popupManPage(String cmd_name) {
    FormatMan fm = new FormatMan(cmd_name);
    String html = null;
    try {
      html = fm.format();
    } catch (IOException e) {} catch (InterruptedException e) {}
    
    if (html != null) {
      manPopup.setTitle(cmd_name);
      
      manJep.setEditable(false);
      manJep.setText(html);
      try {
        manJep.setContentType("text/html");
        manJep.read(new StringReader(html), new HTMLDocument());
      } catch (IOException e) {}
      
      manJsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      manJsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      manJsp.setPreferredSize(new Dimension(720, 640));
      manJsp.setMinimumSize(new Dimension(50, 50));
    } else {
      manPopup.setTitle("Error!");
      manJep.setEditable(false);
      manJep.setText("No manual page found.");
      manJsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
      manJsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }
    manPopup.add(manJsp);
    
    manPopup.pack();
    manPopup.setVisible(true);
    receiveResponse("bash # man " + cmd_name);
    
  }
  
  String old_dir = "";
  
  public void directoryChange(String dir) {
    old_dir = shellChooser.getCurrentDirectory().getAbsolutePath();
    directory_change_intentional = true;
    shellChooser.setCurrentDirectory(new File(dir));
    directory_change_intentional = false;
  }
  
  private void shellChooserActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_shellChooserActionPerformed
    try {
      ci.sendCommand(String.format("cd %s", shellChooser.getCurrentDirectory()
                                                        .getAbsolutePath()));
      System.err.println(shellChooser.getCurrentDirectory().getAbsolutePath());
    } catch (IOException e) {
      System.err.println("booboo occurred.");
    }
  }// GEN-LAST:event_shellChooserActionPerformed
  
  private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTextField1ActionPerformed
    // TODO add your handling code here:
  }// GEN-LAST:event_jTextField1ActionPerformed
  
  /**
   * @param args
   *          the command line arguments
   */
  public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        new GUI().setVisible(true);
      }
    });
  }
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel fileViewerFrame;
  private javax.swing.JFrame jFrame1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JList manPageList;
  private javax.swing.JTextField jTextField1;
  private java.awt.Label label1;
  private java.awt.Menu menu1;
  private java.awt.MenuBar menuBar1;
  private javax.swing.JPanel panelInfo;
  private javax.swing.JPanel shellBody;
  private javax.swing.JFileChooser shellChooser;
  private javax.swing.JTextField shellCommand;
  private java.awt.TextArea shellText;
  
  // End of variables declaration//GEN-END:variables
  
  private class Model extends javax.swing.AbstractListModel {
    private static final long serialVersionUID = 1L;
    String[] items = null;
    
    String[] baseItems = {
                          "apropos -- search the whatis database for strings",
                          "awk -- pattern-directed scanning and processing language",
                          "cat -- concatenate and print files",
                          "cd -- change directory",
                          "chmod -- change file modes or Access Control Lists",
                          "cp -- copy files", "diff - compare files line by line",
                          "echo -- write arguments to the standard output",
                          "grep -- print lines matching a pattern",
                          "kill -- terminate or signal a process",
                          "ls -- list directory contents",
                          "man -- format and display the on-line manual pages",
                          "mkdir -- make directories", "mv -- move files",
                          "rm -- remove directory entries",
                          "rmdir -- remove directories",
                          "scp -- secure copy (remote file copy program)",
                          "sed -- stream editor" };
    
    public Model() {
      items = baseItems;
    }
    
    public Model(String[] args) {
      if (args != null) {
        items = args;
      } else {
        items = baseItems;
      }
    }
    
    @Override
    public int getSize() {
      return items.length;
    }
    
    @Override
    public Object getElementAt(int i) {
      return items[i];
    }
  }
  
  final String[] cmd_names = { "apropos", "awk", "cat", "cd", "chmod", "cp", "diff",
                              "echo", "grep", "kill", "ls", "man", "mkdir", "mv",
                              "rm", "rmdir", "scp", "sed" };
  
}
