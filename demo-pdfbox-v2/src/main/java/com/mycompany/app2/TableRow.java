package com.mycompany.app2;

/**
 * Represents a single row in the table with 6 columns
 */
public class TableRow {
    private String[] cells;
    
    /**
     * Constructor: Creates a new table row with 6 empty cells
     */
    public TableRow() {
        this.cells = new String[6];
        // Initialize all cells as empty strings to avoid null values
        for (int i = 0; i < 6; i++) {
            cells[i] = "";
        }
    }
    
    /**
     * Gets the content of a specific cell by index
     * @param index Column index (0-5)
     * @return Cell content as string, empty string if index is invalid
     */
    public String getCell(int index) {
        return (index >= 0 && index < cells.length) ? cells[index] : "";
    }
    
    /**
     * Sets the content of a specific cell
     * @param index Column index (0-5)
     * @param value New value for the cell
     */
    public void setCell(int index, String value) {
        if (index >= 0 && index < cells.length) {
            cells[index] = value != null ? value : "";
        }
    }
    
    /**
     * Appends text to an existing cell (useful for combining multiple text fragments)
     * @param index Column index (0-5)
     * @param text Text to append to the cell
     */
    public void appendToCell(int index, String text) {
        if (index >= 0 && index < cells.length && text != null) {
            cells[index] += text;
        }
    }
    
    /**
     * Checks if this row has any non-empty content
     * @return true if at least one cell has content, false if all cells are empty
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
     * Prints the row to console in a compact format
     * Format: [cell1]|[cell2]|[cell3]|[cell4]|[cell5]|[cell6]
     */
    public void printRow() {
        for (int i = 0; i < cells.length; i++) {
            System.out.printf("[%s]", cells[i]);
            if (i < cells.length - 1) System.out.print("|");
        }
        System.out.println();
    }
    
    /**
     * Returns a readable string representation of the row
     * @return String with all cells separated by " | "
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cells.length; i++) {
            if (i > 0) sb.append(" | ");
            sb.append(cells[i]);
        }
        return sb.toString();
    }
}