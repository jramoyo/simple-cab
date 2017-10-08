package com.datarepublic.simplecab;

import com.datarepublic.simplecab.model.MedallionsSummary;
import com.datarepublic.simplecab.service.HttpSimpleCabService;
import com.datarepublic.simplecab.service.SimpleCabService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Client {

    private final SimpleCabService simpleCabService;

    public Client(String baseUrl) {
        this.simpleCabService = new HttpSimpleCabService(baseUrl);
    }

    public static void main(String[] args) throws Exception {

        List<String> medallions = new ArrayList<>();
        medallions.add("D7D598CD99978BD012A87A76A7C891B7");
        medallions.add("5455D5FF2BD94D10B304A15D4B7F2735");

        Date pickupDate = new SimpleDateFormat("yyyy-MM-dd").parse("2013-12-01");

        Client client = new Client("http://localhost:8080");
        MedallionsSummary summary = client.simpleCabService.getMedallionsSummary(medallions, pickupDate);

        summary.getTrips().forEach((k, v) -> System.out.println(k + " -> " + v));
    }

}
