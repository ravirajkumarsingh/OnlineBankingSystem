package com.cg.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cg.dao.AccountDao;
import com.cg.dao.BankDao;
import com.cg.dao.CustomerDao;
import com.cg.dao.TxnDao;
import com.cg.dto.AccountForm;
import com.cg.dto.CustomerForm;
import com.cg.dto.TransferFundForm;
import com.cg.entity.AccTransaction;
import com.cg.entity.Account;
import com.cg.entity.Customer;
import com.cg.exceptions.AccountException;
import com.cg.exceptions.BalanceException;
import com.cg.exceptions.CustomerException;
import com.cg.exceptions.TransactionException;
import com.cg.util.AccountConstants;


/**
 * @author raviraj
 *
 */
@Service("accountser")
@Transactional
public class AccountServiceImpl implements AccountService{

	@Autowired
	private AccountDao accdao;
	@Autowired
	private CustomerDao custdao;
	@Autowired
	private TxnDao txdao;
	@Autowired
	private BankDao accountDao;
	
	
	@Override
	public List<Account> viewAccounts(String custId) throws CustomerException {
		List<Account> accList = accountDao.viewAccounts(custId);
		if(accList.isEmpty())
			throw new CustomerException(AccountConstants.INVALID_CUSTOMER);
		accList.sort((acc1, acc2)-> acc1.getAccountName().compareTo(acc2.getAccountName()));
		return accList;
	}

	@Override
	public List<Account> viewAccount(String accId) throws AccountException {
		List<Account> accList = accountDao.viewAccount(accId);
		if(accList.isEmpty())
			throw new AccountException(AccountConstants.INVALID_ACCOUNT);
		accList.sort((acc1, acc2)-> acc1.getAccountName().compareTo(acc2.getAccountName()));
		return accList;
	}

	@Override
	@Transactional
	public String transferFund(TransferFundForm form) throws AccountException, BalanceException, TransactionException {
	Optional<Account> optfromAccount =accountDao.findById(form.getFromAccountId());
	if(!optfromAccount.isPresent())
		throw new AccountException(AccountConstants.INVALID_ACCOUNT + form.getFromAccountId());
	
	Optional<Account> opttoAccount =accountDao.findById(form.getToAccountId());
	if(!opttoAccount.isPresent())
		throw new AccountException(AccountConstants.INVALID_ACCOUNT + form.getToAccountId());
	
	Account fromAcc = optfromAccount.get();
	Account toAcc = opttoAccount.get();
	if(fromAcc.getAccountStatus().equals(AccountConstants.IN_ACTIVE)) 
		throw new TransactionException(form.getFromAccountId() + AccountConstants.IN_ACTIVE_MSG);
	if(toAcc.getAccountStatus().equals(AccountConstants.IN_ACTIVE)) 
		throw new TransactionException(form.getToAccountId() + AccountConstants.IN_ACTIVE_MSG);
	if(fromAcc.getAccountBalance() < form.getAmt())
		throw new BalanceException(AccountConstants.INSUFFICIENT_BALANCE);
	fromAcc.setAccountBalance(fromAcc.getAccountBalance() - form.getAmt());
	toAcc.setAccountBalance(toAcc.getAccountBalance() + form.getAmt());
	accountDao.save(fromAcc);
	accountDao.save(toAcc);
	AccTransaction txFrom = new AccTransaction();
	txFrom.setTransAmount(form.getAmt());
	txFrom.setTransDate(LocalDate.now());
	txFrom.setTransType(AccountConstants.DEBIT);
	txFrom.setTransDescription(AccountConstants.TRANSFER_FROM_DESC + form.getFromAccountId());
	txFrom.setAccount(fromAcc);
	txdao.save(txFrom);
	
	AccTransaction txTo = new AccTransaction();
	txTo.setTransAmount(form.getAmt());
	txTo.setTransDate(LocalDate.now());
	txTo.setTransType(AccountConstants.CREDIT);
	txTo.setTransDescription(AccountConstants.TRANSFER_TO_DESC + form.getToAccountId());
	txTo.setAccount(toAcc);
	txdao.save(txTo);
	
		return AccountConstants.TRANSFERED;
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public String addAccount(AccountForm accountForm) throws CustomerException {
		Account account = new Account();
		Optional<Customer> optcustomer=custdao.findById(accountForm.getCustomerID());
		if (!optcustomer.isPresent())
			throw new CustomerException(AccountConstants.CUSTOMER_NOT_FOUND);
		LocalDateTime now = LocalDateTime.now();
		String id = accountForm.getAccountName()+accountForm.getCustomerID();
		account.setAccountId(id);
		account.setAccountBalance(accountForm.getBalance());
		account.setAccountName(accountForm.getAccountName());
		account.setAccountStatus(AccountConstants.ACTIVE);
		account.setLastUpdated(LocalDate.now());
		account.setCustomer(optcustomer.get());
		Account acc=accdao.save(account);
		accdao.flush();
		AccTransaction tx=new AccTransaction();
		tx.setTransAmount(accountForm.getBalance());
		tx.setTransDate(LocalDate.now());
		tx.setTransType(AccountConstants.CREDIT);
		tx.setTransDescription(AccountConstants.OPEN_DESC);
		tx.setAccount(account);
		txdao.save(tx);
		return acc.getAccountId();
			}

	@Override
	public String addCustomer(CustomerForm custForm)  {
		Customer cust = new Customer();
		LocalDateTime now = LocalDateTime.now();
		String custID = AccountConstants.EMPTY+ now.getYear()+ now.getMonthValue()+now.getDayOfMonth()+now.getHour()+now.getMinute()+now.getSecond();
		cust.setCustomerId(custID);
		cust.setCustomerName(custForm.getCustomerName());
		cust.setCustomerDob(custForm.getCustomerDob());
		cust.setCustomerAadhar(custForm.getCustomerAadhar());
		cust.setCustomerAddress(custForm.getCustomerAddress());
		cust.setCustomerContact(custForm.getCustomerContact());
		cust.setCustomerPan(custForm.getCustomerPan());
		cust.setPassword(custForm.getPassword());
		cust.setCustomerGender(custForm.getCustomerGender());
		cust.setRole(AccountConstants.ROLE_USER);
		custdao.save(cust);
		return custID;
	}

	
}














