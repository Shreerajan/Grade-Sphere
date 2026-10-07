package service;

import model.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StudentManager {

    private ArrayList<Student> students;
    private static final String DATA_FILE = "students_data.dat";
    private String loadError = null;

    public StudentManager() {
        students = new ArrayList<>();
        loadData();
    }

    public void addStudent(Student student) {
        students.add(student);
        saveData();
    }
    
    public void updateStudent(Student student) {
        // Since we are mutating the student object directly via references in the UI,
        // we mostly just need to save data when an update occurs.
        saveData();
    }

    public ArrayList<Student> getAllStudents() {
        return students;
    }

    public Student findStudent(String id) {
        if (id == null) return null;
        for (Student student : students) {
            if (student.getId().equalsIgnoreCase(id)) {
                return student;
            }
        }
        return null;
    }
    
    public List<Student> searchStudents(String query) {
        List<Student> results = new ArrayList<>();
        if (query == null) return results;
        String q = query.trim().toLowerCase();
        for (Student s : students) {
            if (s.getId().toLowerCase().contains(q) ||
                s.getName().toLowerCase().contains(q)) {
                results.add(s);
            }
        }
        return results;
    }

    public boolean deleteStudent(String id) {
        boolean removed = students.removeIf(student -> student.getId().equalsIgnoreCase(id));
        if (removed) {
            saveData();
        }
        return removed;
    }

    public int getTotalStudents() {
        return students.size();
    }
    
    public int getTotalSubjects() {
        return students.stream().mapToInt(s -> s.getSubjects().size()).sum();
    }

    /** Students who have at least one subject with marks. Statistics use only these. */
    private List<Student> getStudentsWithMarks() {
        List<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (s.hasMarks()) result.add(s);
        }
        return result;
    }

    public int getStudentsWithMarksCount() {
        return getStudentsWithMarks().size();
    }

    public double getAveragePercentage() {
        List<Student> graded = getStudentsWithMarks();
        if (graded.isEmpty()) return 0.0;
        double total = 0;
        for (Student s : graded) total += s.getPercentage();
        return total / graded.size();
    }

    public double getAverageGPA() {
        List<Student> graded = getStudentsWithMarks();
        if (graded.isEmpty()) return 0.0;
        double total = 0;
        for (Student s : graded) total += s.getGPA();
        return total / graded.size();
    }

    public double getHighestGPA() {
        List<Student> graded = getStudentsWithMarks();
        if (graded.isEmpty()) return 0.0;
        double highest = graded.get(0).getGPA();
        for (Student s : graded) {
            if (s.getGPA() > highest) highest = s.getGPA();
        }
        return highest;
    }

    public double getLowestGPA() {
        List<Student> graded = getStudentsWithMarks();
        if (graded.isEmpty()) return 0.0;
        double lowest = graded.get(0).getGPA();
        for (Student s : graded) {
            if (s.getGPA() < lowest) lowest = s.getGPA();
        }
        return lowest;
    }

    /** Number of students (with marks) who passed. */
    public int getPassedStudentsCount() {
        int count = 0;
        for (Student s : getStudentsWithMarks()) {
            if (s.hasPassed()) count++;
        }
        return count;
    }

    /** Number of students (with marks) who failed. */
    public int getFailedStudentsCount() {
        return getStudentsWithMarks().size() - getPassedStudentsCount();
    }

    /** Pass rate in percent, based on students who have marks. */
    public double getPassRate() {
        int graded = getStudentsWithMarks().size();
        if (graded == 0) return 0.0;
        return getPassedStudentsCount() * 100.0 / graded;
    }

    /** Number of students whose overall grade equals the given grade. */
    public int getGradeCount(String grade) {
        int count = 0;
        for (Student s : students) {
            if (s.hasMarks() && s.getOverallGrade().equals(grade)) count++;
        }
        return count;
    }

    public Student getTopPerformingStudent() {
        Student top = null;
        for (Student s : getStudentsWithMarks()) {
            if (top == null || s.getGPA() > top.getGPA()
                    || (s.getGPA() == top.getGPA() && s.getPercentage() > top.getPercentage())) {
                top = s;
            }
        }
        return top;
    }

    public List<Student> getRecentStudents() {
        int count = Math.min(5, students.size());
        List<Student> recent = new ArrayList<>();
        for (int i = students.size() - 1; i >= students.size() - count; i--) {
            recent.add(students.get(i));
        }
        return recent;
    }
    
    public void clearAllData() {
        students.clear();
        saveData();
    }

    public String getDataFilePath() {
        return new File(DATA_FILE).getAbsolutePath();
    }

    /** Loads students from disk. A missing, empty or corrupted file never crashes the app. */
    @SuppressWarnings("unchecked")
    private void loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists() || file.length() == 0) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object data = ois.readObject();
            if (data instanceof ArrayList) {
                for (Object item : (ArrayList<Object>) data) {
                    if (item instanceof Student) {
                        Student student = (Student) item;
                        if (student.getSubjects() == null) {
                            student.setSubjects(new ArrayList<>());
                        }
                        students.add(student);
                    }
                }
            }
        } catch (Exception e) {
            // Keep the unreadable file as a backup so it is not silently overwritten.
            students.clear();
            File backup = new File(DATA_FILE + ".bak");
            backup.delete();
            file.renameTo(backup);
            loadError = "Saved data could not be read and was moved to " + backup.getName()
                    + ". GradeSphere started with an empty student list.";
        }
    }

    /** Returns a message if the last load failed, otherwise null (and clears it). */
    public String consumeLoadError() {
        String message = loadError;
        loadError = null;
        return message;
    }

    /** Saves to a temp file first, then replaces the real file, so a crash cannot corrupt it. */
    public boolean saveData() {
        File temp = new File(DATA_FILE + ".tmp");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(temp))) {
            oos.writeObject(students);
        } catch (Exception e) {
            return false;
        }
        File target = new File(DATA_FILE);
        try {
            java.nio.file.Files.move(temp.toPath(), target.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
