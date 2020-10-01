package com.cg.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cg.dto.AccountMessage;
import com.cg.dto.CustomerForm;
import com.cg.dto.LoanRequestDto;
import com.cg.dto.LoanSuccessMessage;
import com.cg.dto.SuccessMessage;
import com.cg.dto.TransferFundForm;
import com.cg.entity.Account;
import com.cg.entity.LoanRequest;
import com.cg.exceptions.AccountException;
import com.cg.exceptions.BalanceException;
import com.cg.exceptions.CustomerException;
import com.cg.exceptions.LoanProcessingException;
import com.cg.exceptions.NoRequestsFoundException;
import com.cg.exceptions.TransactionException;
import com.cg.service.ILoanService;
import com.cg.util.AccountConstants;
/**
 * @author mihir
 *
 */
@RestController
@CrossOrigin(origins={"http://localhost:4200"})
public class ViewAndProcessLoanRestController {
	
	Logger logger = LoggerFactory.getLogger(ViewAndProcessLoanRestController.class);
	
	@Autowired
	private ILoanService loanService;
	
	@Autowired
	ILoanService service;

	// this method is used to request a loan, this takes the loan request object as
	// the input, checks whether the object is  null and then passes to service layer

	@PostMapping("loanrequest")
	public LoanSuccessMessage createLoanRequest(@RequestBody LoanRequestDto loanreqdto) throws NoRequestsFoundException {
		
		String loanRequestId=service.createLoanRequest(loanreqdto);
			
		    return new LoanSuccessMessage(AccountConstants.LOAN_REQ_CREATED+ AccountConstants.GENERATED_LOAN_REQ+ loanRequestId);
	  
	}
	
	//viewing all the pending loan requests
	@GetMapping(AccountConstants.VIEW_ALL_PENDING_REQUESTS) //get method for fetching the details
	public List<LoanRequest> getLoanRequests() throws NoRequestsFoundException{
		List<LoanRequest> loanRequestList= loanService.viewAllLoanRequests();
		return loanRequestList;
	}
	
	
	//to process loan requests raised by customer based on the loan request ID
	@GetMapping(AccountConstants.PROCESS_REQUESTS_BY_ID)  //get method for fetching the details
	public LoanSuccessMessage processLoanRequest(@PathVariable("reqID") String requestId) throws NoRequestsFoundException, LoanProcessingException {
		
		String res=loanService.processLoanRequest(requestId);
		return new LoanSuccessMessage(res); //displays the success message
	}
	
	
	//to view the approved loans
	@GetMapping(AccountConstants.APPROVED_LOAN_REQUESTS_URL) //get method for fetching details
	public List<LoanRequest> getAcceptedLoanRequests() throws NoRequestsFoundException{
       List<LoanRequest> loanRequestAcceptedList= loanService.viewAcceptedLoans();
		return loanRequestAcceptedList;
	}
	
	
	//to view rejected loans
	@GetMapping(AccountConstants.REJECTED_LOAN_REQUESTS_URL)  //get method for fetching details
	public List<LoanRequest> getRejectedLoanRequests() throws NoRequestsFoundException{
		List<LoanRequest> loanRequestRejectedList= loanService.viewRejectedLoans();
		return loanRequestRejectedList;
	}
	
	
	//to view account details
	@GetMapping(AccountConstants.UPDATED_ACCOUNT_LIST_URL) //get method for fetching details
	public List<Account> getUpdatedAccountList(){
		List<Account> accList=loanService.getUpdatedAccountList();
		return accList;
	}
	
}
