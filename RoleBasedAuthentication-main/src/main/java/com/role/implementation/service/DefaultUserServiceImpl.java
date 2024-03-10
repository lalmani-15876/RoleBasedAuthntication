package com.role.implementation.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.role.implementation.DTO.UserRegisteredDTO;
import com.role.implementation.model.Role;
import com.role.implementation.model.User;
import com.role.implementation.repository.RoleRepository;
import com.role.implementation.repository.UserRepository;

@Service
public class DefaultUserServiceImpl implements DefaultUserService{
   @Autowired
	private UserRepository userRepo;
   @Autowired
    private RoleRepository roleRepo;
   
   
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	
		User user = userRepo.findByEmail(email);
		if(user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRole()));		
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
	}

	@Override
	public User save(UserRegisteredDTO userRegisteredDTO) {
		Role role = new Role();
		if(userRegisteredDTO.getRole().equals("USER"))
		  role = roleRepo.findByRole("USER");
		else if(userRegisteredDTO.getRole().equals("ADMIN"))
		 role = roleRepo.findByRole("ADMIN");
		User user = new User();
		user.setEmail(userRegisteredDTO.getEmail_id());
		user.setName(userRegisteredDTO.getName());
		user.setPassword(passwordEncoder.encode(userRegisteredDTO.getPassword()));
		user.setRole(role);
		
		return userRepo.save(user);
	}
	@Override
	public User findById(Integer id) {
		return userRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
	}

	@Override
	public User findByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	@Override
	public User update(User user) {
		// Check if user exists
		findById(user.getId());
		return userRepo.save(user);
	}

	@Override
	public void delete(Integer id) {
		// Check if user exists
		findById(id);
		userRepo.deleteById(id);
	}
	@Override
	public List<User> findAll() {
		Iterable<User> iterable = userRepo.findAll();
		List<User> userList = new ArrayList<>();
		iterable.forEach(userList::add);
		return userList;
	}
	public User save(User user) {
		// Encode the password before saving
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// Assuming you have logic to set the user's role, such as fetching it from the database
		Role role = roleRepo.findByRole("USER");
		user.setRole(role);

		return userRepo.save(user);
	}
}
