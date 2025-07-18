package com.mycompany.app2;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Extracts text with positions from PDF files
 * Uses PDFBox to read PDF content and filter text based on table boundaries
 */
public class PDFTextExtractor {

    private final TableBoundaries boundaries;

    /**
     * Constructor
     * @param boundaries Table boundary configuration for filtering text
     */
    public PDFTextExtractor(TableBoundaries boundaries) {
        this.boundaries = boundaries;
    }

    /**
     * Extracts table data from all pages of a PDF
     * @param pdfPath Path to the PDF file
     * @return List of all table rows found in the PDF
     * @throws IOException If PDF cannot be read
     */
    public List<TableRow> extractFromPDF(String pdfPath) throws IOException {
        return extractFromPDF(pdfPath, -1); // -1 means process all pages
    }

    /**
     * Extracts table data from a specific page or all pages of a PDF
     * @param pdfPath Path to the PDF file
     * @param specificPage Page number to process (1-based), or -1 for all pages
     * @return List of table rows found on the specified page(s)
     * @throws IOException If PDF cannot be read or page doesn't exist
     */
    public List<TableRow> extractFromPDF(String pdfPath, int specificPage) throws IOException {
        List<TableRow> allTableRows = new ArrayList<>();

        // Open the PDF document
        PDDocument document = PDDocument.load(new File(pdfPath));
        int totalPages = document.getNumberOfPages();

        System.out.println("PDF has " + totalPages + " pages");

        // Validate specific page request
        if (specificPage > 0) {
            if (specificPage > totalPages) {
                System.out.println("Error: Page " + specificPage + " doesn't exist. PDF only has " + totalPages + " pages.");
                document.close();
                return allTableRows;
            }
            System.out.println("Processing only page: " + specificPage);
        } else {
            System.out.println("Processing all pages");
        }

        // Create and use custom text stripper to extract positioned text
        CustomTextStripper stripper = new CustomTextStripper(boundaries, specificPage);
        stripper.getText(document);  // This triggers the text extraction process
        document.close();

        // Retrieve the extracted and processed rows
        allTableRows = stripper.getAllExtractedRows();

        // Print final summary
        System.out.println("\n=== FINAL SUMMARY ===");
        System.out.println("Total data rows found: " + allTableRows.size());

        return allTableRows;
    }

    /**
     * Custom PDFTextStripper that collects text with position information
     * Extends PDFBox's PDFTextStripper to intercept text extraction and capture coordinates
     */
    private static class CustomTextStripper extends PDFTextStripper {

        private final TableBoundaries boundaries;      // Table boundary configuration
        private final TableProcessor processor;        // Processes text into table rows
        private final int targetPage;                  // Specific page to process (-1 for all)
        private int currentPageNumber = 0;             // Current page being processed
        private List<TextWithPosition> currentPageText = new ArrayList<>();  // Text from current page
        private List<TableRow> allExtractedRows = new ArrayList<>();         // All processed rows

        /**
         * Constructor for custom text stripper
         * @param boundaries Table boundary configuration
         * @param targetPage Specific page to process (-1 for all pages)
         * @throws IOException If PDFTextStripper initialization fails
         */
        public CustomTextStripper(TableBoundaries boundaries, int targetPage) throws IOException {
            super();
            this.boundaries = boundaries;
            this.processor = new TableProcessor(boundaries);
            this.targetPage = targetPage;
        }

        /**
         * Called when starting to process a new page
         * Resets the text collection for the new page
         */
        @Override
        protected void startPage(PDPage page) throws IOException {
            currentPageNumber++;
            currentPageText.clear();  // Clear text from previous page
            super.startPage(page);
        }

        /**
         * Called for each string of text found in the PDF
         * This is where we capture text and its position coordinates
         * @param text The text string (may be multiple characters)
         * @param positions List of TextPosition objects, one per character
         */
        @Override
        protected void writeString(String text, List<TextPosition> positions) {
            // Skip processing if we're only interested in a specific page and this isn't it
            if (targetPage > 0 && currentPageNumber != targetPage) {
                return;
            }

            // Process each character/position individually
            for (TextPosition pos : positions) {
                float x = pos.getX();    // X coordinate (horizontal position)
                float y = pos.getY();    // Y coordinate (vertical position)

                // Only keep text that falls within our defined table boundaries
                if (boundaries.isWithinTableBounds(x, y)) {
                    // Create a text-position object and add to current page collection
                    currentPageText.add(new TextWithPosition(pos.getUnicode(), x, y));
                }
                // Text outside boundaries is ignored
            }
        }

        /**
         * Called when finished processing a page
         * This is where we convert collected text into structured table rows
         */
        @Override
        protected void endPage(PDPage page) throws IOException {
            // Only process pages we're interested in
            if (targetPage <= 0 || currentPageNumber == targetPage) {
                System.out.println("\n--- Processing Page " + currentPageNumber + " ---");

                // Convert collected text positions into structured table rows
                List<TableRow> pageRows = processor.processTextIntoRows(currentPageText);

                // Display results for this page
                System.out.println("Found " + pageRows.size() + " data rows on page " + currentPageNumber);
                for (TableRow row : pageRows) {
                    row.printRow();  // Print each row to console
                }

                // Add this page's rows to the overall collection
                allExtractedRows.addAll(pageRows);
            }
            super.endPage(page);
        }

        /**
         * Returns all extracted and processed table rows
         * @return List of all table rows found during extraction
         */
        public List<TableRow> getAllExtractedRows() {
            return allExtractedRows;
        }
    }
}