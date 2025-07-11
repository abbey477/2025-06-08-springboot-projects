package com.example.pdfextractor.service;


import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

//@Service
public class PdfTextExtractorService {


    public String extractText(String pdfPath, int pageNumber,
                              float x, float y, float width, float height) {
        PDDocument doc = null;
        try {

            doc = Loader.loadPDF(new File(pdfPath));

            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setStartPage(pageNumber);
            stripper.setEndPage(pageNumber);
            //stripper.setRegion(new PDRectangle(x, y, width, height));

            String text = stripper.getText(doc);

            return text.trim();

        } catch (IOException e) {
            return "ERROR: " + e.getMessage();
        } finally {
            if (doc != null) {
                try {
                    doc.close();
                } catch (IOException e) {
                    System.err.println("Error closing document: " + e.getMessage());
                }
            }
        }
    }

    public String quickExtract(String pdfPath, int page, String coordinates) {
        String[] coords = coordinates.split(",");
        return extractText(pdfPath, page,
                Float.parseFloat(coords[0]),
                Float.parseFloat(coords[1]),
                Float.parseFloat(coords[2]),
                Float.parseFloat(coords[3])
        );
    }
}