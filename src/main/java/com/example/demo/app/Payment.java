package com.example.demo.app;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Payment {
    public static void fileOutput(Request request, Workbook workbook, String fileName) throws IOException {
        Sheet sheet = workbook.createSheet("Request " + request.getNumber());
        final double TOTAL_PRICE;

        if (request.isProblemsWithFreight()) {
            TOTAL_PRICE = request.getCostTransportation() * (1 + request.getFreight());
        } else {
            TOTAL_PRICE = request.getCostTransportation();
        }
        final double REMAINS = TOTAL_PRICE - request.getPrepayment();


        int rowCounter = 0;
        Row row = sheet.createRow(rowCounter++);
        List<Cell> cells = new ArrayList<>(8);
        for (int i = 0; i < 4; i++) {
            cells.add(row.createCell(i));
        }
        cells.get(0).setCellValue("Total price");
        cells.get(1).setCellValue("Remains");
        cells.get(2).setCellValue("Payment type");
        cells.get(3).setCellValue("Number of days before payment");

        row = sheet.createRow(rowCounter++);
        cells.clear();
        for (int i = 0; i < 4; i++) {
            cells.add(row.createCell(i));
        }
        cells.get(0).setCellValue(TOTAL_PRICE);
        cells.get(1).setCellValue(REMAINS);
        cells.get(2).setCellValue(request.getPaymentType().toString());
        cells.get(3).setCellValue(request.getNumberOfDaysBeforePayment());
        workbook.write(new FileOutputStream(fileName));
    }

}