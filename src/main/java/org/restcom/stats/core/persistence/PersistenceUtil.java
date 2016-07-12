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