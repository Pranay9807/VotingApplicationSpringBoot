package com.jForce.service;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.jForce.model.User;
import com.jForce.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository ur;

	@Autowired
	private BCryptPasswordEncoder encoder;


	// REGISTER
	public User register(User user) {
		if (this.ur.findByEmail(user.getEmail()).isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with email already exists");
		} else {
			User newUser = new User();
			newUser.setName(user.getName());
			newUser.setEmail(user.getEmail());
			newUser.setContact(user.getContact());
			newUser.setPassword(encoder.encode(user.getPassword()));
			newUser.setRoles(Arrays.asList("USER"));
			this.ur.save(newUser);
			return newUser;
		}
	}
	

	// FINDBY EMAIL
	public User findByEmail(String email) {
		return this.ur.findByEmail(email).orElseThrow(() -> {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with email does not eixists");
		});
	}

}
