package com.example.CourseSellingWeb.services.pdf;

import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Service
public interface PDFServiceImpl {
    void exportToPdf(HttpServletResponse response, String employeeName, BigDecimal totalRevenue, int totalCourseSold) throws IOException, DocumentException;

}
