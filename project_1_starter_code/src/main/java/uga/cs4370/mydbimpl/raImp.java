package uga.cs4370.mydbimpl;

import uga.cs4370.mydb.Relation;
import uga.cs4370.mydb.RelationBuilder;


import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import uga.cs4370.mydb.RA;
import uga.cs4370.mydb.Predicate;
import uga.cs4370.mydb.Cell;
import uga.cs4370.mydb.Type;


class raImp implements RA{
    /**
     * Performs the select operation on the relation rel
     * by applying the predicate p.
     * 
     * @return The resulting relation after applying the select operation.
     */

    
    public Relation select(Relation rel, Predicate p){

        List<String> attrs = rel.getAttrs();
        List<Type> types = rel.getTypes();

        RelationBuilder builder = new RelationBuilder();
        builder.attributeNames(attrs).attributeTypes(types);

        // Create a new relation
        Relation result = builder.build();

        // Iterate through rows of rel, applying the predicate p
        for (int i = 0; i < rel.getSize(); i++) {
            List<Cell> row = rel.getRow(i);
            if (p.check(row)) {
                result.insert(row); // Insert row if it satisfies the predicate
            }
        }

        return result;
    }
        
    

    /**
     * Performs the project operation on the relation rel
     * given the attributes list attrs.
     * 
     * @return The resulting relation after applying the project operation.
     * 
     * @throws IllegalArgumentException If attributes in attrs are not
     *                                  present in rel.
     */

    
    public Relation project(Relation rel, List<String> attrs){
            // Check if the requested attributes exist in the relation
            for (String attr : attrs) {
                if (!rel.hasAttr(attr)) {
                    throw new IllegalArgumentException("Attribute does not exist: " + attr);
                }
            }
    
            // Create a new relation with the requested attributes
            List<Type> types = new ArrayList<>();
            for (String attr : attrs) {
                types.add(rel.getTypes().get(rel.getAttrIndex(attr)));
            }
    
            RelationBuilder builder = new RelationBuilder();
            builder.attributeNames(attrs).attributeTypes(types);
    
            // Create a new relation for the result
            Relation result = builder.build();
    
            // Project the rows (only keep the selected columns)
            for (int i = 0; i < rel.getSize(); i++) {
                List<Cell> row = rel.getRow(i);
                List<Cell> projectedRow = new ArrayList<>();
                for (String attr : attrs) {
                    int index = rel.getAttrIndex(attr);
                    projectedRow.add(row.get(index)); // Only keep the selected columns
                }
                result.insert(projectedRow);
            }
    
            return result;
    }

    /**
     * Performs the union operation on the relations rel1 and rel2.
     * 
     * @return The resulting relation after applying the union operation.
     * 
     * @throws IllegalArgumentException If rel1 and rel2 are not compatible.
     */
    public Relation union(Relation rel1, Relation rel2){
            // Check that the relations have the same schema (same attributes and types)
            if (!rel1.getAttrs().equals(rel2.getAttrs()) || !rel1.getTypes().equals(rel2.getTypes())) {
                throw new IllegalArgumentException("Relations are not compatible for union.");
            }
    
            // Create a new relation with the same schema as rel1 (or rel2)
            RelationBuilder builder = new RelationBuilder();
            builder.attributeNames(rel1.getAttrs()).attributeTypes(rel1.getTypes());
    
            // Create a new relation for the result
            Relation result = builder.build();
    
            // Insert rows from rel1
            for (int i = 0; i < rel1.getSize(); i++) {
                result.insert(rel1.getRow(i));
            }
    
            // Insert rows from rel2 (only if not already present)
            for (int i = 0; i < rel2.getSize(); i++) {
                List<Cell> row = rel2.getRow(i);
                boolean exists = false;
                for (int j = 0; j < result.getSize(); j++) {
                    if (result.getRow(j).equals(row)) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    result.insert(row); // Insert row if it’s not already in the result
                }
            }
    
            return result;
    }

    /**
     * Performs the set difference operation on the relations rel1 and rel2.
     * 
     * @return The resulting relation after applying the set difference operation.
     * 
     * @throws IllegalArgumentException If rel1 and rel2 are not compatible.
     */
    public Relation diff(Relation rel1, Relation rel2){
            // Check that the relations have the same schema (same attributes and types)
            if (!rel1.getAttrs().equals(rel2.getAttrs()) || !rel1.getTypes().equals(rel2.getTypes())) {
                throw new IllegalArgumentException("Relations are not compatible for set difference.");
            }
    
            // Create a new relation with the same schema as rel1 (or rel2)
            RelationBuilder builder = new RelationBuilder();
            builder.attributeNames(rel1.getAttrs()).attributeTypes(rel1.getTypes());
    
            // Create a new relation for the result
            Relation result = builder.build();
    
            // Insert rows from rel1 that are not in rel2
            for (int i = 0; i < rel1.getSize(); i++) {
                List<Cell> row = rel1.getRow(i);
                boolean exists = false;
                for (int j = 0; j < rel2.getSize(); j++) {
                    if (rel2.getRow(j).equals(row)) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    result.insert(row); // Insert row if it’s not in rel2
                }
            }
    
            return result;
    }

    /**
     * Renames the attributes in origAttr of relation rel to corresponding
     * names in renamedAttr.
     * 
     * @return The resulting relation after renaming the attributes.
     * 
     * @throws IllegalArgumentException If attributes in origAttr are not present in
     *                                  rel or origAttr and renamedAttr do not have
     *                                  matching argument counts.
     */
    public Relation rename(Relation rel, List<String> origAttr, List<String> renamedAttr){
        // Check if the original and renamed attributes lists are valid
        if (origAttr.size() != renamedAttr.size()) {
            throw new IllegalArgumentException("Original and renamed attribute lists must have the same size.");
        }

        // Create a new relation with the renamed attributes
        List<String> newAttrs = new ArrayList<>(rel.getAttrs());
        List<Type> types = rel.getTypes();

        for (int i = 0; i < origAttr.size(); i++) {
            int index = rel.getAttrIndex(origAttr.get(i));
            newAttrs.set(index, renamedAttr.get(i)); // Rename attributes
        }

        // Build the new relation with the renamed attributes
        RelationBuilder builder = new RelationBuilder();
        builder.attributeNames(newAttrs).attributeTypes(types);

        // Create the new relation for the result
        Relation result = builder.build();

        // Insert all rows from rel into the new relation
        for (int i = 0; i < rel.getSize(); i++) {
            result.insert(rel.getRow(i));
        }

        return result;
    }

    /**
     * Performs cartesian product on relations rel1 and rel2.
     * 
     * @return The resulting relation after applying cartesian product.
     * 
     * @throws IllegalArgumentException if rel1 and rel2 have common attributes.
     */
    public Relation cartesianProduct(Relation rel1, Relation rel2){
        // Check if the relations have any common attributes
        Set<String> commonAttrs = new HashSet<>(rel1.getAttrs());
        commonAttrs.retainAll(rel2.getAttrs());
        if (!commonAttrs.isEmpty()) {
            throw new IllegalArgumentException("Relations cannot have common attributes for Cartesian product.");
        }

        // Create a new relation with the attributes of both rel1 and rel2
        List<String> newAttrs = new ArrayList<>(rel1.getAttrs());
        newAttrs.addAll(rel2.getAttrs());
        List<Type> types = new ArrayList<>(rel1.getTypes());
        types.addAll(rel2.getTypes());

        // Build the new relation
        RelationBuilder builder = new RelationBuilder();
        builder.attributeNames(newAttrs).attributeTypes(types);

        // Create the new relation for the result
        Relation result = builder.build();

        // Perform the Cartesian product
        for (int i = 0; i < rel1.getSize(); i++) {
            List<Cell> row1 = rel1.getRow(i);
            for (int j = 0; j < rel2.getSize(); j++) {
                List<Cell> row2 = rel2.getRow(j);
                List<Cell> combinedRow = new ArrayList<>(row1);
                combinedRow.addAll(row2);
                result.insert(combinedRow); // Insert the combined row
            }
        }

        return result;
    }

    /**
     * Performs natural join on relations rel1 and rel2.
     * 
     * @return The resulting relation after applying natural join.
     */
    public Relation join(Relation rel1, Relation rel2){
        // Check if the relations have common attributes
        Set<String> commonAttrs = new HashSet<>(rel1.getAttrs());
        commonAttrs.retainAll(rel2.getAttrs());
        if (commonAttrs.isEmpty()) {
            throw new IllegalArgumentException("Relations must have common attributes for a natural join.");
        }

        // Create a new relation with the union of the attributes of both rel1 and rel2
        List<String> newAttrs = new ArrayList<>(rel1.getAttrs());
        for (String attr : rel2.getAttrs()) {
            if (!newAttrs.contains(attr)) {
                newAttrs.add(attr);
            }
        }

        List<Type> types = new ArrayList<>(rel1.getTypes());
        types.addAll(rel2.getTypes());

        // Build the new relation
        RelationBuilder builder = new RelationBuilder();
        builder.attributeNames(newAttrs).attributeTypes(types);

        // Create the new relation for the result
        Relation result = builder.build();

        // Perform the natural join
        for (int i = 0; i < rel1.getSize(); i++) {
            List<Cell> row1 = rel1.getRow(i);
            for (int j = 0; j < rel2.getSize(); j++) {
                List<Cell> row2 = rel2.getRow(j);
                boolean joinCondition = true;

                for (String commonAttr : commonAttrs) {
                    int index1 = rel1.getAttrIndex(commonAttr);
                    int index2 = rel2.getAttrIndex(commonAttr);

                    if (!row1.get(index1).equals(row2.get(index2))) {
                        joinCondition = false;
                        break;
                    }
                }

                if (joinCondition) {
                    List<Cell> combinedRow = new ArrayList<>(row1);
                    for (String attr : rel2.getAttrs()) {
                        if (!commonAttrs.contains(attr)) {
                            int index = rel2.getAttrIndex(attr);
                            combinedRow.add(row2.get(index));
                        }
                    }
                    result.insert(combinedRow); // Insert the combined row
                }
            }
        }

        return result;
    }

    /**
     * Performs theta join on relations rel1 and rel2 with predicate p.
     * 
     * @return The resulting relation after applying theta join. The resulting
     *         relation should have the attributes of both rel1 and rel2. The
     *         attributes of rel1 should appear in the the order they appear in rel1
     *         but before the attributes of rel2. Attributes of rel2 as well should
     *         appear in the order they appear in rel2.
     * 
     * @throws IllegalArgumentException if rel1 and rel2 have common attributes.
     */
    public Relation join(Relation rel1, Relation rel2, Predicate p){
        // Create a new relation with the combined attributes of both rel1 and rel2
        List<String> newAttrs = new ArrayList<>(rel1.getAttrs());
        newAttrs.addAll(rel2.getAttrs());

        List<Type> types = new ArrayList<>(rel1.getTypes());
        types.addAll(rel2.getTypes());

        RelationBuilder builder = new RelationBuilder();
        builder.attributeNames(newAttrs).attributeTypes(types);

        // Create the new relation for the result
        Relation result = builder.build();

        // Perform the theta join
        for (int i = 0; i < rel1.getSize(); i++) {
            List<Cell> row1 = rel1.getRow(i);
            for (int j = 0; j < rel2.getSize(); j++) {
                List<Cell> row2 = rel2.getRow(j);
                if (p.check(row1) && p.check(row2)) {
                    List<Cell> combinedRow = new ArrayList<>(row1);
                    combinedRow.addAll(row2);
                    result.insert(combinedRow);
                }
            }
        }

        return result;
    }
    
}
