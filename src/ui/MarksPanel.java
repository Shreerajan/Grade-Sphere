package ui;

import model.Student;
import model.Subject;
import service.StudentManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MarksPanel extends JPanel {

    private StudentManager manager;
    private JComboBox<String> studentComboBox;
    private JTable table;
    private DefaultTableModel tableModel;
    
    private JLabel totalCreditsLabel;
    private JLabel totalMarksLabel;
    private JLabel percentageLabel;
    private JLabel gpaLabel;
    private JLabel gradeLabel;

    private final Color BACKGROUND_COLOR = new Color(245, 246, 250);

    public MarksPanel(StudentManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        createUI();
    }

    private void createUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BACKGROUND_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("Marks / Grade Entry");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));

        JPanel topControls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topControls.setBackground(BACKGROUND_COLOR);
        topControls.add(new JLabel("Select Student:"));
        
        studentComboBox = new JComboBox<>();
        studentComboBox.addActionListener(e -> loadSubjects());
        topControls.add(studentComboBox);
        
        header.add(title, BorderLayout.WEST);
        header.add(topControls, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 30, 30, 30),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        String[] columns = {"Subject Code", "Subject", "Credits", "Marks", "Grade", "Grade Point"};
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
        
        content.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        statsPanel.setBackground(Color.WHITE);
        
        totalCreditsLabel = new JLabel("Total Credits: 0");
        totalMarksLabel = new JLabel("Total Marks: 0.0");
        percentageLabel = new JLabel("Percentage: 0.0%");
        gpaLabel = new JLabel("GPA: 0.0");
        gradeLabel = new JLabel("Overall Grade: -");
        
        statsPanel.add(totalCreditsLabel);
        statsPanel.add(totalMarksLabel);
        statsPanel.add(percentageLabel);
        statsPanel.add(gpaLabel);
        statsPanel.add(gradeLabel);
        
        bottomPanel.add(statsPanel, BorderLayout.CENTER);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(Color.WHITE);
        
        JButton editMarksButton = new JButton("Enter/Edit Marks");
        editMarksButton.setBackground(new Color(60, 140, 200));
        editMarksButton.setForeground(Color.WHITE);
        editMarksButton.addActionListener(e -> editMarks());
        
        actionPanel.add(editMarksButton);
        bottomPanel.add(actionPanel, BorderLayout.EAST);

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

    /** Reloads the table and the totals for the selected student. */
    private void loadSubjects() {
        tableModel.setRowCount(0);
        Student student = getSelectedStudent();
        if (student == null) {
            totalCreditsLabel.setText("Total Credits: 0");
            totalMarksLabel.setText("Total Marks: 0.00");
            percentageLabel.setText("Percentage: 0.00%");
            gpaLabel.setText("GPA: 0.00");
            gradeLabel.setText("Overall Grade: -");
            return;
        }

        for (Subject s : student.getSubjects()) {
            if (s.isMarksEntered()) {
                tableModel.addRow(new Object[]{
                    s.getSubjectCode(), s.getSubjectName(), s.getCredits(),
                    String.format("%.2f", s.getMarks()), s.getGrade(), s.getGradePoint()
                });
            } else {
                tableModel.addRow(new Object[]{
                    s.getSubjectCode(), s.getSubjectName(), s.getCredits(), "-", "-", "-"
                });
            }
        }

        totalCreditsLabel.setText("Total Credits: " + student.getTotalCredits());
        totalMarksLabel.setText(String.format("Total Marks: %.2f", student.getTotalMarks()));
        percentageLabel.setText(String.format("Percentage: %.2f%%", student.getPercentage()));
        gpaLabel.setText(String.format("GPA: %.2f", student.getGPA()));
        gradeLabel.setText("Overall Grade: " + student.getOverallGrade());
    }

    private Student getSelectedStudent() {
        int index = studentComboBox.getSelectedIndex();
        if (index >= 0 && index < manager.getAllStudents().size()) {
            return manager.getAllStudents().get(index);
        }
        return null;
    }

    private void editMarks() {
        Student student = getSelectedStudent();
        if (student == null) {
            JOptionPane.showMessageDialog(this,
                    manager.getAllStudents().isEmpty()
                            ? "There are no students yet. Please add a student first."
                            : "Please select a student first!",
                    "No Student Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (student.getSubjects().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "This student has no subjects yet. Add subjects in the Subjects page first.",
                    "No Subjects", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a subject in the table to enter marks!",
                    "No Subject Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String code = (String) tableModel.getValueAt(selectedRow, 0);
        Subject subject = null;
        for (Subject s : student.getSubjects()) {
            if (s.getSubjectCode().equals(code)) {
                subject = s;
                break;
            }
        }
        if (subject == null) return;

        String current = subject.isMarksEntered() ? String.valueOf(subject.getMarks()) : "";
        String marksText = JOptionPane.showInputDialog(this,
                "Enter marks for " + subject.getSubjectName() + " (0-100):", current);
        if (marksText == null) {
            return; // cancelled
        }
        marksText = marksText.trim();
        if (marksText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter marks between 0 and 100.",
                    "Marks Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double marks;
        try {
            marks = Double.parseDouble(marksText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Marks must be a number, for example 78 or 82.5.",
                    "Invalid Marks", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!Subject.isValidMarks(marks)) {
            JOptionPane.showMessageDialog(this, "Marks must be between 0 and 100.",
                    "Invalid Marks", JOptionPane.ERROR_MESSAGE);
            return;
        }

        subject.setMarks(marks);
        manager.updateStudent(student);
        loadSubjects();
        JOptionPane.showMessageDialog(this, String.format(
                "Marks saved.\nGrade: %s   Grade Point: %d\nGPA: %.2f   Percentage: %.2f%%",
                subject.getGrade(), subject.getGradePoint(), student.getGPA(), student.getPercentage()));
    }
}
