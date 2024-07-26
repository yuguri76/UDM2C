package com.example.livealone.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.livealone.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryQuery {
	Optional<User> findByEmail(String email);
}
