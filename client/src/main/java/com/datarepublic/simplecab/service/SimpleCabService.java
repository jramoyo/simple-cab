package com.datarepublic.simplecab.service;

import com.datarepublic.simplecab.model.MedallionsSummary;

import java.util.Date;
import java.util.List;

public interface SimpleCabService {

    void deleteCache();

    MedallionsSummary getMedallionsSummary(List<String> medallions, Date pickupDate);

    MedallionsSummary getMedallionsSummary(List<String> medallions, Date pickupDate, boolean ignoreCache);

}
