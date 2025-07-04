package com.example.pdfextractor.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class ResourceFileUtil {

    private static final String DATA_FOLDER = "data/";

    /**
     * Read file content as String from resources/data directory
     * @param filename the name of the file
     * @return file content as String
     * @throws IOException if file cannot be read
     */
    public String readFileAsString(String filename) throws IOException {
        Resource resource = new ClassPathResource(DATA_FOLDER + filename);
        try (InputStream inputStream = resource.getInputStream()) {
            byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

    /**
     * Read file content as List of lines from resources/data directory
     * @param filename the name of the file
     * @return file content as List of Strings
     * @throws IOException if file cannot be read
     */
    public List<String> readFileAsLines(String filename) throws IOException {
        Resource resource = new ClassPathResource(DATA_FOLDER + filename);
        try (InputStream inputStream = resource.getInputStream()) {
            return Files.readAllLines(Paths.get(resource.getURI()), StandardCharsets.UTF_8);
        }
    }

    /**
     * Read file as InputStream from resources/data directory
     * @param filename the name of the file
     * @return InputStream of the file
     * @throws IOException if file cannot be read
     */
    public InputStream readFileAsInputStream(String filename) throws IOException {
        Resource resource = new ClassPathResource(DATA_FOLDER + filename);
        return resource.getInputStream();
    }

    /**
     * Read file as byte array from resources/data directory
     * @param filename the name of the file
     * @return file content as byte array
     * @throws IOException if file cannot be read
     */
    public byte[] readFileAsBytes(String filename) throws IOException {
        Resource resource = new ClassPathResource(DATA_FOLDER + filename);
        try (InputStream inputStream = resource.getInputStream()) {
            return FileCopyUtils.copyToByteArray(inputStream);
        }
    }

    /**
     * Check if file exists in resources/data directory
     * @param filename the name of the file
     * @return true if file exists, false otherwise
     */
    public boolean fileExists(String filename) {
        Resource resource = new ClassPathResource(DATA_FOLDER + filename);
        return resource.exists();
    }

    /**
     * Get file size in bytes from resources/data directory
     * @param filename the name of the file
     * @return file size in bytes
     * @throws IOException if file cannot be accessed
     */
    public long getFileSize(String filename) throws IOException {
        Resource resource = new ClassPathResource(DATA_FOLDER + filename);
        return resource.contentLength();
    }
}