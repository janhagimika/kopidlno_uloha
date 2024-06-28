package com.example.kopidlno.data;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class KopidlnoDownloader {
    public File downloadAndExtractZip(String urlStr, String outputDir) throws IOException {
        URL url = new URL(urlStr);
        Path outputPath = Path.of(outputDir);
        Files.createDirectories(outputPath);
        File extractedFile = null;

        try (InputStream in = url.openStream();
             ZipInputStream zis = new ZipInputStream(new BufferedInputStream(in))) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path filePath = outputPath.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(filePath);
                } else {
                    Files.createDirectories(filePath.getParent());
                    try (OutputStream out = Files.newOutputStream(filePath)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            out.write(buffer, 0, len);
                        }
                    }
                    extractedFile = filePath.toFile();
                }
                zis.closeEntry();
            }
        }

        return extractedFile;
    }
}
