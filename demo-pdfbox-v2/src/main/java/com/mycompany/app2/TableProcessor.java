package com.mycompany.app2;

import java.util.*;

/**
 * Processes text positions into structured table rows
 * Takes scattered text fragments with coordinates and organizes them into table format
 */
public class TableProcessor {
    
    private final TableBoundaries boundaries;
    
    /**
     * Constructor
     * @param boundaries Table boundary configuration for column detection
     */
    public TableProcessor(TableBoundaries boundaries) {
        this.boundaries = boundaries;
    }
    
    /**
     * Main processing method: converts text fragments into organized table rows
     * @param allText List of text fragments with their X,Y coordinates
     * @return List of structured table rows with 6 columns each
     */
    public List<TableRow> processTextIntoRows(List<TextWithPosition> allText) {
        List<TableRow> tableRows = new ArrayList<>();
        if (allText.isEmpty()) return tableRows;
        
        // Step 1: Group scattered text into rows based on Y coordinates
        // Text with similar Y coordinates belongs to the same horizontal row
        Map<Integer, List<TextWithPosition>> rowGroups = groupByRows(allText);
        
        // Step 2: Sort rows from top to bottom (PDF coordinate system)
        List<Integer> sortedRowKeys = new ArrayList<>(rowGroups.keySet());
        sortedRowKeys.sort(Collections.reverseOrder()); // PDF Y coordinates decrease going down
        
        // Step 3: Process each row individually
        for (Integer rowKey : sortedRowKeys) {
            List<TextWithPosition> rowText = rowGroups.get(rowKey);
            
            // Sort text within the row from left to right (ascending X coordinates)
            rowText.sort((a, b) -> Float.compare(a.x, b.x));
            
            // Convert the row's text fragments into a structured TableRow
            TableRow tableRow = createRowFromText(rowText);
            
            // Only keep rows that have actual content (skip empty rows)
            if (tableRow.hasContent()) {
                tableRows.add(tableRow);
            }
        }
        
        return tableRows;
    }
    
    /**
     * Groups text fragments by similar Y coordinates to identify table rows
     * Text appearing at nearly the same vertical position belongs to the same row
     * @param allText All text fragments to group
     * @return Map where key is rounded Y coordinate and value is list of text at that Y level
     */
    private Map<Integer, List<TextWithPosition>> groupByRows(List<TextWithPosition> allText) {
        Map<Integer, List<TextWithPosition>> rowGroups = new HashMap<>();
        
        for (TextWithPosition textPos : allText) {
            // Round Y coordinate to group nearby text into same row
            // Grouping within 5 pixels vertically - adjust this if rows are too close/far
            int rowKey = Math.round(textPos.y / 5) * 5;
            
            // Add text to the appropriate row group, creating new group if needed
            rowGroups.computeIfAbsent(rowKey, k -> new ArrayList<>()).add(textPos);
        }
        
        return rowGroups;
    }
    
    /**
     * Converts a list of text fragments (from one row) into a structured TableRow
     * Assigns each text fragment to the appropriate column based on its X coordinate
     * @param rowText List of text fragments that belong to the same row (sorted left to right)
     * @return TableRow with text distributed across 6 columns
     */
    private TableRow createRowFromText(List<TextWithPosition> rowText) {
        TableRow tableRow = new TableRow();
        
        // Process each text fragment and assign it to the correct column
        for (TextWithPosition textPos : rowText) {
            // Determine which column this text belongs to based on X coordinate
            int columnIndex = boundaries.getColumnIndex(textPos.x);
            
            // Only process text that falls within our defined columns (0-5)
            if (columnIndex >= 0 && columnIndex < 6) {
                // Append text to the appropriate column
                // Multiple text fragments in the same cell are concatenated without spaces
                // (e.g., "Hello" + "World" = "HelloWorld")
                tableRow.appendToCell(columnIndex, textPos.text);
            }
            // Text outside column boundaries is ignored
        }
        
        return tableRow;
    }
}