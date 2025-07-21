package org.jngcoding.screenrecorder.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class GUIWindow extends JFrame {    
    public GUIWindow(String title, int width, int height, int x, int y) {
        super(title);
        this.setSize(width, height);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocation(x, y);
    }

    public GUIWindow(String title, int width, int height) {
        super(title);
        this.setSize(width, height);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((size.width - width) / 2, (size.height - height) / 2);
    }
}
