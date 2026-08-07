/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.papermanager;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Dialog used both for adding a new paper and editing an existing one.
 * If an existing Paper is passed in, the dialog behaves in "edit" mode.
 */
public class UploadPaperForm extends JDialog {

    private final PaperController controller;
    private final Paper existingPaper; // null when adding a new paper

    private final JTextField titleField = new JTextField(30);
    private final JTextField authorField = new JTextField(30);
    private final JTextField yearField = new JTextField(10);
    private final JTextField categoryField = new JTextField(20);
    private final JTextArea abstractArea = new JTextArea(5, 30);
    private final JLabel pdfLabel = new JLabel("No file selected");

    private File selectedPdf; // newly chosen file, null if unchanged

    public UploadPaperForm(JFrame parent, PaperController controller, Paper existingPaper) {
        super(parent, existingPaper == null ? "Add Paper" : "Edit Paper", true);
        this.controller = controller;
        this.existingPaper = existingPaper;

        setSize(500, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        buildForm();
        if (existingPaper != null) {
            populateFromExisting();
        }
    }

    private void buildForm() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        addRow(formPanel, gbc, row++, "Title:*", titleField);
        addRow(formPanel, gbc, row++, "Author:*", authorField);
        addRow(formPanel, gbc, row++, "Year:", yearField);
        addRow(formPanel, gbc, row++, "Venue:", categoryField);
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Abstract:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++;
        abstractArea.setLineWrap(true);
        abstractArea.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(abstractArea), gbc);

        JButton choosePdfBtn = new JButton("Choose PDF...");
        choosePdfBtn.addActionListener(e -> onChoosePdf());
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("PDF File:"), gbc);
        JPanel pdfPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pdfPanel.add(choosePdfBtn);
        pdfPanel.add(pdfLabel);
        gbc.gridx = 1; gbc.gridy = row++;
        formPanel.add(pdfPanel, gbc);

        JLabel requiredNote = new JLabel("* required fields");
        requiredNote.setFont(requiredNote.getFont().deriveFont(Font.ITALIC, 11f));
        gbc.gridx = 1; gbc.gridy = row++;
        formPanel.add(requiredNote, gbc);

        JButton saveBtn = new JButton(existingPaper == null ? "Add Paper" : "Save Changes");
        JButton cancelBtn = new JButton("Cancel");
        saveBtn.addActionListener(e -> onSave());
        cancelBtn.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(field, gbc);
    }

    private void populateFromExisting() {
        titleField.setText(existingPaper.getTitle());
        authorField.setText(existingPaper.getAuthor());
        yearField.setText(existingPaper.getYear());
        categoryField.setText(existingPaper.getCategory());
        abstractArea.setText(existingPaper.getAbstractText());
        if (existingPaper.hasPdf()) {
            pdfLabel.setText(new File(existingPaper.getPdfPath()).getName());
        }
    }

    private void onChoosePdf() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedPdf = chooser.getSelectedFile();
            pdfLabel.setText(selectedPdf.getName());
        }
    }

    private void onSave() {
        String title = titleField.getText();
        String author = authorField.getText();
        String year = yearField.getText();
        String category = categoryField.getText();
        String abstractText = abstractArea.getText();

        String error;
        if (existingPaper == null) {
            error = controller.addPaper(title, author, year, category, abstractText, selectedPdf);
        } else {
            error = controller.editPaper(existingPaper.getId(), title, author, year, category, abstractText, selectedPdf);
        }

        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Cannot Save Paper", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                existingPaper == null ? "Paper added successfully." : "Paper updated successfully.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
