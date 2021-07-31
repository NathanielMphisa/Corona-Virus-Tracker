package com.buildappswnathaniel.tracker.coronavirustracker.controllers;

import com.buildappswnathaniel.tracker.coronavirustracker.model.LocationStats;
import com.buildappswnathaniel.tracker.coronavirustracker.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    CoronaVirusDataService coronaVirusDataService;

    @GetMapping("/")
    public String home(Model model){
        List<LocationStats> allStats = coronaVirusDataService.getLocationStats();
        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getTotalCases()).sum();
        model.addAttribute("locationstats", allStats);
        model.addAttribute("totalCases", totalReportedCases);
        return "home";
    }
}
