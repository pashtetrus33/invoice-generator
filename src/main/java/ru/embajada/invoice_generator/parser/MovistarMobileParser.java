package ru.embajada.invoice_generator.parser;

import org.springframework.stereotype.Component;
import ru.embajada.invoice_generator.dto.PdfData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.embajada.invoice_generator.utils.DateFormatUtils;

import static ru.embajada.invoice_generator.utils.ValuesUtils.extractValue;
import static ru.embajada.invoice_generator.utils.ValuesUtils.parsePeriod;

@Component
public class MovistarMobileParser {

    @Value("${app.movistar.mobile.pdk}")
    private String movistarMobilePdk;

    @Value("${app.movistar.mobile.posol}")
    private String movistarMobilePosol;

    @Value("${app.movistar.mobile.stsovetnik}")
    private String movistarMobileStsovetnik;

    public void parse(String text, PdfData data) {
        data.setClientCode(extractValue(text, "Código Cliente :"));
        data.setInvoiceNumber(extractValue(text, "FACTURA ELECTRÓNICA"));
        data.setIssueDate(extractValue(text, "Fecha de Emisión :"));
        data.setDueDate(extractValue(text, "Vencimiento"));
        data.setPeriod(extractValue(text, "Período de Facturación :"));
        data.setTotalAmount(extractValue(text, "Total a Pagar"));
        String extractValue = extractValue(text, "Neto Exento IVA TOTAL");
        String[] values = extractValue.split(" ");
        if (values.length > 3) {
            data.setNetto(values[0]);
            data.setNds(values[2]);
            data.setServiceAmount(values[3]);
        } else {
            data.setNds("n/a");
            data.setNetto("n/a");
            data.setServiceAmount("n/a");
        }

        extractValue = extractValue(text, "Venta segun Boleta/Factura");
        values = extractValue.split(" ");
        if (values.length > 1) {
            data.setAddService(values[1]);
        } else {
            data.setAddService("0");
        }

        extractValue = extractValue(text, "Saldo Anterior vigente al");
        values = extractValue.split(" ");
        if (values.length > 1) {
            data.setSaldoDate(DateFormatUtils.localizeMonth(values[0]));
            data.setDolg(values[1]);
        } else {
            data.setDolg("n/a");
            data.setSaldoDate("n/a");
        }


        if (data.getClientCode().startsWith(movistarMobilePosol)) {
            data.setServiceName("моб.связь Посла");
        } else if (data.getClientCode().startsWith(movistarMobilePdk)) {
            data.setServiceName("моб.связь на ПДК");
        } else if (data.getClientCode().startsWith(movistarMobileStsovetnik)) {
            data.setServiceName("моб.связь старшего советника");
        }


        data.setIssueDate(DateFormatUtils.localizeMonth(data.getIssueDate()));
        data.setDueDate(DateFormatUtils.localizeMonth(data.getDueDate()));
        data.setPeriod(parsePeriod(data));
    }
}
