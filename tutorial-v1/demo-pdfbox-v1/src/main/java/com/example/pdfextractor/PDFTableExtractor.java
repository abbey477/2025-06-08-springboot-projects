package com.example.pdfextractor;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.text.TextPosition;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class PDFTableExtractor {

    // Method 1: Using PDFTextStripperByArea for specific regions
    public static String extractTableTextByArea(PDDocument document, int pageNumber,
                                                float x, float y, float width, float height) throws IOException {

        PDPage page = document.getPage(pageNumber - 1); // PDFBox uses 0-based indexing

        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition(true);

        // Define the rectangle area for the table
        Rectangle2D rect = new Rectangle2D.Float(x, y, width, height);
        stripper.addRegion("table", rect);

        stripper.extractRegions(page);

        return stripper.getTextForRegion("table");
    }

    // Method 2: Custom PDFTextStripper to preserve table structure
    public static class TableTextStripper extends PDFTextStripper {
        private List<TextPosition> textPositions = new ArrayList<>();
        private Map<Float, List<TextPosition>> rowMap = new TreeMap<>();

        public TableTextStripper() throws IOException {
            super();
            setSortByPosition(true);
        }

        @Override
        protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
            this.textPositions.addAll(textPositions);
            super.writeString(string, textPositions);
        }

        public String extractTableText(PDDocument document, int pageNumber) throws IOException {
            setStartPage(pageNumber);
            setEndPage(pageNumber);

            String text = getText(document);

            // Group text positions by Y coordinate (rows)
            for (TextPosition pos : textPositions) {
                float y = pos.getYDirAdj();
                rowMap.computeIfAbsent(y, k -> new ArrayList<>()).add(pos);
            }

            StringBuilder tableText = new StringBuilder();

            // Process each row
            for (Map.Entry<Float, List<TextPosition>> entry : rowMap.entrySet()) {
                List<TextPosition> rowPositions = entry.getValue();

                // Sort by X coordinate (columns)
                rowPositions.sort(Comparator.comparing(TextPosition::getXDirAdj));

                StringBuilder row = new StringBuilder();
                float lastX = 0;

                for (TextPosition pos : rowPositions) {
                    float currentX = pos.getXDirAdj();

                    // Add spacing between columns
                    if (lastX > 0 && currentX - lastX > 20) { // Adjust threshold as needed
                        row.append("\t");
                    }

                    row.append(pos.getUnicode());
                    lastX = currentX + pos.getWidth();
                }

                tableText.append(row.toString().trim()).append("\n");
            }

            return tableText.toString();
        }
    }

    // Method 3: Extract table with column detection
    public static String extractTableWithColumns(PDDocument document, int pageNumber,
                                                 float tableX, float tableY, float tableWidth,
                                                 float tableHeight) throws IOException {

        PDPage page = document.getPage(pageNumber - 1);

        // Custom stripper to collect text positions
        PDFTextStripper stripper = new PDFTextStripper() {
            private List<TextPosition> textPositions = new ArrayList<>();

            @Override
            protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
                // Filter text positions within table bounds
                for (TextPosition pos : textPositions) {
                    if (pos.getXDirAdj() >= tableX &&
                            pos.getXDirAdj() <= tableX + tableWidth &&
                            pos.getYDirAdj() >= tableY &&
                            pos.getYDirAdj() <= tableY + tableHeight) {
                        this.textPositions.add(pos);
                    }
                }
                super.writeString(string, textPositions);
            }

            public List<TextPosition> getTextPositions() {
                return textPositions;
            }
        };

        stripper.setStartPage(pageNumber);
        stripper.setEndPage(pageNumber);
        stripper.getText(document);

        // Process text positions to create table structure
        return processTextPositionsToTable(((PDFTextStripper) stripper).getTextPositions());
    }

    private static String processTextPositionsToTable(List<TextPosition> textPositions) {
        // Group by rows (Y coordinate)
        Map<Float, List<TextPosition>> rows = new TreeMap<>();

        for (TextPosition pos : textPositions) {
            float y = Math.round(pos.getYDirAdj());
            rows.computeIfAbsent(y, k -> new ArrayList<>()).add(pos);
        }

        StringBuilder result = new StringBuilder();

        for (Map.Entry<Float, List<TextPosition>> entry : rows.entrySet()) {
            List<TextPosition> rowPositions = entry.getValue();

            // Sort by X coordinate
            rowPositions.sort(Comparator.comparing(TextPosition::getXDirAdj));

            StringBuilder row = new StringBuilder();
            float lastX = -1;

            for (TextPosition pos : rowPositions) {
                float currentX = pos.getXDirAdj();

                // Detect column separation
                if (lastX > 0 && currentX - lastX > 30) { // Adjust threshold
                    row.append(" | ");
                }

                row.append(pos.getUnicode());
                lastX = currentX + pos.getWidth();
            }

            result.append(row.toString().trim()).append("\n");
        }

        return result.toString();
    }

    // Method 4: Simple region-based extraction (your original approach fixed)
    public static String extractTableTextSimple(PDDocument document, int pageNumber,
                                                float x, float y, float width, float height) throws IOException {

        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setStartPage(pageNumber);
        stripper.setEndPage(pageNumber);
        stripper.setSortByPosition(true);

        // Note: PDFTextStripper doesn't have setRegion method
        // Use PDFTextStripperByArea instead for region-specific extraction

        return stripper.getText(document);
    }

    // Example usage
    public static void main(String[] args) {
        try {
            PDDocument document = Loader.loadPDF(new File("your-pdf-file.pdf"));
            int pageNumber = 1;

            // Method 1: Extract from specific area
            String tableText1 = extractTableTextByArea(document, pageNumber, 100, 200, 400, 300);
            System.out.println("Method 1 - By Area:");
            System.out.println(tableText1);

            // Method 2: Using custom stripper
            TableTextStripper customStripper = new TableTextStripper();
            String tableText2 = customStripper.extractTableText(document, pageNumber);
            System.out.println("\nMethod 2 - Custom Stripper:");
            System.out.println(tableText2);

            // Method 3: With column detection
            String tableText3 = extractTableWithColumns(document, pageNumber, 100, 200, 400, 300);
            System.out.println("\nMethod 3 - With Columns:");
            System.out.println(tableText3);

            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}