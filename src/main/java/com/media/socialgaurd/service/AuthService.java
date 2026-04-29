package com.media.socialgaurd.service;

import com.media.socialgaurd.DTO.AuthResponse;
import com.media.socialgaurd.DTO.LoginRequest;
import com.media.socialgaurd.DTO.RegisterRequest;
import com.media.socialgaurd.Security.JwtService;
import com.media.socialgaurd.model.User;
import com.media.socialgaurd.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager){
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    //Registration of a new user
    public AuthResponse register(RegisterRequest request){
        //check if username already exists
        if(userRepository.existsByUsername(request.getUsername())){
            throw new RuntimeException("username already taken");
        }

        //check if email already taken
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("email already registered");
        }

        User user = User.builder()
                .username(request.getUsername())
                .role(request.getRole())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isPremium(request.getIsPremium())
                .build();

        userRepository.save(user);

        //generate token
        String token = jwtService.generateToken(request.getUsername());

        return AuthResponse.builder()
                .token(token)
                .username(request.getUsername())
                .message("registration successful")
                .build();


    }

    //login
    public AuthResponse login(LoginRequest request){
        try {
          Authentication authentication =  authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch(AuthenticationException e){
            throw new BadCredentialsException("invalid username or password");
        }

        //if we reach here that means credentials are correct
        //find the user
        User user =  userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new RuntimeException("user not found")
        );

        String token = jwtService.generateToken(request.getUsername());

        return AuthResponse.builder()
                .token(token)
                .username(request.getUsername())
                .message("Login Successful!")
                .build();

    }
}
