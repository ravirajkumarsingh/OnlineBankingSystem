package com.cg.service;

import java.util.List;

import com.cg.dto.AccountForm;
import com.cg.dto.CustomerForm;
import com.cg.dto.TransferFundForm;
import com.cg.entity.Account;
import com.cg.exceptions.AccountException;
import com.cg.exceptions.BalanceException;
import com.cg.exceptions.CustomerException;
import com.cg.exceptions.TransactionException;


/**
 * @author raviraj
 *
 */
public interface AccountService {
	

	
	public String transferFund(TransferFundForm form) throws AccountException, BalanceException, TransactionException;
	public String addAccount(AccountForm account)throws CustomerException;
	public String addCustomer(CustomerForm custForm);
	public List<Account> viewAccounts(String custId)throws CustomerException;
	public List<Account> viewAccount(String accId)throws AccountException;
}
