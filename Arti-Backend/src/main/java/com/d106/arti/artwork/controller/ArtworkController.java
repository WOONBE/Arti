package com.d106.arti.artwork.controller;



import com.d106.arti.artwork.service.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArtworkController {

    @Autowired
    private CSVService csvService;

    @GetMapping("/import-csv/{filename}")
    public String importCSV(@PathVariable String filename) {
        String filePath = "src/main/resources/" + filename;
        int limit = 100; // Limit the number of rows to 100
        csvService.readCSVAndSaveData(filePath, limit);
        return "Successfully imported 100 records from " + filename;
    }
}
