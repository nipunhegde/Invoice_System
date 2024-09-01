package com.invoice.Invoice_Management.dto;

import com.invoice.Invoice_Management.model.InvoiceStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {
    private Long id;
    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be positive")
    private BigDecimal amount;

    @DecimalMin(value = "0.00", inclusive = true, message = "Paid amount must be non-negative")
    private BigDecimal paidAmount;

    @NotNull(message = "Due date cannot be null")
    @PastOrPresent(message = "Due date cannot be in the future")
    private LocalDate dueDate;

    //@NotNull(message = "Status cannot be null")
    private InvoiceStatus status;
}
