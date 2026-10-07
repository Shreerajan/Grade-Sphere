🎓 GradeSphere
GradeSphere is a Java Swing-based Student Grade Management System designed to simplify student academic record management, marks calculation, GPA tracking, reporting, and performance analytics.
✨ Features
- 👨‍🎓 Student Management — Add, edit, delete, search, and manage student records.
- 📚 Subject Management — Add/remove subjects with subject code, name, and credits.
- 📝 Marks Management — Enter and update marks with validation from 0–100.
- 🏆 Automatic Grading — Automatically calculates grades and grade points.
- 📊 GPA & Percentage — Calculates GPA using credit-weighted grade points and percentage from entered marks.
- 📈 Analytics Dashboard — View total students, average GPA, average percentage, pass rate, highest/lowest GPA, passed/failed students, grade distribution, and top performer.
- 📄 Reports — Generate individual student reports and class reports and save reports as .txt files.
- ⚙️ Settings — View application information, grading scale, stored data information, and reset application data with confirmation.
- 💾 Data Persistence — Student data is stored locally and remains available after restarting the application.
📊 Grading System
Marks	Grade	Grade Point
90–100	A+	10
80–89	A	9
70–79	B+	8
60–69	B	7
50–59	C	6
40–49	D	5
Below 40	F	0


GPA Formula:
GPA = Σ(Grade Point × Credit) / Σ(Credit)

Subjects without entered marks are excluded from GPA and performance calculations.
🛠️ Technologies Used
- Java
- Java Swing
- Object-Oriented Programming (OOP)
- File Handling
- Java Collections
- Git & GitHub
📁 Project Structure
GradeSphere/
├── src/
│   ├── Main.java
│   ├── model/
│   │   ├── Student.java
│   │   └── Subject.java
│   ├── service/
│   │   └── StudentManager.java
│   └── ui/
│       ├── MainFrame.java
│       ├── DashboardPanel.java
│       ├── StudentsPanel.java
│       ├── SubjectsPanel.java
│       ├── MarksPanel.java
│       ├── ReportsPanel.java
│       ├── AnalyticsPanel.java
│       └── SettingsPanel.java
├── .gitignore
└── README.md

▶️ How to Run
Requirements
- Java JDK 17 or later
- Git (optional)
Check Java:
java -version
javac -version

Clone the Repository
git clone https://github.com/Shreerajan/GradeSphere.git
cd GradeSphere

Compile — Windows PowerShell
javac -encoding UTF-8 -d bin (Get-ChildItem -Recurse src -Filter *.java).FullName

Run
java -cp bin Main

🔄 Application Workflow
Start Application
       ↓
   Dashboard
       ↓
  Add Student
       ↓
  Add Subjects
       ↓
   Enter Marks
       ↓
Grade Calculation
       ↓
 GPA & Percentage
       ↓
 ┌───────────────┐
 ↓               ↓
Analytics      Reports

🎯 Objectives
GradeSphere aims to reduce manual academic calculations and provide a simple platform for managing students, subjects, marks, grades, GPA, reports, and academic performance statistics.
🔮 Future Enhancements
- MySQL database integration
- Student/teacher authentication
- PDF report generation
- Attendance management
- Semester-wise GPA tracking
- Excel import/export
- Advanced performance charts
- Cloud data synchronization
👨‍💻 Developer
Shree Rajan
B.Tech Computer Science & Engineering
📜 License
This project is developed for educational and academic purposes.
