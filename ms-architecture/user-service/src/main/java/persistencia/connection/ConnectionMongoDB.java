/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.connection;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import config.Config;

/**
 *
 * @author Carlo
 */
public class ConnectionMongoDB {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    public static MongoDatabase getDatabase() {
        if (database == null) {
            mongoClient = MongoClients.create("mongodb://"+Config.get("MONGO_HOST")+":"+Config.get("MONGO_PORT")); 
            database = mongoClient.getDatabase(""+Config.get("MONGO_DB")); 
        }
        return database;
    }
    
    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
        }
    }
}
