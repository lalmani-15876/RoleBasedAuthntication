package com.role.implementation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.role.implementation.model.User;


@Repository
public interface UserRepository extends CrudRepository<User, Integer>{

	User findByEmail(String emailId);
}
