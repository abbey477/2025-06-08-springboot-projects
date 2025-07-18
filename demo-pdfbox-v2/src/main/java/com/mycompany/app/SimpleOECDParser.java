package com.mycompany.app;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class SimpleOECDParser {

    // Main method to run the parser
    public static void main(String[] args) {
        try {
            String pdfPath = "pdf/cre-crc-current-english.pdf";

            File filePath = ResourceFileHelper.getResourceFile(pdfPath);
            parseTable(filePath);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Simple data holder for table rows
    public static class TableRow {
        public String[] cells = new String[6]; // 6 columns

        public TableRow() {
            // Initialize all cells as empty
            for (int i = 0; i < 6; i++) {
                cells[i] = "";
            }
        }

        public void printRow(int rowNumber) {
            System.out.printf("Row %d: ", rowNumber);
            for (int i = 0; i < cells.length; i++) {
                System.out.printf("[%s]", cells[i]);
                if (i < cells.length - 1) System.out.print(" | ");
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
                    // Only keep text in our table area (adjust these coordinates as needed)
                    float x = pos.getX();
                    float y = pos.getY();

                    if (x > 50 && x < 600 && y > 100 && y < 700) {
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
        for (int i = 0; i < tableRows.size(); i++) {
            tableRows.get(i).printRow(i + 1);
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
                    // Add text to the appropriate column
                    if (!tableRow.cells[columnIndex].isEmpty()) {
                        tableRow.cells[columnIndex] += " "; // Add space between words
                    }
                    tableRow.cells[columnIndex] += textPos.text;
                }
            }

            // Only add row if it has some content
            if (hasContent(tableRow)) {
                tableRows.add(tableRow);
            }
        }
    }

    // Simple column detection based on X coordinate
    static int getColumnIndex(float x) {
        // Adjust these X boundaries based on your PDF's column positions
        if (x < 120) return 0;      // Column 1
        else if (x < 200) return 1; // Column 2
        else if (x < 280) return 2; // Column 3
        else if (x < 360) return 3; // Column 4
        else if (x < 440) return 4; // Column 5
        else if (x < 520) return 5; // Column 6
        else return -1; // Outside table
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