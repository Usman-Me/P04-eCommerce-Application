package com.example.demo.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	//BCryptPasswordEncoder eingefügt
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	//Reference aus Video L04-
	private Logger log = LoggerFactory.getLogger(UserController.class);


	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		log.debug("FindById called with ID: ", id);
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		log.debug("FindByUserName called with User name: ", username);
		User user = userRepository.findByUsername(username);
		if (user == null) {
			log.error("Did not find user with username: ", user.getUsername());
		} else {
			log.info("User found ", user.getUsername());
		}

		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		//log.info("User name set  ", user.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);


		if(createUserRequest.getPassword().length() < 7 ){
			log.error("[Error] [Create User]  user -> " + user.getUsername() +", REASON -> invalid password");

			return ResponseEntity.badRequest().body("Password must have at least 7 characters");
		}else if (!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			log.error("[Error] [Create User] user -> " + user.getUsername() +", REASON -> password mismatch");
			return ResponseEntity.badRequest().body("Password does not match confirm password");
		}
		String encodedPassword = bCryptPasswordEncoder.encode(createUserRequest.getPassword());
		user.setPassword(encodedPassword);
		userRepository.save(user);
		log.info("[Alert] [Create User] New user successfull created -> " + user.getUsername());

		return ResponseEntity.ok(user);
	}
	
}
