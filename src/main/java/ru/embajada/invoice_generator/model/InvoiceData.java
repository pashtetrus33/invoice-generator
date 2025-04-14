package ru.embajada.invoice_generator.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InvoiceData {
    private LocalDate issueDate;
    private LocalDate dueDate;
    private String additionalFees;
    private LocalDate lastPaymentDate;
    private String lastPaymentAmount;
    private LocalDate serviceCutoffDate;
    private LocalDate servicePeriodStart;
    private LocalDate servicePeriodEnd;
    private String invoiceNumber;
    private String clientName;
    private String clientCountry;
    private String clientNumber;
    private String baseAmount;
    private String totalAmount;
}