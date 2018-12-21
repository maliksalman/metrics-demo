package com.smalik.metrics.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.distribution.HistogramSnapshot;
import io.micrometer.core.instrument.distribution.ValueAtPercentile;
import io.micrometer.core.instrument.step.StepMeterRegistry;
import io.micrometer.core.instrument.step.StepRegistryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class LoggingMeterRegistry extends StepMeterRegistry {

    private Logger log = LoggerFactory.getLogger(LoggingMeterRegistry.class);

    public LoggingMeterRegistry(StepRegistryConfig config, Clock clock) {
        super(config, clock);
    }

    @Override
    protected void publish() {

        ObjectMapper mapper = new ObjectMapper();
        for (Meter meter : getMeters()) {
            try {
                if (meter instanceof Timer) {
                    for (Map<String, Object> map : convertToListOfMaps((Timer) meter)) {
                        log.info(mapper.writeValueAsString(map));
                    }
                } else if (meter instanceof Counter){
                    Map<String, Object> map = convertToMap((Counter) meter);
                    if (map != null) {
                        log.info(mapper.writeValueAsString(map));
                    }
                }
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private List<Map<String, Object>> convertToListOfMaps(Timer timer) {

        ArrayList<Map<String, Object>> list = new ArrayList<>();
        HistogramSnapshot histogramSnapshot = timer.takeSnapshot();

        list.add(convertToMap(timer, "count", histogramSnapshot.count(), false));
        list.add(convertToMap(timer, "max", (int) histogramSnapshot.max(timer.baseTimeUnit()), true));
        list.add(convertToMap(timer, "mean", (int) histogramSnapshot.mean(timer.baseTimeUnit()), true));

        for (ValueAtPercentile percentile : histogramSnapshot.percentileValues()) {
            list.add(convertToMap(timer, "p" + ((int)(percentile.percentile()*100)), (int)percentile.value(timer.baseTimeUnit()), true));
        }

        return list;
    }

    private Map<String, Object> convertToMap(Counter timer) {
        for(Measurement measurement: timer.measure()) {
            if (measurement.getStatistic() == Statistic.COUNT)
                return convertToMap(timer, null, (int)measurement.getValue(), false);
        }
        return null;
    }

    private Map<String, Object> convertToMap(Meter meter, String name, Number value, boolean includeBaseUnit) {

        HashMap<String, Object> map = new HashMap<>();
        if (name != null) {
            map.put(meter.getId().getName() + "." + name, value);
        } else {
            map.put(meter.getId().getName(), value);
        }

        if (includeBaseUnit) {
            map.put("unit", meter.getId().getBaseUnit());
        }

        HashMap<String, String> tags = new HashMap<>();
        map.put("tags", tags);

        for (Tag tag : meter.getId().getTags()) {
            tags.put(tag.getKey(), tag.getValue());
        }

        return map;
    }

    @Override
    protected TimeUnit getBaseTimeUnit() {
        return TimeUnit.MILLISECONDS;
    }
}