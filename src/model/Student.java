package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String email;
    private String course;
    private String semester;
    private List<Subject> subjects;

    public Student(String id, String name, String email, String course, String semester) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.course = course;
        this.semester = semester;
        this.subjects = new ArrayList<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public List<Subject> getSubjects() { return subjects; }
    public void setSubjects(List<Subject> subjects) { this.subjects = subjects; }
    
    public void addSubject(Subject subject) {
        this.subjects.add(subject);
    }

    /** Total credits of all registered subjects. */
    public int getTotalCredits() {
        return subjects.stream().mapToInt(Subject::getCredits).sum();
    }

    /** Subjects that already have marks. Subjects without marks are ignored in calculations. */
    private List<Subject> getGradedSubjects() {
        List<Subject> graded = new ArrayList<>();
        for (Subject s : subjects) {
            if (s.isMarksEntered()) graded.add(s);
        }
        return graded;
    }

    /** True if at least one subject has marks. */
    public boolean hasMarks() {
        return !getGradedSubjects().isEmpty();
    }

    public double getTotalMarks() {
        double total = 0;
        for (Subject s : getGradedSubjects()) total += s.getMarks();
        return total;
    }

    /** Percentage = total marks / (number of graded subjects), since each paper is out of 100. */
    public double getPercentage() {
        List<Subject> graded = getGradedSubjects();
        if (graded.isEmpty()) return 0.0;
        return getTotalMarks() / graded.size();
    }

    /** GPA = sum(grade point x credit) / sum(credit), over subjects with marks. */
    public double getGPA() {
        int creditSum = 0;
        double points = 0;
        for (Subject s : getGradedSubjects()) {
            points += s.getGradePoint() * s.getCredits();
            creditSum += s.getCredits();
        }
        if (creditSum == 0) return 0.0;
        return points / creditSum;
    }

    public String getOverallGrade() {
        if (!hasMarks()) return "N/A";
        double percentage = getPercentage();
        if (percentage >= 90) return "A+";
        if (percentage >= 80) return "A";
        if (percentage >= 70) return "B+";
        if (percentage >= 60) return "B";
        if (percentage >= 50) return "C";
        if (percentage >= 40) return "D";
        return "F";
    }

    /** A student passes if no subject is F and the percentage is at least 40. */
    public boolean hasPassed() {
        if (!hasMarks()) return false;
        for (Subject s : getGradedSubjects()) {
            if ("F".equals(s.getGrade())) return false;
        }
        return getPercentage() >= 40;
    }
}
