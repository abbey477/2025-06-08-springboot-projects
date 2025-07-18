package com.mycompany.app;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class SimpleOECDParser5 {

    // Define column boundaries (X coordinates) - Adjusted Column 2 to avoid overlap
    private static final float[] COLUMN_BOUNDARIES = {
            50f,   // Start of Column 1
            120f,  // Start of Column 2 (Column 1 width: 70px)
            155f,  // Start of Column 3 (Column 2 width: 35px - NARROWER to avoid overlap)
            380f,  // Start of Column 4 (Column 3 width: 225px - LARGEST)
            450f,  // Start of Column 5 (Column 4 width: 70px)
            520f,  // Start of Column 6 (Column 5 width: 70px)
            590f   // End of Column 6   (Column 6 width: 70px)
    };

    // Table area boundaries - updated to match new column layout
    private static final float TABLE_LEFT = 50f;
    private static final float TABLE_RIGHT = 590f;
    private static final float TABLE_TOP = 140f;
    private static final float TABLE_BOTTOM = 700f;

    // Simple data holder for table rows
    public static class TableRow {
        public String[] cells = new String[6]; // 6 columns

        public TableRow() {
            // Initialize all cells as empty
            for (int i = 0; i < 6; i++) {
                cells[i] = "";
            }
        }

        public void printRow() {
            for (int i = 0; i < cells.length; i++) {
                System.out.printf("[%s]", cells[i]);
                if (i < cells.length - 1) System.out.print("|");
            }
            System.out.println();
        }
    }

    public static void parseTable(File pdfPath) throws IOException {
        parseTable(pdfPath, -1); // Parse all pages by default
    }

    public static void parseTable(File pdfPath, int specificPage) throws IOException {
        List<TableRow> tableRows = new ArrayList<>();

        PDDocument document = PDDocument.load(pdfPath);
        int totalPages = document.getNumberOfPages();

        System.out.println("PDF has " + totalPages + " pages");

        if (specificPage > 0) {
            if (specificPage > totalPages) {
                System.out.println("Error: Page " + specificPage + " doesn't exist. PDF only has " + totalPages + " pages.");
                document.close();
                return;
            }
            System.out.println("Processing only page: " + specificPage);
        } else {
            System.out.println("Processing all pages");
        }

        // Simple text stripper that collects all text with positions
        PDFTextStripper stripper = new PDFTextStripper() {
            List<TextWithPosition> allText = new ArrayList<>();
            int currentPageNumber = 0;

            @Override
            protected void startPage(PDPage page) throws IOException {
                currentPageNumber++;
                allText.clear(); // Clear for each page
                super.startPage(page);
            }

            @Override
            protected void writeString(String text, List<TextPosition> positions) {
                // Skip if we're processing a specific page and this isn't it
                if (specificPage > 0 && currentPageNumber != specificPage) {
                    return;
                }

                for (TextPosition pos : positions) {
                    // Only keep text in our table area using defined boundaries
                    float x = pos.getX();
                    float y = pos.getY();

                    if (x >= TABLE_LEFT && x <= TABLE_RIGHT && y >= TABLE_TOP && y <= TABLE_BOTTOM) {
                        allText.add(new TextWithPosition(pos.getUnicode(), x, y));
                    }
                }
            }

            @Override
            protected void endPage(PDPage page) throws IOException {
                // Only process if we're on the right page (or processing all pages)
                if (specificPage <= 0 || currentPageNumber == specificPage) {
                    System.out.println("\n--- Processing Page " + currentPageNumber + " ---");
                    List<TableRow> pageRows = processTextIntoRows(allText);

                    System.out.println("Found " + pageRows.size() + " rows on page " + currentPageNumber);
                    for (TableRow row : pageRows) {
                        row.printRow();
                    }

                    tableRows.addAll(pageRows);
                }
                super.endPage(page);
            }
        };

        stripper.getText(document);
        document.close();

        // Print summary
        System.out.println("\n=== FINAL SUMMARY ===");
        System.out.println("Total rows found: " + tableRows.size());
    }

    // Helper class to store text with its position
    static class TextWithPosition {
        String text;
        float x, y;

        TextWithPosition(String text, float x, float y) {
            this.text = text;
            this.x = x;
            this.y = y;
        }
    }

    static List<TableRow> processTextIntoRows(List<TextWithPosition> allText) {
        List<TableRow> tableRows = new ArrayList<>();
        if (allText.isEmpty()) return tableRows;

        // Step 1: Group text by similar Y coordinates (rows)
        Map<Integer, List<TextWithPosition>> rowGroups = new HashMap<>();

        for (TextWithPosition textPos : allText) {
            // Round Y coordinate to group nearby text into same row
            int rowKey = Math.round(textPos.y / 5) * 5; // Group within 5 pixels

            rowGroups.computeIfAbsent(rowKey, k -> new ArrayList<>()).add(textPos);
        }

        // Step 2: Sort rows by Y coordinate (top to bottom)
        List<Integer> sortedRowKeys = new ArrayList<>(rowGroups.keySet());
        sortedRowKeys.sort(Collections.reverseOrder()); // PDF Y goes down

        // Step 3: Process each row
        for (Integer rowKey : sortedRowKeys) {
            List<TextWithPosition> rowText = rowGroups.get(rowKey);

            // Sort text in this row by X coordinate (left to right)
            rowText.sort((a, b) -> Float.compare(a.x, b.x));

            TableRow tableRow = new TableRow();

            // Step 4: Assign text to columns based on X position
            for (TextWithPosition textPos : rowText) {
                int columnIndex = getColumnIndex(textPos.x);
                if (columnIndex >= 0 && columnIndex < 6) {
                    // Add text to the appropriate column (no spaces between words)
                    tableRow.cells[columnIndex] += textPos.text;
                }
            }

            // Only add row if it has some content
            if (hasContent(tableRow)) {
                tableRows.add(tableRow);
            }
        }

        return tableRows;
    }

    // Precise column detection based on defined boundaries
    static int getColumnIndex(float x) {
        for (int i = 0; i < COLUMN_BOUNDARIES.length - 1; i++) {
            if (x >= COLUMN_BOUNDARIES[i] && x < COLUMN_BOUNDARIES[i + 1]) {
                return i;
            }
        }
        return -1; // Outside defined columns
    }

    // Method to easily adjust column boundaries
    public static void setColumnBoundaries(float[] newBoundaries) {
        if (newBoundaries.length == 7) { // 6 columns = 7 boundaries
            System.arraycopy(newBoundaries, 0, COLUMN_BOUNDARIES, 0, 7);
            System.out.println("Column boundaries updated:");
            printColumnBoundaries();
        } else {
            System.out.println("Error: Need exactly 7 boundary values for 6 columns");
        }
    }

    // Method to print current column setup
    public static void printColumnBoundaries() {
        System.out.println("Current Column Boundaries:");
        for (int i = 0; i < COLUMN_BOUNDARIES.length - 1; i++) {
            System.out.printf("Column %d: %.1f to %.1f (width: %.1f)\n",
                    i + 1,
                    COLUMN_BOUNDARIES[i],
                    COLUMN_BOUNDARIES[i + 1],
                    COLUMN_BOUNDARIES[i + 1] - COLUMN_BOUNDARIES[i]);
        }
        System.out.println();
    }

    static boolean hasContent(TableRow row) {
        for (String cell : row.cells) {
            if (cell != null && !cell.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // Main method to run the parser
    public static void main(String[] args) {
        try {
            String pdfPath = "pdf/cre-crc-current-english.pdf";

            File filePath = ResourceFileHelper.getResourceFile(pdfPath);


            // Print current column setup
            printColumnBoundaries();

            // Example usage:

            // 1. Parse all pages
            //parseTable(filePath);

            // 2. Parse only specific page (e.g., page 2)
            //parseTable(filePath, 2);

            // 3. Parse only first page
            // parseTable(pdfPath, 1);

            // 4. Parse only last page (you'll need to know the page number)
            // parseTable(pdfPath, 5); // if PDF has 5 pages

            parseTable(filePath, 6);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}