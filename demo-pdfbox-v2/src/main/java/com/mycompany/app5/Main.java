package com.mycompany.app5;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            // Both input and output files will be in src/main/resources/pdf/
            PDFProcessor.removeColors(
                    "cre-crc-current-english.pdf",
                    "output_no_colors.pdf");
            System.out.println("Colors removed successfully!");
            System.out.println("Output saved to: src/main/resources/pdf/output_no_colors.pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}