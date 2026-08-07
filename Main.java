/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.papermanager;
import javax.swing.*;

/**
 * Entry point for the Research Paper Management application.
 */
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // fall back to default look and feel
        }

        SwingUtilities.invokeLater(() -> {
            PaperController controller = new PaperController();
            PaperListForm mainForm = new PaperListForm(controller);
            mainForm.setVisible(true);
        });
    }
}
