package com.datarepublic.simplecab.controller;

import com.datarepublic.simplecab.model.MedallionsSummary;
import com.datarepublic.simplecab.service.SimpleCabService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static com.datarepublic.simplecab.Constants.DATE_PATTERN;

@RestController
public final class SimpleCabController {

    @Resource
    private SimpleCabService service;

    @RequestMapping(value = "/trips/count", method = RequestMethod.GET)
    public @ResponseBody
    MedallionsSummary getMedallionsSummary(
            @RequestParam(name = "medallion") List<String> medallions,
            @RequestParam @DateTimeFormat(pattern = DATE_PATTERN) Date pickupDate,
            @RequestParam(required = false, defaultValue = "false") boolean ignoreCache) {

        return service.getMedallionsSummary(medallions, pickupDate, ignoreCache);
    }

    @RequestMapping(value = "/trips/count/cache", method = RequestMethod.DELETE)
    public void invalidateCache() {
        service.invalidateCache();
    }

}
