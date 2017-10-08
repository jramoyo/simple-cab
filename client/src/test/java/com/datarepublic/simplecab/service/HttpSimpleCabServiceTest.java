package com.datarepublic.simplecab.service;

import com.datarepublic.simplecab.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(BlockJUnit4ClassRunner.class)
public class HttpSimpleCabServiceTest {

    private HttpSimpleCabService service;

    @Before
    public void setup() {
        service = new HttpSimpleCabService("http://localhost:8080");
    }

    @Test
    public void buildQueryString_builds_the_correct_query_string() throws Exception {
        List<String> medallions = new ArrayList<>();
        medallions.add("abc");
        medallions.add("cde");
        medallions.add("efg");

        Date pickupDate = new SimpleDateFormat(Constants.DATE_PATTERN).parse("2013-12-01");

        String queryString = service.buildQueryString(medallions, pickupDate, false);
        assertThat(queryString, is("medallion=abc&medallion=cde&medallion=efg&pickupDate=2013-12-01"));
    }

    @Test
    public void buildQueryString_builds_the_correct_query_string_with_ignoreCache() throws Exception {
        List<String> medallions = new ArrayList<>();
        medallions.add("abc");
        medallions.add("cde");
        medallions.add("efg");

        Date pickupDate = new SimpleDateFormat(Constants.DATE_PATTERN).parse("2013-12-01");

        String queryString = service.buildQueryString(medallions, pickupDate, true);
        assertThat(queryString, is("medallion=abc&medallion=cde&medallion=efg&pickupDate=2013-12-01&ignoreCache=true"));
    }


    @Test(expected = IllegalArgumentException.class)
    public void buildQueryString_throws_an_exception_if_medallions_is_empty() throws Exception {
        List<String> medallions = new ArrayList<>();
        Date pickupDate = new SimpleDateFormat(Constants.DATE_PATTERN).parse("2013-12-01");

        service.buildQueryString(medallions, pickupDate, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildQueryString_throws_an_exception_if_medallions_is_null() throws Exception {
        Date pickupDate = new SimpleDateFormat(Constants.DATE_PATTERN).parse("2013-12-01");

        service.buildQueryString(null, pickupDate, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildQueryString_throws_an_exception_if_pickupDate_is_null() throws Exception {
        List<String> medallions = new ArrayList<>();
        medallions.add("abc");
        medallions.add("cde");
        medallions.add("efg");

        service.buildQueryString(medallions, null, false);
    }
}
