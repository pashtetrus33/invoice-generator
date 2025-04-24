package ru.embajada.invoice_generator.parser;

import org.springframework.stereotype.Component;
import ru.embajada.invoice_generator.dto.MovistarMobileData;
import org.springframework.beans.factory.annotation.Value;
import ru.embajada.invoice_generator.utils.DateFormatUtils;

import static ru.embajada.invoice_generator.utils.ValuesUtils.extractValue;
import static ru.embajada.invoice_generator.utils.ValuesUtils.parsePeriod;

@Component
public class MovistarMobileParser {

    MovistarMobileData data;

    @Value("${app.movistar.mobile.pdk}")
    private String movistarMobilePdk;

    @Value("${app.movistar.mobile.posol}")
    private String movistarMobilePosol;

    @Value("${app.movistar.mobile.stsovetnik}")
    private String movistarMobileStsovetnik;

    public MovistarMobileData parse(String text) {

        data = new MovistarMobileData();

        data.setCompanyName("MovistarMobile");
        data.setClientCode(extractValue(text, "Código Cliente :", null));
        data.setInvoiceNumber(extractValue(text, "FACTURA ELECTRÓNICA", null));
        data.setIssueDate(extractValue(text, "Fecha de Emisión :", null));
        data.setDueDate(extractValue(text, "Vencimiento", null));
        data.setPeriod(extractValue(text, "Período de Facturación :", null));
        data.setTotalAmount(extractValue(text, "Total a Pagar", null));
        String extractValue = extractValue(text, "Neto Exento IVA TOTAL", null);
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

        extractValue = extractValue(text, "Venta segun Boleta/Factura", null);
        values = extractValue.split(" ");
        if (values.length > 1) {
            data.setAddService(values[1]);
        } else {
            data.setAddService("0");
        }

        extractValue = extractValue(text, "Saldo Anterior vigente al", null);
        values = extractValue.split(" ");
        if (values.length > 1) {
            data.setSaldoDate(DateFormatUtils.localizeMonth(values[0]));
            data.setDolg(values[1]);
        } else {
            data.setDolg("n/a");
            data.setSaldoDate("n/a");
        }

        extractValue = extractValue(text, "Cargo Fijo", null);
        values = extractValue.split(" ");
        if (values.length > 2) {
            data.setValorFijo(values[0]);
            data.setDesctoFijo(values[1]);
            data.setTotalFijo(values[2]);
        } else {
            data.setValorFijo("0");
            data.setDesctoFijo("0");
            data.setTotalFijo("0");
        }

        extractValue = extractValue(text, "Sub Total Comunicaciones De Larga Distancia Internacional", null);
        values = extractValue.split(" ");
        if (values.length > 2) {
            data.setValorLarga(values[0]);
            data.setDesctoLarga(values[1]);
            data.setTotalLarga(values[2]);
        } else {
            data.setValorLarga("0");
            data.setDesctoLarga("0");
            data.setTotalLarga("0");
        }

        extractValue = extractValue(text, "TOTAL CUENTA UNICA TELEFONICA", null);
        values = extractValue.split(" ");
        if (values.length > 2) {
            data.setValorTotal(values[0]);
            data.setDesctoTotal(values[1]);
            data.setTotalTotal(values[2]);
        } else {
            data.setValorTotal("0");
            data.setDesctoTotal("0");
            data.setTotalTotal("0");
        }

        extractValue = extractValue(text, "Indisponibilidad de Servicio", null);
        values = extractValue.split(" ");
        if (values.length > 2) {
            data.setTotalIndispServicio(values[2]);
        } else {
            data.setTotalIndispServicio("0");
        }


        if (data.getClientCode().startsWith(movistarMobilePosol)) {
            data.setServiceName("моб.связь Посла");
            data.setAddress("Проспект Америко Веспусио Норте 2127, Витакура");
        } else if (data.getClientCode().startsWith(movistarMobilePdk)) {
            data.setServiceName("моб.связь на ПДК");
            data.setAddress("Проспект Америко Веспусио Норте 2127, Витакура");
        } else if (data.getClientCode().startsWith(movistarMobileStsovetnik)) {
            data.setServiceName("моб.связь старшего советника");
            data.setAddress("Проспект Америко Веспусио Норте 2127, Витакура");
        }


        data.setIssueDate(DateFormatUtils.localizeMonth(data.getIssueDate()));
        data.setDueDate(DateFormatUtils.localizeMonth(data.getDueDate()));
        data.setPeriod(parsePeriod(data));

        return data;
    }
}
