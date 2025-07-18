package com.mycompany.app;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class SimpleOECDParser3 {

    // Main method to run the parser
    public static void main(String[] args) {
        try {
            String pdfPath = "pdf/cre-crc-current-english.pdf";

            File filePath = ResourceFileHelper.getResourceFile(pdfPath);

            System.out.println("=== OECD PDF PARSER ===");

            // Print current column setup
            printColumnBoundaries();

            // Example: Adjust column boundaries if needed
            // float[] customBoundaries = {50f, 120f, 200f, 300f, 400f, 500f, 600f};
            // setColumnBoundaries(customBoundaries);

            parseTable(filePath);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Define column boundaries (X coordinates) - Column 3 is the largest
    private static final float[] COLUMN_BOUNDARIES = {
            50f,   // Start of Column 1
            120f,  // Start of Column 2 (Column 1 width: 70px)
            180f,  // Start of Column 3 (Column 2 width: 60px)
            380f,  // Start of Column 4 (Column 3 width: 200px - LARGEST)
            450f,  // Start of Column 5 (Column 4 width: 70px)
            520f,  // Start of Column 6 (Column 5 width: 70px)
            590f   // End of Column 6   (Column 6 width: 70px)
    };

    // Table area boundaries - updated to match new column layout
    private static final float TABLE_LEFT = 50f;
    private static final float TABLE_RIGHT = 590f;
    private static final float TABLE_TOP = 100f;
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
        List<TableRow> tableRows = new ArrayList<>();

        PDDocument document = PDDocument.load(pdfPath);

        // Simple text stripper that collects all text with positions
        PDFTextStripper stripper = new PDFTextStripper() {
            List<TextWithPosition> allText = new ArrayList<>();

            @Override
            protected void writeString(String text, List<TextPosition> positions) {
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
                // Process all collected text after each page
                processTextIntoRows(allText, tableRows);
                allText.clear();
            }
        };

        stripper.getText(document);
        document.close();

        // Print all rows
        System.out.println("=== EXTRACTED TABLE ROWS ===");
        for (TableRow row : tableRows) {
            row.printRow();
        }
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

    static void processTextIntoRows(List<TextWithPosition> allText, List<TableRow> tableRows) {
        if (allText.isEmpty()) return;

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


}