package ui;

import service.StudentManager;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    private StudentManager manager;
    private java.util.function.Consumer<String> navigator;

    private final Color BACKGROUND_COLOR =
            new Color(225, 245, 255);

    private final Color SKY_BLUE =
            new Color(135, 206, 235);

    private final Color GREEN =
            new Color(144, 238, 144);

    private final Color PINK =
            new Color(255, 182, 193);

    private final Color DARK_TEXT =
            new Color(40, 55, 70);


    public DashboardPanel(StudentManager manager) {

        this.manager = manager;

        setLayout(new BorderLayout());

        setBackground(BACKGROUND_COLOR);

        createUI();
    }


    // =========================
    // CREATE DASHBOARD
    // =========================

    private void createUI() {

        removeAll();


        // =========================
        // HEADER
        // =========================

        JPanel header = new JPanel(
                new BorderLayout()
        );

        header.setBackground(BACKGROUND_COLOR);

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        25, 35, 20, 35
                )
        );


        JPanel titlePanel = new JPanel();

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        titlePanel.setBackground(
                BACKGROUND_COLOR
        );


        JLabel title =
                new JLabel("Good Morning, Admin 👋");

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        30
                )
        );

        title.setForeground(DARK_TEXT);


        JLabel subtitle =
                new JLabel(
                        "Here is your student performance overview"
                );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        15
                )
        );

        subtitle.setForeground(
                new Color(100, 120, 135)
        );


        titlePanel.add(title);

        titlePanel.add(
                Box.createVerticalStrut(6)
        );

        titlePanel.add(subtitle);


        header.add(
                titlePanel,
                BorderLayout.WEST
        );


        JButton refreshButton =
                new JButton("↻ Refresh");

        refreshButton.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        refreshButton.addActionListener(
                e -> refreshDashboard()
        );


        header.add(
                refreshButton,
                BorderLayout.EAST
        );


        add(header, BorderLayout.NORTH);


        // =========================
        // MAIN CONTENT
        // =========================

        JPanel content = new JPanel();

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        content.setBackground(
                BACKGROUND_COLOR
        );

        content.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 35, 30, 35
                )
        );


        // =========================
        // STATISTICS CARDS
        // =========================

        JPanel cardsPanel =
                new JPanel(
                        new GridLayout(
                                1, 5, 20, 20
                        )
                );

        cardsPanel.setBackground(
                BACKGROUND_COLOR
        );

        cardsPanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        150
                )
        );


        cardsPanel.add(
                createStatCard(
                        "👨‍🎓",
                        "Total Students",
                        String.valueOf(
                                manager.getTotalStudents()
                        ),
                        SKY_BLUE
                )
        );


        cardsPanel.add(
                createStatCard(
                        "📚",
                        "Total Subjects",
                        String.valueOf(
                                manager.getTotalSubjects()
                        ),
                        GREEN
                )
        );


        cardsPanel.add(
                createStatCard(
                        "📈",
                        "Average %",
                        String.format(
                                "%.2f%%",
                                manager.getAveragePercentage()
                        ),
                        PINK
                )
        );


        cardsPanel.add(
                createStatCard(
                        "⭐",
                        "Average GPA",
                        String.format(
                                "%.2f",
                                manager.getAverageGPA()
                        ),
                        new Color(255, 220, 160)
                )
        );


        cardsPanel.add(
                createStatCard(
                        "✅",
                        "Pass Rate",
                        String.format(
                                "%.2f%%",
                                manager.getPassRate()
                        ),
                        new Color(180, 220, 255)
                )
        );

        content.add(cardsPanel);

        content.add(
                Box.createVerticalStrut(25)
        );


        // =========================
        // PERFORMANCE OVERVIEW
        // =========================

        content.add(
                createPerformancePanel()
        );

        content.add(
                Box.createVerticalStrut(20)
        );


        // =========================
        // QUICK ACTIONS
        // =========================

        content.add(
                createQuickActions()
        );


        add(content, BorderLayout.CENTER);

        revalidate();

        repaint();
    }


    // =========================
    // STAT CARD
    // =========================

    private JPanel createStatCard(
            String icon,
            String title,
            String value,
            Color color
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(color);

        card.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                new Color(
                                        210, 220, 225
                                )
                        ),

                        BorderFactory.createEmptyBorder(
                                20, 20, 20, 20
                        )
                )
        );


        JLabel iconLabel =
                new JLabel(icon);

        iconLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        30
                )
        );


        JPanel textPanel =
                new JPanel();

        textPanel.setLayout(
                new BoxLayout(
                        textPanel,
                        BoxLayout.Y_AXIS
                )
        );

        textPanel.setBackground(color);


        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        titleLabel.setForeground(
                new Color(70, 80, 90)
        );


        JLabel valueLabel =
                new JLabel(value);

        valueLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        28
                )
        );

        valueLabel.setForeground(
                DARK_TEXT
        );


        textPanel.add(titleLabel);

        textPanel.add(
                Box.createVerticalStrut(8)
        );

        textPanel.add(valueLabel);


        card.add(
                textPanel,
                BorderLayout.CENTER
        );

        card.add(
                iconLabel,
                BorderLayout.EAST
        );


        return card;
    }


    // =========================
    // PERFORMANCE PANEL
    // =========================

    private JPanel createPerformancePanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(Color.WHITE);

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        230
                )
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                new Color(
                                        210, 225, 235
                                )
                        ),

                        BorderFactory.createEmptyBorder(
                                25, 25, 25, 25
                        )
                )
        );


        JLabel title =
                new JLabel(
                        "📊 Performance Overview"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        21
                )
        );

        title.setForeground(
                DARK_TEXT
        );


        panel.add(
                title,
                BorderLayout.NORTH
        );


        JPanel stats =
                new JPanel(
                        new GridLayout(
                                2, 2, 20, 20
                        )
                );

        stats.setBackground(
                Color.WHITE
        );

        stats.setBorder(
                BorderFactory.createEmptyBorder(
                        25, 0, 0, 0
                )
        );

        model.Student topStudent = manager.getTopPerformingStudent();
        String topStudentStr = topStudent != null ? topStudent.getName() + " (" + String.format("%.2f", topStudent.getGPA()) + ")" : "N/A";

        stats.add(
                createInfoBox(
                        "Top Performing Student",
                        topStudentStr
                )
        );

        stats.add(
                createInfoBox(
                        "Passed Students",
                        String.valueOf(manager.getPassedStudentsCount())
                )
        );

        stats.add(
                createInfoBox(
                        "Failed Students",
                        String.valueOf(manager.getFailedStudentsCount())
                )
        );

        stats.add(
                createInfoBox(
                        "Pass Rate",
                        String.format("%.2f%%", manager.getPassRate())
                )
        );


        panel.add(
                stats,
                BorderLayout.CENTER
        );


        return panel;
    }


    // =========================
    // INFO BOX
    // =========================

    private JPanel createInfoBox(
            String title,
            String value
    ) {

        JPanel box =
                new JPanel(
                        new BorderLayout()
                );

        box.setBackground(
                new Color(245, 252, 255)
        );

        box.setBorder(
                BorderFactory.createEmptyBorder(
                        12, 15, 12, 15
                )
        );


        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setForeground(
                Color.GRAY
        );


        JLabel valueLabel =
                new JLabel(value);

        valueLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        20
                )
        );

        valueLabel.setForeground(
                DARK_TEXT
        );


        box.add(
                titleLabel,
                BorderLayout.WEST
        );

        box.add(
                valueLabel,
                BorderLayout.EAST
        );


        return box;
    }


    // =========================
    // QUICK ACTIONS
    // =========================

    private JPanel createQuickActions() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                new Color(
                                        210, 225, 235
                                )
                        ),

                        BorderFactory.createEmptyBorder(
                                20, 25, 20, 25
                        )
                )
        );


        JLabel title =
                new JLabel(
                        "⚡ Quick Actions"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        19
                )
        );

        title.setForeground(
                DARK_TEXT
        );


        panel.add(
                title,
                BorderLayout.NORTH
        );


        JPanel buttons =
                new JPanel(
                        new GridLayout(
                                1, 3, 15, 15
                        )
                );

        buttons.setBackground(
                Color.WHITE
        );

        buttons.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 0, 0, 0
                )
        );


        JButton addStudent =
                new JButton(
                        "+ Add Student"
                );

        JButton updateGrades =
                new JButton(
                        "📝 Enter Marks"
                );

        JButton viewReports =
                new JButton(
                        "📄 View Reports"
                );


        addStudent.addActionListener(e -> goTo("Students"));
        updateGrades.addActionListener(e -> goTo("Marks"));
        viewReports.addActionListener(e -> goTo("Reports"));
        buttons.add(addStudent);

        buttons.add(updateGrades);

        buttons.add(viewReports);


        panel.add(
                buttons,
                BorderLayout.CENTER
        );


        return panel;
    }


    // =========================
    // REFRESH DASHBOARD
    // =========================

    /** Lets the quick-action buttons switch pages in MainFrame. */
    public void setNavigator(java.util.function.Consumer<String> navigator) {
        this.navigator = navigator;
    }

    private void goTo(String page) {
        if (navigator != null) navigator.accept(page);
    }

    public void refreshDashboard() {

        createUI();

        revalidate();

        repaint();
    }
}