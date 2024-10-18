package com.example.CourseSellingWeb.models;

public interface ExcelExportable {
    Object[] toExcelRow();
    String[] getColumnHeaders();
}
