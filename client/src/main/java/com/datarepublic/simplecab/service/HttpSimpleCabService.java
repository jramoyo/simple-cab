package com.datarepublic.simplecab.service;

import com.datarepublic.simplecab.model.MedallionsSummary;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.datarepublic.simplecab.Constants.DATE_PATTERN;

public final class HttpSimpleCabService implements SimpleCabService {

    private final String baseUrl;
    private final ScriptEngine scriptEngine;

    public HttpSimpleCabService(String baseUrl) {
        this.baseUrl = baseUrl;
        this.scriptEngine = new ScriptEngineManager()
                .getEngineByName("javascript");
    }

    @Override
    public void deleteCache() throws IOException {
        URL url = new URL(baseUrl + "/trips/count/cache");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("DELETE");
        con.getResponseCode();
    }

    @Override
    public MedallionsSummary getMedallionsSummary(List<String> medallions, Date pickupDate) throws IOException {
        return getMedallionsSummary(medallions, pickupDate, false);
    }

    @Override
    public MedallionsSummary getMedallionsSummary(List<String> medallions, Date pickupDate, boolean ignoreCache) throws IOException {
        String queryString = buildQueryString(medallions, pickupDate, ignoreCache);
        URL url = new URL(baseUrl + "/trips/count?" + queryString);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        Map<String, Object> json = getResponseBody(con);

        return newMedallionsSummary(json);
    }

    private String buildQueryString(List<String> medallions, Date pickupDate, boolean ignoreCache) {
        String queryString = medallions.stream().map(s -> "medallion=" + s).collect(Collectors.joining("&"));

        if (pickupDate != null) {
            queryString += "&pickupDate=" + new SimpleDateFormat(DATE_PATTERN).format(pickupDate);
        }

        if (ignoreCache) {
            queryString += "&ignoreCache=true";
        }

        return queryString;
    }


    @SuppressWarnings("unchecked")
    private Map<String, Object> getResponseBody(HttpURLConnection con) throws IOException {

        try {

            StringBuilder sb = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
            }

            String script = "Java.asJSONCompatible(" + sb.toString() + ")";
            return (Map<String, Object>) this.scriptEngine.eval(script);

        } catch (ScriptException ex) {
            return Collections.emptyMap();
        }

    }

    private MedallionsSummary newMedallionsSummary(Map<String, Object> json) {
        MedallionsSummary medallionsSummary = new MedallionsSummary();

        @SuppressWarnings("unchecked")
        Map<String, Object> trips = (Map<String, Object>) json.get("trips");
        (trips).forEach((k, v) -> medallionsSummary.addTrip(k, Integer.parseInt(v.toString())));

        return medallionsSummary;
    }

}
