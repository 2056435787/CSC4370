package uga.cs4370.mydbimpl;

import java.nio.file.Paths;
import java.util.List;

import uga.cs4370.mydb.Relation;
import uga.cs4370.mydb.RelationBuilder;
import uga.cs4370.mydb.Type;
import uga.cs4370.mydb.RA;
import uga.cs4370.mydb.Predicate;
import uga.cs4370.mydb.Cell;


public class Driver {
    
    public static void main(String[] args) {
        // Following is an example of how to use the relation class.
        // This creates a table with three columns with below mentioned
        // column names and data types.
        // After creating the table, data is loaded from a CSV file.
        // Path should be replaced with a correct file path for a compatible
        // CSV file.
        RA ra = new raImp();

        
        Relation advisor = new RelationBuilder()
                .attributeNames(List.of("Student ID", "Instructor ID"))
                .attributeTypes(List.of(Type.INTEGER, Type.STRING))
                .build();
        
        Relation classroom = new RelationBuilder()
                .attributeNames(List.of("Building", "Room Number", "Capacity"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.INTEGER))
                .build();

        Relation course = new RelationBuilder()
                .attributeNames(List.of("Course ID", "Title", "Department name", "Credits"))
                .attributeTypes(List.of(Type.INTEGER, Type.STRING, Type.STRING, Type.INTEGER))
                .build();                

        Relation department = new RelationBuilder()
                .attributeNames(List.of("Department Name", "Building", "Budget"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.DOUBLE))
                .build();

        Relation instructor = new RelationBuilder()
                .attributeNames(List.of("Instructor ID", "Name", "Dept_Name","Salary"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING, Type.DOUBLE))
                .build();
                
        Relation prereq = new RelationBuilder()
                .attributeNames(List.of("Course ID", "Prerequisite ID"))
                .attributeTypes(List.of(Type.INTEGER, Type.STRING))
                .build();  

        Relation section = new RelationBuilder()
                .attributeNames(List.of("Course ID", "Section ID", "Semester", "Year", "Building","Room Number", "Time Slot ID"))
                .attributeTypes(List.of(Type.INTEGER, Type.INTEGER, Type.STRING, Type.INTEGER,Type.STRING,Type.STRING,Type.STRING ))
                .build(); 

        Relation student = new RelationBuilder()
                .attributeNames(List.of("Student ID", "Name", "Department name", "Total Credits"))
                .attributeTypes(List.of(Type.INTEGER, Type.STRING, Type.STRING, Type.INTEGER))
                .build(); 

        Relation takes = new RelationBuilder()
                .attributeNames(List.of("ID", "Course ID", "Section ID", "Semester", "Year", "Grade"))
                .attributeTypes(List.of(Type.INTEGER, Type.INTEGER, Type.STRING, Type.STRING, Type.INTEGER, Type.STRING))
                .build();

        Relation teaches = new RelationBuilder()
                .attributeNames(List.of("Instructor ID", "Course ID", "Section ID", "Semester", "Year"))
                .attributeTypes(List.of(Type.STRING, Type.INTEGER, Type.INTEGER, Type.STRING, Type.INTEGER))
                .build();  
        
        Relation time_slot = new RelationBuilder()
                .attributeNames(List.of("Time Slot ID", "Day", "Start Hour", "Start Minute", "End Hour", "End Minute"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.INTEGER, Type.INTEGER, Type.INTEGER, Type.INTEGER))
                .build();


        

        System.out.println("Current working directory: " + Paths.get("").toAbsolutePath().toString());

        advisor.loadData("project_1_starter_code/CSV/advisor.csv");
        classroom.loadData("project_1_starter_code/CSV/classroom.csv");
        course.loadData("project_1_starter_code/CSV/course.csv");
        department.loadData("project_1_starter_code/CSV/department_export.csv");
        instructor.loadData("project_1_starter_code/CSV/instructor_export.csv");
        prereq.loadData("project_1_starter_code/CSV/prereq.csv");
        section.loadData("project_1_starter_code/CSV/section.csv");
        student.loadData("project_1_starter_code/CSV/student.csv");
        takes.loadData("project_1_starter_code/CSV/takes.csv");
        teaches.loadData("project_1_starter_code/CSV/teaches.csv");
        time_slot.loadData("project_1_starter_code/CSV/time_slot.csv");
                


        //select the name and ID of students who has been advised by an instructor in statistics department and has Student ID < 5000 
        Predicate instructorStat = (List<Cell> row) -> row.get(2).getAsString().equals("Statistics");
        Relation instructorStatDept = ra.select(instructor, instructorStat);
        Relation instructor_Stat_ID = ra.project(instructorStatDept,List.of("Instructor ID"));
        Relation advised_ID = ra.join(advisor, instructor_Stat_ID);
        Relation student_advised_ID = ra.project(advised_ID, List.of("Student ID"));
        Relation student_advised = ra.join(student_advised_ID,student);
        Relation student_ID_Name = ra.project(student_advised, List.of("Student ID","Name"));
        Predicate student_ID_less = (List<Cell> row) -> row.get(0).getAsInt()<5000;
        Relation result1 = ra.select(student_ID_Name,student_ID_less);

        result1.print();

        // Select name of advisors who advise students in either the accounting or marketing department
        Predicate accounting = (List<Cell> row) -> row.get(2).getAsString().equals("Accounting");
        Predicate marketing = (List<Cell> row) -> row.get(2).getAsString().equals("Marketing");
        Relation accountingMajor = ra.select(student, accounting);
        Relation marketingMajor = ra.select(student, marketing);
        Relation accounting_or_marketing = ra.union(accountingMajor, marketingMajor);
        Relation accounting_or_marketing_ID = ra.project(accounting_or_marketing, List.of("Student ID"));
        Relation advisor_account_marketing = ra.join(advisor, accounting_or_marketing_ID);
        Relation advisor_account_marketing_ID = ra.project(advisor_account_marketing, List.of("Instructor ID"));
        Relation advisor_account_marketing_instructor = ra.join(advisor_account_marketing_ID, instructor);
        Relation result2 = ra.project(advisor_account_marketing_instructor, List.of("Name"));
        
        result2.print();

         
        // Select the name and salary of instructors who have a salary greater than 80000 and taught a course in the year 2009
        Predicate salaryGreaterThan80000 = (List<Cell> row) -> row.get(instructor.getAttrIndex("Salary")).getAsDouble() > 80000;
        Relation highSalaryInstructors = ra.select(instructor, salaryGreaterThan80000);
        Predicate taughtIn2009 = (List<Cell> row) -> row.get(teaches.getAttrIndex("Year")).getAsInt() == 2009;
        Relation coursesIn2009 = ra.select(teaches, taughtIn2009);
        Relation instructorsWhoTaughtIn2009 = ra.join(instructor, coursesIn2009);
        Relation instructorsWhoTaughtIn2009AndHighSalary = ra.join(highSalaryInstructors, instructorsWhoTaughtIn2009);
        Relation result3 = ra.project(instructorsWhoTaughtIn2009AndHighSalary, List.of("Name", "Salary"));

        result3.print();



    }

}
