package com.example.CourseSellingWeb.configurations;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import com.example.CourseSellingWeb.services.pdf.PDFService;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("${api.prefix}/pdf")
@RequiredArgsConstructor
public class PDFController {

    private final PDFService pdfService;

    @GetMapping("/export")
    public void createPDF(HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        pdfService.exportToPdf(response);
    }
}