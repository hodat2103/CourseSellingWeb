package com.example.CourseSellingWeb.services.pdf;

import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface PDFServiceImpl {
    void exportToPdf(HttpServletResponse response) throws IOException, DocumentException;
}
