package codeep.jean.domain;

import codeep.jean.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Family extends BaseEntity{
    @Id @Column(name = "family_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String familyId;
    private String familyPassword;
    private String familyName;

    public Family(String familyName, String familyId,String familyPassword) {
        this.familyName=familyName;
        this.familyId=familyPassword;
    }
}
