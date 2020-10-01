package com.cg.web;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cg.exceptions.LoanProcessingException;
import com.cg.exceptions.NoRequestsFoundException;
@RestControllerAdvice
@CrossOrigin(origins={"https://localhost:4200"})
public class BankLoanRequestAdvice {

//Exception to be raised if there are no pending loan requests or if the loan request ID is invalid
@ExceptionHandler(NoRequestsFoundException.class)
@ResponseStatus(code=HttpStatus.NOT_FOUND)
public Error handleNoRequestsFoundException(NoRequestsFoundException ex) {
	return new Error(HttpStatus.NOT_FOUND.toString());
	
}
//Exception to be raised when the customer has already availed loans or the half percent of monthly salary of customer is less than EMI 
@ExceptionHandler(LoanProcessingException.class)
@ResponseStatus(code=HttpStatus.BAD_REQUEST)
public Error handleLoanProcessingException(LoanProcessingException ex) {
	return new Error(HttpStatus.BAD_REQUEST.toString());
	
}

}
