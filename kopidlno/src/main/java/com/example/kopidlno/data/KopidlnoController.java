package com.example.kopidlno.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;

@Component
public class KopidlnoController {

    @Autowired
    private KopidlnoDownloader kopidlnoDownloader;

    @Autowired
    private KopidlnoParser kopidlnoParser;

    @GetMapping("/process")
    public String processXml() {
        try {
            File xmlFile = kopidlnoDownloader.downloadAndExtractZip("https://www.smartform.cz/download/kopidlno.xml.zip", "output");
            kopidlnoParser.parseAndSaveData(xmlFile);
            return "Data processed successfully";
        } catch (IOException | XMLStreamException e) {
            return "Error processing data: " + e.getMessage();
        }
    }

}

