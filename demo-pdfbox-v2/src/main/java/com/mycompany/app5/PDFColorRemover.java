package com.mycompany.app5;

import org.apache.pdfbox.contentstream.PDFStreamEngine;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import java.io.IOException;
import java.util.List;

public class PDFColorRemover extends PDFStreamEngine {

    @Override
    protected void processOperator(Operator operator, List<COSBase> operands) throws IOException {
        String op = operator.getName();

        // Skip all color-setting operations
        if (op.equals("rg") || op.equals("RG") ||  // RGB colors
                op.equals("k") || op.equals("K") ||   // CMYK colors
                op.equals("g") || op.equals("G") ||   // Gray colors
                op.equals("sc") || op.equals("SC") || // Color space colors
                op.equals("scn") || op.equals("SCN")) { // Color space with name
            return; // Skip these operations
        }

        super.processOperator(operator, operands);
    }
}