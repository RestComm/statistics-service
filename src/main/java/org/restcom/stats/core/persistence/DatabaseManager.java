package org.restcom.stats.core.persistence;

import com.mongodb.ConnectionString;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Named;
import javax.inject.Singleton;
import org.bson.Document;

/**
 * @author Ricardo Limonta
 */
@Singleton
@Named
public class DatabaseManager {

    private static MongoClient mongoClient;
    private static MongoDatabase database;

    @PostConstruct
    private void startup() {
        mongoClient = MongoClients.create(new ConnectionString("mongodb://localhost"));
        database = mongoClient.getDatabase("restcomm-stats");
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        return database.getCollection(collectionName);
    }
    
    @PreDestroy
    private void shutdown() {
        mongoClient.close();
    }
}