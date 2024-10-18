package com.example.CourseSellingWeb.services.excel;

import com.example.CourseSellingWeb.models.ExcelExportable;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelService implements ExcelServiceImpl {
    public void exportToExcel(List<? extends ExcelExportable> dataList, String fileName, HttpServletResponse response) {
        if (dataList == null || dataList.isEmpty()) {
            throw new IllegalArgumentException("Data list cannot be null or empty.");
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");

            // Create styles
            CellStyle headerStyle = createHeaderCellStyle(workbook);
            CellStyle dataCellStyle = createDataCellStyle(workbook);
            CellStyle dateCellStyle = createDateCellStyle(workbook);

            // Create header row
            int rowNum = 0;
            String[] columnHeaders = dataList.get(0).getColumnHeaders();
            Row headerRow = sheet.createRow(rowNum++);
            createHeaderRow(headerRow, columnHeaders, headerStyle);

            // Create data rows
            createDataRows(dataList, sheet, rowNum, dataCellStyle, dateCellStyle);

            // Adjust column widths
            autoSizeColumns(sheet, columnHeaders.length);

            // Set response headers
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".xlsx\"");

            // Write workbook to the response
            try (ServletOutputStream outputStream = response.getOutputStream()) {
                workbook.write(outputStream);
                outputStream.flush(); // Ensure all data is written to the output stream
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to export data to Excel", e);
        }
    }

    // Helper methods to create styles and populate the Excel sheet...
    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        return headerStyle;
    }

    private CellStyle createDataCellStyle(Workbook workbook) {
        CellStyle dataCellStyle = workbook.createCellStyle();
        dataCellStyle.setAlignment(HorizontalAlignment.CENTER);
        return dataCellStyle;
    }

    private CellStyle createDateCellStyle(Workbook workbook) {
        DataFormat format = workbook.createDataFormat();
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(format.getFormat("dd/MM/yyyy"));
        dateCellStyle.setAlignment(HorizontalAlignment.CENTER);
        return dateCellStyle;
    }

    private void createHeaderRow(Row headerRow, String[] columnHeaders, CellStyle headerStyle) {
        for (int i = 0; i < columnHeaders.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnHeaders[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private void createDataRows(List<? extends ExcelExportable> dataList, Sheet sheet, int rowNum,
                                CellStyle dataCellStyle, CellStyle dateCellStyle) {
        for (ExcelExportable obj : dataList) {
            Row row = sheet.createRow(rowNum++);
            Object[] rowData = obj.toExcelRow();
            for (int i = 0; i < rowData.length; i++) {
                Cell cell = row.createCell(i);
                if (rowData[i] instanceof String) {
                    cell.setCellValue((String) rowData[i]);
                    cell.setCellStyle(dataCellStyle);
                } else if (rowData[i] instanceof Long) {
                    cell.setCellValue((Long) rowData[i]);
                    cell.setCellStyle(dataCellStyle);
                } else if (rowData[i] instanceof Date) {
                    cell.setCellValue((Date) rowData[i]);
                    cell.setCellStyle(dateCellStyle);
                } else if (rowData[i] instanceof Float) {
                    cell.setCellValue((Float) rowData[i]);
                    cell.setCellStyle(dataCellStyle);
                } else if (rowData[i] instanceof Integer) {
                    cell.setCellValue((Integer) rowData[i]);
                    cell.setCellStyle(dataCellStyle);
                } else if (rowData[i] instanceof Boolean) {
                    cell.setCellValue((Boolean) rowData[i] ? "1" : "0");
                    cell.setCellStyle(dataCellStyle);
                } else if (rowData[i] instanceof byte[]) {
                    String value = new String((byte[]) rowData[i]);
                    cell.setCellValue(value);
                }
            }
        }
    }

    private void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
