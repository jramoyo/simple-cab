package com.datarepublic.simplecab.service;

import com.datarepublic.simplecab.model.MedallionsSummary;
import com.datarepublic.simplecab.repository.SimpleCabRepository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

public final class SimpleCabService {

    @Resource
    private SimpleCabRepository repository;

    public MedallionsSummary getMedallionsSummary(List<String> medallions, Date pickupDate, boolean ignoreCache) {
        return null;
    }
}
