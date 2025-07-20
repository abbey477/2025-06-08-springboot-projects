package com.mycompany.app5;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class PDFProcessor {

    public static void removeColors(String inputFileName, String outputFileName) throws IOException {
        // Load from resources/pdf/ directory
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

        // Get the resources/pdf directory path for output
        URL resourceUrl = PDFProcessor.class.getClassLoader().getResource("pdf/");
        if (resourceUrl == null) {
            throw new IOException("Resources pdf directory not found");
        }

        String outputPath = new File(resourceUrl.getPath(), outputFileName).getPath();
        System.out.println("Output path: " + outputPath);
        document.save(outputPath);
        document.close();
        inputStream.close();
    }
}