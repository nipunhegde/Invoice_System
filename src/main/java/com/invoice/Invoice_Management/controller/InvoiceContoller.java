package com.invoice.Invoice_Management.controller;

import com.invoice.Invoice_Management.dto.InvoiceDTO;
import com.invoice.Invoice_Management.dto.PaymentDTO;
import com.invoice.Invoice_Management.dto.ProcessOverdueDTO;
import com.invoice.Invoice_Management.service.InvoiceService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoices")
public class InvoiceContoller {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceContoller.class);


    @Autowired
    private InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<Long> createInvoice(@Valid @RequestBody InvoiceDTO invoiceDTO) {
        logger.info("Creating invoice with amount: {}", invoiceDTO.getAmount());
        // Call the service to create the invoice and get its ID
        Long invoiceId = invoiceService.createInvoice(invoiceDTO);
        // Return the ID in the response body with HTTP status 201 Created
        return new ResponseEntity<>(invoiceId, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InvoiceDTO>> getAllInvoices() {
        logger.info("Getting all the invoices");
        List<InvoiceDTO> invoices = invoiceService.getAllInvoices();
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @PostMapping("/{id}/payments")
    public ResponseEntity<InvoiceDTO> payInvoice(@PathVariable Long id, @RequestBody PaymentDTO paymentDTO) {
        logger.info("Paying invoice with ID: {} and amount: {}", id, paymentDTO.getAmount());
        // Call service method to process payment
        InvoiceDTO paidInvoice = invoiceService.payInvoice(id, paymentDTO);
        return new ResponseEntity<>(paidInvoice, HttpStatus.OK);
    }

    @PostMapping("/process-overdue")
    public ResponseEntity<Void> processOverdueInvoices(@RequestBody ProcessOverdueDTO overdueDTO) {
        logger.info("Processing overdue invoices");
        invoiceService.processOverdueInvoices(overdueDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}