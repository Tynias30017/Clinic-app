package org.example.clinic.dto;

import lombok.Data;
import org.example.clinic.model.User;

@Data
public class UserRegisterRequest {
    private String username;
    private String password;
    private User.Role role;
}

