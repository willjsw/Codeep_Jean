package codeep.jean.domain;

import codeep.jean.domain.enums.Role;
import codeep.jean.domain.enums.TimeBlockID;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity{
    @Id @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userEmail;
    private String password;
    private String name;
    private Role role;
    private String contact;

    @ManyToOne
    @JoinColumn(name = "family")
    private Family family;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeTable> timeTables = new ArrayList<>();

    public User(String userEmail, String password, Role role, String name, String contact, Family family,List<TimeTable> timeTables) {
        this.userEmail = userEmail;
        this.password = password;
        this.role = role;
        this.name = name;
        this.contact = contact;
        this.family = family;
        this.timeTables = timeTables;
    }

    public void update(User updateInfo) {
        this.password = updateInfo.password;
        this.name = updateInfo.name;
        this.contact = updateInfo.contact;
        this.timeTables = updateInfo.timeTables;
    }

}
