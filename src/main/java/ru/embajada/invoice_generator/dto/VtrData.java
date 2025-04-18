package ru.embajada.invoice_generator.dto;

import lombok.Data;

@Data
public class MovistarData {
    private String companyName;
    private String invoiceNumber;
    private String issueDate;
    private String dueDate;
    private String period;
    private String totalAmount;
    private String netto;
    private String nds;
    private String addService;
    private String saldoDate;
    private String serviceAmount;
    private String dolg;
    private String clientCode;
    private String serviceName;
}