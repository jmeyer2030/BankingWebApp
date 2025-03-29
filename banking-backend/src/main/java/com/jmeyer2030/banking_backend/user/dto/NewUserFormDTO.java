package com.jmeyer2030.banking_backend.user.dto;

import jakarta.validation.constraints.*;

/**
* DTO (Data transfer object)
*
*/
public class NewUserFormDTO {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;

    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 20, message = "First name must be between 1 and 20 characters")
    private String firstname;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 20, message = "Last name must be between 8 and 20 characters")
    private String lastname;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Override
    public String toString() {
        String usernameStr = "Username: " + username + "\n";
        String passwordStr = "Password: " + password + "\n";
        String firstnameStr = "First Name: " + firstname + "\n";
        String lastnameStr = "Last Name: " + lastname + "\n";
        String emailStr = "Email: " + email;

        return usernameStr + passwordStr + firstnameStr + lastnameStr + emailStr;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}