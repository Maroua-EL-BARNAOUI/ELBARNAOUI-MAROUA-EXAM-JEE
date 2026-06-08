package org.sid.exam.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sid.exam.dto.AuthRequestDTO;
import org.sid.exam.dto.AuthResponseDTO;
import org.sid.exam.dto.RegisterUserDTO;
import org.sid.exam.dto.UserDTO;
import org.sid.exam.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentification", description = "Authentification JWT et gestion des utilisateurs")
public class AuthRestController {

    private final AuthService authService;

    public AuthRestController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Authentifier un utilisateur et retourner un token JWT")
    public AuthResponseDTO login(@RequestBody AuthRequestDTO authRequestDTO) {
        return authService.login(authRequestDTO);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creer un compte client")
    public UserDTO registerClient(@RequestBody RegisterUserDTO registerUserDTO) {
        return authService.registerClient(registerUserDTO);
    }

    @GetMapping("/me")
    @Operation(summary = "Retourner l'utilisateur connecte")
    public String me(Principal principal) {
        return principal.getName();
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Lister les utilisateurs")
    public List<UserDTO> listUsers() {
        return authService.listUsers();
    }
}
