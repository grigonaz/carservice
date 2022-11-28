package cz.cvut.kbss.ear.carservice.rest;

import cz.cvut.kbss.ear.carservice.dto.MoneyDto;
import cz.cvut.kbss.ear.carservice.dto.RegisterDto;
import cz.cvut.kbss.ear.carservice.dto.RoleDto;
import cz.cvut.kbss.ear.carservice.exceptions.NotFoundException;
import cz.cvut.kbss.ear.carservice.model.Role;
import cz.cvut.kbss.ear.carservice.rest.util.JWTUtil;
import cz.cvut.kbss.ear.carservice.security.UserDetails;
import cz.cvut.kbss.ear.carservice.service.UserDetailsService;
import cz.cvut.kbss.ear.carservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Autowired
    public UserController(UserService userService, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> createNewUser(@RequestBody RegisterDto registerDto) {
        userService.createNewUser(registerDto);
        return ResponseEntity.ok("CREATED");
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        jwtUtil.makeInvalid(jwt);

        return ResponseEntity.ok("LOGGED OUT");
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping(value = "/{username}/money")
    public ResponseEntity<?> setMoney(@PathVariable String username, @RequestBody MoneyDto money) {
        userService.setMoney(money.getMoney(), username);

        return ResponseEntity.ok("UPDATED");
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping(value = "/{username}/role")
    public ResponseEntity<?> setRole(@PathVariable String username, @RequestBody RoleDto role) {
        userService.setRole(role, username);
        return ResponseEntity.ok("UPDATED");
    }
}
