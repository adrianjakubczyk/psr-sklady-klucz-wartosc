import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.CollectionEntity;
import com.arangodb.mapping.ArangoJack;

public class Main {
    public static void main(String[] args) {
        ArangoDB arangoDB = new ArangoDB.Builder().serializer(new ArangoJack()).build();

        String dbName = "cinema";
        try {
            arangoDB.createDatabase(dbName);
            System.out.println("Database created: " + dbName);
        } catch(ArangoDBException e) {
            System.err.println("Failed to create database: " + dbName + "; " + e.getMessage());
        }

        String collectionName = "movieCollection";
        try {
            CollectionEntity myArangoCollection=arangoDB.db(dbName).createCollection(collectionName);
            System.out.println("Collection created: " + myArangoCollection.getName());
        } catch(ArangoDBException e) {
            System.err.println("Failed to create collection: " + collectionName + "; " + e.getMessage());
        }


        Menu menu = new Menu(arangoDB.db(dbName).collection(collectionName));

        menu.selectOperation();


        arangoDB.shutdown();
    }
}
