package com.rojsn.searchengine.gui;

import com.rojsn.searchengine.FormattedMatch;
import com.rojsn.searchengine.SearchEngine;
import com.rojsn.searchengine.XMLUtils;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import java.net.URL;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;

public class SearchEngineDemo extends JPanel implements TreeSelectionListener {

    private JPanel searchPane = new JPanel();
    private JEditorPane htmlPane;
    private JTree tree;
    private GroupLayout layout;
    private JLabel label = new JLabel("Поиск строки:");
    private JLabel baseFolder = new JLabel("Поиск из папки:");
    private JTextField textField = new JTextField();
    private JCheckBox cbCaseSensitive = new JCheckBox("Учет регистра");
    private JCheckBox cbWholeWords = new JCheckBox("Целое слово");
    private JCheckBox cbBackward = new JCheckBox("Поиск назад");
    private JButton btnFind = new JButton("Найти");
    private JButton btnCancel = new JButton("Отменить");
    private URL helpURL;
    private static boolean DEBUG = false;
        

    //Optionally play with line styles.  Possible values are
    //"Angled" (the default), "Horizontal", and "None".
    private static boolean playWithLineStyle = false;
    private static String lineStyle = "Horizontal";

    //Optionally set the look and feel.
    private static boolean useSystemLookAndFeel = false;
    
    private String regexp = "";

    public SearchEngineDemo() {

        JSplitPane mainSplitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JPanel grid = new JPanel(new GridLayout(2, 1));       
        grid.setAutoscrolls(true);
        
        cbCaseSensitive.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); 
        cbWholeWords.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); 
        cbBackward.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); 

        //Create the nodes.
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(SearchEngine.BASE_FOLDER);
        tree = new JTree(top);        
        //Create the scroll pane and add the tree to it. 
        JScrollPane treeView = new JScrollPane(tree);
        
        SearchEngine se = new SearchEngine();
        File baseFile = new File(SearchEngine.BASE_FOLDER);
        if (!"".equals(regexp)) {
            if (baseFile.isDirectory()) {
                se.fillOperatedFileNames(baseFile, getRegexp());
            }
        }        
        se.createNodes(top);

        baseFolder.setText(SearchEngine.BASE_FOLDER);
        JButton button = new JButton("Корневой каталог");
        button.setAlignmentX(CENTER_ALIGNMENT); 
        button.addActionListener((ActionEvent e) -> {
            JFileChooser fileopen = new JFileChooser();
            fileopen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int ret = fileopen.showDialog(null, "Выбрать каталог");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();
                baseFolder.setText(file.getAbsolutePath());
//                XMLUtils.
            }
        });
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRegexp(textField.getText());
            }
        });
        btnFind.addActionListener((ActionEvent e) -> {
            if (textField.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Error: Строка поиска не должна быть пустой!", "Error Massage",
                        JOptionPane.ERROR_MESSAGE);
            } else {     
                tree.removeAll();
                se.fillOperatedFileNames(baseFile, textField.getText());                
                se.createNodes(top); 
                DefaultTreeModel model = (DefaultTreeModel)tree.getModel();                
                model.reload();
            }
        });
        
        cbCaseSensitive.addActionListener((ActionEvent e) -> {            
            JOptionPane.showMessageDialog(null, "Еще не реализовано!", "Error Massage",  JOptionPane.ERROR_MESSAGE);            
        });
        cbWholeWords.addActionListener((ActionEvent e) -> {            
            JOptionPane.showMessageDialog(null, "Еще не реализовано!", "Error Massage",  JOptionPane.ERROR_MESSAGE);            
        });
        cbBackward.addActionListener((ActionEvent e) -> {            
            JOptionPane.showMessageDialog(null, "Еще не реализовано!", "Error Massage",  JOptionPane.ERROR_MESSAGE);            
        });

        GroupLayout layout = new GroupLayout(searchPane);   
        searchPane.setLayout(layout);
        searchPane.setSize(1200, 800);
        layout.setAutoCreateGaps(true); 
        layout.setAutoCreateContainerGaps(true); 
        
        // Создание горизонтальной группы
        layout.setHorizontalGroup(layout.createSequentialGroup() 
                
                .addGroup(
                    layout.createParallelGroup(LEADING) 
                        .addComponent(label)  
                        .addGroup(layout.createParallelGroup(LEADING).addComponent(button)
                        .addGroup(layout.createParallelGroup(LEADING).addComponent(baseFolder)
                        ))
                )  
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
                              
                
        ); 
         
        layout.linkSize(SwingConstants.HORIZONTAL, btnFind, btnCancel); 
         
        // Создание вертикальной группы
        layout.setVerticalGroup(layout.createSequentialGroup() 
                .addGroup(
                    layout.createParallelGroup(BASELINE) 
                        .addComponent(label) 
                        .addComponent(textField) 
                        .addComponent(btnFind)
                    ) 
                .addGroup(
                    layout.createParallelGroup(LEADING) 
                        .addGroup(layout.createSequentialGroup() 
                        .addGroup(layout.createParallelGroup(BASELINE) 
                                .addComponent(cbCaseSensitive)
                                .addComponent(cbWholeWords))                                
                        .addGroup(layout.createParallelGroup(BASELINE) 
                                .addComponent(cbBackward))
                    ) 
                .addComponent(btnCancel)
                )                
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(LEADING).addComponent(button))
                        .addGroup(layout.createParallelGroup(LEADING).addComponent(baseFolder))
                )
        ); 
         
        //Create a tree that allows one selection at a time.        
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Listen for when the selection changes.
        tree.addTreeSelectionListener(this);
        tree.setRootVisible(true);

        if (playWithLineStyle) {
            System.out.println("line style = " + lineStyle);
            tree.putClientProperty("JTree.lineStyle", lineStyle);
        }

        
//        layout = new GroupLayout();
  //      getContentPane().setLayout(layout);

        //Create the scroll pane and add the tree to it. 
//        JScrollPane treeView = new JScrollPane(tree);

        //Create the HTML viewing pane.
        htmlPane = new JEditorPane();
        htmlPane.setEditable(false);
//        initHelp();//todo roj
        JScrollPane htmlView = new JScrollPane(htmlPane);

        //Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(treeView);
        splitPane.setRightComponent(htmlView);

//        Dimension minimumSize = new Dimension(100, 50);
//        htmlView.setMinimumSize(minimumSize);
//        treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(300);
        
        //Add the split pane to this panel.
        mainSplitPanel.setTopComponent(searchPane);
        mainSplitPanel.setBottomComponent(splitPane);
        mainSplitPanel.setAutoscrolls(true);
        add(mainSplitPanel);
    }

//    /**
//     * Required by TreeSelectionListener interface.
//     */
//    public void valueChanged1(TreeSelectionEvent e) {
//        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
//
//        if (node == null) {
//            return;
//        }
//
//        Object nodeInfo = node.getUserObject();
//        if (node.isLeaf()) {
//            BookInfo match = (BookInfo) nodeInfo;
//            displayURL(match.matchURL);
//            if (DEBUG) {
//                System.out.print(match.matchURL + ":  \n    ");
//            }
//        } else {
//            displayURL(helpURL);
//        }
//        if (DEBUG) {
//            System.out.println(nodeInfo.toString());
//        }
//    }

    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }
        Object nodeInfo = node.getUserObject();
        if (node.isLeaf()) {
            FormattedMatch match = (FormattedMatch) nodeInfo;
            displayMatch(match);
        }
    }

    private class BookInfo {

        public String matchName;
        public URL matchURL;

        public BookInfo(String match, String filename) {
            matchName = match;
            matchURL = getClass().getResource(filename);
            if (matchURL == null) {
                System.err.println("Couldn't find file: "
                        + filename);
            }
        }

        public String toString() {
            return matchName;
        }
    }

    private void initHelp() {
        String s = "TreeDemoHelp.html";
        helpURL = getClass().getResource(s);
        if (helpURL == null) {
            System.err.println("Couldn't open help file: " + s);
        } else if (DEBUG) {
            System.out.println("Help URL is " + helpURL);
        }

        displayURL(helpURL);
    }

    private void displayURL(URL url) {
        try {
            if (url != null) {
                htmlPane.setPage(url);
            } else { //null url
                htmlPane.setText("File Not Found");
                if (DEBUG) {
                    System.out.println("Attempted to display a null URL.");
                }
            }
        } catch (IOException e) {
            System.err.println("Attempted to read a bad URL: " + url);
        }
    }
    private void displayMatch(FormattedMatch match) {
        if (match != null) {
            htmlPane.setText(match.getTextMatch());
        } else { //null url
            htmlPane.setText("File Not Found");
        }
    }

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event dispatch thread.
     */
    private static void createAndShowGUI() {
        if (useSystemLookAndFeel) {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Couldn't use system look and feel.");
            }
        }

        //Create and set up the window.
        JFrame frame = new JFrame("TreeDemo");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(dim);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Add content to the window.
        frame.add(new SearchEngineDemo());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    /**
     * @return the regexp
     */
    public String getRegexp() {
        return regexp;
    }

    /**
     * @param regexp the regexp to set
     */
    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }
}
