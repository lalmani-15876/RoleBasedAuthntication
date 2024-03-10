package com.role.implementation.controller;

import com.role.implementation.service.DefaultUserService;
import com.role.implementation.service.DefaultUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.role.implementation.model.User;
import com.role.implementation.repository.UserRepository;

import java.security.Principal;


@Controller
@RequestMapping("/dashboard")
public class DashboardController {

	@Autowired
	private DefaultUserServiceImpl userService;
	@Autowired
	private UserRepository userRepository;



	@GetMapping("/userDetails")
	public String showUserDetails(Model model) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userService.findByEmail(userDetails.getUsername());

		// Pass the user details to the view
		model.addAttribute("user", user);
		return "userOwnDetail";
	}

	@PostMapping("/users")
	public String createUser(@ModelAttribute("user") User user, BindingResult result) {
		if (result.hasErrors()) {
			// Handle validation errors
			return "userForm"; // Redirect back to the form with errors
		}
		userService.save(user);
		return "redirect:/dashboard"; // Redirect to dashboard or user list
	}

	@GetMapping("/users/{id}/edit")
	public String showEditForm(@PathVariable("id") Integer id, Model model) {
		User user = userService.findById(id);
		model.addAttribute("user", user);
		return "editForm";
	}
	private boolean isCurrentUser(UserDetails userDetails, User user) {
		return userDetails.getUsername().equals(user.getEmail());
	}


}
