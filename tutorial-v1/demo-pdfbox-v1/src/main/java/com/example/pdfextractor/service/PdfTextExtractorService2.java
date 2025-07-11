package com.example.pdfextractor.service;

import com.example.pdfextractor.util.Const;
import com.example.pdfextractor.util.ResourceFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class PdfTextExtractorService2 implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting PdfTextExtractorService2");
        runVersion01();
    }

    private void runVersion01() throws IOException {

        File file = ResourceFileUtil.getPdfFile(Const.PDF);

        log.info("Is PDF file exist ? {}", file.exists());

        try (PDDocument document = Loader.loadPDF(file)) {
            for (PDPage page : document.getPages()){
                log.info("eeeeeeee");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sdksdkl() {
        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBufferedFile("yourfile.pdf"))) {
            for (PDPage page : document.getPages()){

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void skldksklsl(File file) {
        try (PDDocument document = Loader.loadPDF(file)) {
            int pageIndex = 0;
            float x = 100, y = 200, width = 150, height = 30;

            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setStartPage(pageIndex + 1);
            stripper.setEndPage(pageIndex + 1);

            // Get all text from the page
            String pageText = stripper.getText(document);

            // Now filter by coordinates
            StringBuilder cellText = new StringBuilder();

            stripper = new PDFTextStripper() {
                @Override
                protected void writeString(String string, List<TextPosition> textPositions) {
                    for (TextPosition text : textPositions) {
                        // Check if text is within cell bounds
                        if (text.getX() >= x && text.getX() <= x + width &&
                                text.getY() >= y && text.getY() <= y + height) {
                            cellText.append(text.getUnicode());
                        }
                    }
                }
            };

            stripper.setStartPage(pageIndex + 1);
            stripper.setEndPage(pageIndex + 1);
            stripper.getText(document); // This triggers the writeString method

            System.out.println("Cell text: " + cellText.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
