package codeep.jean.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Family extends BaseEntity{
    @Id @Column(name = "family_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String familyID;
    private String familyPassword;
    private String familyName;

    public Family(String familyName, String familyID, String familyPassword) {
        this.familyName=familyName;
        this.familyID = familyID;
        this.familyPassword=familyPassword;
    }
}
