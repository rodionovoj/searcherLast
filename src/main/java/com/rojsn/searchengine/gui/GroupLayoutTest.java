/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rojsn.searchengine.gui;

import com.rojsn.searchengine.SearchEngine;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author sbt-rodionov-oy
 */
public class GroupLayoutTest extends JFrame { 
    
    private static boolean playWithLineStyle = false;
    private static String lineStyle = "Horizontal";

    //Optionally set the look and feel.
    private static boolean useSystemLookAndFeel = false;
    
    public GroupLayoutTest() { 
        
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); 
        // Список компонентов формы
        JLabel      label           = new JLabel("Поиск строки :"); 
        JTextField  textField       = new JTextField(); 
        JCheckBox   cbCaseSensitive = new JCheckBox("Учет регистра"); 
        JCheckBox   cbWholeWords    = new JCheckBox("Целое слово"  ); 
        JCheckBox   cbBackward      = new JCheckBox("Поиск назад"  ); 
        JButton     btnFind         = new JButton("Найти"   ); 
        JButton     btnCancel       = new JButton("Отменить"); 
        JEditorPane htmlPane        = new JEditorPane();
        
        JScrollPane htmlView = new JScrollPane(htmlPane);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
//        splitPane.setLeftComponent(treeView);
        splitPane.setRightComponent(htmlView);
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(SearchEngine.BASE_FOLDER);
        JTree tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
         //Listen for when the selection changes.
//        tree.addTreeSelectionListener(this);//todo roj
        tree.setRootVisible(true);

        if (playWithLineStyle) {
            System.out.println("line style = " + lineStyle);
            tree.putClientProperty("JTree.lineStyle", lineStyle);
        }
        
             //Create the scroll pane and add the tree to it. 
        JScrollPane treeView = new JScrollPane(tree);
         
        cbCaseSensitive.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); 
        cbWholeWords   .setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); 
        cbBackward     .setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); 
        htmlPane.setEditable(false);

        Dimension minimumSize = new Dimension(100, 50);
        htmlView.setMinimumSize(minimumSize);
        treeView.setMinimumSize(minimumSize);
        splitPane.setLeftComponent(treeView);
        splitPane.setDividerLocation(100);
        splitPane.setPreferredSize(new Dimension(500, 300));

        //Add the split pane to this panel.
        //add(splitPane);
        // Определение менеджера расположения
        GroupLayout layout = new GroupLayout(getContentPane()); 
        getContentPane().setLayout(layout); 
        layout.setAutoCreateGaps(true); 
        layout.setAutoCreateContainerGaps(true); 
        // Создание горизонтальной группы
        layout.setHorizontalGroup(layout.createSequentialGroup() 
                .addComponent(label) 
                .addGroup(layout.createParallelGroup(LEADING) 
                        .addComponent(textField) 
                        .addGroup(layout.createSequentialGroup() 
                        .addGroup(layout.createParallelGroup(LEADING) 
                                .addComponent(cbCaseSensitive) 
                                .addComponent(cbBackward)) 
                        .addGroup(layout.createParallelGroup(LEADING) 
                                .addComponent(cbWholeWords)))) 
                
                .addGroup(layout.createParallelGroup(LEADING) 
                .addComponent(btnFind) 
                .addComponent(btnCancel)) 
                //.addGroup(layout.createParallelGroup(LEADING).addComponent(splitPane))
        ); 
         
        layout.linkSize(SwingConstants.HORIZONTAL, btnFind, btnCancel); 
         
        // Создание вертикальной группы
        layout.setVerticalGroup(layout.createSequentialGroup() 
                .addGroup(layout.createParallelGroup(BASELINE) 
                        .addComponent(label) 
                        .addComponent(textField) 
                        .addComponent(btnFind)) 
                .addGroup(layout.createParallelGroup(LEADING) 
                
                        .addGroup(layout.createSequentialGroup() 
                        .addGroup(layout.createParallelGroup(BASELINE) 
                                .addComponent(cbCaseSensitive)
                                .addComponent(cbWholeWords)) 
                                
                                
                        .addGroup(layout.createParallelGroup(BASELINE) 
                                .addComponent(cbBackward))) 
                .addComponent(btnCancel)) 
                
          //      .addGroup(layout.createParallelGroup(BASELINE).addComponent(splitPane))
                
                
                
        ); 
         
        setTitle("Поиск"); 
        pack(); 
    } 
    public static void main(String args[])
    { 
        java.awt.EventQueue.invokeLater(new Runnable() { 
            public void run() {
//                JFrame frame = new JFrame();
//                frame.setUndecorated(true);
//                frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
 
                JFrame.setDefaultLookAndFeelDecorated(true);
                new GroupLayoutTest().setVisible(true); 
            } 
        }); 
    } 
}
