package com.mycompany.app2;

/**
 * Manages table boundaries and column definitions
 * This class defines where the table is located on the PDF page and how columns are divided
 */
public class TableBoundaries {
    
    // Column boundaries define where each column starts and ends (X coordinates)
    // Default setup: Column 3 is the largest for longer text content
    private float[] columnBoundaries = {
        50f,   // Start of Column 1
        120f,  // Start of Column 2 (Column 1 width: 70px)
        155f,  // Start of Column 3 (Column 2 width: 35px - narrow for short codes)
        380f,  // Start of Column 4 (Column 3 width: 225px - LARGEST for country names)
        450f,  // Start of Column 5 (Column 4 width: 70px)
        520f,  // Start of Column 6 (Column 5 width: 70px)
        590f   // End of Column 6   (Column 6 width: 70px)
    };
    
    // Table area boundaries - defines the rectangular area containing the table
    private float tableLeft = 50f;     // Left edge of table
    private float tableRight = 590f;   // Right edge of table
    private float tableTop = 140f;     // Top edge - adjusted to skip header rows
    private float tableBottom = 700f;  // Bottom edge of table
    
    /**
     * Default constructor using predefined boundaries
     */
    public TableBoundaries() {
        // Use default values defined above
    }
    
    /**
     * Constructor with custom boundaries
     * @param columnBoundaries Array of 7 values defining 6 column boundaries
     * @param left Left edge of table area
     * @param right Right edge of table area  
     * @param top Top edge of table area (higher Y value in PDF coordinates)
     * @param bottom Bottom edge of table area (lower Y value in PDF coordinates)
     */
    public TableBoundaries(float[] columnBoundaries, float left, float right, float top, float bottom) {
        setColumnBoundaries(columnBoundaries);
        this.tableLeft = left;
        this.tableRight = right;
        this.tableTop = top;
        this.tableBottom = bottom;
    }
    
    /**
     * Checks if a given X,Y coordinate is within the defined table area
     * @param x X coordinate to check
     * @param y Y coordinate to check
     * @return true if the point is inside the table boundaries
     */
    public boolean isWithinTableBounds(float x, float y) {
        return x >= tableLeft && x <= tableRight && y >= tableTop && y <= tableBottom;
    }
    
    /**
     * Determines which column a given X coordinate belongs to
     * @param x X coordinate to analyze
     * @return Column index (0-5) or -1 if outside all columns
     */
    public int getColumnIndex(float x) {
        // Check each column boundary pair to find which column contains this X position
        for (int i = 0; i < columnBoundaries.length - 1; i++) {
            if (x >= columnBoundaries[i] && x < columnBoundaries[i + 1]) {
                return i;  // Return column index (0-based)
            }
        }
        return -1; // X coordinate is outside all defined columns
    }
    
    /**
     * Updates the column boundary definitions
     * @param newBoundaries Array of exactly 7 float values (for 6 columns)
     */
    public void setColumnBoundaries(float[] newBoundaries) {
        if (newBoundaries != null && newBoundaries.length == 7) { // 6 columns = 7 boundaries
            this.columnBoundaries = newBoundaries.clone();
            System.out.println("Column boundaries updated:");
            printColumnBoundaries();
        } else {
            System.out.println("Error: Need exactly 7 boundary values for 6 columns");
        }
    }
    
    /**
     * Prints the current column configuration to console for debugging
     * Shows each column's start, end, and width
     */
    public void printColumnBoundaries() {
        System.out.println("Current Column Boundaries:");
        for (int i = 0; i < columnBoundaries.length - 1; i++) {
            System.out.printf("Column %d: %.1f to %.1f (width: %.1f)%n", 
                i + 1, 
                columnBoundaries[i], 
                columnBoundaries[i + 1], 
                columnBoundaries[i + 1] - columnBoundaries[i]);
        }
        System.out.println();
    }
    
    // Getter methods for table area boundaries
    
    /**
     * @return Left edge X coordinate of table area
     */
    public float getTableLeft() { 
        return tableLeft; 
    }
    
    /**
     * @return Right edge X coordinate of table area
     */
    public float getTableRight() { 
        return tableRight; 
    }
    
    /**
     * @return Top edge Y coordinate of table area
     */
    public float getTableTop() { 
        return tableTop; 
    }
    
    /**
     * @return Bottom edge Y coordinate of table area
     */
    public float getTableBottom() { 
        return tableBottom; 
    }
    
    /**
     * Updates all table area boundaries at once
     * @param left New left boundary
     * @param right New right boundary
     * @param top New top boundary
     * @param bottom New bottom boundary
     */
    public void setTableBounds(float left, float right, float top, float bottom) {
        this.tableLeft = left;
        this.tableRight = right;
        this.tableTop = top;
        this.tableBottom = bottom;
    }
    
    /**
     * Returns a copy of the column boundaries array
     * @return Clone of column boundaries to prevent external modification
     */
    public float[] getColumnBoundaries() {
        return columnBoundaries.clone();
    }
}