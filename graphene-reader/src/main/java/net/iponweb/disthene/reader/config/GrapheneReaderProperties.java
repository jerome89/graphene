package net.iponweb.disthene.reader.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Andrei Ivanov
 */
@ConfigurationProperties("graphene.reader")
public class GrapheneReaderProperties {
    private ReaderConfiguration render;
    private StoreConfiguration store;
    private IndexConfiguration index;
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

    public IndexConfiguration getIndex() {
        return index;
    }

    public void setIndex(IndexConfiguration index) {
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
        return "DistheneReaderConfiguration{" +
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
