package com.referexpert.referexpert.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.referexpert.referexpert.beans.JwtRequest;
import com.referexpert.referexpert.beans.JwtResponse;
import com.referexpert.referexpert.beans.RefreshToken;
import com.referexpert.referexpert.beans.TokenRefreshRequest;
import com.referexpert.referexpert.beans.UserRegistration;
import com.referexpert.referexpert.exception.TokenRefreshException;
import com.referexpert.referexpert.security.JwtTokenUtil;
import com.referexpert.referexpert.service.JwtUserDetailsService;
import com.referexpert.referexpert.service.MySQLService;
import com.referexpert.referexpert.service.impl.RefreshTokenService;

import io.jsonwebtoken.impl.DefaultClaims;

@RestController
@CrossOrigin
public class JwtAuthenticationController {
    
    private final static Logger logger = LoggerFactory.getLogger(JwtAuthenticationController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	@Autowired
	RefreshTokenService refreshTokenService;
	
	@Autowired
    private MySQLService mySQLService;

	@PostMapping("/referexpert/validateuser")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
	    logger.info("JwtAuthenticationController :: In createAuthenticationToken : " + authenticationRequest.getUsername());
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);
		
		String criteria = " email = '" + authenticationRequest.getUsername() + "'";
        UserRegistration userRegistration =  mySQLService.selectUser(criteria);
                
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userRegistration.getUserId());

		return ResponseEntity.ok(new JwtResponse(token, refreshToken.getToken()));
	}
	
	@PostMapping("/referexpert/logout")
	public ResponseEntity<?> logoutUser(@RequestBody TokenRefreshRequest request) {
		String token = request.getRefreshToken();
		refreshTokenService.deleteByToken(token);
		return ResponseEntity.ok("Log out successful!");
	}

	private void authenticate(String username, String password) throws Exception {
		try {
		    logger.info("JwtAuthenticationController :: In authenticate : " + username);
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
	
	@PostMapping("/referexpert/refreshtoken")
	public ResponseEntity<?> refreshtoken(@RequestBody TokenRefreshRequest request) throws Exception {
		String refreshToken = request.getRefreshToken();

		try {
			RefreshToken rTwo = refreshTokenService.verifyExpiration(refreshTokenService.findByToken(refreshToken));
			String criteria = " user_id = '" + rTwo.getUserId() + "'";
			UserRegistration userRegistration = mySQLService.selectUser(criteria);

			String token = jwtTokenUtil.generateTokenFromUsername(userRegistration.getEmail());

			return ResponseEntity.ok(new JwtResponse(token, refreshToken));
		} catch (Exception e) {
			e.printStackTrace();
			throw new TokenRefreshException(refreshToken, "Refresh token is not in database!");
		}
	}
	
	public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		for (Entry<String, Object> entry : claims.entrySet()) {
			expectedMap.put(entry.getKey(), entry.getValue());
		}
		return expectedMap;
	}
}
