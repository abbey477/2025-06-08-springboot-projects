package com.mycompany.app4;

import com.mycompany.app.ResourceFileHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.List;
import java.util.ArrayList;

/**
 * Simple usage example for OECD Country Risk Classification PDF parser
 * Updated to support both file path and InputStream (useful for downloaded PDFs)
 */
public class SimpleUsage {
    
    public static void main(String[] args) {
        try {
            // Example 1: Using file path (when PDF is saved locally)
            processFromFilePath();
            
            // Example 2: Using InputStream (when PDF is downloaded/streamed)
            processFromInputStream();
            
        } catch (IOException e) {
            System.err.println("Error processing PDF file:");
            System.err.println("  " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error occurred:");
            System.err.println("  " + e.getMessage());
        }
    }
    
    /**
     * Process PDF using file path (original method)
     */
    private static void processFromFilePath() throws IOException {
        System.out.println("=== PROCESSING FROM FILE PATH ===");
        
        // Step 1: Set your PDF file path
        File filePath = ResourceFileHelper.getResourceFile("pdf/cre-crc-current-english.pdf");
        String pdfPath = filePath.getAbsolutePath();
        
        // Step 2: Create the parser
        OECDParser parser = new OECDParser();
        
        // Step 3: Process pages 1-5 with standard boundaries
        List<TableRow> allData = new ArrayList<>();
        
        for (int pageNum = 1; pageNum <= 5; pageNum++) {
            List<TableRow> pageData = parser.parsePage(pdfPath, pageNum);
            allData.addAll(pageData);
        }
        
        // Step 4: Process page 6 with custom boundaries
        TableBoundaries lastPageBoundaries = new TableBoundaries(50f, 590f, 140f, 600f);
        List<TableRow> lastPageData = parser.parsePage(pdfPath, 6, lastPageBoundaries);
        allData.addAll(lastPageData);
        
        System.out.println("File path method - Total rows: " + allData.size());
    }
    
    /**
     * Process PDF using InputStream (useful for downloaded PDFs)
     */
    private static void processFromInputStream() throws IOException {
        System.out.println("\n=== PROCESSING FROM INPUTSTREAM ===");
        
        // Step 1: Get InputStream (this example uses FileInputStream, but could be from HTTP download)
        File filePath = ResourceFileHelper.getResourceFile("pdf/cre-crc-current-english.pdf");
        String pdfPath = filePath.getAbsolutePath();
        
        // Step 2: Create the parser
        OECDParser parser = new OECDParser();
        
        // Step 3: Process pages 1-5 with standard boundaries
        List<TableRow> allData = new ArrayList<>();
        
        for (int pageNum = 1; pageNum <= 5; pageNum++) {
            // Create new InputStream for each page (required since stream gets consumed)
            try (InputStream pdfStream = new FileInputStream(pdfPath)) {
                List<TableRow> pageData = parser.parsePage(pdfStream, pageNum);
                allData.addAll(pageData);
            }
        }
        
        // Step 4: Process page 6 with custom boundaries
        TableBoundaries lastPageBoundaries = new TableBoundaries(50f, 590f, 140f, 400f);
        try (InputStream pdfStream = new FileInputStream(pdfPath)) {
            List<TableRow> lastPageData = parser.parsePage(pdfStream, 6, lastPageBoundaries);
            allData.addAll(lastPageData);
        }
        
        System.out.println("InputStream method - Total rows: " + allData.size());
        
        // Example: Display sample data
        System.out.println("\nSample data from InputStream processing:");
        for (int i = 0; i < Math.min(3, allData.size()); i++) {
            TableRow row = allData.get(i);
            System.out.printf("Row %d: [%s] | [%s] | [%s]%n", 
                i + 1, row.getCell(0), row.getCell(1), row.getCell(2));
        }
    }
    
    /**
     * Example method for processing downloaded PDF (simulated)
     * In real usage, you would get the InputStream from HTTP response
     */
    public static List<TableRow> processDownloadedPDF(InputStream downloadedPdfStream) throws IOException {
        OECDParser parser = new OECDParser();
        List<TableRow> allData = new ArrayList<>();
        
        // Note: In real usage, you'd need to handle the fact that InputStream can only be read once
        // You might need to save to temporary file or use ByteArrayInputStream for multiple reads
        
        // Process pages 1-5
        for (int pageNum = 1; pageNum <= 5; pageNum++) {
            List<TableRow> pageData = parser.parsePage(downloadedPdfStream, pageNum);
            allData.addAll(pageData);
        }
        
        // Process page 6 with custom boundaries
        TableBoundaries lastPageBoundaries = new TableBoundaries(50f, 590f, 140f, 600f);
        List<TableRow> lastPageData = parser.parsePage(downloadedPdfStream, 6, lastPageBoundaries);
        allData.addAll(lastPageData);
        
        return allData;
    }
}