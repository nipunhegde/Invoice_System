package com.invoice.Invoice_Management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class IdNotFoundException extends RuntimeException{

    public IdNotFoundException(Long fieldValue){
        super(String.format("Given Id is not present in the system :%s ",fieldValue));
    }
}
