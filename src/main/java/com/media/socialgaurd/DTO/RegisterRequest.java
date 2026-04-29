package com.media.socialgaurd.DTO;

import com.media.socialgaurd.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    // @NotBlank = cannot be null or empty string
    @NotBlank(message = "Username is required")
    // @Size = minimum 3, maximum 20 characters
    @Size(min = 3, max = 20, message = "Username must be 3-20 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    // @Email = must be valid email format
    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email")
    private String email;

    @NotBlank(message = "Role is required")
    private Role role;

    // Optional field - defaults to false if not provided
    private Boolean isPremium = false;
}
