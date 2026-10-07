package ui;

import service.StudentManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final java.util.Map<String, JButton> navButtons = new java.util.HashMap<>();
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private StudentManager manager;

    private DashboardPanel dashboardPanel;
    private StudentsPanel studentsPanel;
    private SubjectsPanel subjectsPanel;
    private MarksPanel marksPanel;
    private SettingsPanel settingsPanel;
    private AnalyticsPanel analyticsPanel;
    private ReportsPanel reportsPanel;

    private final Color SIDEBAR_COLOR =
            new Color(25, 32, 55);

    public MainFrame() {

        manager = new StudentManager();

        setTitle("GradeSphere - Smart Student Grade Tracker");

        setSize(1400, 800);

        setMinimumSize(
                new Dimension(1000, 650)
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Create all panels
        dashboardPanel = new DashboardPanel(manager);
        studentsPanel = new StudentsPanel(manager);
        subjectsPanel = new SubjectsPanel(manager);
        marksPanel = new MarksPanel(manager);
        analyticsPanel = new AnalyticsPanel(manager);
        reportsPanel = new ReportsPanel(manager);
        settingsPanel = new SettingsPanel(manager, this::refreshPanels);
        dashboardPanel.setNavigator(this::showPage);

        // Sidebar
        add(createSidebar(), BorderLayout.WEST);

        // Main content
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(dashboardPanel, "Dashboard");
        mainPanel.add(studentsPanel, "Students");
        mainPanel.add(subjectsPanel, "Subjects");
        mainPanel.add(marksPanel, "Marks");
        mainPanel.add(reportsPanel, "Reports");
        mainPanel.add(analyticsPanel, "Analytics");
        mainPanel.add(settingsPanel, "Settings");

        add(mainPanel, BorderLayout.CENTER);

        showPage("Dashboard");

        String loadError = manager.consumeLoadError();
        if (loadError != null) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, loadError,
                    "Data Warning", JOptionPane.WARNING_MESSAGE));
        }
    }

    private JPanel createSidebar() {

        JPanel sidebar = new JPanel(new BorderLayout());

        sidebar.setBackground(SIDEBAR_COLOR);

        sidebar.setPreferredSize(
                new Dimension(230, 0)
        );

        JPanel topPanel = new JPanel();

        topPanel.setBackground(SIDEBAR_COLOR);

        topPanel.setLayout(
                new BoxLayout(
                        topPanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel logo =
                new JLabel("🎓  GradeSphere");

        logo.setForeground(Color.WHITE);

        logo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        22
                )
        );

        logo.setBorder(
                BorderFactory.createEmptyBorder(
                        30, 20, 30, 10
                )
        );

        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        topPanel.add(logo);

        topPanel.add(createNavButton("🏠  Dashboard", "Dashboard"));
        topPanel.add(createNavButton("👨‍🎓  Students", "Students"));
        topPanel.add(createNavButton("📚  Subjects", "Subjects"));
        topPanel.add(createNavButton("📝  Marks / Grade Entry", "Marks"));
        topPanel.add(createNavButton("📄  Reports", "Reports"));
        topPanel.add(createNavButton("📊  Analytics", "Analytics"));
        topPanel.add(createNavButton("⚙  Settings", "Settings"));

        sidebar.add(topPanel, BorderLayout.NORTH);

        JLabel user =
                new JLabel("👤  Admin User");

        user.setForeground(Color.WHITE);

        user.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        sidebar.add(user, BorderLayout.SOUTH);

        return sidebar;
    }

    private JButton createNavButton(
            String text,
            String page
    ) {

        JButton button = new JButton(text);

        button.setMaximumSize(
                new Dimension(230, 50)
        );

        button.setPreferredSize(
                new Dimension(230, 50)
        );

        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.setForeground(Color.WHITE);

        button.setBackground(SIDEBAR_COLOR);

        button.setBorderPainted(false);

        button.setFocusPainted(false);

        button.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        navButtons.put(page, button);

        button.addActionListener(e -> showPage(page));

        return button;
    }

    /** Reloads every panel from the current data so all pages stay in sync. */
    private void refreshPanels() {
        studentsPanel.loadStudents();
        subjectsPanel.loadStudents();
        marksPanel.loadStudents();
        analyticsPanel.refreshAnalytics();
        dashboardPanel.refreshDashboard();
        settingsPanel.refreshInfo();
    }

    public void showPage(String page) {
        refreshPanels();
        cardLayout.show(mainPanel, page);
        for (java.util.Map.Entry<String, JButton> entry : navButtons.entrySet()) {
            entry.getValue().setBackground(
                    entry.getKey().equals(page) ? new Color(60, 75, 120) : SIDEBAR_COLOR);
        }
    }
}
