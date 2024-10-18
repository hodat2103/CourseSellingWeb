package com.example.CourseSellingWeb.services.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor

public class PDFService implements PDFServiceImpl{
    @Override
    public void exportToPdf(HttpServletResponse response) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font fontHeader = FontFactory.getFont(FontFactory.TIMES_BOLD);
        fontHeader.setSize(22);

        Paragraph headerParagraph = new Paragraph("## PDF Heading ##", fontHeader);
        headerParagraph.setAlignment(Paragraph.ALIGN_CENTER);

        Font fontParagraph = FontFactory.getFont(FontFactory.TIMES);
        fontParagraph.setSize(14);

        Paragraph pdfParagraph = new Paragraph("", fontParagraph);
        pdfParagraph.setAlignment(Paragraph.ALIGN_LEFT);

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        for(int i=0;i<=15;i++) {
            pdfParagraph.add("Row: "+i
                    +" Time: "+dateFormatter.format(new Date()) +"\n");
        }

        document.add(headerParagraph);
        document.add(pdfParagraph);
        document.close();
    }
}
