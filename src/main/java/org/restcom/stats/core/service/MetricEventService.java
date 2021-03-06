/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.restcom.stats.core.service;

import com.mongodb.client.MongoCursor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.restcom.stats.core.dto.MetricStatusDTO;
import org.restcom.stats.core.persistence.DatabaseManager;
import org.restcom.stats.core.type.MetricType;

/**
 * @author Ricardo Limonta
 */
@Named
public class MetricEventService implements Serializable {
    
    @Inject
    private DatabaseManager dbm;
    
    /**
     * Retrieve metric status from interval timestamps.
     * @param fromTime timestamp from.
     * @param toTime timestemp to.
     * @param metricType Metric type.
     * @return metric status.
     */
    public MetricStatusDTO restrieveStatus(long fromTime, long toTime, MetricType metricType) {
        MetricStatusDTO status = new MetricStatusDTO();
        
        //create params list
        List<Bson> params = new ArrayList<>();
        
        //define match criteria
        params.add(new Document("$match", new Document("timestamp", new Document("$gte", fromTime))));
        params.add(new Document("$match", new Document("timestamp", new Document("$lte", toTime))));
        
        //define grouping criteria
        params.add(new Document("$group", new Document("_id", "status")
                                              .append("totalEvents", new Document("$sum", 1))
                                              .append("lastEvent", new Document("$max", "$timestamp"))));
        //exec query
        MongoCursor<Document> result = dbm.getCollection(metricType.getCollectionName()).aggregate(params).iterator();
        
        //convert document result into dto
        if (result.hasNext()) {
            Document statsDoc = result.next();
            status.setTotalEvents(statsDoc.getInteger("totalEvents"));
            status.setTimestamp(new Date(statsDoc.getLong("lastEvent")));            
        }
        
        return status;
    }
    
    /**
     * Retrieve metric keys.
     * @param metricType Metric type.
     * @return metric keys.
     */
    public List<String> retrieveMetricKeys(MetricType metricType) {
        List<String> keys = new ArrayList<>();
        
        //retrieve disctinct key values
        MongoCursor<String> result = dbm.getCollection(metricType.getCollectionName())
                                                                              .distinct("key", String.class).iterator();
        
        while (result.hasNext()) {
            keys.add(result.next());
        }
        
        return keys;
    }
}