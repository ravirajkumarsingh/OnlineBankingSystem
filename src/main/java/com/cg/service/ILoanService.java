package com.cg.service;

import java.util.List;

import com.cg.dto.LoanRequestDto;
import com.cg.entity.Account;
import com.cg.entity.LoanRequest;
import com.cg.exceptions.LoanProcessingException;
import com.cg.exceptions.NoRequestsFoundException;

/**
 * @author mihir
 *
 */
public interface ILoanService {
 public List<LoanRequest> viewAllLoanRequests() throws NoRequestsFoundException;
 public String processLoanRequest(String loanRequestId) throws NoRequestsFoundException,LoanProcessingException;
 public List<LoanRequest> viewAcceptedLoans() throws NoRequestsFoundException;
 public List<LoanRequest> viewRejectedLoans() throws NoRequestsFoundException;
 public List<Account> getUpdatedAccountList();
 public String createLoanRequest(LoanRequestDto loanreqdto);

}
