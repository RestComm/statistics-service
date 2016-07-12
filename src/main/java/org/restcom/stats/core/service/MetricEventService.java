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
import org.restcom.stats.core.dto.MetricEventDTO;
import org.restcom.stats.core.persistence.DatabaseManager;
import org.restcom.stats.core.type.MetricType;

/**
 * @author Ricardo Limonta
 */
@Named
public class MetricEventService implements Serializable {
    
    @Inject
    private DatabaseManager dbm;
    
    public MetricEventDTO restrieveStatus(MetricType metricType) {
        MetricEventDTO status = new MetricEventDTO();
        
        //create params list
        List<Bson> params = new ArrayList<>();
        
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
    
    public List<MetricEventDTO> retrieveMetrics(MetricType metricType, String key) {
        List<MetricEventDTO> metrics = new ArrayList<>();
        
        //create params list
        List<Bson> params = new ArrayList<>();
        
        //define match criteria
        params.add(new Document("$match", new Document("key", new Document("$eq", key))));
        
        //define grouping criteria
        params.add(new Document("$group", new Document("_id", "$timestamp")
                                              .append("totalCount", new Document("$sum", "$count"))));
        //define order criteria
        params.add(new Document("$sort", new Document("_id", 1)));
        
        //exec query
        MongoCursor<Document> result = dbm.getCollection(metricType.getCollectionName()).aggregate(params).iterator();
        
        //convert document result into dto
        while (result.hasNext()) {
            Document statsDoc = result.next();
            metrics.add(new MetricEventDTO(new Date(statsDoc.getLong("_id")), statsDoc.getInteger("totalCount")));          
        }
        
        return metrics;
    }
}