package com.mycompany.app4;

/**
 * Simple table boundaries - defines where the table is located on the PDF page
 * This tells the parser which area of the page to look for table data
 */
public class TableBoundaries {
    
    // Table area boundaries (adjust these to match your PDF)
    public float left = 50f;    // Left edge of table (pixels from left side of page)
    public float right = 590f;  // Right edge of table
    public float top = 140f;    // Top of table (skip headers by using higher value)
    public float bottom = 700f; // Bottom of table (use lower value to avoid footer text)
    
    // Column boundaries - where each column starts and ends
    // Format: [col1_start, col2_start, col3_start, col4_start, col5_start, col6_start, table_end]
    public float[] columns = {50f, 120f, 155f, 380f, 450f, 520f, 590f};
    
    /**
     * Default constructor - uses standard OECD table boundaries
     */
    public TableBoundaries() {
        // Uses the default values defined above
    }
    
    /**
     * Constructor with custom table area
     * @param left Left edge X coordinate
     * @param right Right edge X coordinate  
     * @param top Top edge Y coordinate (higher value = skip more header area)
     * @param bottom Bottom edge Y coordinate (lower value = avoid footer text)
     */
    public TableBoundaries(float left, float right, float top, float bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        // Note: columns array stays the same unless you call setColumns()
    }
    
    /**
     * Check if a point (X,Y coordinate) is inside the table area
     * @param x Horizontal position to check
     * @param y Vertical position to check
     * @return true if the point is within the table boundaries
     */
    public boolean isInside(float x, float y) {
        return x >= left && x <= right && y >= top && y <= bottom;
    }
    
    /**
     * Determine which column a given X coordinate belongs to
     * @param x Horizontal position to check
     * @return Column number (0-5) or -1 if outside all columns
     */
    public int getColumnIndex(float x) {
        // Check each column boundary to see which column this X position falls into
        for (int i = 0; i < columns.length - 1; i++) {
            if (x >= columns[i] && x < columns[i + 1]) {
                return i;  // Return column index (0=first column, 1=second, etc.)
            }
        }
        return -1;  // X position is outside all defined columns
    }
    
    /**
     * Set custom column boundaries
     * @param newColumns Array of 7 values defining where each column starts/ends
     *                   Example: {50f, 120f, 200f, 300f, 400f, 500f, 590f}
     */
    public void setColumns(float[] newColumns) {
        if (newColumns != null && newColumns.length == 7) {
            this.columns = newColumns;
        }
    }
}