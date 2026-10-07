package ui;

import model.Student;
import model.Subject;
import service.StudentManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ReportsPanel extends JPanel {

    private StudentManager manager;

    private JTextField idField;
    private JTextArea reportArea;

    public ReportsPanel(StudentManager manager) {

        this.manager = manager;

        setLayout(new BorderLayout());

        setBackground(new Color(245, 246, 250));

        createUI();
    }


    // =========================
    // CREATE UI
    // =========================

    private void createUI() {

        // HEADER

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(new Color(245, 246, 250));

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        30,
                        20,
                        30
                )
        );

        JLabel title =
                new JLabel("Student Reports");

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        30
                )
        );

        header.add(title, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);


        // MAIN PANEL

        JPanel mainPanel =
                new JPanel(new BorderLayout(10, 10));

        mainPanel.setBackground(
                new Color(245, 246, 250)
        );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        30,
                        30,
                        30
                )
        );


        // SEARCH PANEL

        JPanel searchPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        searchPanel.setBackground(Color.WHITE);

        searchPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );


        searchPanel.add(
                new JLabel("Student ID / Name:")
        );


        idField =
                new JTextField(10);

        searchPanel.add(idField);


        JButton searchButton =
                new JButton("Generate Student Report");

        JButton classReportButton =
                new JButton("Generate Class Report");


        searchPanel.add(searchButton);

        searchPanel.add(classReportButton);


        mainPanel.add(
                searchPanel,
                BorderLayout.NORTH
        );


        // REPORT AREA

        reportArea =
                new JTextArea();

        reportArea.setEditable(false);

        reportArea.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        15
                )
        );

        reportArea.setMargin(
                new Insets(
                        20,
                        20,
                        20,
                        20
                )
        );


        JScrollPane scrollPane =
                new JScrollPane(reportArea);


        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );


        // BOTTOM BUTTONS

        JPanel bottomPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        bottomPanel.setBackground(
                new Color(245, 246, 250)
        );


        JButton clearButton =
                new JButton("Clear");

        JButton saveButton =
                new JButton("Save Report");


        bottomPanel.add(clearButton);

        bottomPanel.add(saveButton);


        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );


        add(
                mainPanel,
                BorderLayout.CENTER
        );


        // =========================
        // BUTTON EVENTS
        // =========================

        searchButton.addActionListener(
                e -> searchStudent()
        );

        classReportButton.addActionListener(
                e -> generateClassReport()
        );

        clearButton.addActionListener(
                e -> reportArea.setText("")
        );

        saveButton.addActionListener(
                e -> saveReport()
        );
    }


    // =========================
    // SEARCH STUDENT
    // =========================

    private void searchStudent() {
        String query = idField.getText().trim();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Student ID or name.",
                    "Student Report", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = manager.findStudent(query);
        if (student == null) {
            java.util.List<Student> matches = manager.searchStudents(query);
            if (matches.size() == 1) {
                student = matches.get(0);
            } else if (matches.size() > 1) {
                StringBuilder list = new StringBuilder("Several students match. Enter the exact Student ID:\n\n");
                for (Student m : matches) {
                    list.append(m.getId()).append("  -  ").append(m.getName()).append("\n");
                }
                reportArea.setText(list.toString());
                return;
            }
        }
        if (student == null) {
            reportArea.setText("");
            JOptionPane.showMessageDialog(this, "No student found for \"" + query + "\".",
                    "Student Report", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        reportArea.setText(buildStudentReport(student));
        reportArea.setCaretPosition(0);
    }

    private String buildStudentReport(Student student) {
        String line = "==========================================================\n";
        StringBuilder r = new StringBuilder();
        r.append(line);
        r.append("                STUDENT PERFORMANCE REPORT\n");
        r.append(line).append("\n");
        r.append("Student ID : ").append(student.getId()).append("\n");
        r.append("Name       : ").append(student.getName()).append("\n");
        r.append("Course     : ").append(student.getCourse()).append("\n");
        r.append("Semester   : ").append(student.getSemester()).append("\n\n");

        r.append("---------------------- SUBJECTS ----------------------\n");
        if (student.getSubjects().isEmpty()) {
            r.append("No subjects have been added for this student.\n");
        } else {
            r.append(String.format("%-10s %-20s %-7s %-7s %-6s %s%n",
                    "Code", "Subject", "Credits", "Marks", "Grade", "Points"));
            for (Subject s : student.getSubjects()) {
                String name = s.getSubjectName();
                if (name.length() > 20) name = name.substring(0, 19) + ".";
                if (s.isMarksEntered()) {
                    r.append(String.format("%-10s %-20s %-7d %-7.2f %-6s %d%n",
                            s.getSubjectCode(), name, s.getCredits(), s.getMarks(),
                            s.getGrade(), s.getGradePoint()));
                } else {
                    r.append(String.format("%-10s %-20s %-7d %-7s %-6s %s%n",
                            s.getSubjectCode(), name, s.getCredits(), "-", "-", "-"));
                }
            }
        }

        r.append("\n---------------------- SUMMARY -----------------------\n");
        r.append("Total Credits : ").append(student.getTotalCredits()).append("\n");
        if (student.hasMarks()) {
            r.append(String.format("Percentage    : %.2f%%%n", student.getPercentage()));
            r.append(String.format("GPA           : %.2f%n", student.getGPA()));
            r.append("Overall Grade : ").append(student.getOverallGrade()).append("\n");
            r.append("Result        : ").append(student.hasPassed() ? "PASS" : "FAIL").append("\n");
        } else {
            r.append("No marks have been entered yet.\n");
        }
        r.append("\n").append(line);
        return r.toString();
    }

    // =========================
    // GENERATE CLASS REPORT
    // =========================

    private void generateClassReport() {
        if (manager.getAllStudents().isEmpty()) {
            JOptionPane.showMessageDialog(this, "There are no students to report on yet.",
                    "Class Report", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String line = "========================================================================\n";
        StringBuilder r = new StringBuilder();
        r.append(line);
        r.append("                        GRADESPHERE CLASS REPORT\n");
        r.append(line).append("\n");
        r.append("Total Students    : ").append(manager.getTotalStudents()).append("\n");
        r.append("Students w/ Marks : ").append(manager.getStudentsWithMarksCount()).append("\n");
        r.append(String.format("Average Percentage: %.2f%%%n", manager.getAveragePercentage()));
        r.append(String.format("Average GPA       : %.2f%n", manager.getAverageGPA()));
        r.append(String.format("Highest GPA       : %.2f%n", manager.getHighestGPA()));
        r.append(String.format("Lowest GPA        : %.2f%n", manager.getLowestGPA()));
        r.append(String.format("Passed / Failed   : %d / %d%n",
                manager.getPassedStudentsCount(), manager.getFailedStudentsCount()));
        r.append(String.format("Pass Rate         : %.2f%%%n", manager.getPassRate()));
        r.append("\n------------------------- STUDENT LIST -------------------------\n");
        r.append(String.format("%-10s %-20s %-12s %-10s %-6s %s%n",
                "ID", "Name", "Percentage", "GPA", "Grade", "Result"));
        for (Student s : manager.getAllStudents()) {
            String name = s.getName();
            if (name.length() > 20) name = name.substring(0, 19) + ".";
            if (s.hasMarks()) {
                r.append(String.format("%-10s %-20s %-12s %-10.2f %-6s %s%n",
                        s.getId(), name, String.format("%.2f%%", s.getPercentage()), s.getGPA(),
                        s.getOverallGrade(), s.hasPassed() ? "PASS" : "FAIL"));
            } else {
                r.append(String.format("%-10s %-20s %-12s %-10s %-6s %s%n",
                        s.getId(), name, "-", "-", "N/A", "-"));
            }
        }
        r.append("\n").append(line);
        reportArea.setText(r.toString());
        reportArea.setCaretPosition(0);
    }

    // =========================
    // SAVE REPORT
    // =========================

    private void saveReport() {

        if (reportArea.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please generate a report first!"
            );

            return;
        }


        JFileChooser fileChooser =
                new JFileChooser();

        fileChooser.setDialogTitle(
                "Save GradeSphere Report"
        );


        int result =
                fileChooser.showSaveDialog(
                        this
                );


        if (result ==
                JFileChooser.APPROVE_OPTION) {

            File file =
                    fileChooser.getSelectedFile();


            // Add .txt extension if needed

            if (!file.getName()
                    .toLowerCase()
                    .endsWith(".txt")) {

                file =
                        new File(
                                file.getAbsolutePath()
                                        + ".txt"
                        );
            }


            try (FileWriter writer =
                         new FileWriter(file)) {

                writer.write(
                        reportArea.getText()
                );


                JOptionPane.showMessageDialog(
                        this,
                        "Report saved successfully!"
                );

            } catch (IOException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error saving report!"
                );
            }
        }
    }
}