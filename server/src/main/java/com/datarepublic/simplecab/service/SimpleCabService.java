package com.datarepublic.simplecab.service;

import com.datarepublic.simplecab.model.MedallionsSummary;
import com.datarepublic.simplecab.repository.SimpleCabRepository;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class SimpleCabService {

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private final Logger logger = Logger.getLogger(this.getClass());

    private final ConcurrentMap<String, Integer> cache = new ConcurrentHashMap<>();

    @Resource
    private SimpleCabRepository repository;

    public MedallionsSummary getMedallionsSummary(List<String> medallions, Date pickupDate) {
        return getMedallionsSummary(medallions, pickupDate, false);
    }

    public MedallionsSummary getMedallionsSummary(List<String> medallions, Date pickupDate, boolean ignoreCache) {
        MedallionsSummary summary = new MedallionsSummary();

        for (String medallion : medallions) {
            summary.addTrip(medallion, getCount(medallion, pickupDate, ignoreCache));
        }

        return summary;
    }

    private Integer getCount(String medallion, Date pickupDate, boolean ignoreCache) {
        if (ignoreCache) {
            return repository.getCountByMedallionAndPickupDatetime(medallion, pickupDate);
        }

        String key = createKey(medallion, pickupDate);
        return cache.computeIfAbsent(key, _key -> repository.getCountByMedallionAndPickupDatetime(medallion, pickupDate));
    }

    private String createKey(String medallion, Date pickupDate) {
        return medallion + "|" + new SimpleDateFormat(DATE_PATTERN).format(pickupDate); // only the date part matters
    }

    public void invalidateCache() {
        cache.clear();
        logger.warn("Cache invalidated.");
    }
}
