package ru.embajada.invoice_generator.fillers;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.springframework.stereotype.Component;
import ru.embajada.invoice_generator.dto.MovistarMobileData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static ru.embajada.invoice_generator.utils.ValuesUtils.fillField;

@Component
public class MovistarMobileFiller {
    public byte[] fill(MovistarMobileData data, File template) throws IOException {
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
            fillField(form, "valorFijo", data.getValorFijo());
            fillField(form, "desctoFijo", data.getDesctoFijo());
            fillField(form, "totalFijo", data.getTotalFijo());
            fillField(form, "valorLarga", data.getValorLarga());
            fillField(form, "desctoLarga", data.getDesctoLarga());
            fillField(form, "totalLarga", data.getTotalLarga());
            fillField(form, "valorTotal", data.getValorTotal());
            fillField(form, "desctoTotal", data.getDesctoTotal());
            fillField(form, "totalTotal", data.getTotalTotal());
            fillField(form, "indispServicio", "Отсутствие связи");
            fillField(form, "totalIndispServicio", data.getTotalIndispServicio());
            fillField(form, "address", data.getAddress());


            // Можно заблокировать поля, чтобы нельзя было редактировать в Acrobat
            form.getFields().forEach(field -> field.setReadOnly(true));

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            templateDoc.save(out);
            return out.toByteArray();
        }
    }
}
