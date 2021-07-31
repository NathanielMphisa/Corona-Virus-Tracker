package com.buildappswnathaniel.tracker.coronavirustracker.services;

import com.buildappswnathaniel.tracker.coronavirustracker.model.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronaVirusDataService {
    // this is a service class that will fetch data for us.
    // when the application calls its going to make a call for the data using a url

//    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/07-28-2020.csv";
    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/archived_data/archived_time_series/time_series_2019-ncov-Confirmed.csv";
    private List<LocationStats> locationStats = new ArrayList<>();

    public List<LocationStats> getLocationStats() {
        return locationStats;
    }

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        List<LocationStats> list = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();
        HttpResponse<String> httpResponse = client.send(request,HttpResponse.BodyHandlers.ofString());

        StringReader cvsBodyReader = new StringReader(httpResponse.body());

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(cvsBodyReader);
        for (CSVRecord record : records) {

              LocationStats stats = new LocationStats();
              stats.setCountry(record.get("Country/Region"));
              stats.setState(record.get("Province/State"));
              int previousDayCases = Integer.parseInt(record.get(record.size()- 2));
              int latestCases = Integer.parseInt(record.get(record.size()- 1));
              stats.setTotalCases(latestCases);
              stats.setDifPrevDay(latestCases - previousDayCases);
//              System.out.println(stats);
              list.add(stats);
        }
        this.locationStats = list;
    }
}

