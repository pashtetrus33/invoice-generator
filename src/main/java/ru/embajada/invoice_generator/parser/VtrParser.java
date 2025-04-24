package ru.embajada.invoice_generator.parser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.embajada.invoice_generator.dto.VtrData;
import ru.embajada.invoice_generator.utils.DateFormatUtils;

import static ru.embajada.invoice_generator.utils.ValuesUtils.extractValue;
import static ru.embajada.invoice_generator.utils.ValuesUtils.parsePeriod;

@Component
public class VtrParser {

    VtrData data;

    @Value("${app.vtr.inet.school}")
    private String vtrInetSchool;

    @Value("${app.vtr.tv.residence}")
    private String vtrTvResidence;

    @Value("${app.vtr.inet.ko}")
    private String vtrInetKo;

    @Value("${app.vtr.tv.embassy}")
    private String vtrTvEmbassy;

    public VtrData parse(String text) {

        data = new VtrData();

        data.setCompanyName("Vtr");
        data.setClientCode(extractValue(text, "CLIENTE N°", null));
        data.setInvoiceNumber(extractValue(text, "BOLETA ELECTRÓNICA", null));
        data.setIssueDate(extractValue(text, "EMISION", null));
        data.setDueDate(extractValue(text, "Vence el", null));
        data.setPeriod(extractValue(text, "Periodo Facturado del", null));
        data.setTotalAmount(extractValue(text, "Total a Pagar", null));
        data.setCortarDate(extractValue(text, "Corte a partir del:", null));
        data.setTotalServicios(extractValue(text, "Total Servicios Principales Contratados", null));
        data.setDopTv(extractValue(text, "Televisión", null));
        data.setOtroCargos(extractValue(text, "Otros Cargos", null));
        data.setTotalOtroCargos(extractValue(text, "Total Consumos y Servicios Adicionales", null));
        data.setTotalServicios(extractValue(text, "Total Servicios Mes Actual", null));
        data.setTotalSaldo(extractValue(text, "Total Saldo Anterior, Cuotas y Otros", null));
        data.setMontoNeto(extractValue(text,"Monto Neto", null));
        data.setMontoExento(extractValue(text,"Monto Exento", null));
        data.setIva(extractValue(text,"IVA 19%", null));
        data.setServicios(extractValue(text, "Cobros y/o Descuentos Mensuales F. Contrato F. Período", "Adicionales y/o Descuentos"));





        if (data.getClientCode().startsWith(vtrInetSchool)) {
            data.setServiceName("интернет школы при Посольстве");
            data.setAddress("Канделария Гоенечеа 4435 кв.4, Витакура");
        } else if (data.getClientCode().startsWith(vtrInetKo)) {
            data.setServiceName("интернет консульского отдела");
            data.setAddress("Проспект Америко Веспусио Норте 2127 кв. 101, Витакура ");
        } else if (data.getClientCode().startsWith(vtrTvEmbassy)) {
            data.setServiceName("телевидение Посольства");

        } else if (data.getClientCode().startsWith(vtrTvResidence)) {
            data.setServiceName("телевидение на резиденции Посла");
            data.setAddress("Виа Роха 4592, Витакура");
        } else {
            data.setServiceName("service n/a");
        }

        data.setIssueDate(DateFormatUtils.localizeMonth(data.getIssueDate()));
        data.setDueDate(DateFormatUtils.localizeMonth(data.getDueDate()));
        data.setPeriod(parsePeriod(data));

        return data;
    }
}
