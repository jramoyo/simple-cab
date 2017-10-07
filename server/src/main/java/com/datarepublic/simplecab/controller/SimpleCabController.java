package com.datarepublic.simplecab.controller;

import com.datarepublic.simplecab.model.MedallionsSummary;
import com.datarepublic.simplecab.service.SimpleCabService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
public final class SimpleCabController {

    @Resource
    private SimpleCabService service;

    @RequestMapping(value = "/trips/count", method = RequestMethod.GET)
    public @ResponseBody
    MedallionsSummary getMedallionsSummary(
            @RequestParam(name = "medallion") List<String> medallions,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date pickupDate,
            @RequestParam(required = false, defaultValue = "false") boolean ignoreCache) {

        return service.getMedallionsSummary(medallions, pickupDate, ignoreCache);
    }

    @RequestMapping(value = "/cache", method = RequestMethod.DELETE)
    public void invalidateCache() {
        service.invalidateCache();
    }

}
