package com.role.implementation.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.role.implementation.DTO.UserRegisteredDTO;
import com.role.implementation.model.User;

import java.util.List;


public interface DefaultUserService extends UserDetailsService{

	User save(UserRegisteredDTO userRegisteredDTO);
	User findById(Integer id);
	User findByEmail(String email);
	User update(User user);
	void delete(Integer id);
	List<User> findAll();


}
