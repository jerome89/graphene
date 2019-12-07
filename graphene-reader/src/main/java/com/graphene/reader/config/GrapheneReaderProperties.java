package com.graphene.reader.config;

import com.graphene.reader.service.index.model.IndexProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Andrei Ivanov
 */
@ConfigurationProperties("graphene.reader")
public class GrapheneReaderProperties {
    private ReaderConfiguration render;
    private StoreConfiguration store;
    private IndexProperty index;
    private StatsConfiguration stats;

    public ReaderConfiguration getRender() {
        return render;
    }

    public void setRender(ReaderConfiguration render) {
        this.render = render;
    }

    public StoreConfiguration getStore() {
        return store;
    }

    public void setStore(StoreConfiguration store) {
        this.store = store;
    }

    public IndexProperty getIndex() {
        return index;
    }

    public void setIndex(IndexProperty index) {
        this.index = index;
    }

    public StatsConfiguration getStats() {
        return stats;
    }

    public void setStats(StatsConfiguration stats) {
        this.stats = stats;
    }

    @Override
    public String toString() {
        return "GrapheneReaderProperties{" +
                "reader=" + render +
                ", store=" + store +
                ", index=" + index +
                ", stats=" + stats +
                '}';
    }

    public boolean isStatsEnabled() {
        return false;
    }
}
