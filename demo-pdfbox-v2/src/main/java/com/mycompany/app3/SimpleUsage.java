package com.mycompany.app3;

import com.mycompany.app.ResourceFileHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * Simple usage example for OECD Country Risk Classification PDF parser
 * This shows the most straightforward way to process all 6 pages of the PDF
 */
public class SimpleUsage {
    
    public static void main(String[] args) {
        try {
            // Step 1: Set your PDF file path
            File filePath = ResourceFileHelper.getResourceFile("pdf/cre-crc-current-english.pdf");
            
            // Step 2: Create the parser
            OECDParser parser = new OECDParser();
            
            // Step 3: Process each page individually
            System.out.println("Starting PDF processing...");
            
            // Collect all data from all pages
            List<TableRow> allData = new ArrayList<>();
            
            // Process pages 1-5 with standard boundaries
            for (int pageNum = 1; pageNum <= 5; pageNum++) {
                System.out.println("\n--- Processing Page " + pageNum + " ---");
                
                List<TableRow> pageData = parser.parsePage(filePath, pageNum);
                allData.addAll(pageData);
                
                System.out.println("Page " + pageNum + " completed: " + pageData.size() + " rows extracted");
            }
            
            // Process page 6 (last page) with custom boundaries
            // This page has a smaller table and footer text we want to avoid
            System.out.println("\n--- Processing Page 6 (Last Page) ---");
            
            TableBoundaries lastPageBoundaries = new TableBoundaries(
                50f,   // left - same as other pages
                590f,  // right - same as other pages
                140f,  // top - same as other pages
                400f   // bottom - REDUCED from 700f to 600f to avoid footer
            );
            
            List<TableRow> lastPageData = parser.parsePage(filePath, 6, lastPageBoundaries);
            allData.addAll(lastPageData);
            
            System.out.println("Page 6 completed: " + lastPageData.size() + " rows extracted");
            
            // Step 4: Process the extracted data
            System.out.println("\n=== FINAL RESULTS ===");
            System.out.println("Total rows extracted from all pages: " + allData.size());
            
            // Example: Display first 5 rows to verify data
            System.out.println("\nSample data (first 5 rows):");
            for (int i = 0; i < Math.min(5, allData.size()); i++) {
                TableRow row = allData.get(i);
                System.out.printf("Row %d: [%s] | [%s] | [%s] | [%s] | [%s] | [%s]%n", 
                    i + 1, 
                    row.getCell(0), row.getCell(1), row.getCell(2),
                    row.getCell(3), row.getCell(4), row.getCell(5));
            }
            
            // Step 5: Use the data as needed
            // Example: Count non-empty rows
            int nonEmptyRows = 0;
            for (TableRow row : allData) {
                if (row.hasContent()) {
                    nonEmptyRows++;
                }
            }
            System.out.println("Non-empty rows: " + nonEmptyRows);
            
            // Example: Access specific column data
            System.out.println("\nColumn 1 values (first 3 non-empty):");
            int count = 0;
            for (TableRow row : allData) {
                String col1 = row.getCell(0);
                if (!col1.isEmpty() && count < 3) {
                    System.out.println("  " + col1);
                    count++;
                }
            }
            
            System.out.println("\nProcessing completed successfully!");
            
        } catch (IOException e) {
            System.err.println("Error processing PDF file:");
            System.err.println("  " + e.getMessage());
            System.err.println("\nPlease check:");
            System.err.println("  - PDF file path is correct");
            System.err.println("  - PDF file exists and is readable");
            System.err.println("  - PDF file is not corrupted");
        } catch (Exception e) {
            System.err.println("Unexpected error occurred:");
            System.err.println("  " + e.getMessage());
            e.printStackTrace();
        }
    }
}