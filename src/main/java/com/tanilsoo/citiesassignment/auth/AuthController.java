package com.tanilsoo.citiesassignment.auth;

import com.tanilsoo.citiesassignment.user.User;
import com.tanilsoo.citiesassignment.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<String> log() {
        return new ResponseEntity<>(
                passwordEncoder.encode("test123"), HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<AuthDto> login(@RequestBody LoginDto dto) throws Exception {

        if (dto.getUsername() == null || dto.getPassword() == null) {
            throw new Exception();
        }

        User user = this.userService.getUserByUsername(dto.getUsername());

        if (user == null) {
            throw new Exception("Incorrect password or username");
        }

        if (!this.authService.authenticatePassword(dto)) {
            throw new Exception("Incorrect password or username");
        }

        var token = this.authService.generateToken(user);

        return new ResponseEntity<>(
            AuthDto.builder().token(token).build(), HttpStatus.OK
        );
    }

    @PatchMapping
    public ResponseEntity<String> test() {
        return new ResponseEntity<>(
                passwordEncoder.encode("test123"), HttpStatus.OK
        );
    }


}
