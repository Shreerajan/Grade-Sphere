package ui;

import service.StudentManager;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {

    private final StudentManager manager;
    private final Runnable onDataChanged;
    private JLabel studentsInfo;
    private JLabel subjectsInfo;
    private JLabel fileInfo;

    private final Color BACKGROUND_COLOR = new Color(245, 246, 250);

    public SettingsPanel(StudentManager manager, Runnable onDataChanged) {
        this.manager = manager;
        this.onDataChanged = onDataChanged;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        createUI();
        refreshInfo();
    }

    private void createUI() {
        JLabel title = new JLabel("Settings");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setBorder(BorderFactory.createEmptyBorder(25, 30, 20, 30));
        add(title, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BACKGROUND_COLOR);
        content.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));

        // Application information
        JPanel about = createSection("Application Information");
        about.add(infoRow("Application", "GradeSphere - Smart Student Grade Tracker"));
        about.add(infoRow("Version", "1.0"));
        about.add(infoRow("Grading scale", "A+ = 10, A = 9, B+ = 8, B = 7, C = 6, D = 5, F = 0"));
        about.add(infoRow("GPA formula", "Sum(Grade Point x Credit) / Sum(Credit)"));
        studentsInfo = new JLabel();
        subjectsInfo = new JLabel();
        fileInfo = new JLabel();
        about.add(infoRow("Students stored", studentsInfo));
        about.add(infoRow("Subjects stored", subjectsInfo));
        about.add(infoRow("Data file", fileInfo));
        content.add(about);
        content.add(Box.createVerticalStrut(20));

        // Data management
        JPanel data = createSection("Data Management");
        JLabel warning = new JLabel("Resetting permanently deletes every student, subject and mark.");
        warning.setFont(new Font("SansSerif", Font.PLAIN, 14));
        warning.setAlignmentX(Component.LEFT_ALIGNMENT);
        data.add(warning);
        data.add(Box.createVerticalStrut(12));

        JButton resetButton = new JButton("Reset All Data");
        resetButton.setBackground(new Color(220, 70, 70));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFocusPainted(false);
        resetButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        resetButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        resetButton.addActionListener(e -> resetAllData());
        data.add(resetButton);
        content.add(data);

        add(content, BorderLayout.CENTER);
    }

    private JPanel createSection(String heading) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(heading);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(label);
        section.add(Box.createVerticalStrut(12));
        return section;
    }

    private JPanel infoRow(String name, String value) {
        return infoRow(name, new JLabel(value));
    }

    private JPanel infoRow(String name, JLabel valueLabel) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 3));
        row.setBackground(Color.WHITE);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel nameLabel = new JLabel(name + ":");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        nameLabel.setPreferredSize(new Dimension(150, 22));
        valueLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        row.add(nameLabel);
        row.add(valueLabel);
        return row;
    }

    /** Updates the counts shown in the information section. */
    public void refreshInfo() {
        studentsInfo.setText(String.valueOf(manager.getTotalStudents()));
        subjectsInfo.setText(String.valueOf(manager.getTotalSubjects()));
        fileInfo.setText(manager.getDataFilePath());
    }

    private void resetAllData() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete all GradeSphere data?",
                "Confirm Reset", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
        manager.clearAllData();
        refreshInfo();
        if (onDataChanged != null) {
            onDataChanged.run();
        }
        JOptionPane.showMessageDialog(this, "All GradeSphere data has been deleted.");
    }
}
