package com.goodluck;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTextArea textArea;

    public MainFrame() {
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1024, 768));
        setAlwaysOnTop(true);
        setVisible(true);
        pack();

        textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", 1, 10));
        textArea.setEditable(false);
        add(textArea);
    }

    public void resetText(String text) {
        textArea.setText("");
        textArea.setText(text);
        textArea.paintImmediately(textArea.getBounds());
    }
}