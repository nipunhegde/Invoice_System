package com.invoice.Invoice_Management.config;

import com.invoice.Invoice_Management.dto.InvoiceDTO;
import com.invoice.Invoice_Management.model.Invoice;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InvoiceMapper {

    @Autowired
    private ModelMapper modelMapper;

    public InvoiceDTO toDTO(Invoice invoice) {
        return modelMapper.map(invoice, InvoiceDTO.class);
    }

    public Invoice toEntity(InvoiceDTO invoiceDTO) {
        return modelMapper.map(invoiceDTO, Invoice.class);
    }
}