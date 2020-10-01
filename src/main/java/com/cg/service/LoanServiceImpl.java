package com.cg.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.dao.AccountDao;
import com.cg.dao.CustomerDao;
import com.cg.dao.LoanRequestDao;
import com.cg.dto.LoanRequestDto;
import com.cg.entity.Account;
import com.cg.entity.Customer;
import com.cg.entity.LoanRequest;
import com.cg.exceptions.LoanProcessingException;
import com.cg.exceptions.NoRequestsFoundException;
import com.cg.util.AccountConstants;
import com.cg.web.ViewAndProcessLoanRestController;

@Service
public class LoanServiceImpl implements ILoanService {
	Logger logger = LoggerFactory.getLogger(ViewAndProcessLoanRestController.class);

	@Autowired
	LoanRequestDao lrdao;

	@Autowired
	CustomerDao custdao;
	@Autowired
	private LoanRequestDao loanRequestDao;

	@Autowired
	private AccountDao accountDao;

	/**
	 * @author mihir
	 *
	 */
	@Override
	@Transactional
	public String processLoanRequest(String loanRequestId) throws NoRequestsFoundException, LoanProcessingException {
		try {
			Optional<LoanRequest> optLoan = loanRequestDao.findById(loanRequestId);
			if (!optLoan.isPresent()) {
				throw new NoRequestsFoundException(AccountConstants.NO_LOAN_REQUESTS);
			}
			LoanRequest req = optLoan.get();
			String custId = req.getCustomer().getCustomerId();
			int count = loanRequestDao.getAvailedLoans(custId, AccountConstants.LOAN_APPROVED);

			if (count > 0) {
				req.setReqStatus(AccountConstants.LOAN_REJECTED);
				loanRequestDao.save(req);
				throw new LoanProcessingException(AccountConstants.LOAN_UNDERGOING);
			}
			double ci = calculateCompoundInt(req.getLoanTenure(), req.getLoanAmount());
			double emi = calculateEmi(ci, req.getLoanTenure());
			logger.info(emi + AccountConstants.EMI);
			double fiftyPercentOfAnnualIncome = req.getAnnualIncome() * 0.5 / 12;
			logger.info(fiftyPercentOfAnnualIncome + AccountConstants.FIFTY_PERCENT_OF_SALARY);
			if (emi > fiftyPercentOfAnnualIncome) {
				req.setReqStatus(AccountConstants.LOAN_REJECTED);
				loanRequestDao.save(req);
				throw new LoanProcessingException(AccountConstants.NOT_ENOUGH_INCOME);
			}
			req.setReqStatus(AccountConstants.LOAN_APPROVED);
			loanRequestDao.save(req);
			Account acc = new Account();
			int loanCount = loanRequestDao.countLoansOfCustomer(custId) + 1;
			acc.setAccountId(AccountConstants.LOAN + custId + "" + loanCount);
			acc.setAccountName(AccountConstants.PERSONAL_LOAN);
			acc.setAccountStatus(AccountConstants.ACTIVE);
			acc.setCustomer(req.getCustomer());
			acc.setAccountBalance(req.getLoanAmount());
			acc.setLastUpdated(LocalDate.now());
			accountDao.save(acc);
			return AccountConstants.APPROVED;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public double calculateCompoundInt(int years, double amt) {
		return amt * Math.pow((1 + 0.1), years);
	}

	public double calculateEmi(double amt, int years) {
		return amt / (years * 12);
	}

	// this method displays the approved/accepted loans
	@Override
	public List<LoanRequest> viewAcceptedLoans() throws NoRequestsFoundException {
		List<LoanRequest> loanRequestList = loanRequestDao.getAcceptedRequests(AccountConstants.APPROVED_REQUESTS);
		if (loanRequestList.isEmpty())
			throw new NoRequestsFoundException(AccountConstants.NO_ACCEPTED_LOAN_REQUESTS);
		loanRequestList.sort((loanRequest1, loanRequest2) -> loanRequest1.getDateOfRequest()
				.compareTo(loanRequest2.getDateOfRequest()));
		return loanRequestList;

	}

	// this method is used for displaying the rejected loans
	@Override
	public List<LoanRequest> viewRejectedLoans() throws NoRequestsFoundException {
		List<LoanRequest> loanRequestList = loanRequestDao.getRejectedRequests(AccountConstants.REJECTED_REQUESTS);
		if (loanRequestList.isEmpty())
			throw new NoRequestsFoundException(AccountConstants.NO_REJECTED_LOAN_REQUESTS);
		loanRequestList.sort((loanRequest1, loanRequest2) -> loanRequest1.getDateOfRequest()
				.compareTo(loanRequest2.getDateOfRequest()));
		return loanRequestList;
	}

	// This method displays the account details after a loan account gets added
	// after the approval of the loan of a customer
	@Override
	public List<Account> getUpdatedAccountList() {

		List<Account> account = accountDao.findAll();
		return account;
	}

	@Override
	public List<LoanRequest> viewAllLoanRequests() throws NoRequestsFoundException {
			List<LoanRequest> loanRequestList = loanRequestDao.getPendingRequests(AccountConstants.PENDING_REQUESTS);
		
		if (loanRequestList.isEmpty())
			throw new NoRequestsFoundException(AccountConstants.NO_LOAN_REQUESTS);
		loanRequestList.sort((loanRequest1, loanRequest2) -> loanRequest1.getDateOfRequest()
				.compareTo(loanRequest2.getDateOfRequest()));
		return loanRequestList;
		
	}

	/**
	 * @author vaibhav
	 *
	 */
	@Override
	@Transactional(readOnly = false)
	public String createLoanRequest(LoanRequestDto loanreqdto) {
		LoanRequest loanrequest = new LoanRequest();
		LocalDateTime now = LocalDateTime.now();
		String loanID = AccountConstants.LOAN_ID + now.getYear() + now.getMonthValue() + now.getDayOfMonth()
				+ now.getHour() + now.getMinute() + now.getSecond();
		loanrequest.setLoanRequestId(loanID);
		loanrequest.setLoanAmount(loanreqdto.getLoanAmount());
		loanrequest.setLoanType(loanreqdto.getLoanType());
		loanrequest.setLoanTenure(loanreqdto.getLoanTenure());
		loanrequest.setReqStatus(loanreqdto.getReqStatus());
		loanrequest.setDateOfRequest(loanreqdto.getDateOfRequest());
		loanrequest.setAnnualIncome(loanreqdto.getAnnualIncome());
		Customer cust = custdao.getCustomer(loanreqdto.getCustomerId());
		loanrequest.setCustomer(cust);
		LoanRequest persistedLr = lrdao.save(loanrequest);
		return loanID;
	}

}
