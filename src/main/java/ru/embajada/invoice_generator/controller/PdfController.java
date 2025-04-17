package ru.embajada.invoice_generator.controller;

import ru.embajada.invoice_generator.dto.PdfData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.embajada.invoice_generator.service.PdfProcessingService;
import ru.embajada.invoice_generator.utils.DateFormatUtils;
import ru.embajada.invoice_generator.utils.HeaderUtils;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PdfController {

    private final PdfProcessingService pdfProcessingService;

    @GetMapping
    public String uploadForm() {
        return "upload-form";
    }

    @PostMapping("/process-pdf")
    public ResponseEntity<Resource> processPdf(@RequestParam("file") MultipartFile file) {
        try {
            PdfData data = pdfProcessingService.extractDataFromPdf(file);

            byte[] result = pdfProcessingService.fillTemplateWithData(data, file);

            String rawFilename = DateFormatUtils.convertRussianMonthToNumeric(data.getIssueDate()) + "-" +
                    data.getCompanyName() + "-" + data.getServiceName() + ".pdf";

            ByteArrayResource resource = new ByteArrayResource(result);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, HeaderUtils.createUtf8AttachmentHeader(rawFilename));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (Exception e) {
            log.error("Ошибка обработки PDF", e);
            return ResponseEntity.badRequest().build();
        }
    }
}