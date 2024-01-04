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
    private String familyName;
    @OneToMany
    private List<User> familyMembers = new ArrayList<>();

    public Family(String familyName,List<User> familyMembers) {
        this.familyName=familyName;
        this.familyMembers=familyMembers;
    }
}
