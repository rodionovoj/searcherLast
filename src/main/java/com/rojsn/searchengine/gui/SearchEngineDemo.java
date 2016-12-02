package com.rojsn.searchengine.gui;

import com.rojsn.searchengine.FormattedMatch;
import com.rojsn.searchengine.SearchEngine;
import com.rojsn.searchengine.XMLUtils;
import java.awt.BorderLayout;
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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.tree.TreePath;

public class SearchEngineDemo extends JPanel implements TreeSelectionListener {

    private final JPanel searchPane = new JPanel();
    private JEditorPane htmlPane;
    private JTree tree;
    private GroupLayout layout;
    private final JLabel label = new JLabel("Поиск строки:");
    private final JLabel baseFolder = new JLabel("Поиск из папки:");
    private final JTextField textField = new JTextField();
    private final JCheckBox cbCaseSensitive = new JCheckBox("Учет регистра");
    private final JCheckBox cbWholeWords = new JCheckBox("Целое слово");
    private final JCheckBox cbBackward = new JCheckBox("Поиск назад");
    private final JButton btnFind = new JButton("Найти");
    private final JButton btnCancel = new JButton("Отменить");
    private URL helpURL;
    private static final boolean DEBUG = false;
    private JSplitPane mainSplitPanel;
    private JScrollPane treeView;
        
    //Optionally play with line styles.  Possible values are
    //"Angled" (the default), "Horizontal", and "None".
    private static final boolean playWithLineStyle = false;
    private static final String lineStyle = "Horizontal";

    //Optionally set the look and feel.
    private static final boolean useSystemLookAndFeel = false;
    
    public SearchEngineDemo() {
        initComponents();        
        new SearchData();     
    }
    
    private void initComponents() {
               
        mainSplitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();        
        mainSplitPanel.setPreferredSize(dim);        
        cbCaseSensitive.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); 
        cbCaseSensitive.addActionListener(new NotImplementedYet());
        cbBackward.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); 
        cbBackward.addActionListener(new NotImplementedYet());
        cbWholeWords.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));        
        cbWholeWords.addActionListener(new NotImplementedYet());        

        //Create the nodes.
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(SearchEngine.BASE_FOLDER);
        tree = new JTree(top); 
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        //Listen for when the selection changes.
        tree.addTreeSelectionListener(this);
        tree.setRootVisible(true);
        
        SearchEngine se = new SearchEngine();        
        //Create the scroll pane and add the tree to it. 
        treeView = new JScrollPane(tree);        
        treeView.setWheelScrollingEnabled(true);
        treeView.setViewportView(tree);
        
        baseFolder.setText(SearchEngine.BASE_FOLDER);
        JButton button = new JButton("Корневой каталог");
        button.setAlignmentX(CENTER_ALIGNMENT); 
        button.addActionListener(new FolderChooser());
        btnFind.addActionListener(new SearchData());            

           //Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(searchPane);
        splitPane.setBottomComponent(treeView);        

        GroupLayout layout = new GroupLayout(searchPane);   
        searchPane.setLayout(layout);
        layout.setAutoCreateGaps(true); 
        layout.setAutoCreateContainerGaps(true); 
                
        // Создание горизонтальной группы
        layout.setHorizontalGroup(layout.createSequentialGroup()                
            .addGroup(
                layout.createParallelGroup(LEADING) 
                    .addComponent(label)  
                    .addGroup(layout.createParallelGroup(LEADING).addComponent(button)                    
                )
            )  
            .addGroup(layout.createParallelGroup(LEADING)                     
                    .addComponent(baseFolder) 
                    .addGroup(layout.createParallelGroup(LEADING).addComponent(textField))
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
                 .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(BASELINE).addComponent(button).addComponent(baseFolder))
            )
            .addGroup(
                layout.createParallelGroup(BASELINE) 
                    .addComponent(label) 
                    .addComponent(textField) 
                    .addComponent(btnFind)
                ) 
            .addGroup(
                layout.createParallelGroup(LEADING) 
                    .addGroup(
                        layout.createSequentialGroup() 
                            .addGroup(
                                layout.createParallelGroup(BASELINE) 
                                    .addComponent(cbCaseSensitive)
                                    .addComponent(cbWholeWords)
                            )                                
                            .addGroup(
                                layout.createParallelGroup(BASELINE) 
                                    .addComponent(cbBackward)
                            )
                ) 
            .addComponent(btnCancel)
            )                
           
        ); 
         
        if (playWithLineStyle) {
            System.out.println("line style = " + lineStyle);
            tree.putClientProperty("JTree.lineStyle", lineStyle);
        }

        //Create the HTML viewing pane.
        htmlPane = new JEditorPane();
        htmlPane.setEditable(false);
//        initHelp();//todo roj
        JScrollPane htmlView = new JScrollPane(htmlPane);
        mainSplitPanel.setLeftComponent(splitPane);
        mainSplitPanel.setRightComponent(htmlView);
        add(mainSplitPanel);
    }
   
    private TreeSelectionListener createSelectionListener() {
        return new TreeSelectionListener() {
            @Override
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
        };
    }

    private class FolderChooser implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {         
            JFileChooser fileopen = new JFileChooser(SearchEngine.BASE_FOLDER);
            fileopen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int ret = fileopen.showDialog(null, "Выбрать каталог");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();
                baseFolder.setText(file.getAbsolutePath());
                XMLUtils.saveProperty(SearchEngine.BASE_DOC_FOLDER, file.getAbsolutePath());
            }
        }
}

    private class SearchData implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {            
            SearchEngine se = new SearchEngine();
            DefaultMutableTreeNode top = new DefaultMutableTreeNode(baseFolder.getText());
            File baseFile = new File(baseFolder.getText());
                if (textField.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Error: Строка поиска не должна быть пустой!", "Error Massage",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    if (baseFile.isDirectory()) {
                        se.fillOperatedFileNames(baseFile, textField.getText());
                    }
                    se.createNodes(top);  
                    tree = new JTree(top); 
                    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
                    //Listen for when the selection changes.
                    tree.addTreeSelectionListener(createSelectionListener());
                    tree.setRootVisible(true);
                    treeView.getViewport().add(tree);
                }      
        }    
    }
    
    private class NotImplementedYet implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {            
            JOptionPane.showMessageDialog(null, "Еще не реализовано!", "Error Massage",  JOptionPane.ERROR_MESSAGE);            
        }
    
    }
    
    @Override
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
        JFrame frame = new JFrame("Поиск документов");
//        setCenterPosition(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Add content to the window.
        frame.add(new SearchEngineDemo());
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    private static void setCenterPosition(JFrame frame) {
        
          Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
          Dimension frameSize = frame.getPreferredSize();

          if (frameSize.height > screenSize.height) {
               frameSize.height = screenSize.height;
          }

          if (frameSize.width > screenSize.width) {
               frameSize.width = screenSize.width;
          }
          int newWidth = (int) (screenSize.getWidth() - frameSize.getWidth())/2;
          int newHeight = (int) (screenSize.getHeight()- frameSize.getHeight())/2;

          frame.setLocation(newWidth, newHeight);
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
}
