package org.sid.exam.services;

import org.sid.exam.dto.AuthRequestDTO;
import org.sid.exam.dto.AuthResponseDTO;
import org.sid.exam.dto.RegisterUserDTO;
import org.sid.exam.dto.UserDTO;
import org.sid.exam.entities.AppUser;
import org.sid.exam.repositories.AppUserRepository;
import org.sid.exam.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(AppUserRepository appUserRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtService jwtService) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponseDTO login(AuthRequestDTO authRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword())
        );

        String token = jwtService.generateToken(authRequestDTO.getUsername(), authentication.getAuthorities());
        Set<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .collect(java.util.stream.Collectors.toSet());
        return new AuthResponseDTO(token, "Bearer", authRequestDTO.getUsername(), roles);
    }

    @Override
    public UserDTO registerClient(RegisterUserDTO registerUserDTO) {
        if (appUserRepository.existsByUsername(registerUserDTO.getUsername())) {
            throw new IllegalArgumentException("Ce nom d'utilisateur existe deja");
        }
        AppUser appUser = new AppUser();
        appUser.setUsername(registerUserDTO.getUsername());
        appUser.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
        appUser.setRoles(Set.of("ROLE_CLIEN"));
        return toDTO(appUserRepository.save(appUser));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> listUsers() {
        return appUserRepository.findAll().stream().map(this::toDTO).toList();
    }

    private UserDTO toDTO(AppUser appUser) {
        return new UserDTO(appUser.getId(), appUser.getUsername(), appUser.getRoles());
    }
}
