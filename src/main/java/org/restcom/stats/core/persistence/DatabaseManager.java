package org.restcom.stats.core.persistence;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.Serializable;
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
public class DatabaseManager implements Serializable {

    private static MongoClient mongoClient;
    private static MongoDatabase database;

    @PostConstruct
    public void startup() {
        mongoClient = new MongoClient();
        database = mongoClient.getDatabase("restcomm-stats");
    }
    
    public MongoCollection<Document> getCollection(String collectionName) {
        return database.getCollection(collectionName);
    }
    
    @PreDestroy
    public void shutdown() {
        mongoClient.close();
    }
}