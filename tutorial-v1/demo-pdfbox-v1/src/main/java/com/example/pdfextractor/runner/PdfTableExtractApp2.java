package com.example.pdfextractor.runner;


import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.Rectangle;
import java.io.File;




public class PdfTableExtractApp2 implements CommandLineRunner {


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

            // Define table area coordinates (x, y, width, height)
            // You'll need to adjust these coordinates based on your PDF
            extractTextFromArea(document, 50, 600, 500, 200, "Table Area");

            // Extract multiple cells individually
            extractTableCells(document);
        }
    }

    private void extractTextFromArea(PDDocument document, float x, float y,
                                     float width, float height, String areaName) throws Exception {

        PDFTextStripperByArea stripper = new PDFTextStripperByArea();

        // Create rectangle for the table area
        Rectangle rect = new Rectangle((int)x, (int)y, (int)width, (int)height);
        stripper.addRegion(areaName, rect);

        // Extract from first page (index 0)
        stripper.extractRegions(document.getPage(0));

        String text = stripper.getTextForRegion(areaName);
        System.out.println("Text from " + areaName + ":");
        System.out.println("-------------------------");
        System.out.println(text);
        System.out.println();
    }

    private void extractTableCells(PDDocument document) throws Exception {
        PDFTextStripperByArea stripper = new PDFTextStripperByArea();

        // Define individual cell areas (adjust coordinates for your PDF)
        // Format: x, y, width, height
        stripper.addRegion("cell1", new Rectangle(50, 650, 100, 30));   // Name column
        stripper.addRegion("cell2", new Rectangle(150, 650, 80, 30));   // Age column
        stripper.addRegion("cell3", new Rectangle(230, 650, 120, 30));  // Department column

        stripper.addRegion("cell4", new Rectangle(50, 620, 100, 30));   // Next row
        stripper.addRegion("cell5", new Rectangle(150, 620, 80, 30));
        stripper.addRegion("cell6", new Rectangle(230, 620, 120, 30));

        stripper.extractRegions(document.getPage(0));

        System.out.println("Individual Cell Extraction:");
        System.out.println("---------------------------");
        System.out.println("Row 1: " +
                stripper.getTextForRegion("cell1").trim() + " | " +
                stripper.getTextForRegion("cell2").trim() + " | " +
                stripper.getTextForRegion("cell3").trim());

        System.out.println("Row 2: " +
                stripper.getTextForRegion("cell4").trim() + " | " +
                stripper.getTextForRegion("cell5").trim() + " | " +
                stripper.getTextForRegion("cell6").trim());
    }
}
