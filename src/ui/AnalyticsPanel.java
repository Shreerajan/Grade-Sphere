package ui;

import model.Student;
import service.StudentManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AnalyticsPanel extends JPanel {

    private StudentManager manager;

    private final Color BACKGROUND_COLOR =
            new Color(245, 246, 250);

    public AnalyticsPanel(StudentManager manager) {

        this.manager = manager;

        setLayout(new BorderLayout());

        setBackground(BACKGROUND_COLOR);

        createUI();
    }


    // =========================
    // CREATE UI
    // =========================

    private void createUI() {

        // Header

        JPanel header = new JPanel(
                new BorderLayout()
        );

        header.setBackground(BACKGROUND_COLOR);

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        30,
                        20,
                        30
                )
        );


        JLabel title = new JLabel(
                "Performance Analytics"
        );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        30
                )
        );


        JButton refreshButton =
                new JButton("Refresh");

        refreshButton.addActionListener(
                e -> refreshAnalytics()
        );


        header.add(
                title,
                BorderLayout.WEST
        );

        header.add(
                refreshButton,
                BorderLayout.EAST
        );


        add(
                header,
                BorderLayout.NORTH
        );


        // Main Content

        JPanel content = new JPanel();

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        content.setBackground(BACKGROUND_COLOR);

        content.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        30,
                        30,
                        30
                )
        );


        // Statistics Cards
        JPanel statsPanel = new JPanel(new GridLayout(2, 4, 15, 15));
        statsPanel.setBackground(BACKGROUND_COLOR);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        statsPanel.add(createCard("Total Students", String.valueOf(manager.getTotalStudents())));
        statsPanel.add(createCard("Average Percentage", String.format("%.2f%%", manager.getAveragePercentage())));
        statsPanel.add(createCard("Average GPA", String.format("%.2f", manager.getAverageGPA())));
        statsPanel.add(createCard("Pass Rate", String.format("%.2f%%", manager.getPassRate())));
        statsPanel.add(createCard("Highest GPA", String.format("%.2f", manager.getHighestGPA())));
        statsPanel.add(createCard("Lowest GPA", String.format("%.2f", manager.getLowestGPA())));
        statsPanel.add(createCard("Passed", String.valueOf(manager.getPassedStudentsCount())));
        statsPanel.add(createCard("Failed", String.valueOf(manager.getFailedStudentsCount())));
        content.add(statsPanel);

        content.add(
                Box.createVerticalStrut(20)
        );


        // Bottom Section

        JPanel bottomPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                20,
                                20
                        )
                );

        bottomPanel.setBackground(
                BACKGROUND_COLOR
        );

        bottomPanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        340
                )
        );


        bottomPanel.add(
                createTopPerformerPanel()
        );

        bottomPanel.add(
                createGradeDistributionPanel()
        );


        content.add(bottomPanel);


        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }


    // =========================
    // CREATE STAT CARD
    // =========================

    private JPanel createCard(
            String title,
            String value
    ) {

        JPanel card =
                new JPanel(
                        new GridLayout(
                                2,
                                1
                        )
                );

        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                new Color(
                                        220,
                                        220,
                                        220
                                )
                        ),

                        BorderFactory.createEmptyBorder(
                                20,
                                20,
                                20,
                                20
                        )
                )
        );


        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setForeground(Color.GRAY);

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        15
                )
        );


        JLabel valueLabel =
                new JLabel(value);

        valueLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        26
                )
        );


        card.add(titleLabel);

        card.add(valueLabel);

        return card;
    }


    // =========================
    // TOP PERFORMER PANEL
    // =========================

    private JPanel createTopPerformerPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                new Color(
                                        220,
                                        220,
                                        220
                                )
                        ),

                        BorderFactory.createEmptyBorder(
                                20,
                                20,
                                20,
                                20
                        )
                )
        );


        JLabel title =
                new JLabel(
                        "🏆 Top Performer"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        20
                )
        );

        panel.add(
                title,
                BorderLayout.NORTH
        );


        Student topStudent =
                getTopStudent();


        JPanel details =
                new JPanel();

        details.setLayout(
                new BoxLayout(
                        details,
                        BoxLayout.Y_AXIS
                )
        );

        details.setBackground(Color.WHITE);


        if (topStudent != null) {

            JLabel name =
                    new JLabel(
                            topStudent.getName()
                    );

            name.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            24
                    )
            );


            JLabel average =
                    new JLabel(
                            "Percentage: "
                                    + String.format(
                                    "%.2f%%",
                                    topStudent.getPercentage()
                            )
                    );


            JLabel grade =
                    new JLabel(
                            "Grade: "
                                    + topStudent.getOverallGrade()
                    );


            average.setFont(
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            16
                    )
            );

            grade.setFont(
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            16
                    )
            );


            details.add(
                    Box.createVerticalStrut(30)
            );

            details.add(name);

            details.add(
                    Box.createVerticalStrut(10)
            );

            details.add(average);

            details.add(
                    Box.createVerticalStrut(10)
            );

            details.add(grade);

        } else {

            JLabel empty =
                    new JLabel(
                            "No student data available."
                    );

            details.add(empty);
        }


        panel.add(
                details,
                BorderLayout.CENTER
        );


        return panel;
    }


    // =========================
    // GRADE DISTRIBUTION
    // =========================

    private JPanel createGradeDistributionPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                new Color(
                                        220,
                                        220,
                                        220
                                )
                        ),

                        BorderFactory.createEmptyBorder(
                                20,
                                20,
                                20,
                                20
                        )
                )
        );


        JLabel title =
                new JLabel(
                        "🎓 Grade Distribution"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        20
                )
        );

        panel.add(
                title,
                BorderLayout.NORTH
        );


        JPanel grades =
                new JPanel(
                        new GridLayout(
                                7,
                                2,
                                10,
                                10
                        )
                );

        grades.setBackground(Color.WHITE);


        String[] gradeNames = {
                "A+",
                "A",
                "B+",
                "B",
                "C",
                "D",
                "F"
        };


        for (String grade : gradeNames) {

            grades.add(
                    new JLabel(
                            "Grade " + grade
                    )
            );

            grades.add(
                    new JLabel(
                            String.valueOf(
                                    getGradeCount(
                                            grade
                                    )
                            )
                            + " Students"
                    )
            );
        }


        panel.add(
                grades,
                BorderLayout.CENTER
        );


        return panel;
    }


    // =========================
    // HELPERS (all data comes from StudentManager)
    // =========================

    private Student getTopStudent() {
        return manager.getTopPerformingStudent();
    }

    private int getGradeCount(String grade) {
        return manager.getGradeCount(grade);
    }

    // =========================
    // REFRESH ANALYTICS
    // =========================

    public void refreshAnalytics() {

        removeAll();

        createUI();

        revalidate();

        repaint();
    }
}