package ru.embajada.invoice_generator.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import ru.embajada.invoice_generator.model.InvoiceData;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfGenerationService {

    private static final String FONT_PATH = "/fonts/times.ttf";

    public byte[] generatePdf(InvoiceData data) throws Exception {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfContentByte canvas = writer.getDirectContent();
        BaseFont baseFont = BaseFont.createFont(FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 14);
        Font smallFont = new Font(baseFont, 8);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter periodFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        // Добавление всех элементов в PDF
        addText(canvas, "ИТОГО в текущем месяце: " + data.getTotalAmount(), 72, 70.58f, font);
        addText(canvas, "ИТОГО: " + data.getTotalAmount(), 72, 118.78f, font);
        addText(canvas, "Дополнительные сборы (задолженность, другие услуги): " +
                data.getAdditionalFees(), 72, 134.98f, font);
        addText(canvas, "Стоимость: " + data.getBaseAmount(), 72, 167.16f, font);
        addText(canvas, "Основные услуги: ТАРИФ Internet Grandes Cuentas (Mega 300)",
                72, 183.24f, font);
        addText(canvas, "Период услуги: с " + data.getServicePeriodStart().format(periodFormatter) +
                        " по " + data.getServicePeriodEnd().format(periodFormatter),
                72, 199.31f, font);
        addText(canvas, "Отключение услуги: " + data.getServiceCutoffDate().format(dateFormatter),
                72, 231.59f, font);
        addText(canvas, "Сумма последней оплаты: " + data.getLastPaymentAmount(),
                72, 263.74f, font);
        addText(canvas, "Дата последней оплаты: " + data.getLastPaymentDate().format(dateFormatter),
                72, 295.90f, font);
        addText(canvas, "ИТОГО " + data.getTotalAmount(), 72, 328.05f, font);
        addText(canvas, "Срок оплаты: " + data.getDueDate().format(dateFormatter),
                72, 360.33f, font);
        addText(canvas, "Дата чека: " + data.getIssueDate().format(dateFormatter),
                72, 392.48f, font);
        addText(canvas, "Проспект Америко Веспусио Норте 2127 кв. 101, Витакура",
                72, 424.63f, font);
        addText(canvas, "Номер клиента: " + data.getClientNumber(), 72, 456.91f, font);
        addText(canvas, "Клиент: " + data.getClientName() + ", " + data.getClientCountry(),
                72, 489.06f, font);
        addText(canvas, "Основной офис: Авенида Эль Сальто 5450 Уечураба, Сантьяго, Чили",
                72, 521.21f, font);
        addText(canvas, "Деятельность: Услуги телекоммуникации", 72, 553.49f, font);
        addText(canvas, "Компания: АО ВТР Комуникасьонес", 72, 585.64f, font);
        addText(canvas, "N° " + data.getInvoiceNumber(), 72, 617.79f, font);
        addText(canvas, "ИНН: 76.114.143-0 ЭЛЕКТРОННЫЙ ЧЕК", 72, 666.14f, font);
        addText(canvas, "ПЕРЕВОД C ИСПАНСКОГО ЭЛЕКТРОННОГО ЧЕКА ЗА УСЛУГИ ИНТЕРНЕТА С " +
                        data.getIssueDate().format(dateFormatter) + " по " +
                        data.getDueDate().format(dateFormatter) + " (КОНСУЛЬСКИЙ ОТДЕЛ)",
                72, 698.30f, smallFont);

        document.close();
        return outputStream.toByteArray();
    }

    private void addText(PdfContentByte canvas, String text, float x, float y, Font font) {
        ColumnText columnText = new ColumnText(canvas);
        columnText.setSimpleColumn(
                new Phrase(text, font),
                x, y, x + 500, y + 20,
                0,
                Element.ALIGN_LEFT
        );
        try {
            columnText.go();
        } catch (DocumentException e) {
            throw new RuntimeException("Error adding text to PDF", e);
        }
    }
}