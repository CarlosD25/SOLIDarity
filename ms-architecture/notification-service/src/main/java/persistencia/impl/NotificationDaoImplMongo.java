/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.impl;

import persistencia.connection.ConnectionMongoDB;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.Notification;
import org.bson.Document;
import org.bson.types.ObjectId;
import persistencia.NotificationDao;

/**
 *
 * @author Carlo
 */
public class NotificationDaoImplMongo implements NotificationDao {

    private final MongoCollection<Document> collection;

    public NotificationDaoImplMongo() {
        MongoDatabase database = ConnectionMongoDB.getDatabase();
        collection = database.getCollection("notifications");
    }

    @Override
    public Notification create(Notification notification) {
        Document doc = new Document()
                .append("user_id", notification.getIdUser())
                .append("message", notification.getMessage())
                .append("fecha", notification.getFecha());

        collection.insertOne(doc);

        ObjectId id = doc.getObjectId("_id");
        notification.setId(id.hashCode()); 
        return notification;
    }

    @Override
    public List<Notification> findByUserId(int id) {
        List<Notification> notifications = new ArrayList<>();

        FindIterable<Document> docs = collection.find(Filters.eq("user_id", id))
                .sort(Sorts.descending("fecha"));

        for (Document doc : docs) {
            Notification n = new Notification();
            n.setId(doc.getObjectId("_id").hashCode()); 
            n.setIdUser(doc.getInteger("user_id"));
            n.setMessage(doc.getString("message"));
            Object fechaObj = doc.get("fecha");
            if (fechaObj instanceof java.util.Date) {
                n.setFecha(new Timestamp(((java.util.Date) fechaObj).getTime()));
            }
            notifications.add(n);
        }

        return notifications;
    }
}
