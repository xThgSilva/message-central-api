package com.message.central.repositories;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.message.central.entities.User;
import org.springframework.data.domain.Pageable;

@Repository
public interface UserRepository extends JpaRepository <User, Long>{
	Optional <User> findByEmail(String email);
	
	Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
