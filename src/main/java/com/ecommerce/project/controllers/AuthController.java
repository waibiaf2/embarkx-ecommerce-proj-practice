package com.ecommerce.project.controllers;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.models.AppRole;
import com.ecommerce.project.models.Role;
import com.ecommerce.project.models.User;
import com.ecommerce.project.repositories.RoleRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.security.jwt.JwtUtils;
import com.ecommerce.project.security.request.LoginRequest;
import com.ecommerce.project.security.request.SignupRequest;
import com.ecommerce.project.security.response.MessageResponse;
import com.ecommerce.project.security.response.UserInfoResponse;
import com.ecommerce.project.security.services.UserDetailsImpl;
import com.ecommerce.project.security.services.UserDetailsServiceImpl;
import io.jsonwebtoken.security.Password;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping(AppConstants.BASE_URL + "/auth")
public class AuthController {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private AuthenticationManager authenticationManager;
    private UserDetailsServiceImpl userDetailsService;
    private JwtUtils jwtUtils;
    private PasswordEncoder passwordEncoder;
    
    public AuthController(
        UserRepository userRepository,
        RoleRepository roleRepository,
        AuthenticationManager authenticationManager,
        UserDetailsServiceImpl userDetailsService,
        JwtUtils jwtUtils,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }
    
    @PostMapping("/signin")
    public ResponseEntity<?> authenticatUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        
        try {
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );
        } catch (AuthenticationException ex) {
            Map<String, Object> map = new HashMap<>();
            
            map.put("message", "Bad Credentials");
            map.put("status", false);
            
            return new ResponseEntity<Object>(map, HttpStatus.OK);
        }
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateTokenFromUserName(userDetails);
        
        List<String> roles = userDetails.getAuthorities().stream().map(
            item -> item.getAuthority()).toList();
        
        UserInfoResponse response = new UserInfoResponse(
            userDetails.getId(),
            userDetails.getUsername(),
            jwtToken,
            roles
        );
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        boolean userNameExists = userRepository.existsByUserName(signupRequest.getUsername());
        boolean emailExists = userRepository.existsByEmail(signupRequest.getEmail());
        
        if (userNameExists)
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        
        if (emailExists)
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        
        User user = new User(
            signupRequest.getUsername(),
            signupRequest.getEmail(),
            passwordEncoder.encode(signupRequest.getPassword())
        );
        
        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();
        
        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER).orElseThrow(
                () -> new RuntimeException("Role not found")
            );
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN).orElseThrow(
                            () -> new RuntimeException("Role not found")
                        );
                        roles.add(adminRole);
                        break;
                    case "seller":
                        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER).orElseThrow(
                            () -> new RuntimeException("Role not found")
                        );
                        roles.add(sellerRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER).orElseThrow(
                            () -> new RuntimeException("Role not found")
                        );
                        roles.add(userRole);
                }
            });
        }
        
        user.setRoles(roles);
        userRepository.save(user);
        
        return new ResponseEntity<>(
            new MessageResponse("User successfully created!"),
            HttpStatus.CREATED
        );
    }
}
