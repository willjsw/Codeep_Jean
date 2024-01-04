package codeep.jean.controller.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinResponseDTO {
    private Long id;
    private String userEmail;
    private String familyName;

}
