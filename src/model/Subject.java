package model;

import java.io.Serializable;

public class Subject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String subjectCode;
    private String subjectName;
    private int credits;
    private double marks;
    private boolean marksEntered;

    /** Creates a subject whose marks have not been entered yet. */
    public Subject(String subjectCode, String subjectName, int credits) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.credits = credits;
        this.marks = 0.0;
        this.marksEntered = false;
    }

    /** Creates a subject with marks already entered. */
    public Subject(String subjectCode, String subjectName, int credits, double marks) {
        this(subjectCode, subjectName, credits);
        setMarks(marks);
    }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public double getMarks() { return marks; }
    public void setMarks(double marks) {
        this.marks = marks;
        this.marksEntered = true;
    }

    /** True once the user has entered marks for this subject. */
    public boolean isMarksEntered() { return marksEntered; }

    /** Valid marks are 0 to 100 (NaN and infinity are rejected). */
    public static boolean isValidMarks(double marks) {
        return !Double.isNaN(marks) && !Double.isInfinite(marks) && marks >= 0 && marks <= 100;
    }

    public String getGrade() {
        if (marks >= 90) return "A+";
        if (marks >= 80) return "A";
        if (marks >= 70) return "B+";
        if (marks >= 60) return "B";
        if (marks >= 50) return "C";
        if (marks >= 40) return "D";
        return "F";
    }

    public int getGradePoint() {
        String grade = getGrade();
        switch (grade) {
            case "A+": return 10;
            case "A": return 9;
            case "B+": return 8;
            case "B": return 7;
            case "C": return 6;
            case "D": return 5;
            default: return 0; // F
        }
    }
}
