package com.rojsn.searchengine.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 * This application is a demo of the Metal Look & Feel
 *
 * @version 1.16 07/26/04
 * @author Steve Wilson
 */
public class Metalworks {

    public static void main(String[] args) {
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        JDialog.setDefaultLookAndFeelDecorated(true);
        JFrame.setDefaultLookAndFeelDecorated(true);
        Toolkit.getDefaultToolkit().setDynamicLayout(true);
        System.setProperty("sun.awt.noerasebackground", "true");

        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("Metal Look & Feel not supported on this platform. \nProgram Terminated");
            System.exit(0);
        }
        JFrame frame = new MetalworksFrame();
        frame.setVisible(true);
    }
}
