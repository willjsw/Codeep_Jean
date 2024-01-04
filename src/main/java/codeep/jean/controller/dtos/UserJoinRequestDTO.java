package codeep.jean.controller.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinRequestDTO {
    private String userEmail;
    private String password;
    private String name;
    private String contact;
    private Long familyId;
    
}
