/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.papermanager;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;

/**
 * Main GUI window: sidebar navigation + a styled table of papers.
 * Each row has View / Edit / Delete icon buttons in the Actions column.
 * Icons are drawn as vector graphics (not text glyphs) so they render
 * consistently regardless of the system font.
 */
public class PaperListForm extends JFrame {

    private static final Color SIDEBAR_BG = new Color(0x14, 0x22, 0x3D);
    private static final Color SIDEBAR_SELECTED = new Color(0x24, 0x4A, 0x8C);
    private static final Color ACCENT_BLUE = new Color(0x2F, 0x6F, 0xED);
    private static final Color CONTENT_BG = new Color(0xF4, 0xF6, 0xFA);
    private static final Color TABLE_HEADER_BG = new Color(0xF0, 0xF2, 0xF7);
    private static final Color ROW_ALT_BG = new Color(0xFA, 0xFB, 0xFD);
    private static final Color TEXT_DARK = new Color(0x1A, 0x22, 0x33);
    private static final Color TEXT_MUTED = new Color(0x8A, 0x93, 0xA6);
    private static final Color NAV_TEXT = new Color(0xC7, 0xD0, 0xE0);
    private static final Color VIEW_COLOR = new Color(0x2F, 0x6F, 0xED);
    private static final Color EDIT_COLOR = new Color(0xE8, 0xA2, 0x3A);
    private static final Color DELETE_COLOR = new Color(0xE0, 0x4F, 0x4F);

    private final PaperController controller;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JLabel totalLabel = new JLabel();
    private final JPanel contentBody = new JPanel(new BorderLayout(0, 14));

    public PaperListForm(PaperController controller) {
        super("Research Paper Management System");
        this.controller = controller;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 560);
        setMinimumSize(new Dimension(800, 480));
        setLocationRelativeTo(null);
        getContentPane().setBackground(CONTENT_BG);
        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);

        String[] columns = {"ID", "Title", "Authors", "Published Year", "Venue", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(48);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(0xE9, 0xF0, 0xFD));
        table.setSelectionForeground(TEXT_DARK);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(TABLE_HEADER_BG);
        table.getTableHeader().setForeground(TEXT_MUTED);
        table.getTableHeader().setPreferredSize(new Dimension(0, 38));
        table.getTableHeader().setBorder(BorderFactory.createEmptyBorder());
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(5).setMaxWidth(120);
        table.setDefaultRenderer(Object.class, new StripedCellRenderer());

        ActionsPanelFactory factory = new ActionsPanelFactory();
        table.getColumnModel().getColumn(5).setCellRenderer(new ActionsRenderer(factory));
        table.getColumnModel().getColumn(5).setCellEditor(new ActionsEditor(factory));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(new LineBorder(new Color(0xE4, 0xE7, 0xEE), 1, true));
        tableCard.add(scrollPane, BorderLayout.CENTER);

        contentBody.add(tableCard, BorderLayout.CENTER);
        contentBody.add(buildFooter(), BorderLayout.SOUTH);

        refreshTable();
    }

    // ---------- Sidebar ----------

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        sidebar.add(buildProfileBlock());
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(buildNavButton("list", "Paper List", true, this::refreshTable));
        sidebar.add(buildNavButton("plus", "Add Paper", false, this::onAdd));
        sidebar.add(buildNavButton("edit", "Edit Paper", false, this::onEdit));
        sidebar.add(buildNavButton("trash", "Delete Paper", false, this::onDelete));
        sidebar.add(buildNavButton("search", "View Paper Details", false, this::onViewDetails));
        sidebar.add(buildNavButton("doc", "Open PDF", false, this::onOpenPdf));
        sidebar.add(Box.createVerticalGlue());

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(0x2A, 0x3B, 0x5C));
        sep.setMaximumSize(new Dimension(1000, 1));
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(buildNavButton("power", "Logout", false, this::onLogout));

        return sidebar;
    }

    private JPanel buildProfileBlock() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(SIDEBAR_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(1000, 60));

        JComponent avatar = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xE8, 0xEE, 0xF9));
                g2.fillOval(0, 0, 44, 44);
                g2.setColor(new Color(0x8A, 0xA6, 0xE0));
                g2.fillOval(14, 8, 16, 16);
                g2.fillOval(6, 26, 32, 22);
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(44, 44));
        avatar.setMaximumSize(new Dimension(44, 44));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(SIDEBAR_BG);
        JLabel subLabel = new JLabel("Research Paper");
        subLabel.setForeground(Color.WHITE);
        subLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JLabel subLabel2 = new JLabel("Management");
        subLabel2.setForeground(TEXT_MUTED);
        subLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        textPanel.add(Box.createVerticalGlue());
        textPanel.add(subLabel);
        textPanel.add(subLabel2);
        textPanel.add(Box.createVerticalGlue());

        panel.add(avatar);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(textPanel);
        return panel;
    }

    private JButton buildNavButton(String iconType, String text, boolean selected, Runnable action) {
        JButton btn = new JButton(text);
        btn.setIcon(new VectorIcon(iconType, 16, selected ? Color.WHITE : NAV_TEXT));
        btn.setIconTextGap(12);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(1000, 42));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (selected) {
            btn.setBackground(SIDEBAR_SELECTED);
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(SIDEBAR_BG);
            btn.setForeground(NAV_TEXT);
        }
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private void onLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to log out and close the application?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // ---------- Content area ----------

    private JPanel buildContent() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(CONTENT_BG);
        wrapper.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        wrapper.add(buildHeader(), BorderLayout.NORTH);
        contentBody.setOpaque(false);
        wrapper.add(contentBody, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        JLabel title = new JLabel("Paper List");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_DARK);

        RoundedButton addBtn = new RoundedButton("+  Add New Paper", ACCENT_BLUE, Color.WHITE, 8);
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addBtn.addActionListener(e -> onAdd());

        header.add(title, BorderLayout.WEST);
        header.add(addBtn, BorderLayout.EAST);
        return header;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 4, 0, 4));
        totalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        totalLabel.setForeground(TEXT_MUTED);
        footer.add(totalLabel, BorderLayout.WEST);
        return footer;
    }

    // ---------- Table logic ----------

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Paper> papers = controller.getAllPapers();
        for (Paper p : papers) {
            tableModel.addRow(new Object[]{
                    p.getId(), p.getTitle(), p.getAuthor(), p.getYear(), p.getCategory(), ""
            });
        }
        totalLabel.setText("Total Papers: " + papers.size());
    }

    private int getSelectedId() {
        int row = table.getSelectedRow();
        if (row == -1) return -1;
        return (int) tableModel.getValueAt(row, 0);
    }

    private int idForRow(int row) {
        return (int) tableModel.getValueAt(row, 0);
    }

    private void onAdd() {
        UploadPaperForm form = new UploadPaperForm(this, controller, null);
        form.setVisible(true);
        refreshTable();
    }

    private void onEdit() {
        int id = getSelectedId();
        if (id == -1) {
            showNoSelectionWarning();
            return;
        }
        editRow(id);
    }

    private void editRow(int id) {
        Paper paper = controller.getPaperById(id);
        UploadPaperForm form = new UploadPaperForm(this, controller, paper);
        form.setVisible(true);
        refreshTable();
    }

    private void onDelete() {
        int id = getSelectedId();
        if (id == -1) {
            showNoSelectionWarning();
            return;
        }
        deleteRow(id);
    }

    private void deleteRow(int id) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this paper? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.deletePaper(id);
            refreshTable();
        }
    }

    private void onViewDetails() {
        int id = getSelectedId();
        if (id == -1) {
            showNoSelectionWarning();
            return;
        }
        viewRow(id);
    }

    private void viewRow(int id) {
        Paper paper = controller.getPaperById(id);
        PaperDetailsForm form = new PaperDetailsForm(this, paper);
        form.setVisible(true);
    }

    private void onOpenPdf() {
        int id = getSelectedId();
        if (id == -1) {
            showNoSelectionWarning();
            return;
        }
        boolean opened = controller.openPdf(id);
        if (!opened) {
            JOptionPane.showMessageDialog(this,
                    "No PDF is attached to this paper, or it could not be opened.",
                    "Cannot Open PDF", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showNoSelectionWarning() {
        JOptionPane.showMessageDialog(this,
                "Please select a paper from the list first.",
                "No Paper Selected", JOptionPane.WARNING_MESSAGE);
    }

    // ---------- Striped row renderer for normal columns ----------

    private class StripedCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected,
                                                         boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
            setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? Color.WHITE : ROW_ALT_BG);
                c.setForeground(TEXT_DARK);
            }
            return c;
        }
    }

    // ---------- Actions column: view / edit / delete icon buttons ----------

    private class ActionsPanelFactory {
        JPanel createPanel(ActionListener onView, ActionListener onEdit, ActionListener onDelete) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 2));
            panel.setOpaque(false);
            panel.add(iconButton("eye", VIEW_COLOR, onView));
            panel.add(iconButton("edit", EDIT_COLOR, onEdit));
            panel.add(iconButton("trash", DELETE_COLOR, onDelete));
            return panel;
        }

        private JButton iconButton(String type, Color color, ActionListener listener) {
            JButton btn = new JButton();
            btn.setIcon(new VectorIcon(type, 14, color));
            btn.setBackground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setContentAreaFilled(true);
            btn.setOpaque(true);
            btn.setBorder(new LineBorder(color, 1, true));
            btn.setPreferredSize(new Dimension(28, 28));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addActionListener(listener);
            return btn;
        }
    }

    private class ActionsRenderer implements TableCellRenderer {
        private final ActionsPanelFactory factory;

        ActionsRenderer(ActionsPanelFactory factory) {
            this.factory = factory;
        }

        @Override
        public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected,
                                                         boolean hasFocus, int row, int column) {
            JPanel panel = factory.createPanel(e -> {}, e -> {}, e -> {});
            panel.setBackground(row % 2 == 0 ? Color.WHITE : ROW_ALT_BG);
            return panel;
        }
    }

    private class ActionsEditor extends AbstractCellEditor implements TableCellEditor {
        private final ActionsPanelFactory factory;
        private JPanel panel;
        private int currentRow;

        ActionsEditor(ActionsPanelFactory factory) {
            this.factory = factory;
        }

        @Override
        public Component getTableCellEditorComponent(JTable tbl, Object value, boolean isSelected, int row, int column) {
            currentRow = row;
            panel = factory.createPanel(
                    e -> { fireEditingStopped(); viewRow(idForRow(currentRow)); },
                    e -> { fireEditingStopped(); editRow(idForRow(currentRow)); },
                    e -> { fireEditingStopped(); deleteRow(idForRow(currentRow)); }
            );
            panel.setBackground(row % 2 == 0 ? Color.WHITE : ROW_ALT_BG);
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    // ---------- A button that always paints its own background, regardless of L&F ----------

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

    // ---------- Small vector icons drawn with Graphics2D (no font dependency) ----------

    private static class VectorIcon implements Icon {
        private final String type;
        private final int size;
        private final Color color;

        VectorIcon(String type, int size, Color color) {
            this.type = type;
            this.size = size;
            this.color = color;
        }

        @Override
        public int getIconWidth() { return size; }

        @Override
        public int getIconHeight() { return size; }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.translate(x, y);

            switch (type) {
                case "eye":
                    g2.draw(new Ellipse2D.Double(1, size * 0.28, size - 2, size * 0.44));
                    g2.fill(new Ellipse2D.Double(size / 2.0 - 1.6, size / 2.0 - 1.6, 3.2, 3.2));
                    break;
                case "edit":
                    g2.draw(new Line2D.Double(2, size - 2, size - 4, 3));
                    Path2D tip = new Path2D.Double();
                    tip.moveTo(size - 4, 3);
                    tip.lineTo(size - 1, 4);
                    tip.lineTo(size - 3, 6);
                    tip.closePath();
                    g2.fill(tip);
                    break;
                case "trash":
                    g2.draw(new RoundRectangle2D.Double(3, 5, size - 6, size - 6, 2, 2));
                    g2.draw(new Line2D.Double(1, 4, size - 1, 4));
                    g2.draw(new RoundRectangle2D.Double(size / 2.0 - 3, 1, 6, 3, 1, 1));
                    g2.draw(new Line2D.Double(size / 2.0 - 2, 8, size / 2.0 - 2, size - 4));
                    g2.draw(new Line2D.Double(size / 2.0 + 2, 8, size / 2.0 + 2, size - 4));
                    break;
                case "list":
                    for (int i = 0; i < 3; i++) {
                        double lineY = 2 + i * (size / 3.0);
                        g2.draw(new Line2D.Double(0, lineY, size, lineY));
                    }
                    break;
                case "plus":
                    g2.draw(new Line2D.Double(size / 2.0, 1, size / 2.0, size - 1));
                    g2.draw(new Line2D.Double(1, size / 2.0, size - 1, size / 2.0));
                    break;
                case "search":
                    double d = size * 0.65;
                    g2.draw(new Ellipse2D.Double(0, 0, d, d));
                    g2.draw(new Line2D.Double(d - 1, d - 1, size, size));
                    break;
                case "doc":
                    g2.draw(new RoundRectangle2D.Double(2, 0, size - 4, size, 2, 2));
                    for (int i = 0; i < 3; i++) {
                        double lineY = size * 0.32 + i * (size * 0.2);
                        g2.draw(new Line2D.Double(size * 0.32, lineY, size * 0.68, lineY));
                    }
                    break;
                case "power":
                    g2.draw(new Arc2D.Double(1, 1, size - 2, size - 2, 55, 250, Arc2D.OPEN));
                    g2.draw(new Line2D.Double(size / 2.0, 0, size / 2.0, size / 2.0));
                    break;
                default:
                    g2.draw(new Ellipse2D.Double(1, 1, size - 2, size - 2));
            }
            g2.dispose();
        }
    }
}
