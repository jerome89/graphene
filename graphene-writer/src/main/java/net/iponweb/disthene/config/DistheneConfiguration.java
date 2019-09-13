package net.iponweb.disthene.config;

import net.iponweb.disthene.service.aggregate.CarbonConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Andrei Ivanov
 */
@SuppressWarnings("UnusedDeclaration")
@ConfigurationProperties(prefix = "graphene.writer")
public final class DistheneConfiguration {
    private CarbonConfiguration carbon;
    private StoreConfiguration store;
    private IndexConfiguration index;
    private StatsConfiguration stats;

    public DistheneConfiguration(CarbonConfiguration carbon, StoreConfiguration store, IndexConfiguration index, StatsConfiguration stats) {
        this.carbon = carbon;
        this.store = store;
        this.index = index;
        this.stats = stats;
    }

    public CarbonConfiguration getCarbon() {
        return carbon;
    }

    public void setCarbon(CarbonConfiguration carbon) {
        this.carbon = carbon;
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
        return "DistheneConfiguration{" +
                "carbon=" + carbon +
                ", store=" + store +
                ", index=" + index +
                ", stats=" + stats +
                '}';
    }
}
