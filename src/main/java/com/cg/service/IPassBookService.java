package com.cg.service;

import java.time.LocalDate;
import java.util.List;

import com.cg.entity.AccTransaction;
import com.cg.exceptions.AccountException;
import com.cg.exceptions.TransactionException;

/**
 * @author raviraj
 *
 */
public interface IPassBookService {
	
	public List<AccTransaction> getTransactions(LocalDate startDt, LocalDate endDt, String accountId) throws TransactionException, AccountException;
	public List<AccTransaction> getTransactionById(String accountId) throws TransactionException, AccountException;
}
