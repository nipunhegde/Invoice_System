package com.invoice.Invoice_Management.repository;

import com.invoice.Invoice_Management.model.Invoice;
import com.invoice.Invoice_Management.model.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByStatusAndDueDateBefore(InvoiceStatus status, LocalDate date);

}
