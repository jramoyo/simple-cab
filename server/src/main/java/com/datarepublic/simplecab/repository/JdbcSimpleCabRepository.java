package com.datarepublic.simplecab.repository;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.Date;

public final class JdbcSimpleCabRepository implements SimpleCabRepository {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public Integer getCountByMedallionAndPickupDatetime(String medallion, Date pickupDate) {
//        select count(*) from cab_trip_data
//            where medallion = 'D7D598CD99978BD012A87A76A7C891B7'
//            and pickup_datetime >= '2013-12-01 00:00:00'
//            and pickup_datetime <  '2013-12-02 00:00:00';
        return null;
    }
}
