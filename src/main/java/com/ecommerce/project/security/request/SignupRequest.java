package com.ecommerce.project.security.request;

import com.ecommerce.project.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    
    @NotBlank
    @Email
    @Size(max = 20)
    private String email;
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
    
    
    private Set<String> role;
}
