package com.mycompany.app2;

import com.mycompany.app.ResourceFileHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Example usage of the OECD Country Risk Parser
 * Demonstrates various ways to use the parser and process extracted data
 */
public class ParserUsageExample {
    
    public static void main(String[] args) {
        String pdfPathx = "pdf/cre-crc-current-english.pdf";
        File filePath = ResourceFileHelper.getResourceFile(pdfPathx);
        String pdfPath = filePath.getAbsolutePath();

        try {
            // === BASIC USAGE ===
            
            // 1. Simple parsing with default settings
            // Most users should start here - works for standard OECD PDF layouts
            OECDCountryRiskParser parser = new OECDCountryRiskParser();
            List<TableRowDto> rows = parser.parsePageAsDto(pdfPath, 1);
            
            // 2. Process the extracted data
            processExtractedData(rows);
            
            // === ADVANCED USAGE ===
            
            // 3. Custom boundaries if default settings don't work for your PDF
            // Use the coordinate finder tool first to determine these values
            float[] customColumnBoundaries = {
                60f,   // Column 1 start - adjust based on your PDF
                130f,  // Column 2 start  
                165f,  // Column 3 start
                390f,  // Column 4 start
                460f,  // Column 5 start
                530f,  // Column 6 start
                600f   // Table end
            };
            
            // Create parser with custom settings
            TableBoundaries customBoundaries = new TableBoundaries(
                customColumnBoundaries, 
                60f,   // table left edge
                600f,  // table right edge
                150f,  // table top edge (higher value skips headers)
                680f   // table bottom edge
            );
            
            OECDCountryRiskParser customParser = new OECDCountryRiskParser(customBoundaries);
            
            // 4. Parse multiple pages with custom settings
            // Useful when table data spans multiple pages
            for (int page = 1; page <= 3; page++) {
                System.out.println("\n=== PROCESSING PAGE " + page + " ===");
                List<TableRowDto> pageRows = customParser.parsePageAsDto(pdfPath, page);
                
                System.out.println("Page " + page + " extracted " + pageRows.size() + " rows");
                
                // Process each page's data separately
                processExtractedData(pageRows);
            }
            
            // 5. Parse all pages at once (use with caution)
            // This processes every page - may include non-table content from first/last pages
            List<TableRowDto> allRows = parser.parseAllPagesAsDto(pdfPath);
            System.out.println("\nTotal rows from entire PDF: " + allRows.size());
            
        } catch (IOException e) {
            System.err.println("Error processing PDF: " + e.getMessage());
        }
    }
    
    /**
     * Basic example of how to process the extracted table data
     * Shows how to access individual cells and filter data
     * @param rows List of extracted table rows
     */
    private static void processExtractedData(List<TableRowDto> rows) {
        System.out.println("\n--- PROCESSING " + rows.size() + " ROWS ---");
        
        for (int i = 0; i < rows.size(); i++) {
            TableRowDto row = rows.get(i);
            
            // Access individual cells by column index (0-5)
            String col1 = row.getCol1();  // First column (often country codes)
            String col2 = row.getCol2();  // Second column (often risk ratings)
            String col3 = row.getCol3();  // Third column (often country names - largest column)
            
            // Example: Print only rows with content in first 3 columns
            if (!col1.isEmpty() && !col2.isEmpty() && !col3.isEmpty()) {
                System.out.printf("Row %d: %s | %s | %s%n", i + 1, col1, col2, col3);
            }
        }
    }
}