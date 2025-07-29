package com.mycompany.app4.process;

/**
 * Text with its position on the page
 * This stores a piece of text (like a letter or word) and where it appears on the PDF page
 */
public class TextWithPosition {
    public final String text;  // The actual text content (e.g., "A", "Country", "7")
    public final float x;      // Horizontal position (pixels from left edge)
    public final float y;      // Vertical position (pixels from bottom in PDF coordinates)
    
    /**
     * Constructor: Create text with position
     * @param text The actual text content
     * @param x Horizontal position on page (left to right)
     * @param y Vertical position on page (bottom to top in PDF coordinate system)
     */
    public TextWithPosition(String text, float x, float y) {
        this.text = text != null ? text : "";
        this.x = x;
        this.y = y;
    }
}