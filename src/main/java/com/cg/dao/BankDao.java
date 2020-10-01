package com.cg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cg.entity.Account;


/**
 * @author raviraj
 *
 */
@Repository
public interface BankDao extends JpaRepository<Account,String>{


	@Query("from Account a where a.customer.customerId=:custId")
	public List<Account> viewAccounts(@Param("custId")String customerId);
	//Fetching the required account details from the database using sql queries
		@Query("from Account a where a.accountId=:accId")
		public List<Account> viewAccount(@Param("accId") String accountId);
		
		
			
	
}
