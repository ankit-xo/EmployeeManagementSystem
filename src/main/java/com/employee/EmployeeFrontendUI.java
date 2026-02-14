package com.employee;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class EmployeeFrontendUI extends JFrame {
    private static final Color IOS_BG = new Color(241, 244, 247);
    private static final Color IOS_CARD = Color.WHITE;
    private static final Color IOS_FIELD_BG = Color.WHITE;
    private static final Color IOS_TEXT = new Color(31, 41, 55);
    private static final Color IOS_MUTED_TEXT = new Color(107, 114, 128);
    private static final Color IOS_BORDER = new Color(213, 217, 222);

    private static final Color IOS_BLUE = new Color(28, 111, 66);
    private static final Color IOS_BLUE_HOVER = new Color(23, 93, 55);
    private static final Color IOS_GREEN = new Color(33, 115, 70);
    private static final Color IOS_GREEN_HOVER = new Color(27, 94, 58);
    private static final Color IOS_RED = new Color(220, 38, 38);
    private static final Color IOS_RED_HOVER = new Color(185, 28, 28);
    private static final Color IOS_ORANGE = new Color(238, 243, 247);
    private static final Color IOS_ORANGE_HOVER = new Color(227, 234, 239);
    private static final Color IOS_GRAY = new Color(238, 243, 247);
    private static final Color IOS_GRAY_HOVER = new Color(227, 234, 239);
    private static final Color HEADER_BG = IOS_BLUE;
    private static final Color HEADER_BORDER = IOS_BLUE_HOVER;
    private static final Color HEADER_TEXT = Color.WHITE;
    private static final Color SECTION_TITLE = new Color(45, 64, 84);
    private static final Color TABLE_HEADER_BG = new Color(237, 242, 247);
    private static final Color TABLE_HEADER_TEXT = new Color(57, 68, 81);
    private static final Color TABLE_ALT_ROW = new Color(246, 250, 247);
    private static final Color PANEL_SHADOW = new Color(0, 0, 0, 12);
    private static final Color BUTTON_STROKE = new Color(0, 0, 0, 28);

    private static final int CARD_RADIUS = 14;
    private static final int CONTROL_RADIUS = 10;

    private final EmployeeDatabase database = new EmployeeDatabase();
    private final String uiFontFamily = resolveUIFontFamily();

    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextField idField;
    private JTextField positionField;
    private JTextField contactField;
    private JTextField emailField;
    private JTextField addressField;
    private JTextField searchField;
    private JLabel statusLabel;
    private JLabel formMessageLabel;
    private JComboBox<String> departmentCombo;
    private JComboBox<String> searchFilterCombo;
    private JTabbedPane tabbedPane;

    public EmployeeFrontendUI() {
        setTitle("Employee Management System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                database.saveEmployeesManually();
                System.exit(0);
            }
        });

        getContentPane().setLayout(new BorderLayout(0, 14));
        getContentPane().setBackground(IOS_BG);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(16, 16, 16, 16));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createStatusPanel(), BorderLayout.SOUTH);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        styleTabbedPane(tabbedPane);
        tabbedPane.addTab("Employee Form", createFormPanel());
        tabbedPane.addTab("Employee Directory", createViewPanel());
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new RoundedPanel(CARD_RADIUS, HEADER_BG, HEADER_BORDER, PANEL_SHADOW);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(12, 16, 12, 16));
        JLabel titleLabel = new JLabel("Employee Management System");
        titleLabel.setFont(uiFont(Font.BOLD, 28));
        titleLabel.setForeground(HEADER_TEXT);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        return headerPanel;
    }

    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 2));
        statusPanel.setBackground(IOS_CARD);
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(IOS_BORDER, 1, CONTROL_RADIUS),
                new EmptyBorder(6, 8, 6, 8)
        ));

        statusLabel = new JLabel("Ready");
        statusLabel.setFont(uiFont(Font.PLAIN, 12));
        statusLabel.setForeground(IOS_MUTED_TEXT);
        statusPanel.add(statusLabel);

        return statusPanel;
    }

    private void styleTabbedPane(JTabbedPane pane) {
        UIManager.put("TabbedPane.font", uiFont(Font.BOLD, 12));
        UIManager.put("TabbedPane.selected", IOS_CARD);
        UIManager.put("TabbedPane.contentAreaColor", IOS_BG);
        UIManager.put("TabbedPane.background", IOS_BG);
        UIManager.put("TabbedPane.foreground", IOS_TEXT);
        UIManager.put("TabbedPane.darkShadow", IOS_BORDER);
        UIManager.put("TabbedPane.light", IOS_BORDER);
        UIManager.put("TabbedPane.highlight", IOS_BORDER);

        pane.setFont(uiFont(Font.BOLD, 12));
        pane.setBackground(IOS_BG);
        pane.setForeground(IOS_TEXT);
        pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    }

    private JPanel createFormPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 14));
        mainPanel.setBackground(IOS_BG);

        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setBackground(IOS_BG);

        JPanel personalPanel = createFormSection("Personal Information");
        JPanel personalGridPanel = new JPanel(new GridBagLayout());
        personalGridPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        personalGridPanel.add(createStyledLabeledTextField("ID", idField = new JTextField()), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        personalGridPanel.add(createStyledLabeledTextField("Name", nameField = new JTextField()), gbc);

        personalPanel.add(personalGridPanel, BorderLayout.CENTER);
        containerPanel.add(personalPanel);
        containerPanel.add(Box.createVerticalStrut(14));

        JPanel employmentPanel = createFormSection("Employment Details");
        JPanel employmentGridPanel = new JPanel(new GridBagLayout());
        employmentGridPanel.setOpaque(false);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        employmentGridPanel.add(createStyledDepartmentPanel(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        employmentGridPanel.add(createStyledLabeledTextField("Position", positionField = new JTextField()), gbc);

        employmentPanel.add(employmentGridPanel, BorderLayout.CENTER);
        containerPanel.add(employmentPanel);
        containerPanel.add(Box.createVerticalStrut(14));

        JPanel contactPanel = createFormSection("Contact Information");
        JPanel contactGridPanel = new JPanel(new GridBagLayout());
        contactGridPanel.setOpaque(false);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        contactGridPanel.add(createStyledLabeledTextField("Email", emailField = new JTextField()), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        contactGridPanel.add(createStyledLabeledTextField("Contact (10 digits)", contactField = new JTextField()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        contactGridPanel.add(createStyledLabeledTextField("Address", addressField = new JTextField()), gbc);

        contactPanel.add(contactGridPanel, BorderLayout.CENTER);
        containerPanel.add(contactPanel);

        JScrollPane formScrollPane = new JScrollPane(containerPanel);
        formScrollPane.setBorder(BorderFactory.createEmptyBorder());
        formScrollPane.setBackground(IOS_BG);
        formScrollPane.getViewport().setBackground(IOS_BG);
        formScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(formScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(IOS_BG);

        JButton addButton = createModernButton("Add", IOS_GREEN, IOS_GREEN_HOVER);
        addButton.addActionListener(new AddEmployeeAction());
        buttonPanel.add(addButton);

        JButton updateButton = createModernButton("Update", IOS_BLUE, IOS_BLUE_HOVER);
        updateButton.addActionListener(new UpdateEmployeeAction());
        buttonPanel.add(updateButton);

        JButton deleteButton = createModernButton("Delete", IOS_RED, IOS_RED_HOVER);
        deleteButton.addActionListener(new DeleteEmployeeAction());
        buttonPanel.add(deleteButton);

        JButton viewButton = createModernButton("View", IOS_ORANGE, IOS_ORANGE_HOVER);
        viewButton.addActionListener(new ViewEmployeeAction());
        buttonPanel.add(viewButton);

        JButton clearButton = createModernButton("Clear", IOS_GRAY, IOS_GRAY_HOVER);
        clearButton.addActionListener(e -> {
            clearFields();
            clearFormMessage();
        });
        buttonPanel.add(clearButton);

        formMessageLabel = new JLabel(" ");
        formMessageLabel.setFont(uiFont(Font.PLAIN, 12));
        formMessageLabel.setForeground(IOS_MUTED_TEXT);
        formMessageLabel.setBorder(new EmptyBorder(0, 8, 4, 8));

        JPanel footerPanel = new JPanel(new BorderLayout(0, 4));
        footerPanel.setBackground(IOS_BG);
        footerPanel.add(formMessageLabel, BorderLayout.NORTH);
        footerPanel.add(buttonPanel, BorderLayout.CENTER);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createFormSection(String title) {
        JPanel sectionPanel = createCardPanel();
        sectionPanel.setLayout(new BorderLayout(0, 12));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(uiFont(Font.BOLD, 14));
        titleLabel.setForeground(SECTION_TITLE);
        sectionPanel.add(titleLabel, BorderLayout.NORTH);

        return sectionPanel;
    }

    private JPanel createStyledLabeledTextField(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);

        JLabel label = new JLabel(labelText + ":");
        label.setFont(uiFont(Font.PLAIN, 13));
        label.setForeground(IOS_TEXT);
        label.setPreferredSize(new Dimension(150, 40));

        styleTextField(textField);

        panel.add(label, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStyledDepartmentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);

        JLabel label = new JLabel("Department:");
        label.setFont(uiFont(Font.PLAIN, 13));
        label.setForeground(IOS_TEXT);
        label.setPreferredSize(new Dimension(150, 40));

        departmentCombo = new JComboBox<>(new String[]{
                "Engineering", "Marketing", "HR", "Finance", "Sales", "Operations", "IT", "Other"
        });
        styleComboBox(departmentCombo);

        panel.add(label, BorderLayout.WEST);
        panel.add(departmentCombo, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createViewPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 14));
        mainPanel.setBackground(IOS_BG);

        JPanel searchCard = createCardPanel();
        searchCard.setLayout(new BorderLayout(8, 6));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        filterPanel.setOpaque(false);

        JLabel searchLabel = new JLabel("Search by:");
        searchLabel.setFont(uiFont(Font.PLAIN, 13));
        searchLabel.setForeground(IOS_TEXT);

        searchFilterCombo = new JComboBox<>(new String[]{"Name", "ID", "Department", "Position"});
        styleComboBox(searchFilterCombo);
        searchFilterCombo.setPreferredSize(new Dimension(130, 38));

        searchField = new JTextField(20);
        styleTextField(searchField);
        searchField.addKeyListener(new SearchKeyListener());

        JButton searchButton = createModernButton("Search", IOS_GREEN, IOS_GREEN_HOVER);
        searchButton.setPreferredSize(new Dimension(110, 38));
        searchButton.addActionListener(e -> performSearch());

        JButton refreshButton = createModernButton("Refresh", IOS_GRAY, IOS_GRAY_HOVER);
        refreshButton.setPreferredSize(new Dimension(110, 38));
        refreshButton.addActionListener(e -> loadAllEmployees());

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 2));
        actionPanel.setOpaque(false);
        actionPanel.add(searchButton);
        actionPanel.add(refreshButton);

        filterPanel.add(searchLabel);
        filterPanel.add(searchFilterCombo);
        filterPanel.add(searchField);

        searchCard.add(filterPanel, BorderLayout.CENTER);
        searchCard.add(actionPanel, BorderLayout.EAST);

        mainPanel.add(searchCard, BorderLayout.NORTH);

        JPanel tableCard = createCardPanel();
        tableCard.setLayout(new BorderLayout());

        String[] columns = {"ID", "Name", "Department", "Position", "Contact", "Email", "Address"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employeeTable = new JTable(tableModel);
        configureTable(employeeTable);
        attachTableRowAutoFill();

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(IOS_CARD);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        tableCard.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(tableCard, BorderLayout.CENTER);

        loadAllEmployees();
        return mainPanel;
    }

    private void configureTable(JTable table) {
        table.setFont(uiFont(Font.PLAIN, 12));
        table.setRowHeight(31);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.setGridColor(IOS_BORDER);
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setSelectionBackground(IOS_BLUE);
        table.setSelectionForeground(Color.WHITE);
        table.setBackground(IOS_CARD);
        table.setForeground(IOS_TEXT);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JTableHeader header = table.getTableHeader();
        header.setFont(uiFont(Font.BOLD, 12));
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(TABLE_HEADER_TEXT);
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, IOS_BORDER));
        header.setPreferredSize(new Dimension(0, 32));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column
            ) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(new EmptyBorder(0, 10, 0, 10));

                if (isSelected) {
                    comp.setBackground(IOS_BLUE);
                    comp.setForeground(Color.WHITE);
                } else {
                    comp.setBackground(row % 2 == 0 ? IOS_CARD : TABLE_ALT_ROW);
                    comp.setForeground(IOS_TEXT);
                }
                return comp;
            }
        };

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        configureColumnWidths(table);
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(uiFont(Font.PLAIN, 13));
        textField.setPreferredSize(new Dimension(250, 38));
        textField.setBackground(IOS_FIELD_BG);
        textField.setForeground(IOS_TEXT);
        textField.setCaretColor(IOS_BLUE);
        textField.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(IOS_BORDER, 1, CONTROL_RADIUS),
                new EmptyBorder(8, 12, 8, 12)
        ));

        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        new RoundedBorder(IOS_BLUE, 2, CONTROL_RADIUS),
                        new EmptyBorder(7, 11, 7, 11)
                ));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        new RoundedBorder(IOS_BORDER, 1, CONTROL_RADIUS),
                        new EmptyBorder(8, 12, 8, 12)
                ));
            }
        });
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(uiFont(Font.PLAIN, 13));
        comboBox.setBackground(IOS_FIELD_BG);
        comboBox.setForeground(IOS_TEXT);
        comboBox.setPreferredSize(new Dimension(250, 38));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(IOS_BORDER, 1, CONTROL_RADIUS),
                new EmptyBorder(2, 8, 2, 8)
        ));
        comboBox.setFocusable(false);
    }

    private JPanel createCardPanel() {
        JPanel panel = new RoundedPanel(CARD_RADIUS, IOS_CARD, IOS_BORDER, PANEL_SHADOW);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));
        return panel;
    }

    private void configureColumnWidths(JTable table) {
        int[] widths = {90, 190, 160, 170, 140, 240, 300};
        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    private JButton createModernButton(String text, Color normalColor, Color hoverColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color fill = normalColor;
                if (getModel().isPressed()) {
                    fill = hoverColor.darker();
                } else if (getModel().isRollover()) {
                    fill = hoverColor;
                }

                g2.setColor(fill);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), CONTROL_RADIUS, CONTROL_RADIUS);
                g2.setColor(BUTTON_STROKE);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CONTROL_RADIUS, CONTROL_RADIUS);
                g2.dispose();

                super.paintComponent(g);
            }
        };
        button.setFont(uiFont(Font.BOLD, 13));
        button.setForeground(isDarkColor(normalColor) ? Color.WHITE : IOS_TEXT);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(128, 42));

        return button;
    }

    private boolean isDarkColor(Color color) {
        int brightness = (int) Math.sqrt(
                color.getRed() * color.getRed() * 0.299 +
                        color.getGreen() * color.getGreen() * 0.587 +
                        color.getBlue() * color.getBlue() * 0.114
        );
        return brightness < 140;
    }

    private Font uiFont(int style, int size) {
        return new Font(uiFontFamily, style, size);
    }

    private String resolveUIFontFamily() {
        String[] preferredFonts = {"SF Pro Text", "SF Pro Display", "Helvetica Neue", "Segoe UI", "Arial"};
        String[] availableFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        for (String preferred : preferredFonts) {
            for (String available : availableFonts) {
                if (preferred.equalsIgnoreCase(available)) {
                    return available;
                }
            }
        }
        return "SansSerif";
    }

    private void loadAllEmployees() {
        tableModel.setRowCount(0);
        List<Employee> employees = database.getEmployees();

        for (Employee emp : employees) {
            tableModel.addRow(new Object[]{
                    emp.getId(),
                    emp.getName(),
                    emp.getDepartment(),
                    emp.getPosition(),
                    emp.getContact(),
                    emp.getEmail(),
                    emp.getAddress()
            });
        }

        int count = employees.size();
        updateStatus(count + " " + employeeLabel(count) + " loaded");
    }

    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        String filterType = (String) searchFilterCombo.getSelectedItem();

        if (searchTerm.isEmpty()) {
            loadAllEmployees();
            return;
        }

        tableModel.setRowCount(0);
        List<Employee> employees = database.getEmployees();

        for (Employee emp : employees) {
            boolean matches = false;

            switch (filterType) {
                case "Name":
                    matches = emp.getName().toLowerCase().contains(searchTerm.toLowerCase());
                    break;
                case "ID":
                    matches = String.valueOf(emp.getId()).contains(searchTerm);
                    break;
                case "Department":
                    matches = emp.getDepartment().toLowerCase().contains(searchTerm.toLowerCase());
                    break;
                case "Position":
                    matches = emp.getPosition().toLowerCase().contains(searchTerm.toLowerCase());
                    break;
                default:
                    break;
            }

            if (matches) {
                tableModel.addRow(new Object[]{
                        emp.getId(),
                        emp.getName(),
                        emp.getDepartment(),
                        emp.getPosition(),
                        emp.getContact(),
                        emp.getEmail(),
                        emp.getAddress()
                });
            }
        }

        int count = tableModel.getRowCount();
        updateStatus("Search results: " + count + " " + employeeLabel(count) + " found");
    }

    private String employeeLabel(int count) {
        return count == 1 ? "employee" : "employees";
    }

    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }

    private void showFormError(String message) {
        showFormMessage(message, IOS_RED);
    }

    private void showFormSuccess(String message) {
        showFormMessage(message, IOS_GREEN);
    }

    private void clearFormMessage() {
        if (formMessageLabel != null) {
            formMessageLabel.setText(" ");
            formMessageLabel.setForeground(IOS_MUTED_TEXT);
        }
    }

    private void showFormMessage(String message, Color color) {
        if (formMessageLabel != null) {
            formMessageLabel.setText(message);
            formMessageLabel.setForeground(color);
        }
        updateStatus(message);
    }

    private class SearchKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                performSearch();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

    private class RoundedPanel extends JPanel {
        private final int radius;
        private final Color fillColor;
        private final Color borderColor;
        private final Color shadowColor;

        RoundedPanel(int radius, Color fillColor, Color borderColor, Color shadowColor) {
            this.radius = radius;
            this.fillColor = fillColor;
            this.borderColor = borderColor;
            this.shadowColor = shadowColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth() - 1;
            int height = getHeight() - 1;

            g2.setColor(shadowColor);
            g2.fillRoundRect(1, 2, width - 1, height - 1, radius, radius);

            g2.setColor(fillColor);
            g2.fillRoundRect(0, 0, width - 1, height - 1, radius, radius);

            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, width - 1, height - 1, radius, radius);

            g2.dispose();
        }
    }

    private static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int thickness;
        private final int radius;

        RoundedBorder(Color color, int thickness, int radius) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness, thickness, thickness, thickness);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = thickness;
            insets.right = thickness;
            insets.top = thickness;
            insets.bottom = thickness;
            return insets;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            int offset = thickness / 2;
            g2.drawRoundRect(
                    x + offset,
                    y + offset,
                    width - thickness,
                    height - thickness,
                    radius,
                    radius
            );
            g2.dispose();
        }
    }

    private class AddEmployeeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String name = nameField.getText().trim();
                String idStr = idField.getText().trim();
                String department = (String) departmentCombo.getSelectedItem();
                String position = positionField.getText().trim();
                String contact = contactField.getText().trim();
                String email = emailField.getText().trim();
                String address = addressField.getText().trim();

                if (name.isEmpty() || idStr.isEmpty() || position.isEmpty() || contact.isEmpty() || email.isEmpty() || address.isEmpty()) {
                    showFormError("Please fill all fields.");
                    return;
                }

                if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    showFormError("Please enter a valid email address.");
                    return;
                }

                if (!contact.matches("^\\d{10}$")) {
                    showFormError("Contact must be a 10-digit number.");
                    return;
                }

                int id = Integer.parseInt(idStr);

                boolean added = database.addEmployee(new Employee(name, id, department, position, contact, email, address));
                if (!added) {
                    showFormError("Employee ID already exists.");
                    return;
                }

                showFormSuccess("Employee added successfully. Enter details for next employee.");
                clearFields();
                loadAllEmployees();
                focusOnFirstField();
            } catch (NumberFormatException ex) {
                showFormError("Please enter a valid ID (number).");
            }
        }
    }

    private class UpdateEmployeeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String idStr = idField.getText().trim();
                String newName = nameField.getText().trim();
                String newDepartment = (String) departmentCombo.getSelectedItem();
                String newPosition = positionField.getText().trim();
                String newContact = contactField.getText().trim();
                String newEmail = emailField.getText().trim();
                String newAddress = addressField.getText().trim();

                if (idStr.isEmpty() || newName.isEmpty() || newPosition.isEmpty() || newContact.isEmpty() || newEmail.isEmpty() || newAddress.isEmpty()) {
                    showFormError("Please fill all fields.");
                    return;
                }

                if (!newEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    showFormError("Please enter a valid email address.");
                    return;
                }

                if (!newContact.matches("^\\d{10}$")) {
                    showFormError("Contact must be a 10-digit number.");
                    return;
                }

                int id = Integer.parseInt(idStr);

                boolean updated = database.updateEmployee(id, new Employee(newName, id, newDepartment, newPosition, newContact, newEmail, newAddress));
                if (!updated) {
                    showFormError("Employee not found.");
                    return;
                }

                showFormSuccess("Employee updated successfully.");
                clearFields();
                loadAllEmployees();
            } catch (NumberFormatException ex) {
                showFormError("Please enter a valid ID (number).");
            }
        }
    }

    private class DeleteEmployeeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String idStr = idField.getText().trim();
                if (idStr.isEmpty()) {
                    showFormError("Please enter an employee ID.");
                    return;
                }

                int id = Integer.parseInt(idStr);
                int confirm = JOptionPane.showConfirmDialog(
                        EmployeeFrontendUI.this,
                        "Are you sure you want to delete this employee?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    if (database.deleteEmployee(id)) {
                        showFormSuccess("Employee deleted successfully.");
                        clearFields();
                        loadAllEmployees();
                    } else {
                        showFormError("Employee not found.");
                    }
                }
            } catch (NumberFormatException ex) {
                showFormError("Please enter a valid ID.");
            }
        }
    }

    private class ViewEmployeeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String idStr = idField.getText().trim();
                if (idStr.isEmpty()) {
                    showFormError("Please enter an employee ID.");
                    return;
                }

                int id = Integer.parseInt(idStr);
                Employee emp = database.getEmployeeById(id);

                if (emp != null) {
                    populateFormWithEmployee(emp);
                    loadAllEmployees();
                    selectEmployeeRowById(emp.getId());
                    tabbedPane.setSelectedIndex(0);
                    showFormSuccess("Employee loaded: " + emp.getName());
                } else {
                    showFormError("Employee not found.");
                }
            } catch (NumberFormatException ex) {
                showFormError("Please enter a valid ID.");
            }
        }
    }

    private void clearFields() {
        nameField.setText("");
        idField.setText("");
        departmentCombo.setSelectedIndex(0);
        positionField.setText("");
        contactField.setText("");
        emailField.setText("");
        addressField.setText("");
    }

    private void focusOnFirstField() {
        SwingUtilities.invokeLater(() -> idField.requestFocusInWindow());
    }

    private void populateFormWithEmployee(Employee emp) {
        idField.setText(String.valueOf(emp.getId()));
        nameField.setText(emp.getName());
        positionField.setText(emp.getPosition());
        contactField.setText(emp.getContact());
        emailField.setText(emp.getEmail());
        addressField.setText(emp.getAddress());
        setDepartmentSelection(emp.getDepartment());
    }

    private void setDepartmentSelection(String department) {
        ComboBoxModel<String> model = departmentCombo.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            if (department.equalsIgnoreCase(model.getElementAt(i))) {
                departmentCombo.setSelectedIndex(i);
                return;
            }
        }
        departmentCombo.setSelectedItem("Other");
    }

    private void selectEmployeeRowById(int id) {
        for (int modelRow = 0; modelRow < tableModel.getRowCount(); modelRow++) {
            Object value = tableModel.getValueAt(modelRow, 0);
            if (value != null && String.valueOf(value).equals(String.valueOf(id))) {
                int viewRow = employeeTable.convertRowIndexToView(modelRow);
                if (viewRow >= 0) {
                    employeeTable.setRowSelectionInterval(viewRow, viewRow);
                    employeeTable.scrollRectToVisible(employeeTable.getCellRect(viewRow, 0, true));
                }
                return;
            }
        }
    }

    private void attachTableRowAutoFill() {
        employeeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int viewRow = employeeTable.rowAtPoint(e.getPoint());
                if (viewRow < 0) {
                    return;
                }
                int modelRow = employeeTable.convertRowIndexToModel(viewRow);
                Object idValue = tableModel.getValueAt(modelRow, 0);
                if (idValue == null) {
                    return;
                }
                try {
                    int id = Integer.parseInt(String.valueOf(idValue));
                    Employee emp = database.getEmployeeById(id);
                    if (emp != null) {
                        populateFormWithEmployee(emp);
                        tabbedPane.setSelectedIndex(0);
                        showFormSuccess("Loaded from directory: " + emp.getName());
                    }
                } catch (NumberFormatException ignored) {
                    showFormError("Selected row has invalid employee ID.");
                }
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            EmployeeFrontendUI frame = new EmployeeFrontendUI();
            frame.setVisible(true);
        });
    }
}
