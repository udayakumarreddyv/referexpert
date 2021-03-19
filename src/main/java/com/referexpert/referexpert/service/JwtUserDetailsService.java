package com.referexpert.referexpert.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.referexpert.referexpert.beans.UserRegistration;
import com.referexpert.referexpert.service.impl.MySQLServiceImpl;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    
    @Autowired
    private MySQLServiceImpl mySQLService;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    String criteria = " email = '" + username +"'";
   	    UserRegistration userRegistration = mySQLService.selectUser(criteria);
		if (userRegistration == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(userRegistration.getEmail(), userRegistration.getPassword(),
				new ArrayList<>());
	}

	public UserRegistration save(UserRegistration userRegistration) {
	    userRegistration.setPassword(bcryptEncoder.encode(userRegistration.getPassword()));
		return mySQLService.save(userRegistration);
	}
}