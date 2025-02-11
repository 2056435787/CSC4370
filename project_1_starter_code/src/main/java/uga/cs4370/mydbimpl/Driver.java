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

        Relation instructor = new RelationBuilder()
                .attributeNames(List.of("ID", "Name", "Dept_Name","Salary"))
                .attributeTypes(List.of(Type.INTEGER, Type.STRING, Type.STRING, Type.DOUBLE))
                .build();
        instructor.loadData("C:\\Users\\haizh\\Documents\\Schools\\database\\mysql-files\\instructor_export.csv");

        Predicate instructorTest = (List<Cell> row) -> row.get(2).getAsString().equals("English");
        Relation instructorT = ra.select(instructor,instructorTest);

        instructorT.print();

        
       
    }

}
