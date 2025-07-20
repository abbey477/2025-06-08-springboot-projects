package com.mycompany.app6;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            // Method 1: Enhanced color removal
            System.out.println("Trying enhanced color removal...");
            PDFProcessor.removeColors("cre-crc-current-english.pdf", "output_no_colors_v1.pdf");
            System.out.println("Method 1 completed!");

            // Method 2: Create clean text-only PDF (most reliable)
            System.out.println("Creating clean text-only PDF...");
            PDFProcessor.createCleanTextPDF("cre-crc-current-english.pdf", "output_clean_text.pdf");
            System.out.println("Method 2 completed!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}