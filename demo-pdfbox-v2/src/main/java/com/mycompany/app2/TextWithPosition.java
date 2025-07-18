package com.mycompany.app2;

/**
 * Helper class to store text with its X,Y coordinates from PDF
 * This is used to track where each character/word appears on the page
 */
public class TextWithPosition {
    public final String text;  // The actual text character or word
    public final float x;      // X coordinate (horizontal position, left to right)
    public final float y;      // Y coordinate (vertical position, bottom to top in PDF coordinate system)
    
    /**
     * Constructor: Creates a text position object
     * @param text The text content (character, word, or phrase)
     * @param x X coordinate in PDF points (1 point = 1/72 inch)
     * @param y Y coordinate in PDF points (note: PDF Y increases from bottom to top)
     */
    public TextWithPosition(String text, float x, float y) {
        this.text = text != null ? text : "";
        this.x = x;
        this.y = y;
    }
    
    /**
     * Returns a readable representation showing text and its coordinates
     * @return String in format "[text] X:123.4 Y:567.8"
     */
    @Override
    public String toString() {
        //
        return String.format("[%s] X:%.1f Y:%.1f", text, x, y);
    }
}