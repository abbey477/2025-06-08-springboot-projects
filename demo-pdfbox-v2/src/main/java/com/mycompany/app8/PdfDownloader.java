package com.mycompany.app8;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PdfDownloader {

    public static void main(String[] args) {
        String url = "https://www.oecd.org/content/dam/oecd/en/topics/policy-sub-issues/country-risk-classification/cre-crc-current-english.pdf";
        String outputFileName = "cre-crc-current-english.pdf";

        try {
            downloadPdf(url, outputFileName);
            System.out.println("PDF downloaded successfully: " + outputFileName);
        } catch (Exception e) {
            System.err.println("Error downloading PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void downloadPdf(String url, String outputFileName) throws Exception {
        // Configure request timeouts
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(30000)  // 30 seconds
                .setSocketTimeout(300000)  // 5 minutes
                .setConnectionRequestTimeout(30000)
                .build();

        // Create HttpClient with configuration
        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(config)
                .build()) {

            // Create GET request
            HttpGet httpGet = new HttpGet(url);

            // Set headers
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            httpGet.setHeader("Accept", "application/pdf,*/*");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate");

            // Execute request
            System.out.println("Downloading PDF from: " + url);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {

                // Check status code
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    throw new IOException("HTTP " + statusCode + ": " +
                            response.getStatusLine().getReasonPhrase());
                }

                // Get the response entity
                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    throw new IOException("No content received");
                }

                // Get content length for progress tracking
                long contentLength = entity.getContentLength();
                System.out.println("Content-Length: " +
                        (contentLength > 0 ? contentLength + " bytes" : "Unknown"));

                // Download using InputStream with progress tracking
                try (InputStream inputStream = entity.getContent();
                     FileOutputStream outputStream = new FileOutputStream(outputFileName);
                     BufferedOutputStream bufferedOutput = new BufferedOutputStream(outputStream)) {

                    byte[] buffer = new byte[8192]; // 8KB buffer
                    int bytesRead;
                    long totalBytesRead = 0;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        bufferedOutput.write(buffer, 0, bytesRead);
                        totalBytesRead += bytesRead;

                        // Show progress
                        if (contentLength > 0) {
                            double progress = (double) totalBytesRead / contentLength * 100;
                            System.out.printf("\rProgress: %.1f%% (%d/%d bytes)",
                                    progress, totalBytesRead, contentLength);
                        }
                    }

                    System.out.println("\nDownload completed. Total bytes: " + totalBytesRead);
                }

                // Ensure the entity is fully consumed
                EntityUtils.consume(entity);
            }
        }
    }

    // Alternative simple method using Files.copy()
    public static void downloadPdfSimple(String url, String outputFileName) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("User-Agent", "Java Apache HttpClient");

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new IOException("HTTP " + response.getStatusLine().getStatusCode());
                }

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    try (InputStream inputStream = entity.getContent()) {
                        Files.copy(inputStream, Paths.get(outputFileName));
                    }
                    EntityUtils.consume(entity);
                }
            }
        }
    }

    // Method with response headers logging
    public static void downloadPdfWithHeaders(String url, String outputFileName) throws Exception {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(30000)
                .setSocketTimeout(300000)
                .setRedirectsEnabled(true)
                .setMaxRedirects(5)
                .build();

        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(config)
                .build()) {

            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "application/pdf,application/octet-stream,*/*");
            httpGet.setHeader("User-Agent", "Java Apache HttpClient PDF Downloader/1.0");

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {

                // Log response headers
                System.out.println("Response Status: " + response.getStatusLine());
                System.out.println("Response Headers:");
                for (org.apache.http.Header header : response.getAllHeaders()) {
                    System.out.println(header.getName() + ": " + header.getValue());
                }
                System.out.println();

                if (response.getStatusLine().getStatusCode() >= 200 &&
                        response.getStatusLine().getStatusCode() < 300) {

                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        // Check content type
                        String contentType = entity.getContentType() != null ?
                                entity.getContentType().getValue() : "Unknown";
                        System.out.println("Content-Type: " + contentType);

                        try (InputStream inputStream = entity.getContent();
                             FileOutputStream fos = new FileOutputStream(outputFileName)) {

                            // Copy stream
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                fos.write(buffer, 0, bytesRead);
                            }
                        }
                        EntityUtils.consume(entity);
                    }
                } else {
                    throw new IOException("Failed to download: " + response.getStatusLine());
                }
            }
        }
    }

    // Method with custom SSL and proxy support
    public static void downloadPdfAdvanced(String url, String outputFileName) throws Exception {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(30000)
                .setSocketTimeout(300000)
                .setConnectionRequestTimeout(30000)
                .setRedirectsEnabled(true)
                .build();

        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(config)
                .setUserAgent("Apache-HttpClient/4.5.14 (Java PDF Downloader)")
                .build()) {

            HttpGet httpGet = new HttpGet(url);

            // Add comprehensive headers
            httpGet.setHeader("Accept", "application/pdf,application/octet-stream,*/*;q=0.8");
            httpGet.setHeader("Accept-Language", "en-US,en;q=0.5");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate");
            httpGet.setHeader("Connection", "keep-alive");
            httpGet.setHeader("Upgrade-Insecure-Requests", "1");

            System.out.println("Starting download...");
            long startTime = System.currentTimeMillis();

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    throw new IOException("HTTP " + statusCode + ": " +
                            response.getStatusLine().getReasonPhrase());
                }

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    long contentLength = entity.getContentLength();

                    try (InputStream inputStream = entity.getContent();
                         BufferedInputStream bis = new BufferedInputStream(inputStream);
                         FileOutputStream fos = new FileOutputStream(outputFileName);
                         BufferedOutputStream bos = new BufferedOutputStream(fos)) {

                        byte[] buffer = new byte[16384]; // 16KB buffer
                        int bytesRead;
                        long totalBytes = 0;
                        long lastProgressTime = System.currentTimeMillis();

                        while ((bytesRead = bis.read(buffer)) != -1) {
                            bos.write(buffer, 0, bytesRead);
                            totalBytes += bytesRead;

                            // Update progress every 500ms
                            long currentTime = System.currentTimeMillis();
                            if (currentTime - lastProgressTime > 500) {
                                if (contentLength > 0) {
                                    double progress = (double) totalBytes / contentLength * 100;
                                    double speed = totalBytes / ((currentTime - startTime) / 1000.0) / 1024; // KB/s
                                    System.out.printf("\rProgress: %.1f%% (%.1f KB/s)   ", progress, speed);
                                }
                                lastProgressTime = currentTime;
                            }
                        }

                        long endTime = System.currentTimeMillis();
                        double totalTime = (endTime - startTime) / 1000.0;
                        double avgSpeed = totalBytes / totalTime / 1024; // KB/s

                        System.out.printf("\nDownload completed: %d bytes in %.1f seconds (%.1f KB/s)\n",
                                totalBytes, totalTime, avgSpeed);
                    }
                    EntityUtils.consume(entity);
                }
            }
        }
    }
}