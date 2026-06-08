package org.sid.exam.services;

import org.sid.exam.dto.AuthRequestDTO;
import org.sid.exam.dto.AuthResponseDTO;
import org.sid.exam.dto.RegisterUserDTO;
import org.sid.exam.dto.UserDTO;

import java.util.List;

public interface AuthService {
    AuthResponseDTO login(AuthRequestDTO authRequestDTO);

    UserDTO registerClient(RegisterUserDTO registerUserDTO);

    List<UserDTO> listUsers();
}
