package com.example.pdfextractor.runner;

import com.example.pdfextractor.service.PdfTextExtractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PdfExtractorRunner implements CommandLineRunner {

    @Autowired
    private PdfTextExtractorService extractorService;

    @Override
    public void run(String... args) throws Exception {

        String pdfFile = "test-table-extraction.pdf";

        System.out.println("🚀 Spring Boot PDF Text Extractor Started");
        System.out.println("📄 Processing: " + pdfFile);
        System.out.println();

        // Extract table cells from Page 1
        System.out.println("=== PAGE 1 EXTRACTION ===");

        String cell1 = extractorService.extractText(pdfFile, 1, 100, 400, 150, 150);
        System.out.println("Cell [0,0]: '" + cell1 + "'");

        String cell2 = extractorService.extractText(pdfFile, 1, 250, 400, 150, 150);
        System.out.println("Cell [1,0]: '" + cell2 + "'");

        String cell3 = extractorService.extractText(pdfFile, 1, 400, 400, 150, 150);
        System.out.println("Cell [2,0]: '" + cell3 + "'");

        String cell4 = extractorService.extractText(pdfFile, 1, 100, 550, 150, 150);
        System.out.println("Cell [0,1]: '" + cell4 + "'");

        String cell5 = extractorService.extractText(pdfFile, 1, 250, 550, 150, 150);
        System.out.println("Cell [1,1]: '" + cell5 + "'");

        String cell6 = extractorService.extractText(pdfFile, 1, 400, 550, 150, 150);
        System.out.println("Cell [2,1]: '" + cell6 + "'");

        System.out.println();
        System.out.println("=== QUICK EXTRACT TEST ===");

        // Test the quick extract method
        String quickTest1 = extractorService.quickExtract(pdfFile, 1, "100,400,150,150");
        System.out.println("Quick extract [0,0]: '" + quickTest1 + "'");

        String quickTest2 = extractorService.quickExtract(pdfFile, 1, "250,550,150,150");
        System.out.println("Quick extract [1,1]: '" + quickTest2 + "'");

        System.out.println();
        System.out.println("=== PAGE 2 TEST ===");

        // Test different page/coordinates
        String page2Cell = extractorService.extractText(pdfFile, 2, 80, 450, 150, 150);
        System.out.println("Page 2, Cell [0,0]: '" + page2Cell + "'");

        System.out.println();
        System.out.println("✅ Extraction completed!");
    }
}