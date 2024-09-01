package com.invoice.Invoice_Management.service.serviceImpl;

import com.invoice.Invoice_Management.dto.InvoiceDTO;
import com.invoice.Invoice_Management.config.InvoiceMapper;
import com.invoice.Invoice_Management.dto.PaymentDTO;
import com.invoice.Invoice_Management.dto.ProcessOverdueDTO;
import com.invoice.Invoice_Management.exception.IdNotFoundException;
import com.invoice.Invoice_Management.model.Invoice;
import com.invoice.Invoice_Management.model.InvoiceStatus;
import com.invoice.Invoice_Management.repository.InvoiceRepository;
import com.invoice.Invoice_Management.service.INVOICEESERVICE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class invoiceeserviceimpl implements INVOICEESERVICE {
    private static final Logger logger = LoggerFactory.getLogger(INVOICEESERVICE.class);

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceMapper invoiceMapper;

    /**
     * @param invoiceDTO
     * @return
     */
    @Override
    public Long createInvoice(InvoiceDTO invoiceDTO) {
        Invoice invoice = invoiceMapper.toEntity(invoiceDTO);
        if (invoice == null) {
            throw new RuntimeException("Invoice object is null");
        }
        invoice.setStatus(InvoiceStatus.PENDING);

        Invoice savedInvoice = invoiceRepository.save(invoice);
        return savedInvoice.getId();
    }

    /**
     * @return
     */
    @Override
    public List<InvoiceDTO> getAllInvoices() {
        return invoiceRepository.findAll()
                .stream()
                .map(invoiceMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * @param id
     * @param paymentDTO
     * @return
     */
    @Override
    @Transactional
    public InvoiceDTO payInvoice(Long id, PaymentDTO paymentDTO) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id));
        if (invoice == null) {
            throw new RuntimeException("Invoice not found");
        }
        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new RuntimeException("Invoice is already paid");
        }
        invoice.setPaidAmount(invoice.getPaidAmount().add(paymentDTO.getAmount()));
        if (invoice.getPaidAmount().compareTo(invoice.getAmount()) >= 0) {
            invoice.setStatus(InvoiceStatus.PAID);
        }
        Invoice updatedInvoice = invoiceRepository.save(invoice);
        if (updatedInvoice == null) {
            throw new RuntimeException("Failed to update invoice");
        }
        return invoiceMapper.toDTO(updatedInvoice);
    }

    /**
     * @param overdueDTO
     */
    @Override
    @Transactional
    public void processOverdueInvoices(ProcessOverdueDTO overdueDTO) {
        logger.info("Processing overdue invoices with late fee: {} and overdue days: {}", overdueDTO.getLateFee(), overdueDTO.getOverdueDays());

        List<Invoice> overdueInvoices = invoiceRepository.findByStatusAndDueDateBefore(
                InvoiceStatus.PENDING, LocalDate.now().minusDays(overdueDTO.getOverdueDays()));

        overdueInvoices.forEach(invoice -> {
            if (invoice.getPaidAmount().compareTo(invoice.getAmount()) >= 0) {
                // Fully paid or overpaid invoices should remain as PAID
                invoice.setStatus(InvoiceStatus.PAID);
            } else if (invoice.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) {
                // Partially paid invoices
                invoice.setStatus(InvoiceStatus.PAID);
                BigDecimal remainingAmount = invoice.getAmount().subtract(invoice.getPaidAmount());
                createInvoiceWithRemainingAmount(remainingAmount.add(overdueDTO.getLateFee()));
            } else {
                // Unpaid invoices
                invoice.setStatus(InvoiceStatus.VOID);
                createInvoiceWithRemainingAmount(invoice.getAmount().add(overdueDTO.getLateFee()));
            }
            invoiceRepository.save(invoice);
            logger.info("Overdue invoices processing completed.");
        });
    }

    private void createInvoiceWithRemainingAmount(BigDecimal amount) {
        Invoice newInvoice = new Invoice();
        newInvoice.setAmount(amount);
        newInvoice.setDueDate(LocalDate.now().plusDays(30)); // Set due date to 30 days from now
        newInvoice.setStatus(InvoiceStatus.PENDING);
        invoiceRepository.save(newInvoice);
        logger.info("Created new invoice with amount: {}", amount);
    }


}