package net.iponweb.disthene.service.metric;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;
import net.iponweb.disthene.events.DistheneEvent;
import net.iponweb.disthene.events.MetricReceivedEvent;
import net.iponweb.disthene.events.MetricStoreEvent;
import net.iponweb.disthene.service.blacklist.BlacklistService;
import org.springframework.stereotype.Component;

/** @author Andrei Ivanov */
@Component
@Listener(references = References.Strong)
public class MetricService {

  private MBassador<DistheneEvent> bus;
  private BlacklistService blacklistService;

  public MetricService(
      MBassador<DistheneEvent> bus,
      BlacklistService blacklistService) {
    this.bus = bus;
    this.blacklistService = blacklistService;
    this.bus.subscribe(this);
  }

  @Handler(rejectSubtypes = false)
  public void handle(MetricReceivedEvent metricReceivedEvent) {
    if (blacklistService.isBlackListed(metricReceivedEvent.getMetric())) {
      return;
    }

    bus.post(new MetricStoreEvent(metricReceivedEvent.getMetric())).now();
  }
}
