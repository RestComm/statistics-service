package org.restcom.stats.core.type;

/**
 * @author Ricardo Limonta
 */
public enum MetricType {

    COUNTER("counter"), HISTOGRAM("histogram"), METER("meter"), TIMER("timer");

    private final String collectionName;

    private MetricType(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getCollectionName() {
        return collectionName;
    }
}