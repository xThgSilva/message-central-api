package com.message.central.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.message.central.entities.User;
import com.message.central.requests.LoginRequest;
import com.message.central.requests.UserRequest;
import com.message.central.responses.LoginResponse;
import com.message.central.responses.UserResponse;
import com.message.central.services.JwtService;
import com.message.central.services.UserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	JwtService jwt;
	
	@PostMapping(value = "/register")
	public ResponseEntity<UserResponse> createAccount(@RequestBody UserRequest request) {
		return ResponseEntity.ok(userService.createAccount(request));
	}
	
	@GetMapping
	public ResponseEntity<Page<UserResponse>> findUsers(@RequestParam(required = false) String name, @RequestParam int page, 
			@RequestParam int size) {
	    return ResponseEntity.ok(userService.findUsers(name, page, size));
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<UserResponse> findUserById(@PathVariable Long id) {
		return ResponseEntity.ok(userService.findUserById(id));
	}
	
	@DeleteMapping(value = "/delete")
	public ResponseEntity<?> deleteUserById(@AuthenticationPrincipal User user) {
		userService.deleteUserById(user.getId());
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/update/")
	public ResponseEntity<UserResponse> updateUserById(@AuthenticationPrincipal User user, @RequestBody UserRequest request) {
		return ResponseEntity.ok(userService.updateUserById(user.getId(), request));
	}
	
	// Auth Endpoints
	@PostMapping(value = "/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
		return ResponseEntity.ok(userService.login(request, response));
	}
	
	@GetMapping(value = "/me")
	public ResponseEntity<UserResponse> getMeInformations(Authentication authentication) {
		return ResponseEntity.ok(userService.whoIsMe(authentication));
	}
}	