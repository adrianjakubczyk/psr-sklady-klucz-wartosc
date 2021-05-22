import com.arangodb.ArangoCollection;
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.IndexEntity;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Menu {
    final private static Random random = new Random(System.currentTimeMillis());
    private ArangoCollection collection;

    public Menu(ArangoCollection collection) {
        this.collection = collection;
    }

    private void printMenu() {
        System.out.println("===================================================");
        System.out.println("Please choose operation:");
        System.out.println("1: Add");
        System.out.println("2: Select by ID");
        System.out.println("3: Select All");
        System.out.println("4: Update release year");
        System.out.println("5: Delete by ID");
        System.out.println("6: Calculate average release year (server side)");
        System.out.println("7: Calculate average release year (client side)");

        System.out.println("9: Exit");
        System.out.println("===================================================");
    }

    public void selectOperation() {
        boolean doExit = false;
        Scanner scanner = new Scanner(System.in);
        while (!doExit) {
            printMenu();
            int op = scanner.nextInt();

            switch (op) {
                case 1:
                    System.out.println("Adding");
                    addMovie();
                    break;
                case 2:
                    System.out.println("Select by ID");
                    selectById();
                    break;
                case 3:
                    System.out.println("Select All");
                    selectAll();
                    break;
                case 4:
                    System.out.println("Update");
                    updateMovie();
                    break;
                case 5:
                    System.out.println("Delete");
                    deleteById();
                    break;
                case 6:
                    System.out.println("Calculate average release year (server side)");
                    calculateAvgYearServerSide();
                    break;
                case 7:
                    System.out.println("Calculate average release year (client side)");
                    calculateAvgYearClientSide();
                    break;
                case 9:
                    System.out.println("Exit");
                    doExit = true;
                    break;
                default:
                    System.out.println("Not a recognizable choice");
                    break;
            }
        }


    }

    private void addMovie() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Movie name:");
        String name = scanner.nextLine();
        System.out.println("Movie release year:");
        int year = scanner.nextInt();

        BaseDocument myObject = new BaseDocument();
        myObject.setKey(String.valueOf(Math.abs(random.nextInt())));
        myObject.addAttribute("name", name);
        myObject.addAttribute("year", year);
        try {
            collection.insertDocument(myObject);
            System.out.println("Document created");
        } catch(ArangoDBException e) {
            System.err.println("Failed to create document. " + e.getMessage());
        }

    }

    private void selectById() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Movie id:");
        long id = scanner.nextLong();

        BaseDocument movie = collection.getDocument(String.valueOf(id),BaseDocument.class);
        System.out.println("Key: " + movie.getKey()+" -> "+"Title: "+movie.getAttribute("name")+"("+movie.getAttribute("year")+")");

    }

    private void selectAll() {
        try {
            String query = "FOR movie IN "+collection.name()+" RETURN movie";
            ArangoCursor<BaseDocument> cursor = collection.db().query(query,BaseDocument.class);
            cursor.forEachRemaining(movie -> {
                System.out.println("Key: " + movie.getKey()+" -> "+"Title: "+movie.getAttribute("name")+"("+movie.getAttribute("year")+")");
            });
        } catch (ArangoDBException e) {
            System.err.println("Failed to execute query. " + e.getMessage());
        }
    }

    private void updateMovie() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Movie id:");
        long id = scanner.nextLong();
        BaseDocument movie = collection.getDocument(String.valueOf(id),BaseDocument.class);
        System.out.println("Key: " + movie.getKey()+" -> "+"Title: "+movie.getAttribute("name")+"("+movie.getAttribute("year")+")");
        System.out.println("Change year to:");
        int year = scanner.nextInt();

        movie.updateAttribute("year",year);
        collection.updateDocument(movie.getKey(),movie);
        movie = collection.getDocument(String.valueOf(id),BaseDocument.class);
        System.out.println("Updated");
        System.out.println("Key: " + movie.getKey()+" -> "+"Title: "+movie.getAttribute("name")+"("+movie.getAttribute("year")+")");


    }

    private void deleteById() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Movie id:");
        long id = scanner.nextLong();
        BaseDocument movie = collection.getDocument(String.valueOf(id),BaseDocument.class);
        System.out.println("Key: " + movie.getKey()+" -> "+"Title: "+movie.getAttribute("name")+"("+movie.getAttribute("year")+")");
        collection.deleteDocument(movie.getKey());
        System.out.println("Deleted");
    }

    private void calculateAvgYearServerSide(){
        try {
            String query = "FOR m IN "+collection.name()+" COLLECT AGGREGATE avgYear = avg(m.year) RETURN avgYear";
            ArangoCursor<Float> cursor = collection.db().query(query,Float.class);
            cursor.forEachRemaining(year -> {
                System.out.println("Average year: " + year.toString());
            });
        } catch (ArangoDBException e) {
            System.err.println("Failed to execute query. " + e.getMessage());
        }
    }

    private void calculateAvgYearClientSide(){
        try {
            String query = "FOR movie IN "+collection.name()+" RETURN movie";
            ArangoCursor<BaseDocument> cursor = collection.db().query(query,BaseDocument.class);
            final AverageUtil averageUtil = new AverageUtil();
            cursor.forEachRemaining(movie -> {
                averageUtil.addElement((int)movie.getAttribute("year"));
            });
            System.out.println("Average year: " + averageUtil.calculate());

        } catch (ArangoDBException e) {
            System.err.println("Failed to execute query. " + e.getMessage());
        }
    }

}
