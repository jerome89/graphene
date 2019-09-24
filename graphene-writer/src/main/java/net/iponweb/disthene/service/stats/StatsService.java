package net.iponweb.disthene.service.stats;

import com.graphene.writer.event.MetricStoreEvent;
import net.iponweb.disthene.config.Rollup;
import net.iponweb.disthene.config.StatsConfiguration;
import net.iponweb.disthene.events.*;
import net.iponweb.disthene.service.aggregate.CarbonConfiguration;
import net.iponweb.disthene.util.NamedThreadFactory;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Andrei Ivanov
 */
@Component
public class StatsService implements StatsServiceMBean {
    private static final String SCHEDULER_NAME = "distheneStatsFlusher";

    private Logger logger = Logger.getLogger(StatsService.class);

    private StatsConfiguration statsConfiguration;

    private Rollup rollup;
    private AtomicLong storeSuccess = new AtomicLong(0);
    private AtomicLong storeError = new AtomicLong(0);
    private ConcurrentMap<String, StatsRecord> stats = new ConcurrentHashMap<>();

    // MBean
    private long lastStoreSuccess = 0;
    private long lastStoreError = 0;
    private long lastMetricsReceived = 0;
    private long lastWriteCount = 0;
    private Map<String, Long> lastMetricsReceivedPerTenant = new HashMap<>();
    private Map<String, Long> lastWriteCountPerTenant = new HashMap<>();


    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, new NamedThreadFactory(SCHEDULER_NAME));

    public StatsService(StatsConfiguration statsConfiguration, CarbonConfiguration carbonConfiguration) {
        this.statsConfiguration = statsConfiguration;
        this.rollup = carbonConfiguration.getBaseRollup();

        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                flush();
            }
        }, 60 - ((System.currentTimeMillis() / 1000L) % 60), statsConfiguration.getInterval(), TimeUnit.SECONDS);

    }

    private StatsRecord getStatsRecord(String tenant) {
        StatsRecord statsRecord = stats.get(tenant);
        if (statsRecord == null) {
            StatsRecord newStatsRecord = new StatsRecord();
            statsRecord = stats.putIfAbsent(tenant, newStatsRecord);
            if (statsRecord == null) {
                statsRecord = newStatsRecord;
            }
        }

        return statsRecord;
    }

    public void handle(MetricReceivedEvent metricReceivedEvent) {
        getStatsRecord(metricReceivedEvent.getMetric().getTenant()).incMetricsReceived();
    }

    public void handle(MetricStoreEvent metricStoreEvent) {
        getStatsRecord(metricStoreEvent.getTenant()).incMetricsWritten();
    }

    public void handle(StoreSuccessEvent storeSuccessEvent) {
        storeSuccess.addAndGet(storeSuccessEvent.getCount());
    }

    public void handle(StoreErrorEvent storeErrorEvent) {
        storeError.addAndGet(storeErrorEvent.getCount());
    }

    public void flush() {
        Map<String, StatsRecord> statsToFlush = new HashMap<>();
        long storeSuccess = this.storeSuccess.getAndSet(0);
        long storeError = this.storeError.getAndSet(0);

        for (ConcurrentMap.Entry<String, StatsRecord> entry : stats.entrySet()) {
            statsToFlush.put(entry.getKey(), entry.getValue().reset());
        }

        doFlush(statsToFlush, storeSuccess, storeError, DateTime.now(DateTimeZone.UTC).withSecondOfMinute(0).withMillisOfSecond(0).getMillis() / 1000);
    }

    private void doFlush(Map<String, StatsRecord> stats, long storeSuccess, long storeError, long timestamp) {
        logger.debug("Flushing stats for " + timestamp);
        lastMetricsReceivedPerTenant.clear();
        lastWriteCountPerTenant.clear();

        long totalReceived = 0;
        long totalWritten = 0;

        if (statsConfiguration.isLog()) {
            logger.info("Graphene stats:");
            logger.info("=========================================================================");
            logger.info("Tenant\t\tmetrics_received\t\twrite_count");
            logger.info("=========================================================================");
        }

        for (Map.Entry<String, StatsRecord> entry : stats.entrySet()) {
            String tenant = entry.getKey();
            StatsRecord statsRecord = entry.getValue();

            totalReceived += statsRecord.getMetricsReceived();
            totalWritten += statsRecord.getMetricsWritten();

            lastMetricsReceivedPerTenant.put(tenant, statsRecord.getMetricsReceived());

            lastMetricsReceivedPerTenant.put(tenant, statsRecord.getMetricsWritten());

            if (statsConfiguration.isLog()) {
                logger.info(tenant + "\t\t" + statsRecord.metricsReceived + "\t\t" + statsRecord.getMetricsWritten());
            }
        }

        lastMetricsReceived = totalReceived;
        lastWriteCount = totalWritten;
        lastStoreSuccess = storeSuccess;
        lastStoreError = storeError;

        if (statsConfiguration.isLog()) {
            logger.info("total\t\t" + totalReceived + "\t\t" + totalWritten);
            logger.info("=========================================================================");
            logger.info("store.success:\t\t" + storeSuccess);
            logger.info("store.error:\t\t" + storeError);
            logger.info("=========================================================================");
        }
    }

    @PreDestroy
    public synchronized void shutdown() {
        scheduler.shutdown();
    }

    // MBean


    @Override
    public long getStoreSuccess() {
        return lastStoreSuccess;
    }

    @Override
    public long getStoreError() {
        return lastStoreError;
    }

    @Override
    public long getMetricsReceived() {
        return lastMetricsReceived;
    }

    @Override
    public long getWriteCount() {
        return lastWriteCount;
    }

    @Override
    public Map<String, Long> getMetricsReceivedPerTenant() {
        return lastMetricsReceivedPerTenant;
    }

    @Override
    public Map<String, Long> getWriteCountPerTenant() {
        return lastWriteCountPerTenant;
    }

    private class StatsRecord {
        private AtomicLong metricsReceived = new AtomicLong(0);
        private AtomicLong metricsWritten = new AtomicLong(0);

        private StatsRecord() {
        }

        public StatsRecord(long metricsReceived, long metricsWritten) {
            this.metricsReceived = new AtomicLong(metricsReceived);
            this.metricsWritten = new AtomicLong(metricsWritten);
        }

        /**
         * Resets the stats to zeroes and returns a snapshot of the record
         * @return snapshot of the record
         */
        public StatsRecord reset() {
            return new StatsRecord(metricsReceived.getAndSet(0), metricsWritten.getAndSet(0));
        }

        public void incMetricsReceived() {
            metricsReceived.addAndGet(1);
        }

        public void incMetricsWritten() {
            metricsWritten.addAndGet(1);
        }

        public long getMetricsReceived() {
            return metricsReceived.get();
        }

        public long getMetricsWritten() {
            return metricsWritten.get();
        }
    }
}
