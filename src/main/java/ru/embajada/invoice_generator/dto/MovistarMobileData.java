package ru.embajada.invoice_generator.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MovistarMobileData extends PdfData {
    private String addService;
    private String saldoDate;
    private String serviceAmount;
    private String dolg;
    private String valorFijo;
    private String desctoFijo;
    private String totalFijo;
    private String valorLarga;
    private String desctoLarga;
    private String totalLarga;
    private String valorTotal;
    private String desctoTotal;
    private String totalTotal;
    private String indispServicio;
    private String totalIndispServicio;
}