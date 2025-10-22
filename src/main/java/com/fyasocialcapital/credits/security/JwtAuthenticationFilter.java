package com.fyasocialcapital.credits.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) throws ServletException, IOException {
         
        try {
            String jwt = getJwtFromRequest(request);
            
            if (jwt == null) {
                filterChain.doFilter(request, response);
                return;
            }
            
            
            if (!jwtUtil.validateToken(jwt)) {
                filterChain.doFilter(request, response);
                return;
            }
            
            
            String username = jwtUtil.extractUsername(jwt);
            
            if (username == null) {
                filterChain.doFilter(request, response);
                return;
            }
            
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            
            if (!jwtUtil.validateToken(jwt, userDetails)) {
                filterChain.doFilter(request, response);
                return;
            }
            
            UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(
                            userDetails, 
                            null, 
                            userDetails.getAuthorities()
                    );
            
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            
            // Establecer en el contexto
            SecurityContextHolder.getContext().setAuthentication(authentication);
    
            
        } catch (Exception e) {
            log.error("ERROR en el filtro JWT: {}", e.getMessage());
            log.error(" Stack trace:", e);
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        log.info("Authorization header: {}", 
                 bearerToken != null ? bearerToken.substring(0, Math.min(50, bearerToken.length())) + "..." : "NULL");
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        
        return null;
    }
}