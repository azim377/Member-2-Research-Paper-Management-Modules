/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.papermanager;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.File;
import java.time.LocalDate;

/**
 * Dialog used both for adding a new paper and editing an existing one.
 * If an existing Paper is passed in, the dialog behaves in "edit" mode.
 */
public class UploadPaperForm extends JDialog {

    private static final Color ACCENT_BLUE = new Color(0x2F, 0x6F, 0xED);
    private static final Color TEXT_DARK = new Color(0x1A, 0x22, 0x33);
    private static final Color TEXT_MUTED = new Color(0x8A, 0x93, 0xA6);
    private static final Color FIELD_BORDER = new Color(0xD8, 0xDC, 0xE4);
    private static final Color PDF_RED = new Color(0xE5, 0x3E, 0x3E);

    private final PaperController controller;
    private final Paper existingPaper; // null when adding a new paper

    private final PlaceholderField titleField = new PlaceholderField("Enter paper title");
    private final PlaceholderField authorField = new PlaceholderField("Enter authors (comma separated)");
    private final JComboBox<String> yearCombo = new JComboBox<>();
    private final PlaceholderField categoryField = new PlaceholderField("Enter venue / journal / conference");
    private final PlaceholderField keywordsField = new PlaceholderField("Enter keywords (comma separated)");
    private final JTextArea abstractArea = new JTextArea(3, 20);
    private final JLabel pdfLabel = new JLabel("No file chosen");

    private File selectedPdf; // newly chosen file, null if unchanged

    public UploadPaperForm(JFrame parent, PaperController controller, Paper existingPaper) {
        super(parent, true);
        this.controller = controller;
        this.existingPaper = existingPaper;

        setUndecorated(true);
        setSize(700, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        ((JComponent) getContentPane()).setBorder(new LineBorder(new Color(0xDD, 0xDF, 0xE5), 1));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildBody(), BorderLayout.CENTER);

        if (existingPaper != null) {
            populateFromExisting();
        }
    }

    // ---------- Header (custom colored bar with a working close button + drag-to-move) ----------

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ACCENT_BLUE);
        header.setPreferredSize(new Dimension(0, 44));
        header.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 10));

        JLabel title = new JLabel(existingPaper == null ? "Add New Paper" : "Edit Paper");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JButton closeBtn = new JButton("\u2715");
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dispose());

        header.add(title, BorderLayout.WEST);
        header.add(closeBtn, BorderLayout.EAST);

        MouseAdapter dragHandler = new MouseAdapter() {
            private Point dragStart;

            @Override
            public void mousePressed(MouseEvent e) {
                dragStart = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point loc = getLocation();
                setLocation(loc.x + e.getX() - dragStart.x, loc.y + e.getY() - dragStart.y);
            }
        };
        header.addMouseListener(dragHandler);
        header.addMouseMotionListener(dragHandler);

        return header;
    }

    // ---------- Body ----------

    private JScrollPane buildBody() {
        JPanel body = new JPanel(new BorderLayout(24, 0));
        body.setBackground(Color.WHITE);
        body.setBorder(BorderFactory.createEmptyBorder(20, 28, 20, 28));

        JPanel formColumn = new JPanel();
        formColumn.setLayout(new BoxLayout(formColumn, BoxLayout.Y_AXIS));
        formColumn.setBackground(Color.WHITE);

        JLabel subtitle = new JLabel("Upload Paper");
        subtitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        subtitle.setForeground(TEXT_DARK);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formColumn.add(subtitle);
        formColumn.add(Box.createVerticalStrut(16));

        setupYearCombo();

        abstractArea.setLineWrap(true);
        abstractArea.setWrapStyleWord(true);
        abstractArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane abstractScroll = new JScrollPane(abstractArea);
        abstractScroll.setBorder(new LineBorder(FIELD_BORDER, 1, true));
        abstractScroll.setPreferredSize(new Dimension(0, 70));

        formColumn.add(fieldBlock("Title", true, titleField));
        formColumn.add(fieldBlock("Authors", true, authorField));
        formColumn.add(fieldBlock("Published Year", true, yearCombo));
        formColumn.add(fieldBlock("Venue", true, categoryField));
        formColumn.add(fieldBlock("Keywords", false, keywordsField));
        formColumn.add(fieldBlockFixed("Abstract", false, abstractScroll, 70));
        formColumn.add(fieldBlock("PDF File", true, buildPdfRow()));

        formColumn.add(Box.createVerticalStrut(10));
        formColumn.add(buildButtonRow());

        body.add(formColumn, BorderLayout.CENTER);
        body.add(buildPdfGraphic(), BorderLayout.EAST);

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        return scroll;
    }

    private void setupYearCombo() {
        int current = LocalDate.now().getYear();
        for (int y = current + 1; y >= current - 30; y--) {
            yearCombo.addItem(String.valueOf(y));
        }
        yearCombo.setSelectedItem(String.valueOf(current));
        yearCombo.setBackground(Color.WHITE);
        yearCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    private JPanel fieldBlock(String labelText, boolean required, JComponent field) {
        JPanel block = new JPanel();
        block.setLayout(new BoxLayout(block, BoxLayout.Y_AXIS));
        block.setBackground(Color.WHITE);
        block.setAlignmentX(Component.LEFT_ALIGNMENT);
        block.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        JLabel label = new JLabel(labelText + (required ? " *" : ""));
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(required ? TEXT_DARK : TEXT_MUTED);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        if (field instanceof JTextField || field instanceof JComboBox) {
            field.setBorder(new CompoundBorder(
                    new LineBorder(FIELD_BORDER, 1, true),
                    BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        }

        block.add(label);
        block.add(Box.createVerticalStrut(6));
        block.add(field);
        return block;
    }

    private JPanel fieldBlockFixed(String labelText, boolean required, JComponent field, int height) {
        JPanel block = new JPanel();
        block.setLayout(new BoxLayout(block, BoxLayout.Y_AXIS));
        block.setBackground(Color.WHITE);
        block.setAlignmentX(Component.LEFT_ALIGNMENT);
        block.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        JLabel label = new JLabel(labelText + (required ? " *" : ""));
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(required ? TEXT_DARK : TEXT_MUTED);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));

        block.add(label);
        block.add(Box.createVerticalStrut(6));
        block.add(field);
        return block;
    }

    private JPanel buildPdfRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setBackground(Color.WHITE);

        JButton chooseBtn = new JButton("Choose File");
        chooseBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chooseBtn.setFocusPainted(false);
        chooseBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        chooseBtn.addActionListener(e -> onChoosePdf());

        pdfLabel.setForeground(TEXT_MUTED);
        pdfLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        row.add(chooseBtn);
        row.add(pdfLabel);

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(Color.WHITE);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(row);

        JLabel hint = new JLabel("(Only PDF files are allowed)");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        hint.setForeground(TEXT_MUTED);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        hint.setBorder(BorderFactory.createEmptyBorder(4, 2, 0, 0));
        wrapper.add(hint);

        return wrapper;
    }

    private JPanel buildButtonRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setBackground(Color.WHITE);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        clearBtn.setFocusPainted(false);
        clearBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        clearBtn.addActionListener(e -> clearForm());

        RoundedButton uploadBtn = new RoundedButton(
                existingPaper == null ? "\u2191  Upload" : "Save Changes",
                ACCENT_BLUE, Color.WHITE, 6);
        uploadBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        uploadBtn.setBorder(BorderFactory.createEmptyBorder(9, 22, 9, 22));
        uploadBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        uploadBtn.addActionListener(e -> onSave());

        row.add(clearBtn);
        row.add(uploadBtn);
        return row;
    }

    private JComponent buildPdfGraphic() {
        JComponent graphic = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = 110, h = 140, x = (getWidth() - w) / 2, y = 30;
                int fold = 22;

                Path2D page = new Path2D.Double();
                page.moveTo(x, y);
                page.lineTo(x + w - fold, y);
                page.lineTo(x + w, y + fold);
                page.lineTo(x + w, y + h);
                page.lineTo(x, y + h);
                page.closePath();
                g2.setColor(new Color(0xF7, 0xF8, 0xFA));
                g2.fill(page);
                g2.setColor(new Color(0xC9, 0xCE, 0xD8));
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(page);

                Path2D foldTri = new Path2D.Double();
                foldTri.moveTo(x + w - fold, y);
                foldTri.lineTo(x + w, y + fold);
                foldTri.lineTo(x + w - fold, y + fold);
                foldTri.closePath();
                g2.setColor(new Color(0xE3, 0xE6, 0xEC));
                g2.fill(foldTri);

                int badgeW = 76, badgeH = 28;
                int badgeX = x + (w - badgeW) / 2, badgeY = y + 55;
                g2.setColor(PDF_RED);
                g2.fill(new RoundRectangle2D.Double(badgeX, badgeY, badgeW, badgeH, 4, 4));
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                String label = "PDF";
                int textX = badgeX + (badgeW - fm.stringWidth(label)) / 2;
                int textY = badgeY + (badgeH + fm.getAscent()) / 2 - 2;
                g2.drawString(label, textX, textY);

                for (int i = 0; i < 3; i++) {
                    int lineY = y + h - 24 + i * 7;
                    g2.setColor(new Color(0xE3, 0xE6, 0xEC));
                    g2.drawLine(x + 14, lineY, x + w - 14 - (i * 12), lineY);
                }

                g2.dispose();
            }
        };
        graphic.setPreferredSize(new Dimension(180, 220));
        return graphic;
    }

    // ---------- Data binding ----------

    private void populateFromExisting() {
        titleField.setText(existingPaper.getTitle());
        authorField.setText(existingPaper.getAuthor());
        if (existingPaper.getYear() != null && !existingPaper.getYear().isEmpty()) {
            yearCombo.setSelectedItem(existingPaper.getYear());
        }
        categoryField.setText(existingPaper.getCategory());
        keywordsField.setText(existingPaper.getKeywords());
        abstractArea.setText(existingPaper.getAbstractText());
        if (existingPaper.hasPdf()) {
            pdfLabel.setText(new File(existingPaper.getPdfPath()).getName());
            pdfLabel.setForeground(TEXT_DARK);
        }
    }

    private void clearForm() {
        titleField.setText("");
        authorField.setText("");
        categoryField.setText("");
        keywordsField.setText("");
        abstractArea.setText("");
        yearCombo.setSelectedItem(String.valueOf(LocalDate.now().getYear()));
        selectedPdf = null;
        pdfLabel.setText("No file chosen");
        pdfLabel.setForeground(TEXT_MUTED);
    }

    private void onChoosePdf() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedPdf = chooser.getSelectedFile();
            pdfLabel.setText(selectedPdf.getName());
            pdfLabel.setForeground(TEXT_DARK);
        }
    }

    private void onSave() {
        String title = titleField.getText();
        String author = authorField.getText();
        String year = (String) yearCombo.getSelectedItem();
        String category = categoryField.getText();
        String keywords = keywordsField.getText();
        String abstractText = abstractArea.getText();

        String error;
        if (existingPaper == null) {
            error = controller.addPaper(title, author, year, category, keywords, abstractText, selectedPdf);
        } else {
            error = controller.editPaper(existingPaper.getId(), title, author, year, category, keywords, abstractText, selectedPdf);
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

    // ---------- Helper components ----------

    private static class PlaceholderField extends JTextField {
        private final String placeholder;

        PlaceholderField(String placeholder) {
            this.placeholder = placeholder;
            setFont(new Font("Segoe UI", Font.PLAIN, 13));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(TEXT_MUTED);
                g2.setFont(getFont());
                Insets ins = getInsets();
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(placeholder, ins.left, ins.top + fm.getAscent());
                g2.dispose();
            }
        }
    }

    private static class RoundedButton extends JButton {
        private final Color bgColor;
        private final int arc;

        RoundedButton(String text, Color bg, Color fg, int arc) {
            super(text);
            this.bgColor = bg;
            this.arc = arc;
            setForeground(fg);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
