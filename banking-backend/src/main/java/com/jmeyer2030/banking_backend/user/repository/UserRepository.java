package com.jmeyer2030.banking_backend.user.repository;

import com.jmeyer2030.banking_backend.user.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
* Extending a JpaRepository means that it autogenerates the database queries for the methods
* This works because:
*  - This repository knows we're working with users because it specifies UserDTO
*  - It knows to link to the users table because UserDTO specifies that table
*/

public interface UserRepository extends JpaRepository<UserDTO, Long> {
    // Returns the UserDTO with that username
    UserDTO findByUsername(String username);

    // Checks if that username exists in the table
    boolean existsByUsername(String username);

    // Checks if that email exists in the table
    boolean existsByEmail(String email);
}
