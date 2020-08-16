package com.interview.template.controller;

import java.net.URI;
import java.util.List;

import com.interview.template.exceptions.DuplicateUserException;
import com.interview.template.exceptions.ReservedUsernameException;
import com.interview.template.exceptions.UserNotFoundException;
import com.interview.template.model.UserEntity;
import com.interview.template.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping(UserController.BASE_URL)
@AllArgsConstructor
class UserController {

	static final String BASE_URL = "/api/v1";

	private final UserService userService;

	@GetMapping
	public List<UserEntity> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/users/{userId}")
	UserEntity getUser(@PathVariable Long userId) throws UserNotFoundException {
		return userService.getUser(userId);
	}

	@GetMapping("/search/users/{searchText}")
	List<UserEntity> getUser(@PathVariable String searchText) {
		return userService.searchUsers(searchText);
	}

	@RequestMapping(method = RequestMethod.HEAD, value = "/users/{userId}")
	void checkExists(@PathVariable Long userId) throws UserNotFoundException {
		userService.checkUserExists(userId);
	}

	@PostMapping("/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody UserEntity user) throws DuplicateUserException, ReservedUsernameException {
		UserEntity savedUser = userService.createUser(user);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedUser.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/users/{userId}")
	public void deleteUser(@PathVariable Long userId) throws UserNotFoundException{
		userService.delete(userId);
	}


}
