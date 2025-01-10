package com.example.CourseSellingWeb.services.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor

public class PDFService implements PDFServiceImpl{
    @Override
    public void exportToPdf(HttpServletResponse response, String employeeName, BigDecimal totalRevenue, int totalCourseSold) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        String fontPath = "src/main/resources/font/arial.ttf";
        BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font fontBold = new Font(baseFont, 14, Font.BOLD);

        Font fontHeader = new Font(baseFont, 22, Font.BOLD);
        Font fontParagraph = new Font(baseFont, 14, Font.NORMAL);

        Paragraph headerParagraph = new Paragraph("BÁO CÁO THỐNG KÊ", fontHeader);
        headerParagraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(headerParagraph);

        Paragraph infoParagraph = new Paragraph();
        infoParagraph.setFont(fontParagraph);
        infoParagraph.add("\nHọ và tên: " + employeeName + "\n\n");
        infoParagraph.add("Ngày xuất báo cáo: " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + "\n\n");

        // Report Content
        infoParagraph.add(new Chunk("Nội dung: \n\n",fontBold));
        infoParagraph.add(" - Tổng doanh thu trên hóa đơn năm 2024: " + String.format("%.2f", totalRevenue) + " VND\n\n");
        infoParagraph.add(" - Tổng số khóa học đã bán: " + totalCourseSold + " khóa học\n\n");

        document.add(infoParagraph);

        // Declaration
        Paragraph declarationParagraph = new Paragraph("Tôi xin cam kết và chịu trách nhiệm với báo cáo thống kê này!\n\n ", fontParagraph);
        declarationParagraph.setAlignment(Paragraph.ALIGN_LEFT);

        Paragraph signParagraph = new Paragraph("Người lập\n" + employeeName,fontBold);
        signParagraph.setAlignment(Paragraph.ALIGN_RIGHT);
        signParagraph.setIndentationRight(100);
        document.add(declarationParagraph);
        document.add(signParagraph);

        document.close();
    }


}

