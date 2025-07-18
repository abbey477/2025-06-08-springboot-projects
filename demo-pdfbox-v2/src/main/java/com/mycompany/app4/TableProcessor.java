package com.mycompany.app4;


import java.util.*;

/**
 * Simple processor to convert text positions into table rows
 * Takes scattered text fragments and organizes them into structured table rows
 */
public class TableProcessor {
    
    private TableBoundaries boundaries;  // Defines where columns are located
    
    /**
     * Constructor
     * @param boundaries Table boundary settings for organizing text into columns
     */
    public TableProcessor(TableBoundaries boundaries) {
        this.boundaries = boundaries;
    }
    
    /**
     * Convert scattered text positions into organized table rows
     * @param texts List of text fragments with their positions on the page
     * @return List of organized table rows with 6 columns each
     */
    public List<TableRow> process(List<TextWithPosition> texts) {
        if (texts.isEmpty()) return new ArrayList<>();
        
        // Step 1: Group text by Y coordinate (horizontal rows)
        // Text with similar Y positions belongs to the same row
        Map<Integer, List<TextWithPosition>> rows = new HashMap<>();
        for (TextWithPosition text : texts) {
            // Round Y coordinate to group nearby text into same row
            // (Text within 5 pixels vertically is considered same row)
            int rowKey = Math.round(text.y / 5) * 5;
            rows.computeIfAbsent(rowKey, k -> new ArrayList<>()).add(text);
        }
        
        // Step 2: Convert grouped text into TableRow objects
        List<TableRow> tableRows = new ArrayList<>();
        
        // Sort rows from top to bottom (PDF Y coordinates work backwards)
        List<Integer> sortedKeys = new ArrayList<>(rows.keySet());
        sortedKeys.sort(Collections.reverseOrder());
        
        // Process each row
        for (Integer key : sortedKeys) {
            List<TextWithPosition> rowTexts = rows.get(key);
            
            // Sort text within this row from left to right
            rowTexts.sort((a, b) -> Float.compare(a.x, b.x));
            
            // Create a new table row and fill it with text
            TableRow row = new TableRow();
            for (TextWithPosition text : rowTexts) {
                // Determine which column this text belongs to
                int col = boundaries.getColumnIndex(text.x);
                if (col >= 0 && col < 6) {  // Valid column (0-5)
                    // Add text to the appropriate column
                    row.appendToCell(col, text.text);
                }
                // Text outside column boundaries is ignored
            }
            
            // Only keep rows that have actual content
            if (row.hasContent()) {
                tableRows.add(row);
            }
        }
        
        return tableRows;
    }
}