package org.restcom.stats.core.persistence;

import java.util.Map;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Named;
import org.bson.Document;

/**
 * @author Ricardo Limonta
 */
@Named
public class PersistenceUtil {
    
    @Inject
    private DatabaseManager dbm;

    private static final Logger LOGGER = Logger.getLogger("restcomm-stats");

    public void insert(Map<String, Object> value, String collection) {
        dbm.getCollection(collection).insertOne(new Document(value));
    }
}