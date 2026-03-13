package com.message.central.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.message.central.entities.User;
import com.message.central.exceptions.EmailAlreadyExistsException;
import com.message.central.exceptions.InsufficienteCharactersException;
import com.message.central.exceptions.InvalidCredentials;
import com.message.central.exceptions.NotFoundException;
import com.message.central.repositories.UserRepository;
import com.message.central.requests.LoginRequest;
import com.message.central.requests.UserRequest;
import com.message.central.responses.LoginResponse;
import com.message.central.responses.UserResponse;
import org.springframework.data.domain.Pageable;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordConfig;

	@Autowired
	EmailService emailService;

	@Autowired
	JwtService jwt;

	public UserResponse createAccount(UserRequest request) {
		User user = new User();

		user.setName(request.getName());
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new EmailAlreadyExistsException("Email already registered.");
		}
		user.setEmail(request.getEmail());

		if (request.getPassword().length() < 5) {
			throw new InsufficienteCharactersException("The password must be at least 5 characters long.");
		}
		user.setPassword(passwordConfig.encode(request.getPassword()));

		user = userRepository.save(user);
		emailService.sendWelcomeEmail(user);

		return new UserResponse(user);
	}

	public Page<UserResponse> findUsers(String name, int page, int size) {

	    PageRequest pageable = PageRequest.of(page, size);

	    Page<User> users;

	    if (name == null || name.isBlank()) {
	        users = userRepository.findAll(pageable);
	    } else {
	        users = userRepository.findByNameContainingIgnoreCase(name, pageable);
	    }

	    return users.map(UserResponse::new);
	}

	public UserResponse findUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("User with Id " + id + " not found."));

		return new UserResponse(user);
	}
	
	public void deleteUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("User with Id " + id + " not found to delete."));

		userRepository.deleteById(id);
	}

	public UserResponse updateUserById(Long id, UserRequest update) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("User with Id " + id + " not found to update."));

		if (update.getName() != null && !update.getName().isBlank()) {
			user.setName(update.getName());
		}

		if (update.getEmail() != null && !update.getEmail().isBlank()) {
			if (userRepository.findByEmail(update.getEmail()).isPresent()) {
				throw new EmailAlreadyExistsException("Email already registered and cannot be used.");
			}
		}

		if (update.getPassword() != null && !update.getPassword().isBlank()) {
			if (update.getPassword().length() < 5) {
				throw new InsufficienteCharactersException("The password must be at least 5 characters long.");
			}
		}
		return new UserResponse(userRepository.save(user));
	}

	public LoginResponse login(LoginRequest request, HttpServletResponse httpResponse) {
		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new InvalidCredentials("Invalid e-mail."));

		if (!passwordConfig.matches(request.getPassword(), user.getPassword())) {
			throw new InvalidCredentials("Invalid password");
		}

		String token = jwt.generateToken(request.getEmail());

		return new LoginResponse("Login Succesully", token);
	}

	public UserResponse whoIsMe(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		
		UserResponse response = new UserResponse(
			user.getId(),
			user.getName(),
			user.getEmail()
		);
		
		return response;
	}
}