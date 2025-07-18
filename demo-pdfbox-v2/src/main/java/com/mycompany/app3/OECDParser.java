package com.mycompany.app3;

import com.mycompany.app.ResourceFileHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Simple OECD PDF parser - the main class you use to extract table data
 * This is the easiest way to get table data from OECD Country Risk PDFs
 */
public class OECDParser {
    
    private TableBoundaries boundaries;  // Current boundary settings
    
    /**
     * Constructor with default boundaries
     * Uses standard settings that work for most OECD Country Risk PDFs
     */
    public OECDParser() {
        this.boundaries = new TableBoundaries();
    }
    
    /**
     * Constructor with custom boundaries
     * Use this if the default settings don't work for your specific PDF
     * @param boundaries Custom table boundary settings
     */
    public OECDParser(TableBoundaries boundaries) {
        this.boundaries = boundaries;
    }
    
    /**
     * Parse a specific page using the current boundary settings
     * @param pdfPath Full path to your PDF file
     * @param pageNumber Page number to extract (1=first page, 2=second page, etc.)
     * @return List of table rows found on this page
     * @throws IOException If PDF file cannot be read
     */
    public List<TableRow> parsePage(File pdfPath, int pageNumber) throws IOException {
        // Create extractor with current boundaries
        PDFExtractor extractor = new PDFExtractor(boundaries);
        
        // Extract data from the specified page
        List<TableRow> rows = extractor.extractPage(pdfPath, pageNumber);
        
        // Print results to console for verification
        System.out.println("Page " + pageNumber + ": " + rows.size() + " rows");
        for (TableRow row : rows) {
            row.printRow();  // Print each row in format [col1]|[col2]|[col3]...
        }
        
        return rows;
    }
    
    /**
     * Parse a specific page with custom boundaries (useful for last page)
     * @param pdfPath Full path to your PDF file
     * @param pageNumber Page number to extract
     * @param customBounds Custom boundary settings for this specific page
     * @return List of table rows found on this page
     * @throws IOException If PDF file cannot be read
     */
    public List<TableRow> parsePage(File pdfPath, int pageNumber, TableBoundaries customBounds) throws IOException {
        // Create extractor with the custom boundaries
        PDFExtractor extractor = new PDFExtractor(customBounds);
        
        // Extract data from the specified page
        List<TableRow> rows = extractor.extractPage(pdfPath, pageNumber);
        
        // Print results to console for verification
        System.out.println("Page " + pageNumber + " (custom bounds): " + rows.size() + " rows");
        for (TableRow row : rows) {
            row.printRow();  // Print each row in format [col1]|[col2]|[col3]...
        }
        
        return rows;
    }
    
    /**
     * Update the default boundary settings
     * @param left Left edge of table area
     * @param right Right edge of table area
     * @param top Top edge of table area (higher number = skip more header area)
     * @param bottom Bottom edge of table area (lower number = avoid footer text)
     */
    public void setBoundaries(float left, float right, float top, float bottom) {
        this.boundaries = new TableBoundaries(left, right, top, bottom);
    }
    
    /**
     * Example usage demonstrating how to parse pages with different boundary settings
     */
    public static void main(String[] args) {
        try {
            String pdfPathx = "pdf/cre-crc-current-english.pdf";
            File filePath = ResourceFileHelper.getResourceFile(pdfPathx);  // Change this to your actual PDF file path
            OECDParser parser = new OECDParser();
            
            // Parse regular pages (1-5) with standard boundaries
            parser.parsePage(filePath, 1);  // First page
            parser.parsePage(filePath, 2);  // Second page
            // ... continue for pages 3, 4, 5
            
            // Parse last page (6) with smaller boundaries to avoid footer text
            // The key change: bottom boundary is 600f instead of 700f (100 pixels smaller)
            TableBoundaries smallerBounds = new TableBoundaries(50f, 590f, 140f, 600f);
            parser.parsePage(filePath, 6, smallerBounds);
            
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}