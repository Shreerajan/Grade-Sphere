package ui;

import model.Student;
import model.Subject;
import service.StudentManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SubjectsPanel extends JPanel {

    private StudentManager manager;
    private JComboBox<String> studentComboBox;
    private JTable table;
    private DefaultTableModel tableModel;

    private final Color BACKGROUND_COLOR = new Color(245, 246, 250);
    private final Color PRIMARY_COLOR = new Color(110, 78, 190);

    public SubjectsPanel(StudentManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        createUI();
    }

    private void createUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BACKGROUND_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("Subjects Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));

        JPanel topControls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topControls.setBackground(BACKGROUND_COLOR);
        topControls.add(new JLabel("Select Student:"));
        
        studentComboBox = new JComboBox<>();
        studentComboBox.addActionListener(e -> loadSubjects());
        topControls.add(studentComboBox);

        JButton addButton = new JButton("+ Add Subject");
        addButton.setBackground(PRIMARY_COLOR);
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> showAddSubjectDialog());
        topControls.add(addButton);

        header.add(title, BorderLayout.WEST);
        header.add(topControls, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 30, 30, 30),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        String[] columns = {"Subject Code", "Subject Name", "Credits"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        content.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);

        JButton deleteButton = new JButton("Remove Subject");
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(220, 70, 70));
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> removeSelectedSubject());
        
        bottomPanel.add(deleteButton);
        content.add(bottomPanel, BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);
    }

    public void loadStudents() {
        Student previous = getSelectedStudent();
        studentComboBox.removeAllItems();
        for (Student s : manager.getAllStudents()) {
            studentComboBox.addItem(s.getId() + " - " + s.getName());
        }
        int index = previous == null ? -1 : manager.getAllStudents().indexOf(previous);
        if (index >= 0) {
            studentComboBox.setSelectedIndex(index);
        } else if (studentComboBox.getItemCount() > 0) {
            studentComboBox.setSelectedIndex(0);
        }
        loadSubjects();
    }

    private void loadSubjects() {
        tableModel.setRowCount(0);
        Student student = getSelectedStudent();
        if (student != null) {
            for (Subject s : student.getSubjects()) {
                tableModel.addRow(new Object[]{s.getSubjectCode(), s.getSubjectName(), s.getCredits()});
            }
        }
    }

    private Student getSelectedStudent() {
        int index = studentComboBox.getSelectedIndex();
        if (index >= 0 && index < manager.getAllStudents().size()) {
            return manager.getAllStudents().get(index);
        }
        return null;
    }

    private void showAddSubjectDialog() {
        Student student = getSelectedStudent();
        if (student == null) {
            JOptionPane.showMessageDialog(this,
                    manager.getAllStudents().isEmpty()
                            ? "There are no students yet. Please add a student first."
                            : "Please select a student first!",
                    "No Student Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField codeField = new JTextField(15);
        JTextField nameField = new JTextField(15);
        JTextField creditsField = new JTextField(15);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Subject Code:")); panel.add(codeField);
        panel.add(new JLabel("Subject Name:")); panel.add(nameField);
        panel.add(new JLabel("Credits (1-10):")); panel.add(creditsField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Add Subject for " + student.getName(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String code = codeField.getText().trim();
        String name = nameField.getText().trim();
        String creditText = creditsField.getText().trim();

        if (code.isEmpty() || name.isEmpty() || creditText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Subject code, name and credits are all required.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int credits;
        try {
            credits = Integer.parseInt(creditText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Credits must be a whole number (for example 3).",
                    "Invalid Credits", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (credits < 1 || credits > 10) {
            JOptionPane.showMessageDialog(this, "Credits must be between 1 and 10.",
                    "Invalid Credits", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Subject existing : student.getSubjects()) {
            if (existing.getSubjectCode().equalsIgnoreCase(code)) {
                JOptionPane.showMessageDialog(this,
                        "This student already has a subject with code \"" + code + "\".",
                        "Duplicate Subject", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        student.addSubject(new Subject(code, name, credits));
        manager.updateStudent(student);
        loadSubjects();
        JOptionPane.showMessageDialog(this, "Subject added!");
    }

    private void removeSelectedSubject() {
        Student student = getSelectedStudent();
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Please select a student first!",
                    "No Student Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a subject to remove!",
                    "No Subject Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String code = (String) tableModel.getValueAt(selectedRow, 0);
        int choice = JOptionPane.showConfirmDialog(this,
                "Remove subject " + code + "? Any marks entered for it will be lost.",
                "Confirm Remove", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
        student.getSubjects().removeIf(s -> s.getSubjectCode().equals(code));
        manager.updateStudent(student);
        loadSubjects();
        JOptionPane.showMessageDialog(this, "Subject removed!");
    }
}
