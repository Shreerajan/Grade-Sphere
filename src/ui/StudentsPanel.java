package ui;

import model.Student;
import service.StudentManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentsPanel extends JPanel {

    private StudentManager manager;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    private final Color BACKGROUND_COLOR = new Color(245, 246, 250);
    private final Color PRIMARY_COLOR = new Color(110, 78, 190);

    public StudentsPanel(StudentManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        createUI();
        loadStudents();
    }

    private void createUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BACKGROUND_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("Students");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));

        JButton addButton = new JButton("+ Add Student");
        addButton.setBackground(PRIMARY_COLOR);
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        addButton.addActionListener(e -> showAddStudentDialog());

        header.add(title, BorderLayout.WEST);
        header.add(addButton, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 30, 30, 30),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);

        JLabel searchLabel = new JLabel("🔍 Search:");
        searchLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        searchField = new JTextField(25);
        JButton searchButton = new JButton("Search");
        JButton showAllButton = new JButton("Show All");

        searchButton.addActionListener(e -> searchStudent());
        searchField.addActionListener(e -> searchStudent());
        showAllButton.addActionListener(e -> {
            searchField.setText("");
            loadStudents();
        });

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);
        content.add(searchPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Email", "Course", "Semester", "Percentage", "GPA", "Grade"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(table);
        content.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);

        JButton editButton = new JButton("Edit Selected");
        editButton.setForeground(Color.WHITE);
        editButton.setBackground(new Color(60, 140, 200));
        editButton.setFocusPainted(false);
        editButton.addActionListener(e -> editSelectedStudent());

        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(220, 70, 70));
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> deleteSelectedStudent());

        bottomPanel.add(editButton);
        bottomPanel.add(deleteButton);
        content.add(bottomPanel, BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);
    }

    public void loadStudents() {
        tableModel.setRowCount(0);
        for (Student student : manager.getAllStudents()) {
            addStudentToTable(student);
        }
    }

    private void addStudentToTable(Student student) {
        tableModel.addRow(new Object[]{
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getCourse(),
                student.getSemester(),
                String.format("%.2f%%", student.getPercentage()),
                String.format("%.2f", student.getGPA()),
                student.getOverallGrade()
        });
    }

    private void searchStudent() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            loadStudents();
            return;
        }
        tableModel.setRowCount(0);
        java.util.List<Student> results = manager.searchStudents(searchText);
        for (Student student : results) {
            addStudentToTable(student);
        }
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students match \"" + searchText + "\".",
                    "Search", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showAddStudentDialog() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField courseField = new JTextField();
        JTextField semesterField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.add(new JLabel("Student ID:")); panel.add(idField);
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Email:")); panel.add(emailField);
        panel.add(new JLabel("Course:")); panel.add(courseField);
        panel.add(new JLabel("Semester:")); panel.add(semesterField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Student", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String course = courseField.getText().trim();
            String semester = semesterField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || email.isEmpty() || course.isEmpty() || semester.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }
            if (!email.contains("@")) {
                JOptionPane.showMessageDialog(this, "Invalid email format!");
                return;
            }
            if (manager.findStudent(id) != null) {
                JOptionPane.showMessageDialog(this, "Student ID already exists!");
                return;
            }

            Student student = new Student(id, name, email, course, semester);
            manager.addStudent(student);
            loadStudents();
            JOptionPane.showMessageDialog(this, "Student Added Successfully!");
        }
    }

    private void editSelectedStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student first!");
            return;
        }
        String id = (String) tableModel.getValueAt(selectedRow, 0);
        Student student = manager.findStudent(id);
        if (student == null) return;

        JTextField idField = new JTextField(student.getId());
        idField.setEditable(false);
        JTextField nameField = new JTextField(student.getName());
        JTextField emailField = new JTextField(student.getEmail());
        JTextField courseField = new JTextField(student.getCourse());
        JTextField semesterField = new JTextField(student.getSemester());

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.add(new JLabel("Student ID:")); panel.add(idField);
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Email:")); panel.add(emailField);
        panel.add(new JLabel("Course:")); panel.add(courseField);
        panel.add(new JLabel("Semester:")); panel.add(semesterField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Student", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String course = courseField.getText().trim();
            String semester = semesterField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || course.isEmpty() || semester.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }
            if (!email.contains("@")) {
                JOptionPane.showMessageDialog(this, "Invalid email format!");
                return;
            }

            student.setName(name);
            student.setEmail(email);
            student.setCourse(course);
            student.setSemester(semester);
            
            manager.updateStudent(student);
            loadStudents();
            JOptionPane.showMessageDialog(this, "Student Updated Successfully!");
        }
    }

    private void deleteSelectedStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student first!");
            return;
        }
        String id = (String) tableModel.getValueAt(selectedRow, 0);
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            manager.deleteStudent(id);
            loadStudents();
            JOptionPane.showMessageDialog(this, "Student Deleted Successfully!");
        }
    }
}