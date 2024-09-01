package com.invoice.Invoice_Management.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data@AllArgsConstructor@NoArgsConstructor
public class ProcessOverdueDTO {
    @NotNull(message = "Late fee cannot be null")
    @DecimalMin(value = "0.01", inclusive = true, message = "Late fee must be positive")
    private BigDecimal lateFee;

    @NotNull(message = "Overdue days cannot be null")
    @Positive(message = "Overdue days must be positive")
    private int overdueDays;

}
