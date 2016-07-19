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
package org.restcom.stats.core.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author Ricardo Limonta
 */
public class MetricStatusDTO implements Serializable {
    private Date timestamp;
    private long totalEvents;

    public MetricStatusDTO() {
        super();
    }

    public MetricStatusDTO(Date timestamp, long totalEvents) {
        super();
        this.timestamp = timestamp;
        this.totalEvents = totalEvents;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public long getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(long totalEvents) {
        this.totalEvents = totalEvents;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.timestamp);
        hash = 59 * hash + (int) (this.totalEvents ^ (this.totalEvents >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MetricStatusDTO other = (MetricStatusDTO) obj;
        if (this.totalEvents != other.totalEvents) {
            return false;
        }
        return Objects.equals(this.timestamp, other.timestamp);
    }
}
