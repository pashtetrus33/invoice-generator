package ru.embajada.invoice_generator.service;

import ru.embajada.invoice_generator.dto.MovistarData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.embajada.invoice_generator.dto.MovistarMobileData;
import ru.embajada.invoice_generator.dto.PdfData;
import ru.embajada.invoice_generator.dto.VtrData;
import ru.embajada.invoice_generator.fillers.MovistarFiller;
import ru.embajada.invoice_generator.fillers.MovistarMobileFiller;
import ru.embajada.invoice_generator.fillers.VtrFiller;
import ru.embajada.invoice_generator.parser.MovistarMobileParser;
import ru.embajada.invoice_generator.parser.MovistarParser;
import ru.embajada.invoice_generator.parser.VtrParser;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
public class PdfProcessingService {

    private final MovistarParser movistarParser;
    private final MovistarMobileParser movistarMobileParser;
    private final VtrParser vtrParser;

    private final MovistarFiller movistarFiller;
    private final MovistarMobileFiller movistarMobileFiller;
    private final VtrFiller vtrFiller;

    //private static final String TEMPLATES_FOLDER = "/app/templates/";
    private static final String TEMPLATES_FOLDER = "app/templates/";


    public PdfData processPdf(MultipartFile file) throws IOException {
        PDDocument document = PDDocument.load(file.getInputStream());
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);
        document.close();

        PdfData pdfData = null;

        if (text.contains("Telefonica Chile S.A")) {
            pdfData = movistarParser.parse(text);
        } else if (text.contains("Telefónica Móviles Chile S.A.")) {
            pdfData = movistarMobileParser.parse(text);
        } else if (text.contains("VTR COMUNICACIONES SpA")) {
            pdfData = vtrParser.parse(text);
        }
        return pdfData;
    }


    public byte[] fillTemplateWithData(PdfData data) throws IOException {

        File template = findTemplateByPrefix(data.getCompanyName());
        if (template == null) {
            throw new FileNotFoundException("Template not found for prefix: " + data.getCompanyName());
        }
        byte[] result = null;

        if (data instanceof MovistarData) {
            result = movistarFiller.fill((MovistarData) data, template);
        } else if (data instanceof MovistarMobileData) {
            result = movistarMobileFiller.fill((MovistarMobileData) data, template);
        } else if (data instanceof VtrData) {
            result = vtrFiller.fill((VtrData) data, template);
        }

        return result;
    }

    private File findTemplateByPrefix(String prefix) throws IOException {
        return Files.list(Paths.get(TEMPLATES_FOLDER))
                .map(Path::toFile)
                .filter(file -> file.getName().equals(prefix + ".pdf"))
                .findFirst()
                .orElse(null);
    }

    public String createFilename(PdfData data) throws IOException {
        return data.getIssueDate() + "-" + data.getCompanyName() + "-" + data.getServiceName() + ".pdf";
    }
}