package ru.embajada.invoice_generator.dto;

import lombok.Data;

@Data
public abstract class PdfData  {
    String issueDate;
    String dueDate;
    String companyName;
    String serviceName;
    String period;
    String invoiceNumber;
    String totalAmount;
    String netto;
    String nds;
    String clientCode;
    String address;
}