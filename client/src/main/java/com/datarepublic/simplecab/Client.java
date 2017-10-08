package com.datarepublic.simplecab;

import com.datarepublic.simplecab.model.MedallionsSummary;
import com.datarepublic.simplecab.service.HttpSimpleCabService;
import com.datarepublic.simplecab.service.SimpleCabService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.datarepublic.simplecab.Constants.DATE_PATTERN;

public class Client {

    public static final String CMD_TRIP_COUNT = "trip-count";
    public static final String CMD_DELETE_CACHE = "delete-cache";
    public static final String CMD_HELP = "help";
    private final SimpleCabService simpleCabService;

    public Client(String baseUrl) {
        this.simpleCabService = new HttpSimpleCabService(baseUrl);
    }

    private static void tripCount(String[] args) {
        if (args.length < 4) {
            help();
            return;
        }

        try {

            String baseUrl = args[1];
            List<String> medallions = Arrays.asList(args[2].split(","));
            Date pickupDate = new SimpleDateFormat(DATE_PATTERN).parse(args[3]);
            boolean ignoreCache = false;
            if (args.length == 5 && args[4].equals("--ignore-cache")) {
                ignoreCache = true;
            }

            Client client = new Client(baseUrl);
            MedallionsSummary summary = client.simpleCabService.getMedallionsSummary(medallions, pickupDate, ignoreCache);
            summary.getTrips().forEach((k, v) -> System.out.println(k + " -> " + v));

        } catch (ParseException ex) {

            System.err.println("Pickup date must be in " + DATE_PATTERN + " format.");
            ex.printStackTrace();

        } catch (Exception ex) {

            System.err.println("Cannot get trip summary.");
            ex.printStackTrace();

        }
    }

    private static void deleteCache(String[] args) {
        if (args.length < 2) {
            help();
            return;
        }

        try {

            String baseUrl = args[1];
            Client client = new Client(baseUrl);
            client.simpleCabService.deleteCache();
            System.out.println("Done.");

        } catch (Exception ex) {

            System.err.println("Cannot delete cache.");
            ex.printStackTrace();

        }
    }

    private static void help() {
        StringBuilder sb = new StringBuilder();

        sb.append("usage: Client [command] [args]").append("\n\n");
        sb.append("Commands:").append("\n\n");

        sb.append(CMD_TRIP_COUNT).append(" [base_url] [medallion<,medallion>] [pickup_date] [--ignore-cache]").append('\n')
                .append('\t').append("base_url       - base url of the server (http://localhost:8080)").append('\n')
                .append('\t').append("medallion      - comma-separated medallions (D7D598CD99978BD012A87A76A7C891B7,5455D5FF2BD94D10B304A15D4B7F2735)").append('\n')
                .append('\t').append("pickup_date    - pickup date in " + DATE_PATTERN + " format (2013-12-01)").append('\n')
                .append('\t').append("--ignore-cache - whether to ignore the server cache").append("\n\n");

        sb.append(CMD_DELETE_CACHE).append(" [base_url]").append('\n')
                .append('\t').append("base_url       - base url of the server (http://localhost:8080)").append("\n\n");

        sb.append(CMD_HELP).append('\n');

        System.out.println(sb.toString());
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            help();
        } else {
            String command = args[0];
            switch (command) {
                case CMD_TRIP_COUNT:
                    tripCount(args);
                    break;
                case CMD_DELETE_CACHE:
                    deleteCache(args);
                    break;
                default:
                    help();
                    break;
            }
        }
    }

}
