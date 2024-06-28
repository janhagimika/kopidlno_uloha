package com.example.kopidlno.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;

@Service
public class KopidlnoParser {
    @Autowired
    private DatabaseService databaseService;
    private static final String VF_NAMESPACE = "urn:cz:isvs:ruian:schemas:VymennyFormatTypy:v1";

    public void parseAndSaveData(File xmlFile) throws FileNotFoundException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(xmlFile))) {
            XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

            Obec obec;
            CastObce castObce;
            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLStreamReader.START_ELEMENT) {
                    String localName = reader.getLocalName();
                    String namespaceURI = reader.getNamespaceURI();

                    if (VF_NAMESPACE.equals(namespaceURI)) {
                        switch (localName) {
                            case "Obec":
                                obec = new Obec();
                                System.out.println("Found Obec");
                                while (reader.hasNext()) {
                                    event = reader.next();
                                    if (event == XMLStreamReader.START_ELEMENT) {
                                        String nestedLocalName = reader.getLocalName();
                                        String prefix = reader.getPrefix();
                                        if ("obi".equals(prefix)) {
                                            switch (nestedLocalName) {
                                                case "Kod":
                                                    obec.setKod(Long.parseLong(reader.getElementText()));
                                                    System.out.println("Kod: " + obec.getKod());
                                                    break;
                                                case "Nazev":
                                                    obec.setNazev(reader.getElementText());
                                                    System.out.println("Nazev: " + obec.getNazev());
                                                    break;
                                            }
                                        }
                                    } else if (event == XMLStreamReader.END_ELEMENT && "Obec".equals(reader.getLocalName())) {
                                        break;
                                    }
                                }
                                if (obec.getKod() != null && obec.getNazev() != null) {
                                    System.out.println("Saving Obec: kod=" + obec.getKod() + ", nazev=" + obec.getNazev());
                                    databaseService.saveObec(obec);
                                } else {
                                    System.out.println("Skipping Obec with missing values: kod=" + obec.getKod() + ", nazev=" + obec.getNazev());
                                }
                                break;

                            case "CastObce":
                                castObce = new CastObce();
                                System.out.println("Found CastObce");
                                while (reader.hasNext()) {
                                    event = reader.next();
                                    if (event == XMLStreamReader.START_ELEMENT) {
                                        String nestedLocalName = reader.getLocalName();
                                        String prefix = reader.getPrefix();
                                        if ("coi".equals(prefix)) {
                                            switch (nestedLocalName) {
                                                case "Kod":
                                                    castObce.setKod(Long.parseLong(reader.getElementText()));
                                                    System.out.println("Kod: " + castObce.getKod());
                                                    break;
                                                case "Nazev":
                                                    castObce.setNazev(reader.getElementText());
                                                    System.out.println("Nazev: " + castObce.getNazev());
                                                    break;
                                            }
                                        }
                                        if ("Obec".equals(nestedLocalName) && "coi".equals(prefix)) {
                                            while (reader.hasNext()) {
                                                event = reader.next();
                                                if (event == XMLStreamReader.START_ELEMENT && "Kod".equals(reader.getLocalName()) && "obi".equals(reader.getPrefix())) {
                                                    castObce.setObecKod(Long.parseLong(reader.getElementText()));
                                                    System.out.println("Obec Kod: " + castObce.getObecKod());
                                                    break;
                                                } else if (event == XMLStreamReader.END_ELEMENT && "Obec".equals(reader.getLocalName())) {
                                                    break;
                                                }
                                            }
                                        }
                                    } else if (event == XMLStreamReader.END_ELEMENT && "CastObce".equals(reader.getLocalName())) {
                                        break;
                                    }
                                }
                                if (castObce.getKod() != null && castObce.getNazev() != null && castObce.getObecKod() != null) {
                                    System.out.println("Saving CastObce: kod=" + castObce.getKod() + ", nazev=" + castObce.getNazev() + ", obecKod=" + castObce.getObecKod());
                                    databaseService.saveCastObce(castObce);
                                } else {
                                    System.out.println("Skipping CastObce with missing values: kod=" + castObce.getKod() + ", nazev=" + castObce.getNazev() + ", obecKod=" + castObce.getObecKod());
                                }
                                break;
                        }
                    }
                } else if (event == XMLStreamReader.END_DOCUMENT) {
                    break;
                }
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Finished parsing and saving data.");
    }

}
