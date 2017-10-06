package com.datarepublic.simplecab.service;

import com.datarepublic.simplecab.model.MedallionsSummary;
import com.datarepublic.simplecab.repository.SimpleCabRepository;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class SimpleCabService {

    private static final String DATE_PATTERN = "YYYY-MM-dd";

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

        String key = medallion + "|" + toString(pickupDate);
        return cache.computeIfAbsent(key, _key -> repository.getCountByMedallionAndPickupDatetime(medallion, pickupDate));
    }

    private String toString(Date pickupDate) {
        return new SimpleDateFormat(DATE_PATTERN).format(pickupDate);
    }

    public void invalidateCache() {
        cache.clear();
    }
}
