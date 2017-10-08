package com.datarepublic.simplecab.service;

import com.datarepublic.simplecab.model.MedallionsSummary;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.BufferedReader;
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

            String queryString = medallions.stream().map(s -> "medallion=" + s).collect(Collectors.joining("&"));

            if (pickupDate != null) {
                queryString += "&pickupDate=" + new SimpleDateFormat("yyyy-MM-dd").format(pickupDate);
            }

            if (ignoreCache) {
                queryString += "&ignoreCache=true";
            }

            URL url = new URL(baseUrl + "/trips/count?" + queryString);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            StringBuilder sb = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
            }

            String jsonString = sb.toString();

            String script = "Java.asJSONCompatible(" + jsonString + ")";
            Map<String, Object> json = (Map<String, Object>) this.scriptEngine.eval(script);

            MedallionsSummary medallionsSummary = new MedallionsSummary();
            ((Map<String, Object>) json.get("trips")).forEach((k, v) -> {
                medallionsSummary.addTrip(k, Integer.parseInt(v.toString()));
            });

            return medallionsSummary;

        } catch (Exception ex) {

            return null;

        }


    }

}
