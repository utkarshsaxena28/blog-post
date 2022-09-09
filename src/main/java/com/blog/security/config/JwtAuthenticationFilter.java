package com.blog.security.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.blog.entity.User;
import com.blog.repositiory.UserRepositiory;
import com.blog.security.util.JwtTokenUtil;
import com.blog.security.service.CustomUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
    private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
    private JwtTokenUtil jwtUtil;

	// this method will be called when api request gets hit
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// Get Token from the header of request object pass the key that is Authorization bcoz its value = token 
		String requestToken = request.getHeader("Authorization");
		System.out.println(requestToken);
		
		// but requestToken me jo token hai usme Bearer lgaa hua hai to Bearer word nikalo or baki ka token variable me save kero
		String token = null;
		// or Bearer word ko hum separate krenge real token se with the use substring(7); method of String class
		
		// Get user name from token with the use of method in JwtTokenUtil and save it in Uname
		String userName = null;
		
		// Below If block is used to get token and user name from token
		if(requestToken != null &&  requestToken.startsWith("Bearer")) 
		{
			// Separating bearer word from request token 
			token = requestToken.substring(7);
			
			// taking user name out from token using JwtTokenUtil class
			try {
				userName = jwtUtil.getUsernameFromToken(token);
			} catch (IllegalArgumentException e) {
				logger.error("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				logger.error("JWT Token has expired");
			} catch (MalformedJwtException e) {
				logger.error("Invalid JWT");
			}

			
		}
		else {
			logger.warn("Jwt Token is not available or JWT Token does not begin with Bearer String");
		}

		// Once we get the token validate it.
		if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			// take user Details value from data base
			//User Uname = userRepo.findByName(userName);
			UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);

			if (jwtUtil.validateToken(token, userDetails)) { // if true than token is valid then set authentication

				// now generate admit card that is permission means authentication
				UsernamePasswordAuthenticationToken usernamePAT =
						new UsernamePasswordAuthenticationToken
								(userDetails, null, userDetails.getAuthorities()); // What is getAuthorities?
				// Why this is done ????
				usernamePAT.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the
				// Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePAT);
			} else {
				logger.error("Invalid Jwt Token");
			}
		}
		else {
			logger.error("User name is null or context is not null");
		}

		// What is the purpose of this filterChain
		filterChain.doFilter(request, response);
	}
}
