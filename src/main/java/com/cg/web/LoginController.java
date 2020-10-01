package com.cg.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cg.dto.LoginDto;
import com.cg.entity.Customer;
import com.cg.service.LoginService;


@RestController
@CrossOrigin("*")
public class LoginController {

	@Autowired
	LoginService loginservice;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginDto login) {
		String s = loginservice.login(login.getCustomer_ID(), login.getPassword());
		return new ResponseEntity<String>(s, HttpStatus.OK);
	}
}
