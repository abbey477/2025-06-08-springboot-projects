package com.mycompany.app6;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceGray;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class PDFProcessor {

    // Method 1: Enhanced color removal
    public static void removeColors(String inputFileName, String outputFileName) throws IOException {
        InputStream inputStream = PDFProcessor.class.getClassLoader()
                .getResourceAsStream("pdf/" + inputFileName);

        if (inputStream == null) {
            throw new IOException("File not found in resources/pdf/: " + inputFileName);
        }

        PDDocument document = PDDocument.load(inputStream);

        for (PDPage page : document.getPages()) {
            PDFColorRemover remover = new PDFColorRemover();
            remover.processPage(page);
        }

        URL resourceUrl = PDFProcessor.class.getClassLoader().getResource("pdf/");
        if (resourceUrl == null) {
            throw new IOException("Resources pdf directory not found");
        }

        String outputPath = new File(resourceUrl.getPath(), outputFileName).getPath();
        document.save(outputPath);
        document.close();
        inputStream.close();
    }

    // Method 2: Create completely new PDF with just text (most reliable)
    public static void createCleanTextPDF(String inputFileName, String outputFileName) throws IOException {
        InputStream inputStream = PDFProcessor.class.getClassLoader()
                .getResourceAsStream("pdf/" + inputFileName);

        if (inputStream == null) {
            throw new IOException("File not found in resources/pdf/: " + inputFileName);
        }

        PDDocument originalDoc = PDDocument.load(inputStream);
        PDDocument newDoc = new PDDocument();

        // Extract text and recreate pages
        PDFTextStripper stripper = new PDFTextStripper();

        for (int i = 1; i <= originalDoc.getNumberOfPages(); i++) {
            stripper.setStartPage(i);
            stripper.setEndPage(i);
            String pageText = stripper.getText(originalDoc);

            // Create new page with just text
            PDPage newPage = new PDPage(PDRectangle.A4);
            newDoc.addPage(newPage);

            PDPageContentStream contentStream = new PDPageContentStream(newDoc, newPage);
            contentStream.beginText();
            contentStream.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(50, 750);

            // Add text line by line
            String[] lines = pageText.split("\\r?\\n");
            for (String line : lines) {
                contentStream.showText(line);
                contentStream.newLine();
            }

            contentStream.endText();
            contentStream.close();
        }

        URL resourceUrl = PDFProcessor.class.getClassLoader().getResource("pdf/");
        String outputPath = new File(resourceUrl.getPath(), outputFileName).getPath();
        newDoc.save(outputPath);

        originalDoc.close();
        newDoc.close();
        inputStream.close();
    }
}