package com.role.implementation.controller;


import com.role.implementation.service.DefaultUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.role.implementation.model.User;
import com.role.implementation.repository.UserRepository;



@Controller
@RequestMapping("/adminScreen")
public class AdminController {

	@Autowired
	private DefaultUserServiceImpl userService;
	@Autowired
	UserRepository userRepository;



	@GetMapping("/users")
	public String listUsers(Model model) {
		model.addAttribute("users", userService.findAll());
		return "userList"; // Assuming you have a view for displaying user list
	}

	@GetMapping("/users/{id}")
	public String viewUser(@PathVariable("id") Integer id, Model model) {
		User user = userService.findById(id);
		model.addAttribute("user", user);
		return "userDetails"; // Assuming you have a view for displaying user details
	}

	@GetMapping("/users/new")
	public String createUserForm(Model model) {
		model.addAttribute("user", new User());
		return "userForm"; // Assuming you have a form view for creating a new user
	}

	@PostMapping("/users")
	public String createUser(@ModelAttribute("user") User user) {
		userService.save(user);
		return "redirect:/adminScreen/users";
	}

	@GetMapping("/users/{id}/edit")
	public String editUserForm(@PathVariable("id") Integer id, Model model) {
		User user = userService.findById(id);
		model.addAttribute("user", user);
		return "userDetails"; // Assuming you have a form view for editing user details
	}

	@PostMapping("/users/{id}/edit")
	public String editUser(@PathVariable("id") Integer id, @ModelAttribute("user") User user) {
		user.setId(id);
		userService.save(user);
		return "redirect:/adminScreen/users";
	}

	@PostMapping("/users/{id}/delete")
	public String deleteUser(@PathVariable("id") Integer id) {
		userService.delete(id);
		return "redirect:/adminScreen/users";
	}


	private String returnUsername() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails user = (UserDetails) securityContext.getAuthentication().getPrincipal();
		User users = userRepository.findByEmail(user.getUsername());
		return users.getName();
	}
	
}
