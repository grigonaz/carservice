package cz.cvut.kbss.ear.carservice.rest;

import cz.cvut.kbss.ear.carservice.security.UserDetails;
import cz.cvut.kbss.ear.carservice.dto.AuthorizationResponseDto;
import cz.cvut.kbss.ear.carservice.dto.LoginDto;
import cz.cvut.kbss.ear.carservice.service.UserDetailsService;
import cz.cvut.kbss.ear.carservice.rest.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class AuthorizationController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;

    @Autowired
    public AuthorizationController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            System.out.println(loginDto.getUsername() + " " + loginDto.getPassword());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        } catch (BadCredentialsException e) {
            System.out.println(loginDto.getUsername() + " " + loginDto.getPassword());
            throw new BadCredentialsException("Bad credentials exception is throwed.");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        final String role = userDetails.getAuthorities().toString();

        return ResponseEntity.ok(new AuthorizationResponseDto(jwt,role));
    }
}