package ru.embajada.invoice_generator.parser;

import org.springframework.stereotype.Component;
import ru.embajada.invoice_generator.dto.PdfData;
import org.springframework.beans.factory.annotation.Value;
import ru.embajada.invoice_generator.utils.DateFormatUtils;

import static ru.embajada.invoice_generator.utils.ValuesUtils.extractValue;
import static ru.embajada.invoice_generator.utils.ValuesUtils.parsePeriod;

@Component
public class MovistarParser {

    @Value("${app.movistar.inet.embajada}")
    private String movistarInetEmbajada;

    @Value("${app.movistar.fijo.8843}")
    private String movistarFijo8843;

    @Value("${app.movistar.fijo.res_kv}")
    private String movistarFijoResKv;

    @Value("${app.movistar.fijo.res_posol}")
    private String movistarFijoResPosol;

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


        if (data.getClientCode().startsWith(movistarInetEmbajada)) {
            data.setServiceName("интернет (Посольство)");
        } else if (data.getClientCode().startsWith(movistarFijo8843)) {
            data.setServiceName("cтац. связь на ПДК (8843)");
        } else if (data.getClientCode().startsWith(movistarFijoResPosol)) {
            data.setServiceName("cтац. связь на резиденции Посла (4774)");
        } else if (data.getClientCode().startsWith(movistarFijoResKv)) {
            data.setServiceName("cтац. связь на резиденции (жилье 0720)");
        } else {
            data.setServiceName("service n/a");
        }

        data.setIssueDate(DateFormatUtils.localizeMonth(data.getIssueDate()));
        data.setDueDate(DateFormatUtils.localizeMonth(data.getDueDate()));
        data.setPeriod(parsePeriod(data));
    }
}
