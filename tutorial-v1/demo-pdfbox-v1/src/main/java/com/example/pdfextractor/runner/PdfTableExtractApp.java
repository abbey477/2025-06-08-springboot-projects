package com.example.pdfextractor.runner;


import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class PdfTableExtractApp implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(PdfTableExtractApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java -jar pdf-table-extract.jar <path-to-pdf>");
            return;
        }

        String pdfPath = args[0];
        extractTableText(pdfPath);
    }

    private void extractTableText(String pdfPath) throws Exception {
        File pdfFile = new File(pdfPath);
        try (PDDocument document = Loader.loadPDF(pdfFile)) {

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            // Extract table rows (assuming tab or multi-space separated)
            String[] lines = text.split("\n");

            System.out.println("Table Data Found:");
            System.out.println("-----------------");

            for (String line : lines) {
                if (isTableRow(line)) {
                    String[] cells = parseRow(line);
                    System.out.println("Row: " + String.join(" | ", cells));
                }
            }
        }
    }

    private boolean isTableRow(String line) {
        // Simple check: line has multiple words separated by spaces/tabs
        return line.trim().split("\\s{2,}|\\t").length > 1;
    }

    private String[] parseRow(String line) {
        // Split by multiple spaces or tabs
        return line.trim().split("\\s{2,}|\\t");
    }
}