package com.mycompany.app4;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Simple PDF text extractor
 * Uses PDFBox library to read PDF files and extract text with position information
 */
public class PDFExtractor {
    
    private TableBoundaries boundaries;  // Defines which area of the page to extract from
    
    /**
     * Constructor
     * @param boundaries Table boundary settings that define where to look for text
     */
    public PDFExtractor(TableBoundaries boundaries) {
        this.boundaries = boundaries;
    }
    
    /**
     * Extract table data from a specific page of the PDF
     * @param pdfPath Full path to the PDF file
     * @param pageNumber Page number to extract from (1-based, so 1 = first page)
     * @return List of table rows found on this page
     * @throws IOException If PDF file cannot be read
     */
    public List<TableRow> extractPage(String pdfPath, int pageNumber) throws IOException {
        // List to collect text with positions from the specified page
        List<TextWithPosition> texts = new ArrayList<>();
        
        // Open the PDF document
        PDDocument document = PDDocument.load(new File(pdfPath));
        
        // Create custom text stripper to capture text positions
        PDFTextStripper stripper = new PDFTextStripper() {
            int currentPage = 0;  // Track which page we're currently processing
            
            /**
             * Called when starting each page
             */
            @Override
            protected void startPage(PDPage page) throws IOException {
                currentPage++;
                super.startPage(page);
            }
            
            /**
             * Called for each piece of text found in the PDF
             * @param text The text string found
             * @param positions List of position objects, one for each character
             */
            @Override
            protected void writeString(String text, List<TextPosition> positions) {
                // Only process the page we're interested in
                if (currentPage == pageNumber) {
                    // Check each character's position
                    for (TextPosition pos : positions) {
                        float x = pos.getX();  // Horizontal position
                        float y = pos.getY();  // Vertical position
                        
                        // Only keep text that's within our table boundaries
                        if (boundaries.isInside(x, y)) {
                            // Save this character with its position
                            texts.add(new TextWithPosition(pos.getUnicode(), x, y));
                        }
                        // Text outside boundaries is ignored
                    }
                }
            }
        };
        
        // Process the PDF (this triggers the text extraction)
        stripper.getText(document);
        document.close();
        
        // Convert the collected text positions into organized table rows
        TableProcessor processor = new TableProcessor(boundaries);
        return processor.process(texts);
    }
    
    /**
     * Extract table data from a specific page of the PDF using InputStream
     * @param pdfInputStream InputStream of the PDF file (useful for downloaded files)
     * @param pageNumber Page number to extract from (1-based, so 1 = first page)
     * @return List of table rows found on this page
     * @throws IOException If PDF stream cannot be read
     */
    public List<TableRow> extractPage(java.io.InputStream pdfInputStream, int pageNumber) throws IOException {
        // List to collect text with positions from the specified page
        List<TextWithPosition> texts = new ArrayList<>();
        
        // Open the PDF document from InputStream
        PDDocument document = PDDocument.load(pdfInputStream);
        
        // Create custom text stripper to capture text positions
        PDFTextStripper stripper = new PDFTextStripper() {
            int currentPage = 0;  // Track which page we're currently processing
            
            /**
             * Called when starting each page
             */
            @Override
            protected void startPage(PDPage page) throws IOException {
                currentPage++;
                super.startPage(page);
            }
            
            /**
             * Called for each piece of text found in the PDF
             * @param text The text string found
             * @param positions List of position objects, one for each character
             */
            @Override
            protected void writeString(String text, List<TextPosition> positions) {
                // Only process the page we're interested in
                if (currentPage == pageNumber) {
                    // Check each character's position
                    for (TextPosition pos : positions) {
                        float x = pos.getX();  // Horizontal position
                        float y = pos.getY();  // Vertical position
                        
                        // Only keep text that's within our table boundaries
                        if (boundaries.isInside(x, y)) {
                            // Save this character with its position
                            texts.add(new TextWithPosition(pos.getUnicode(), x, y));
                        }
                        // Text outside boundaries is ignored
                    }
                }
            }
        };
        
        // Process the PDF (this triggers the text extraction)
        stripper.getText(document);
        document.close();
        
        // Convert the collected text positions into organized table rows
        TableProcessor processor = new TableProcessor(boundaries);
        return processor.process(texts);
    }
}