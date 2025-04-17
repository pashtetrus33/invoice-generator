package ru.embajada.invoice_generator.service;

import ru.embajada.invoice_generator.dto.PdfData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.embajada.invoice_generator.parser.MovistarMobileParser;
import ru.embajada.invoice_generator.parser.MovistarParser;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
public class PdfProcessingService {

    private final MovistarParser movistarParser;
    private final MovistarMobileParser movistarMobileParser;

    //private static final String TEMPLATES_FOLDER = "/app/templates/";
    private static final String TEMPLATES_FOLDER = "app/templates/";


    public PdfData extractDataFromPdf(MultipartFile file) throws IOException {
        PDDocument document = PDDocument.load(file.getInputStream());
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);
        document.close();

        PdfData data = new PdfData();

        if (text.contains("Telefonica Chile S.A")) {
            data.setCompanyName("Movistar");
            movistarParser.parse(text, data);
        } else if (text.contains("Telefónica Móviles Chile S.A.")) {
            data.setCompanyName("MovistarMobile");
            movistarMobileParser.parse(text, data);
        } else {
            data.setCompanyName("Company is Not recognized");
        }

        return data;
    }


    public byte[] fillTemplateWithData(PdfData data, MultipartFile originalFile) throws IOException {

        File template = findTemplateByPrefix(data.getCompanyName());
        if (template == null) {
            throw new FileNotFoundException("Template not found for prefix: " + data.getCompanyName());
        }

        try (PDDocument templateDoc = PDDocument.load(template)) {
            PDAcroForm form = templateDoc.getDocumentCatalog().getAcroForm();

            if (form == null) {
                throw new IOException("No AcroForm found in PDF template.");
            }

            // Обеспечиваем отображение введённых значений
            form.setNeedAppearances(true);

            // Заполнение полей
            fillField(form, "invoiceNumber", data.getInvoiceNumber());
            fillField(form, "issueDate", data.getIssueDate());
            fillField(form, "dueDate", data.getDueDate());
            fillField(form, "period", data.getPeriod());
            fillField(form, "serviceName", data.getServiceName());
            fillField(form, "total", data.getTotalAmount());
            fillField(form, "netto", data.getNetto());
            fillField(form, "nds", data.getNds());
            fillField(form, "addService", data.getAddService());
            fillField(form, "saldoDate", data.getSaldoDate());
            fillField(form, "serviceAmount", data.getServiceAmount());
            fillField(form, "dolg", data.getDolg());
            fillField(form, "clientCode", data.getClientCode());


            // Можно заблокировать поля, чтобы нельзя было редактировать в Acrobat
            form.getFields().forEach(field -> field.setReadOnly(true));

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            templateDoc.save(out);
            return out.toByteArray();
        }
    }

    private void fillField(PDAcroForm form, String fieldName, String value) throws IOException {
        PDField field = form.getField(fieldName);
        if (field != null) {
            field.setValue(value != null ? value : "");
        } else {
            System.out.println("Поле не найдено: " + fieldName);
        }
    }


    private File findTemplateByPrefix(String prefix) throws IOException {
        return Files.list(Paths.get(TEMPLATES_FOLDER))
                .map(java.nio.file.Path::toFile)
                .filter(file -> file.getName().startsWith(prefix))
                .findFirst()
                .orElse(null);
    }
}