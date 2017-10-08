package com.datarepublic.simplecab.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class JdbcSimpleCabRepository implements SimpleCabRepository {

    private static final String SQL_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer getCountByMedallionAndPickupDatetime(String medallion, Date pickupDate) {

        String query = "select count(*) from cab_trip_data " +
                "where medallion = :medallion " +
                "and pickup_datetime >= :fromDate " +
                "and pickup_datetime <  :toDate";

        Map<String, Object> params = new HashMap<>();
        params.put("medallion", medallion);
        params.put("fromDate", fromDate(pickupDate));
        params.put("toDate", toDate(pickupDate));

        return namedParameterJdbcTemplate.queryForObject(query, params, Integer.class);
    }

    private String fromDate(Date pickupDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(pickupDate);

        // clear the time
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return new SimpleDateFormat(SQL_DATE_PATTERN).format(cal.getTime());
    }

    private String toDate(Date pickupDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(pickupDate);

        // clear the time
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        // add 1 day
        cal.add(Calendar.DATE, 1);

        return new SimpleDateFormat(SQL_DATE_PATTERN).format(cal.getTime());
    }
}