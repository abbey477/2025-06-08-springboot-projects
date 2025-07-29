package com.mycompany.app4.main;

import com.mycompany.app4.domain.BoundaryFactory;
import com.mycompany.app4.process.TableBoundaries;
import com.mycompany.app4.process.TableRow;
import com.mycompany.app4.process.OECDParser;
import com.mycompany.app4.util.ResourceFileHelper;

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

    /**
     * Process PDF using file path (original method)
     */
    public static void processFromFilePath(String pdfPath) throws IOException {
        System.out.println("=== PROCESSING FROM FILE PATH ===");
        
        // Step 1: Set boundaries
        TableBoundaries boundaries = new TableBoundaries(BoundaryFactory.getPageOneToFiveBoundary());
        
        // Step 2: Create the parser
        OECDParser parser = new OECDParser(boundaries);
        
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
    public static void processFromInputStream(String pdfPath ) throws IOException {
        System.out.println("\n=== PROCESSING FROM INPUTSTREAM ===");
        
        // Step 1: Get InputStream (this example uses FileInputStream, but could be from HTTP download)
        
        // Step 2: Create the parser
        TableBoundaries boundaries = new TableBoundaries(BoundaryFactory.getPageOneToFiveBoundary());
        OECDParser parser = new OECDParser(boundaries);
        
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
    private static List<TableRow> processDownloadedPDF(InputStream downloadedPdfStream) throws IOException {
        TableBoundaries boundaries = new TableBoundaries(BoundaryFactory.getPageOneToFiveBoundary());
        OECDParser parser = new OECDParser(boundaries);
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