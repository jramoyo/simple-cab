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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpSimpleCabService implements SimpleCabService {

    private final String baseUrl;
    private final ScriptEngine scriptEngine;

    public HttpSimpleCabService(String baseUrl) {
        this.baseUrl = baseUrl;
        this.scriptEngine = new ScriptEngineManager()
                .getEngineByName("javascript");
    }

    @Override
    public void deleteCache() {
        try {

            URL url = new URL(baseUrl + "/trips/count/cache");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");

            int responseCode = con.getResponseCode();

        } catch (Exception ex) {

        }
    }

    @Override
    public MedallionsSummary getMedallionsSummary(List<String> medallions, Date pickupDate) {
        return getMedallionsSummary(medallions, pickupDate, false);
    }

    @Override
    public MedallionsSummary getMedallionsSummary(List<String> medallions, Date pickupDate, boolean ignoreCache) {

        try {

            String queryString = buildQueryString(medallions, pickupDate, ignoreCache);
            URL url = new URL(baseUrl + "/trips/count?" + queryString);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            Map<String, Object> json = getResponseBody(con);

            return newMedallionsSummary(json);

        } catch (Exception ex) {

            return null;

        }


    }

    private String buildQueryString(List<String> medallions, Date pickupDate, boolean ignoreCache) {
        String queryString = medallions.stream().map(s -> "medallion=" + s).collect(Collectors.joining("&"));

        if (pickupDate != null) {
            queryString += "&pickupDate=" + new SimpleDateFormat("yyyy-MM-dd").format(pickupDate);
        }

        if (ignoreCache) {
            queryString += "&ignoreCache=true";
        }

        return queryString;
    }


    private Map<String, Object> getResponseBody(HttpURLConnection con) throws IOException, ScriptException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
        }

        String script = "Java.asJSONCompatible(" + sb.toString() + ")";
        return (Map<String, Object>) this.scriptEngine.eval(script);
    }

    private MedallionsSummary newMedallionsSummary(Map<String, Object> json) {
        MedallionsSummary medallionsSummary = new MedallionsSummary();

        Map<String, Object> trips = (Map<String, Object>) json.get("trips");
        (trips).forEach((k, v) -> medallionsSummary.addTrip(k, Integer.parseInt(v.toString())));

        return medallionsSummary;
    }

}
