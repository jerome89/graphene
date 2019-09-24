package net.iponweb.disthene.service.store;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.graphene.writer.input.GrapheneMetric;
import net.engio.mbassy.bus.MBassador;
import net.iponweb.disthene.events.DistheneEvent;

import java.util.Queue;
import java.util.concurrent.Executor;

/**
 * @author Andrei Ivanov
 */
public abstract class WriterThread extends Thread {

    protected volatile boolean shutdown = false;

    protected MBassador<DistheneEvent> bus;
    protected Session session;
    protected PreparedStatement statement;

    protected Queue<GrapheneMetric> metrics;

    protected Executor executor;

    public WriterThread(String name, Session session, PreparedStatement statement, Queue<GrapheneMetric> metrics, Executor executor) {
        super(name);
        this.session = session;
        this.statement = statement;
        this.metrics = metrics;
        this.executor = executor;
    }

    public void shutdown() {
        shutdown = true;
    }
}
