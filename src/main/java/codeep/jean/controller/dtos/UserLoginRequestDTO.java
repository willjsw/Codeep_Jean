package codeep.jean.controller.dtos;

import codeep.jean.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequestDTO {
    private String userEmail;
    private String password;
}
