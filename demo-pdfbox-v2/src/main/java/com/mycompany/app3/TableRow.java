package com.mycompany.app3;

/**
 * Simple table row with 6 columns
 * This represents one row of data from the PDF table
 */
public class TableRow {
    private String[] cells = new String[6];  // Array to hold 6 column values
    
    /**
     * Constructor: Creates empty row with 6 blank cells
     */
    public TableRow() {
        // Initialize all cells as empty strings to avoid null issues
        for (int i = 0; i < 6; i++) {
            cells[i] = "";
        }
    }
    
    /**
     * Get content from a specific column
     * @param index Column number (0-5, where 0=first column, 5=last column)
     * @return Text content of the cell, or empty string if invalid index
     */
    public String getCell(int index) {
        return (index >= 0 && index < 6) ? cells[index] : "";
    }
    
    /**
     * Set content for a specific column
     * @param index Column number (0-5)
     * @param value Text to put in this cell
     */
    public void setCell(int index, String value) {
        if (index >= 0 && index < 6) {
            cells[index] = value != null ? value : "";
        }
    }
    
    /**
     * Add text to existing cell content (used when PDF has multiple text fragments per cell)
     * @param index Column number (0-5)
     * @param text Text to append to existing content
     */
    public void appendToCell(int index, String text) {
        if (index >= 0 && index < 6 && text != null) {
            cells[index] += text;
        }
    }
    
    /**
     * Check if this row has any data
     * @return true if at least one cell contains text, false if all cells are empty
     */
    public boolean hasContent() {
        for (String cell : cells) {
            if (cell != null && !cell.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Print this row to console in format: [col1]|[col2]|[col3]|[col4]|[col5]|[col6]
     * Useful for debugging and seeing what data was extracted
     */
    public void printRow() {
        for (int i = 0; i < 6; i++) {
            System.out.printf("[%s]", cells[i]);
            if (i < 5) System.out.print("|");  // Add separator between columns
        }
        System.out.println();  // New line after the row
    }
}