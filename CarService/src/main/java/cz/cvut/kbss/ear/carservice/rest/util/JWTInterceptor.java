package cz.cvut.kbss.ear.carservice.rest.util;

import cz.cvut.kbss.ear.carservice.exceptions.JwtExpiredException;
import cz.cvut.kbss.ear.carservice.security.UserDetails;
import cz.cvut.kbss.ear.carservice.service.BannedJWTService;
import cz.cvut.kbss.ear.carservice.service.UserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTInterceptor extends OncePerRequestFilter {


    private final UserDetailsService userDetailsService;

    private final JWTUtil jwtUtil;

    private final BannedJWTService bjs;

    @Autowired
    public JWTInterceptor(UserDetailsService userDetailsService, JWTUtil jwtUtil, BannedJWTService bjs) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.bjs = bjs;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwt = authHeader.substring(7);
                username = jwtUtil.extractUsername(jwt);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null && !bjs.exist(jwt)) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } else {
                    throw new JwtExpiredException();
                }
            }
        } catch (ExpiredJwtException e) {
            throw new JwtExpiredException();
        }
        filterChain.doFilter(request, response);
    }
}
