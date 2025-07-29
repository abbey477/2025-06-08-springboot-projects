package com.mycompany.app4.main;

import com.mycompany.app4.domain.BoundaryFactory;
import com.mycompany.app4.domain.TableBoundaryDTO;
import com.mycompany.app4.process.OECDParser;
import com.mycompany.app4.process.TableBoundaries;
import com.mycompany.app4.util.ResourceFileHelper;

import java.io.IOException;

public class Main {

    // Example usage
    public static void main(String[] args) {
        try {
            // Just get the path as string
            String filePath = ResourceFileHelper.getOecdFilePath();

            example01(filePath);

            example02(filePath);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Example usage demonstrating how to parse
     * pages with different boundary settings
     */
    public static void example01(String pdfPath) {
        try {
            TableBoundaryDTO pageOneToFiveBoundary = BoundaryFactory.getPageOneToFiveBoundary();
            TableBoundaries boundaries = new TableBoundaries(pageOneToFiveBoundary);
            OECDParser parser = new OECDParser(boundaries);

            // Parse regular pages (1-5) with standard boundaries
            parser.parsePage(pdfPath, 1);  // First page
            parser.parsePage(pdfPath, 2);  // Second page
            // ... continue for pages 3, 4, 5

            // Parse last page (6) with smaller boundaries to avoid footer text
            // The key change: bottom boundary is 600f instead of 700f (100 pixels smaller)
            TableBoundaries smallerBounds = new TableBoundaries(50f, 590f, 140f, 600f);
            parser.parsePage(pdfPath, 6, smallerBounds);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void example02(String pdfPath) {
        try {
            // Example 1: Using file path (when PDF is saved locally)
            SimpleUsage.processFromFilePath(pdfPath);

            // Example 2: Using InputStream (when PDF is downloaded/streamed)
            SimpleUsage.processFromInputStream(pdfPath);

        } catch (IOException e) {
            System.err.println("Error processing PDF file:");
            System.err.println("  " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error occurred:");
            System.err.println("  " + e.getMessage());
        }
    }
}
