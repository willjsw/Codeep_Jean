package codeep.jean.domain;

import codeep.jean.domain.enums.TimeBlockID;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.LinkedHashMap;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @ManyToOne
    @JoinColumn(name = "family")
    private Family family;
    @OneToMany
    private HashMap<TimeBlockID,TimeTable> timeTableSet = new LinkedHashMap<>();
}
