package ru.embajada.invoice_generator.fillers;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.springframework.stereotype.Component;
import ru.embajada.invoice_generator.dto.MovistarData;
import ru.embajada.invoice_generator.dto.VtrData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static ru.embajada.invoice_generator.utils.ValuesUtils.fillField;

@Component
public class VtrFiller {
    public byte[] fill(VtrData data, File template) throws IOException {
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
            fillField(form, "totalAmount", data.getTotalAmount());
            fillField(form, "netto", data.getNetto());
            fillField(form, "clientCode", data.getClientCode());
            fillField(form, "totalServicios", data.getTotalServicios());
            fillField(form, "dopTv", data.getDopTv());
            fillField(form, "otroCargos", data.getOtroCargos());
            fillField(form, "totalOtroCargos", data.getTotalOtroCargos());
            fillField(form, "totalSaldo", data.getTotalSaldo());
            fillField(form, "totalServicios", data.getTotalServicios());
            fillField(form, "neto", data.getMontoNeto());
            fillField(form, "exento", data.getMontoExento());
            fillField(form, "iva", data.getIva());
            fillField(form, "address", data.getAddress());
            fillField(form, "servicios", data.getServicios());


            // Можно заблокировать поля, чтобы нельзя было редактировать в Acrobat
            form.getFields().forEach(field -> field.setReadOnly(true));

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            templateDoc.save(out);
            return out.toByteArray();
        }
    }
}
