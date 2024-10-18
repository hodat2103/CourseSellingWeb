package com.example.CourseSellingWeb.services.excel;

import com.example.CourseSellingWeb.models.ExcelExportable;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExcelServiceImpl {
    void exportToExcel(List<? extends ExcelExportable> dataList, String fileName, HttpServletResponse response);

}
