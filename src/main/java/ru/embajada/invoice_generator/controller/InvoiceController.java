package ru.embajada.invoice_generator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.embajada.invoice_generator.model.InvoiceData;
import ru.embajada.invoice_generator.service.PdfGenerationService;

@Slf4j
@Controller
public class InvoiceController {

    private final PdfGenerationService pdfGenerationService;

    public InvoiceController(PdfGenerationService pdfGenerationService) {
        this.pdfGenerationService = pdfGenerationService;
    }

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("invoiceData", new InvoiceData());
        return "invoice-form";
    }

    @PostMapping("/generate")
    public ResponseEntity<Resource> generateInvoice(@ModelAttribute InvoiceData invoiceData) {
        try {
            byte[] pdfBytes = pdfGenerationService.generatePdf(invoiceData);

            ByteArrayResource resource = new ByteArrayResource(pdfBytes);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            ContentDisposition.attachment()
                                    .filename("invoice.pdf")
                                    .build()
                                    .toString())
                    .body(resource);
        } catch (Exception e) {
            log.error("Error generating PDF", e);
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    @PostMapping("/generate-from-json")
    public ResponseEntity<Resource> generateInvoiceFromJson(@RequestBody InvoiceData invoiceData) {
        try {
            byte[] pdfBytes = pdfGenerationService.generatePdf(invoiceData);

            ByteArrayResource resource = new ByteArrayResource(pdfBytes);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            ContentDisposition.attachment()
                                    .filename("invoice.pdf")
                                    .build()
                                    .toString())
                    .body(resource);
        } catch (Exception e) {
            log.error("Error generating PDF from JSON", e);
            throw new RuntimeException("Error generating PDF from JSON", e);
        }
    }
}