package codeep.jean.controller.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FamilyJoinResponseDTO {
    private Long id;
    private String familyName;

}
