package uga.cs4370.mydbimpl;

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
                .attributeTypes(List.of(Type.INTEGER, Type.INTEGER))
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
                .attributeTypes(List.of(Type.INTEGER, Type.STRING, Type.STRING, Type.DOUBLE))
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
                .attributeNames(List.of("ID", "Course ID", "Section ID", "Semester", "Year"))
                .attributeTypes(List.of(Type.INTEGER, Type.INTEGER, Type.INTEGER, Type.STRING, Type.INTEGER))
                .build();  
        
        Relation time_slot = new RelationBuilder()
                .attributeNames(List.of("Time Slot ID", "Day", "Start Hour", "Start Minute", "End Hour", "End Minute"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.INTEGER, Type.INTEGER, Type.INTEGER, Type.INTEGER))
                .build();


        advisor.loadData("C:\\Users\\haizh\\Documents\\Schools\\database\\mysql-files\\advisor.csv");
        classroom.loadData("C:\\Users\\haizh\\Documents\\Schools\\database\\mysql-files\\classroom.csv");
        course.loadData("C:\\Users\\haizh\\Documents\\Schools\\database\\mysql-files\\course.csv");
        department.loadData("C:\\Users\\haizh\\Documents\\Schools\\database\\mysql-files\\department_export.csv");
        instructor.loadData("C:\\Users\\haizh\\Documents\\Schools\\database\\mysql-files\\instructor_export.csv");
        prereq.loadData("C:\\Users\\haizh\\Documents\\Schools\\database\\mysql-files\\prereq.csv");
        section.loadData("C:\\Users\\haizh\\Documents\\Schools\\database\\mysql-files\\section.csv");
        student.loadData("C:\\Users\\haizh\\Documents\\Schools\\database\\mysql-files\\student.csv");
        takes.loadData("C:\\Users\\haizh\\Documents\\Schools\\database\\mysql-files\\takes.csv");
        teaches.loadData("C:\\Users\\haizh\\Documents\\Schools\\database\\mysql-files\\teaches.csv");
        time_slot.loadData("C:\\Users\\haizh\\Documents\\Schools\\database\\mysql-files\\time_slot.csv");


        //select the name, ID of student who has been advised by an instructor in statistics department 
        Predicate instructorStat = (List<Cell> row) -> row.get(2).getAsString().equals("Statistics");
        Relation instructorStatDept = ra.select(instructor, instructorStat);
        Relation instructor_Stat_ID = ra.project(instructorStatDept,List.of("Instructor ID"));
        Relation advised_ID = ra.join(advisor, instructor_Stat_ID);
        Relation student_advised_ID = ra.project(advised_ID, List.of("Student ID"));
        Relation student_advised = ra.join(student_advised_ID,student);
        
        Relation result1 = ra.project(student_advised, List.of("Student ID","Name"));
        
        result1.print();
        
    }

}
