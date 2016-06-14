package org.restcom.stats.core.persistence;

import com.mongodb.async.SingleResultCallback;
import java.util.Map;
import java.util.logging.Level;
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

    public void insertAsync(Map<String, Object> value, String collection) {
        dbm.getCollection(collection).insertOne(new Document(value), new SingleResultCallback<Void>() {
            @Override
            public void onResult(final Void result, final Throwable throwable) {
                if (throwable != null) {
                    LOGGER.log(Level.SEVERE, "Async Result: ", result);
                    LOGGER.log(Level.SEVERE, "Async Throwable: ", throwable);
                }
            }
        });
    }
}