package com.mycompany.app2;

import com.mycompany.app.ResourceFileHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Main class for parsing OECD Country Risk Classification PDF tables
 * This is the primary API that users interact with to extract table data from PDFs
 */
public class OECDCountryRiskParser {
    
    private TableBoundaries boundaries;    // Configuration for table location and column definitions
    private PDFTextExtractor extractor;   // Component that handles PDF text extraction
    
    /**
     * Default constructor using predefined table boundaries
     * Uses standard settings optimized for OECD Country Risk Classification PDFs
     */
    public OECDCountryRiskParser() {
        // Initialize with default boundaries suitable for most OECD PDFs
        this.boundaries = new TableBoundaries();
        this.extractor = new PDFTextExtractor(boundaries);
    }
    
    /**
     * Constructor with custom table boundaries
     * Use this when the default settings don't work for your specific PDF layout
     * @param customBoundaries Custom table boundary configuration
     */
    public OECDCountryRiskParser(TableBoundaries customBoundaries) {
        // Initialize with user-provided boundaries
        this.boundaries = customBoundaries;
        this.extractor = new PDFTextExtractor(boundaries);
    }
    
    /**
     * Parse all pages of the PDF and extract table data
     * Processes every page in the document and combines results
     * @param pdfPath Path to the PDF file to process
     * @return List of all table rows found across all pages
     * @throws IOException If the PDF file cannot be read or processed
     */
    public List<TableRow> parseAllPages(String pdfPath) throws IOException {
        return extractor.extractFromPDF(pdfPath);
    }
    
    /**
     * Parse a specific page of the PDF and extract table data
     * Useful when you know which page contains the table or want to avoid non-table pages
     * @param pdfPath Path to the PDF file to process
     * @param pageNumber Page number to process (1-based indexing, e.g., 1 = first page)
     * @return List of table rows found on the specified page
     * @throws IOException If the PDF file cannot be read or the page doesn't exist
     */
    public List<TableRow> parsePage(String pdfPath, int pageNumber) throws IOException {
        return extractor.extractFromPDF(pdfPath, pageNumber);
    }
    
    /**
     * Parse all pages and return results as DTOs
     * @param pdfPath Path to the PDF file to process
     * @return List of TableRowDto objects from all pages
     * @throws IOException If the PDF file cannot be read or processed
     */
    public List<TableRowDto> parseAllPagesAsDto(String pdfPath) throws IOException {
        List<TableRow> tableRows = extractor.extractFromPDF(pdfPath);
        return TableRowDtoConverter.toDtoListFiltered(tableRows);
    }
    
    /**
     * Parse specific page and return results as DTOs
     * @param pdfPath Path to the PDF file to process
     * @param pageNumber Page number to process (1-based indexing)
     * @return List of TableRowDto objects from the specified page
     * @throws IOException If the PDF file cannot be read or the page doesn't exist
     */
    public List<TableRowDto> parsePageAsDto(String pdfPath, int pageNumber) throws IOException {
        List<TableRow> tableRows = extractor.extractFromPDF(pdfPath, pageNumber);
        return TableRowDtoConverter.toDtoListFiltered(tableRows);
    }
    
    /**
     * Display the current table boundaries configuration
     * Useful for debugging and understanding how columns are being detected
     */
    public void printCurrentBoundaries() {
        boundaries.printColumnBoundaries();
    }
    
    /**
     * Update the column boundary definitions
     * Use this when the default column positions don't match your PDF layout
     * @param newBoundaries Array of 7 float values defining 6 column boundaries
     *                      Format: [col1_start, col2_start, col3_start, col4_start, col5_start, col6_start, table_end]
     */
    public void setColumnBoundaries(float[] newBoundaries) {
        boundaries.setColumnBoundaries(newBoundaries);
        // Recreate extractor with updated boundaries to ensure consistency
        this.extractor = new PDFTextExtractor(boundaries);
    }
    
    /**
     * Update the overall table area boundaries
     * Use this when the table is located in a different area of the page
     * @param left Left edge X coordinate of table area
     * @param right Right edge X coordinate of table area
     * @param top Top edge Y coordinate of table area (use higher value to skip headers)
     * @param bottom Bottom edge Y coordinate of table area
     */
    public void setTableBounds(float left, float right, float top, float bottom) {
        boundaries.setTableBounds(left, right, top, bottom);
        // Recreate extractor with updated boundaries to ensure consistency
        this.extractor = new PDFTextExtractor(boundaries);
    }
    
    /**
     * Main method for testing and demonstration
     * Shows various usage patterns of the parser
     */
    public static void main(String[] args) {
        try {
            String pdfPathx = "pdf/cre-crc-current-english.pdf";
            File filePath = ResourceFileHelper.getResourceFile(pdfPathx);
            String pdfPath = filePath.getAbsolutePath();
            
            System.out.println("=== OECD PDF PARSER ===");
            System.out.println("Parsing PDF: " + pdfPath);
            
            // Create parser with default settings
            OECDCountryRiskParser parser = new OECDCountryRiskParser();
            
            // Print current configuration for debugging
            parser.printCurrentBoundaries();
            
            // Example 1: Parse specific page (recommended approach)
            // This avoids processing pages that might not contain table data
            List<TableRow> rows = parser.parsePage(pdfPath, 1);
            System.out.println("Extracted " + rows.size() + " rows from page 1");
            
            // Example 1b: Parse as DTOs for easier data handling
            List<TableRowDto> dtoRows = parser.parsePageAsDto(pdfPath, 1);
            System.out.println("Extracted " + dtoRows.size() + " DTO rows from page 1");
            
            // Print DTO results
            TableRowDtoConverter.printDtos(dtoRows);
            
            // Example: Access DTO data easily
            for (TableRowDto dto : dtoRows) {
                System.out.printf("Country Code: %s, Risk: %s, Name: %s%n", 
                                dto.getCol1(), dto.getCol2(), dto.getCol3());
            }
            
            // Example 2: Customize boundaries if the defaults don't work
            // Uncomment and adjust these values based on your PDF's actual layout
            // float[] customColumns = {50f, 120f, 155f, 380f, 450f, 520f, 590f};
            // parser.setColumnBoundaries(customColumns);
            // parser.setTableBounds(50f, 590f, 150f, 700f);  // left, right, top, bottom
            
            // Example 3: Parse all pages (use carefully - may include non-table content)
            // List<TableRow> allRows = parser.parseAllPages(pdfPath);
            // System.out.println("Total rows from all pages: " + allRows.size());
            
            // Example 4: Process multiple specific pages
            // Useful when table spans multiple pages but you want to avoid the last page with footnotes
            // for (int page = 1; page <= 3; page++) {
            //     System.out.println("\n=== PROCESSING PAGE " + page + " ===");
            //     List<TableRow> pageRows = parser.parsePage(pdfPath, page);
            //     // Process pageRows as needed - convert to your data structure, save to database, etc.
            // }
            
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}