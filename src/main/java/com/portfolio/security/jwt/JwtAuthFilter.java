package com.portfolio.security.jwt;

import com.portfolio.security.UserSS;
import com.portfolio.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if(authorization != null && authorization.startsWith("Bearer")){
            String token = authorization.split(" ")[1];

            if(jwtService.isTokenValid(token)) {
                String emailUser = jwtService.getEmailUser(token);
                String role = jwtService.getRole(token);
                Long id = jwtService.getUserId(token);
                UserSS user = userService.loadUserByUsernameAndRole(emailUser, role, id);
                
                UsernamePasswordAuthenticationToken userAuthentication =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                userAuthentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(userAuthentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
