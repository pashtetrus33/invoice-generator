package ru.embajada.invoice_generator.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VtrData extends PdfData {
    private String dueDate;
    private String addService;
    private String saldoDate;
    private String serviceAmount;
    private String dolg;
    private String lastPaymentDate;
    private String lastPaymentAmount;
    private String cortarDate;
    private String totalServicios;
    private String dopTv;
    private String otroCargos;
    private String totalOtroCargos;
    private String totalSaldo;
    private String montoNeto;
    private String montoExento;
    private String iva;
    private String servicios;
}