package com.example.livealone.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.livealone.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryQuery {
	Optional<User> findByEmail(String email);
}
