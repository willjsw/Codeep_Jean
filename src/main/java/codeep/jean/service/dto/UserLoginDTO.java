package codeep.jean.service.dto;

import codeep.jean.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    private Long id;
    private String userEmail;
    private Role role;
    private String accessToken;
    private String refreshToken;
}
