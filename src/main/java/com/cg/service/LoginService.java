package com.cg.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.dao.CustomerDao;
import com.cg.entity.Customer;
import com.cg.util.AccountConstants;

@Service
/**
 * 
 * service for login service class
 *
 */
public class LoginService implements LoginServiceI {

	@Autowired
	CustomerDao customerDao;

	@Override
	/**
	 * login method
	 */
	public String login(String customer_ID, String password) {
		Optional<Customer> findById = customerDao.findById(customer_ID);
		if (findById.isPresent()) {
			Customer customer = findById.get();
			if (customer.getPassword().equals(password)) {
				if (customer.getRole().equals("admin")) {
					return AccountConstants.ADMIN;
				} else {
					return AccountConstants.ROLE_USER;
				}
			}
			return AccountConstants.PASS_NOT_VALID;
		}
		return AccountConstants.USER_NOT_VALID;
	}
}
