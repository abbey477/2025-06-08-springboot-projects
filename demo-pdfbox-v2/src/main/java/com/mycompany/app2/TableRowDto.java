package com.mycompany.app2;

/**
 * Data Transfer Object (DTO) for holding table row information
 * Provides a clean, structured way to access table data with named fields
 */
public class TableRowDto {
    
    private String col1;  // First column data
    private String col2;  // Second column data  
    private String col3;  // Third column data (typically the largest column)
    private String col4;  // Fourth column data
    private String col5;  // Fifth column data
    private String col6;  // Sixth column data
    
    /**
     * Default constructor - creates DTO with empty strings
     */
    public TableRowDto() {
        this.col1 = "";
        this.col2 = "";
        this.col3 = "";
        this.col4 = "";
        this.col5 = "";
        this.col6 = "";
    }
    
    /**
     * Constructor with all column values
     * @param col1 First column value
     * @param col2 Second column value
     * @param col3 Third column value
     * @param col4 Fourth column value
     * @param col5 Fifth column value
     * @param col6 Sixth column value
     */
    public TableRowDto(String col1, String col2, String col3, String col4, String col5, String col6) {
        this.col1 = col1 != null ? col1.trim() : "";
        this.col2 = col2 != null ? col2.trim() : "";
        this.col3 = col3 != null ? col3.trim() : "";
        this.col4 = col4 != null ? col4.trim() : "";
        this.col5 = col5 != null ? col5.trim() : "";
        this.col6 = col6 != null ? col6.trim() : "";
    }
    
    /**
     * Constructor from TableRow object
     * Converts internal TableRow to external DTO
     * @param tableRow TableRow object to convert
     */
    public TableRowDto(TableRow tableRow) {
        this.col1 = cleanString(tableRow.getCell(0));
        this.col2 = cleanString(tableRow.getCell(1));
        this.col3 = cleanString(tableRow.getCell(2));
        this.col4 = cleanString(tableRow.getCell(3));
        this.col5 = cleanString(tableRow.getCell(4));
        this.col6 = cleanString(tableRow.getCell(5));

        /*if (tableRow != null) {
            this.col1 = cleanString(tableRow.getCell(0));
            this.col2 = cleanString(tableRow.getCell(1));
            this.col3 = cleanString(tableRow.getCell(2));
            this.col4 = cleanString(tableRow.getCell(3));
            this.col5 = cleanString(tableRow.getCell(4));
            this.col6 = cleanString(tableRow.getCell(5));
        } else {
            this(); // Use default constructor
        }*/
    }
    
    /**
     * Static factory method to create DTO from TableRow
     * @param tableRow TableRow to convert
     * @return New TableRowDto instance
     */
    /*public static TableRowDto fromTableRow(TableRow tableRow) {
        return new TableRowDto(tableRow);
    }*/
    
    /**
     * Helper method to clean and validate string values
     * @param value Raw string value
     * @return Cleaned string (trimmed, null-safe)
     */
    private String cleanString(String value) {
        return value != null ? value.trim() : "";
    }
    
    // Getter methods
    
    /**
     * Gets the first column value
     * @return First column content as string
     */
    public String getCol1() {
        return col1;
    }
    
    /**
     * Gets the second column value
     * @return Second column content as string
     */
    public String getCol2() {
        return col2;
    }
    
    /**
     * Gets the third column value (typically the largest column)
     * @return Third column content as string
     */
    public String getCol3() {
        return col3;
    }
    
    /**
     * Gets the fourth column value
     * @return Fourth column content as string
     */
    public String getCol4() {
        return col4;
    }
    
    /**
     * Gets the fifth column value
     * @return Fifth column content as string
     */
    public String getCol5() {
        return col5;
    }
    
    /**
     * Gets the sixth column value
     * @return Sixth column content as string
     */
    public String getCol6() {
        return col6;
    }
    
    // Setter methods
    
    /**
     * Sets the first column value
     * @param col1 New value for first column
     */
    public void setCol1(String col1) {
        this.col1 = cleanString(col1);
    }
    
    /**
     * Sets the second column value
     * @param col2 New value for second column
     */
    public void setCol2(String col2) {
        this.col2 = cleanString(col2);
    }
    
    /**
     * Sets the third column value
     * @param col3 New value for third column
     */
    public void setCol3(String col3) {
        this.col3 = cleanString(col3);
    }
    
    /**
     * Sets the fourth column value
     * @param col4 New value for fourth column
     */
    public void setCol4(String col4) {
        this.col4 = cleanString(col4);
    }
    
    /**
     * Sets the fifth column value
     * @param col5 New value for fifth column
     */
    public void setCol5(String col5) {
        this.col5 = cleanString(col5);
    }
    
    /**
     * Sets the sixth column value
     * @param col6 New value for sixth column
     */
    public void setCol6(String col6) {
        this.col6 = cleanString(col6);
    }
    
    // Utility methods
    
    /**
     * Checks if this DTO has any non-empty content
     * @return true if at least one column has content, false if all are empty
     */
    public boolean hasContent() {
        return !col1.isEmpty() || !col2.isEmpty() || !col3.isEmpty() || 
               !col4.isEmpty() || !col5.isEmpty() || !col6.isEmpty();
    }
    
    /**
     * Checks if all columns have content (no empty fields)
     * @return true if all columns contain data, false if any column is empty
     */
    public boolean isComplete() {
        return !col1.isEmpty() && !col2.isEmpty() && !col3.isEmpty() && 
               !col4.isEmpty() && !col5.isEmpty() && !col6.isEmpty();
    }
    
    /**
     * Gets column value by index (0-based)
     * @param index Column index (0-5)
     * @return Column value or empty string if index is invalid
     */
    public String getColumnByIndex(int index) {
        switch (index) {
            case 0: return col1;
            case 1: return col2;
            case 2: return col3;
            case 3: return col4;
            case 4: return col5;
            case 5: return col6;
            default: return "";
        }
    }
    
    /**
     * Sets column value by index (0-based)
     * @param index Column index (0-5)
     * @param value New value for the column
     */
    public void setColumnByIndex(int index, String value) {
        switch (index) {
            case 0: setCol1(value); break;
            case 1: setCol2(value); break;
            case 2: setCol3(value); break;
            case 3: setCol4(value); break;
            case 4: setCol5(value); break;
            case 5: setCol6(value); break;
        }
    }
    
    /**
     * Returns all column values as an array
     * @return String array containing all 6 column values
     */
    public String[] toArray() {
        return new String[]{col1, col2, col3, col4, col5, col6};
    }
    
    /**
     * Creates a copy of this DTO
     * @return New TableRowDto with same values
     */
    public TableRowDto copy() {
        return new TableRowDto(col1, col2, col3, col4, col5, col6);
    }
    
    /**
     * String representation for debugging and logging
     * @return Formatted string showing all column values
     */
    @Override
    public String toString() {
        return String.format("TableRowDto{col1='%s', col2='%s', col3='%s', col4='%s', col5='%s', col6='%s'}", 
                           col1, col2, col3, col4, col5, col6);
    }
    
    /**
     * Compact string representation for display
     * @return Pipe-separated column values
     */
    public String toDisplayString() {
        return String.format("%s | %s | %s | %s | %s | %s", col1, col2, col3, col4, col5, col6);
    }
    
    /**
     * Equals method for comparing DTOs
     * @param obj Object to compare with
     * @return true if all columns match
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        TableRowDto that = (TableRowDto) obj;
        return col1.equals(that.col1) && col2.equals(that.col2) && col3.equals(that.col3) &&
               col4.equals(that.col4) && col5.equals(that.col5) && col6.equals(that.col6);
    }
    
    /**
     * Hash code for use in collections
     * @return Hash code based on all column values
     */
    @Override
    public int hashCode() {
        int result = col1.hashCode();
        result = 31 * result + col2.hashCode();
        result = 31 * result + col3.hashCode();
        result = 31 * result + col4.hashCode();
        result = 31 * result + col5.hashCode();
        result = 31 * result + col6.hashCode();
        return result;
    }
}