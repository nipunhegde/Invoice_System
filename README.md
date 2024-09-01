**Overview**
The Invoice Management System is designed to handle the creation, processing, and management of invoices within an organization. This system allows users to create invoices, make payments, and process overdue invoices. The application is built using Java, Spring Boot, and various supporting technologies.

**Features**
Create Invoices: Allows users to create new invoices with specified details.
Retrieve Invoices: Provides the ability to fetch and view all invoices.
Pay Invoices: Processes payments for existing invoices and updates their status.
Process Overdue Invoices: Identifies overdue invoices, applies late fees, and creates new invoices if necessary.
Exception Handling: Handles errors and provides informative responses to the users.

**Assumptions**
Data Validation:
Invoice amounts, paid amounts, and late fees must be positive values.
Due dates must be valid dates and should not be set in the future.
Payment amounts must be positive and not exceed the invoice amount.

Invoice Statuses:
Invoices can have one of three statuses: PENDING, PAID, or VOID.
Once an invoice is marked as PAID, no further payments can be processed.
VOID status indicates that the invoice is invalid and should not be processed further.

Late Fee Calculation:
The late fee is applied based on the number of overdue days.
The system adds late fees to the invoice amount and creates a new invoice if the overdue amount exceeds the original invoice amount.

User Roles:
The system does not differentiate between user roles; all users have the same level of access to invoice management functionalities.

Error Handling:
All exceptions are handled globally and return standardized error responses.

**Added Functionality**

Detailed Error Responses:
Custom error messages are returned in a structured format for better debugging and user experience.

Overdue Invoice Processing:
Invoices that are overdue are processed to apply late fees.
New invoices are created to reflect late fees if necessary.

Validation Enhancements:
Added checks to ensure invoice amounts, payment amounts, and late fees are positive and valid.
Ensured that due dates and payment dates are handled correctly.

Model Mapping:
Utilized ModelMapper for converting between entity and DTO objects, ensuring clean and maintainable code.


The application will be accessible at http://localhost:8080.

**API Endpoints**

Create Invoice

Endpoint: POST /invoices
Request Body: InvoiceDTO
Response: InvoiceDTO with created invoice ID
Retrieve Invoices

Endpoint: GET /invoices
Response: List of InvoiceDTOs
Pay Invoice

Endpoint: POST /invoices/{id}/payments
Request Body: PaymentDTO
Response: Updated InvoiceDTO
Process Overdue Invoices

Endpoint: POST /invoices/process-overdue
Request Body: ProcessOverdueDTO
Response: Status of overdue invoice processing

**Components**

DTOs (Data Transfer Objects)
InvoiceDTO: Represents the invoice data used for API communication.
PaymentDTO: Represents the payment data used when paying invoices.
ProcessOverdueDTO: Represents the data required to process overdue invoices.
ErrorResponseDto: Represents the structure of error responses sent from the server.
Entities

Invoice: The main entity representing an invoice in the system. It includes fields like id, amount, paidAmount, dueDate, and status.
Repositories

InvoiceRepository: A JPA repository for performing CRUD operations on the Invoice entity and finding invoices based on status and due date.
Services

InvoiceService: Defines the service interface for invoice operations.
invoiceeserviceimpl: Implements the InoviceService interface, including methods to create invoices, process payments, and handle overdue invoices.
Controllers

InvoiceController: Handles HTTP requests related to invoices, including creating invoices, retrieving all invoices, processing payments, and handling overdue invoices.
Exception Handling

GlobalExceptionHandler: Handles exceptions globally, including validation errors and custom exceptions like IdNotFoundException.
Mappings

InvoiceMapper: Maps between Invoice entities and InvoiceDTOs using ModelMapper.
Configuration

ModelMapperConfiguration: Configures the ModelMapper bean used for mapping DTOs to entities and vice versa.


Invoice Management System
Overview
The Invoice Management System is designed to handle the creation, payment, and processing of invoices. The system uses a microservices architecture with Java and Spring Boot. This document provides a detailed overview of the system's components, including data models, services, and controllers.

Components
DTOs (Data Transfer Objects)

InvoiceDTO: Represents the invoice data used for API communication.
PaymentDTO: Represents the payment data used when paying invoices.
ProcessOverdueDTO: Represents the data required to process overdue invoices.
ErrorResponseDto: Represents the structure of error responses sent from the server.
Entities

Invoice: The main entity representing an invoice in the system. It includes fields like id, amount, paidAmount, dueDate, and status.
Repositories

InvoiceRepository: A JPA repository for performing CRUD operations on the Invoice entity and finding invoices based on status and due date.
Services

INVOICEESERVICE: Defines the service interface for invoice operations.
invoiceeserviceimpl: Implements the INVOICEESERVICE interface, including methods to create invoices, process payments, and handle overdue invoices.
Controllers

InvoiceController: Handles HTTP requests related to invoices, including creating invoices, retrieving all invoices, processing payments, and handling overdue invoices.
Exception Handling

GlobalExceptionHandler: Handles exceptions globally, including validation errors and custom exceptions like IdNotFoundException.
Mappings

InvoiceMapper: Maps between Invoice entities and InvoiceDTOs using ModelMapper.
Configuration

ModelMapperConfiguration: Configures the ModelMapper bean used for mapping DTOs to entities and vice versa.


Key Features
Create Invoices: Allows creation of invoices with validation on the amount and due date.
Retrieve Invoices: Fetches all invoices with their current status and details.
Pay Invoices: Processes payments against invoices and updates their status.
Process Overdue Invoices: Handles overdue invoices by adjusting their status and creating new invoices if needed.
Exception Handling: Provides clear error messages and handles exceptions globally.

Testing
Ensure that the application is tested with various cases, including:
Creating invoices with valid and invalid data.
Paying invoices and verifying state changes.
Processing overdue invoices and ensuring proper handling of late fees and new invoice creation.
