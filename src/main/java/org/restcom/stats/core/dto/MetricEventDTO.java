package org.restcom.stats.core.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author Ricardo Limonta
 */
public class MetricEventDTO implements Serializable {
    private Date timestamp;
    private long totalEvents;

    public MetricEventDTO() {
        super();
    }

    public MetricEventDTO(Date timestamp, long totalEvents) {
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
        final MetricEventDTO other = (MetricEventDTO) obj;
        if (this.totalEvents != other.totalEvents) {
            return false;
        }
        return Objects.equals(this.timestamp, other.timestamp);
    }
}
