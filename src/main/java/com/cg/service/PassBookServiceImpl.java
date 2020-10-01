package com.cg.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cg.dao.AccountDao;
import com.cg.entity.AccTransaction;
import com.cg.entity.Account;
import com.cg.exceptions.AccountException;
import com.cg.exceptions.TransactionException;
import com.cg.util.AccountConstants;

/**
 * @author raviraj
 *
 */
@Service
public class PassBookServiceImpl  implements IPassBookService {
	@Autowired
	private AccountDao accDao;
	
	@Override
	public List<AccTransaction> getTransactions(LocalDate startDt, LocalDate endDt, String accountId) throws TransactionException, AccountException {
	
		if(validateDate(startDt, endDt)) {
			Optional<Account> optaccount = accDao.findById(accountId);
			if(!optaccount.isPresent())
				throw new AccountException(AccountConstants.IN_ACTIVE_ACCOUNT);
				
			List<AccTransaction>  txlist = accDao.getTransactions(startDt, endDt,accountId);
			if(txlist.isEmpty())
				throw new TransactionException(AccountConstants.TXN_NOT_AVAILABLE);
			txlist.sort((txn1, txn2)->txn1.getTransDate().compareTo(txn2.getTransDate()));
			
			return txlist;
		}
		return null;
	}
	
	public boolean validateDate(LocalDate startDt, LocalDate endDt) throws TransactionException {
		
		if(startDt.isAfter(LocalDate.now()))
			throw new TransactionException(AccountConstants.START_AFTER_DATE);
		if(endDt.isBefore(startDt))
			throw new TransactionException(AccountConstants.BEFORE_START_DATE);
		return true;
	}

	@Override
	public List<AccTransaction> getTransactionById(String accountId) throws TransactionException, AccountException {
	
		if(validateDate(accountId)) {
			Optional<Account> optaccount = accDao.findById(accountId);
			if(!optaccount.isPresent())
				throw new AccountException(AccountConstants.NO_ACCOUNT_FOUND);
		
			List<AccTransaction>  txlist = accDao.getTransactionById(accountId);
			if(txlist.isEmpty())
				throw new TransactionException(AccountConstants.TXN_NOT_AVAILABLE);
			txlist.sort((txn1, txn2)->txn1.getTransDate().compareTo(txn2.getTransDate()));
			
			return txlist;
		}
		return null;
	}
	


	private boolean validateDate(String accountId) throws TransactionException {
		if(accountId.isEmpty())
			throw new TransactionException(AccountConstants.START_AFTER_DATE);
		
		return true;
	}

	
}
