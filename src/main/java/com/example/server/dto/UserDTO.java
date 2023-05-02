package com.example.server.dto;

import com.example.server.entity.enums.ERole;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserDTO {
    private Long id;
    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;
    private String username;
    private String bio;
    private ERole role;
}
