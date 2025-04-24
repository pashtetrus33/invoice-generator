package ru.embajada.invoice_generator.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MovistarData extends PdfData {
    private String addService;
    private String saldoDate;
    private String serviceAmount;
    private String dolg;
}