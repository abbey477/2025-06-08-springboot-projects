package com.mycompany.app4.process;

import java.io.IOException;
import java.io.InputStream;
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
        // Use provided boundaries instead of default ones
        this.boundaries = boundaries;
    }
    
    /**
     * Parse a specific page using the current boundary settings
     * @param pdfPath Full path to your PDF file
     * @param pageNumber Page number to extract (1=first page, 2=second page, etc.)
     * @return List of table rows found on this page
     * @throws IOException If PDF file cannot be read
     */
    public List<TableRow> parsePage(String pdfPath, int pageNumber) throws IOException {
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
    public List<TableRow> parsePage(String pdfPath, int pageNumber, TableBoundaries customBounds) throws IOException {
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
     * Parse a specific page using InputStream (useful for downloaded PDFs)
     * @param pdfInputStream InputStream of the PDF file
     * @param pageNumber Page number to extract (1=first page, 2=second page, etc.)
     * @return List of table rows found on this page
     * @throws IOException If PDF stream cannot be read
     */
    public List<TableRow> parsePage(InputStream pdfInputStream, int pageNumber) throws IOException {
        // Create extractor with current boundaries
        PDFExtractor extractor = new PDFExtractor(boundaries);
        
        // Extract data from the specified page
        List<TableRow> rows = extractor.extractPage(pdfInputStream, pageNumber);
        
        // Print results to console for verification
        System.out.println("Page " + pageNumber + ": " + rows.size() + " rows");
        for (TableRow row : rows) {
            row.printRow();  // Print each row in format [col1]|[col2]|[col3]...
        }
        
        return rows;
    }
    
    /**
     * Parse a specific page with custom boundaries using InputStream
     * @param pdfInputStream InputStream of the PDF file
     * @param pageNumber Page number to extract
     * @param customBounds Custom boundary settings for this specific page
     * @return List of table rows found on this page
     * @throws IOException If PDF stream cannot be read
     */
    public List<TableRow> parsePage(InputStream pdfInputStream, int pageNumber, TableBoundaries customBounds) throws IOException {
        // Create extractor with the custom boundaries
        PDFExtractor extractor = new PDFExtractor(customBounds);
        
        // Extract data from the specified page
        List<TableRow> rows = extractor.extractPage(pdfInputStream, pageNumber);
        
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

}