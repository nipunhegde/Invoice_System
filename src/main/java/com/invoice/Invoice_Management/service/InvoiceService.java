package com.invoice.Invoice_Management.service;

import com.invoice.Invoice_Management.dto.InvoiceDTO;
import com.invoice.Invoice_Management.dto.PaymentDTO;
import com.invoice.Invoice_Management.dto.ProcessOverdueDTO;

import java.util.List;

public interface InvoiceService {
    Long createInvoice(InvoiceDTO invoiceDTO);

    List<InvoiceDTO> getAllInvoices();

    InvoiceDTO payInvoice(Long id, PaymentDTO paymentDTO);

    void processOverdueInvoices(ProcessOverdueDTO overdueDTO);
}
