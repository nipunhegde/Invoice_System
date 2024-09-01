package com.invoice.Invoice_Management.service.serviceImpl;

import com.invoice.Invoice_Management.config.InvoiceMapper;
import com.invoice.Invoice_Management.dto.InvoiceDTO;
import com.invoice.Invoice_Management.dto.PaymentDTO;
import com.invoice.Invoice_Management.dto.ProcessOverdueDTO;
import com.invoice.Invoice_Management.exception.IdNotFoundException;
import com.invoice.Invoice_Management.model.Invoice;
import com.invoice.Invoice_Management.model.InvoiceStatus;
import com.invoice.Invoice_Management.repository.InvoiceRepository;
import com.invoice.Invoice_Management.service.serviceImpl.invoiceeserviceimpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class InvoiceServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private InvoiceMapper invoiceMapper;
    @InjectMocks
    private invoiceeserviceimpl invoiceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateInvoice() {
        InvoiceDTO invoiceDTO = new InvoiceDTO(1L, new BigDecimal("100.00"), BigDecimal.ZERO, LocalDate.now(), InvoiceStatus.PENDING);
        Invoice invoice = new Invoice(1L, new BigDecimal("100.00"), BigDecimal.ZERO, LocalDate.now(), InvoiceStatus.PENDING);

        when(invoiceMapper.toEntity(invoiceDTO)).thenReturn(invoice);
        when(invoiceRepository.save(invoice)).thenReturn(invoice);
        when(invoiceMapper.toDTO(invoice)).thenReturn(invoiceDTO);

        long result = invoiceService.createInvoice(invoiceDTO);

        assertNotNull(result);
        assertEquals(1L,result );
        verify(invoiceRepository).save(invoice);}

    @Test
    void testGetAllInvoices() {
        Invoice invoice1 = new Invoice();
        invoice1.setId(1L);
        invoice1.setAmount(BigDecimal.valueOf(100));
        invoice1.setPaidAmount(BigDecimal.ZERO);
        invoice1.setDueDate(LocalDate.now().plusDays(30));
        invoice1.setStatus(InvoiceStatus.PENDING);

        Invoice invoice2 = new Invoice();
        invoice2.setId(2L);
        invoice2.setAmount(BigDecimal.valueOf(200));
        invoice2.setPaidAmount(BigDecimal.ZERO);
        invoice2.setDueDate(LocalDate.now().plusDays(30));
        invoice2.setStatus(InvoiceStatus.PENDING);

        when(invoiceRepository.findAll()).thenReturn(Arrays.asList(invoice1, invoice2));

        List<InvoiceDTO> invoiceDTOs = invoiceService.getAllInvoices();
        assertEquals(2, invoiceDTOs.size());
    }

    @Test
    void testPayInvoice() {
        Invoice invoice = new Invoice(1L, new BigDecimal("100.00"), BigDecimal.ZERO, LocalDate.now(), InvoiceStatus.PENDING);
        InvoiceDTO invoiceDTO = new InvoiceDTO(1L, new BigDecimal("100.00"), BigDecimal.ZERO, LocalDate.now(), InvoiceStatus.PENDING);
        PaymentDTO paymentDTO = new PaymentDTO(new BigDecimal("100.00"));

        when(invoiceRepository.findById(1L)).thenReturn(java.util.Optional.of(invoice));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);
        when(invoiceMapper.toDTO(invoice)).thenReturn(invoiceDTO);

        InvoiceDTO result = invoiceService.payInvoice(1L, paymentDTO);

        assertNotNull(result);
        assertEquals(InvoiceStatus.PENDING, result.getStatus());
        verify(invoiceRepository).save(invoice);
    }

    @Test
    void testProcessOverdueInvoices() {
        ProcessOverdueDTO overdueDTO = new ProcessOverdueDTO();
        overdueDTO.setLateFee(new BigDecimal("10.00"));
        overdueDTO.setOverdueDays(30);

        Invoice overdueInvoice = new Invoice();
        overdueInvoice.setId(1L);
        overdueInvoice.setAmount(new BigDecimal("100.00"));
        overdueInvoice.setPaidAmount(BigDecimal.ZERO);
        overdueInvoice.setDueDate(LocalDate.now().minusDays(31)); // Overdue
        overdueInvoice.setStatus(InvoiceStatus.PENDING);

        // Stubbing the repository methods
        when(invoiceRepository.findByStatusAndDueDateBefore(
                any(InvoiceStatus.class), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(overdueInvoice));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(overdueInvoice);

        // Act
        invoiceService.processOverdueInvoices(overdueDTO);

        // Assert
        verify(invoiceRepository, times(1)).save(overdueInvoice);
    }

    @Test
    void testExceptionHandling() {
        Long invoiceId = 1L;

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> {
            invoiceService.payInvoice(invoiceId, new PaymentDTO(BigDecimal.valueOf(100)));
        });
    }
}