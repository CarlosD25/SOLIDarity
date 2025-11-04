/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 *
 * @author Carlo
 */
public class ConnectionMongoDB {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    public static MongoDatabase getDatabase() {
        if (database == null) {
            mongoClient = MongoClients.create("mongodb://localhost:27017"); 
            database = mongoClient.getDatabase("usuariosDB"); 
        }
        return database;
    }
}
