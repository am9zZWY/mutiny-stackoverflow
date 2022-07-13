package com.consumer;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class Consumer {

    private static final Logger LOGGER = Logger.getLogger("Consumer");
    final BroadcastProcessor<HashMap<String, Long[]>> sink;

    public Consumer() {
        this.sink = BroadcastProcessor.create();
    }

    public void addEvent(HashMap<String, Long[]> time) {
        this.sink.onNext(time);
    }

    public Multi<HashMap<String, Long[]>> getStream() {
        LOGGER.info("Getting Stream from sink ...");
        return Multi.createBy().replaying().ofMulti(this.sink);
    }

    public Uni<List<HashMap<String, Long[]>>> getAvg() {
        return Multi.createBy().replaying().ofMulti(this.sink).capDemandsTo(100L).collect().asList();
    }

    private HashMap<String, Long> toCompactTimes(List<HashMap<String, Long[]>> times) {
        int listLength = times.size();
        LOGGER.info(times);

        HashMap<String, Long> compactTimes = new HashMap<>();
        times.forEach((stringHashMap -> {
            stringHashMap.forEach((stage, time) -> {
                Long timeDiff = time[1] - time[0];
                if (!compactTimes.containsKey(stage)) {
                    compactTimes.put(stage, timeDiff);
                } else {
                    compactTimes.put(stage, compactTimes.get(stage) + timeDiff);
                }
            });
        }));

        compactTimes.replaceAll((key, value) -> (value / listLength) / 1_000_000);
        return compactTimes;
    }
}
