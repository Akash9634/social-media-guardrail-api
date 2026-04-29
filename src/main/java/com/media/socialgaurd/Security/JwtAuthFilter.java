package com.media.socialgaurd.Security;

import com.media.socialgaurd.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthFilter(JwtService jwtService, UserRepository userRepository){
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,   // incoming request
            HttpServletResponse response, // outgoing response
            FilterChain filterChain       // chain of filters
    ) throws ServletException, IOException {

        //get the authorization header
        final String authHeader = request.getHeader("Authorization");

        //if no auth header or doesn't start with "Bearer " just pass it
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            //continue to next filter chain
            filterChain.doFilter(request, response);
            return;
        }

        //extract the token
        final String token = authHeader.substring(7);

        //extract username from token
        final String username = jwtService.extractUsername(token);

        //check if username exists or user already authenticated
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){

            //load user from database
            UserDetails user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("user not found"));

            if(user != null && jwtService.isTokenValid(token, user.getUsername())){

                //create authentication object
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.getAuthorities()
                        );

                //attach request details to auth token
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                //save authentication in security context
                //now spring knows this user is logged in for this context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        //continue to next filter/controller
        filterChain.doFilter(request, response);
    }
}
