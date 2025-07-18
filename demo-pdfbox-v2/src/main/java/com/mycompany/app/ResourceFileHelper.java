package com.mycompany.app;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class ResourceFileHelper {

    /**
     * Get a File from the resources directory
     * @param fileName - name of file in src/main/resources/
     * @return File object pointing to the resource file
     */
    public static File getResourceFile(String fileName) {
        try {
            // Get the resource URL
            URL resourceUrl = ResourceFileHelper.class.getClassLoader().getResource(fileName);

            if (resourceUrl == null) {
                throw new RuntimeException("File not found in resources: " + fileName);
            }

            // Convert URL to File
            return new File(resourceUrl.toURI());

        } catch (Exception e) {
            throw new RuntimeException("Error loading resource file: " + fileName, e);
        }
    }

    /**
     * Alternative method - get resource as InputStream
     * Use this if the file is inside a JAR
     */
    public static InputStream getResourceAsStream(String fileName) {
        InputStream stream = ResourceFileHelper.class.getClassLoader().getResourceAsStream(fileName);

        if (stream == null) {
            throw new RuntimeException("File not found in resources: " + fileName);
        }

        return stream;
    }

    /**
     * Get absolute path to resource file
     */
    public static String getResourcePath(String fileName) {
        return getResourceFile(fileName).getAbsolutePath();
    }

    // Example usage
    public static void main(String[] args) {
        try {
            // For file in src/main/resources/pdf/oecd-report.pdf
            File pdfFile = getResourceFile("pdf/oecd-report.pdf");
            System.out.println("PDF Path: " + pdfFile.getAbsolutePath());

            // Just get the path as string
            String filePath = getResourcePath("pdf/oecd-report.pdf");
            System.out.println("File Path: " + filePath);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}