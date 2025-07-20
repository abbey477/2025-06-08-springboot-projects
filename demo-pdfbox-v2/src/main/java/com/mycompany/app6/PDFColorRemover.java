package com.mycompany.app6;

import org.apache.pdfbox.contentstream.PDFStreamEngine;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSInteger;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class PDFColorRemover extends PDFStreamEngine {

    @Override
    protected void processOperator(Operator operator, List<COSBase> operands) throws IOException {
        String op = operator.getName();

        // Convert all color operations to black (0) or white (1)
        if (op.equals("rg") || op.equals("RG")) {
            // RGB colors - convert to black
            List<COSBase> newOperands = new ArrayList<>();
            newOperands.add(COSInteger.ZERO); // R = 0
            newOperands.add(COSInteger.ZERO); // G = 0
            newOperands.add(COSInteger.ZERO); // B = 0
            super.processOperator(operator, newOperands);
            return;
        }

        if (op.equals("k") || op.equals("K")) {
            // CMYK colors - convert to black
            List<COSBase> newOperands = new ArrayList<>();
            newOperands.add(COSInteger.ZERO); // C = 0
            newOperands.add(COSInteger.ZERO); // M = 0
            newOperands.add(COSInteger.ZERO); // Y = 0
            newOperands.add(COSInteger.ONE);  // K = 1 (black)
            super.processOperator(operator, newOperands);
            return;
        }

        if (op.equals("g") || op.equals("G")) {
            // Gray colors - keep as is or force to black
            List<COSBase> newOperands = new ArrayList<>();
            newOperands.add(COSInteger.ZERO); // Gray = 0 (black)
            super.processOperator(operator, newOperands);
            return;
        }

        if (op.equals("sc") || op.equals("SC") || op.equals("scn") || op.equals("SCN")) {
            // Generic color space - convert to black
            List<COSBase> newOperands = new ArrayList<>();
            newOperands.add(COSInteger.ZERO);
            super.processOperator(operator, newOperands);
            return;
        }

        // Process all other operations normally
        super.processOperator(operator, operands);
    }
}