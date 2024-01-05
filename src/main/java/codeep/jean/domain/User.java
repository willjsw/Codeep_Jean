package codeep.jean.domain;

import codeep.jean.domain.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @OneToOne(cascade=CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name="time_table_id")
    private TimeTable timeTable;

    public User(String userEmail, String password, Role role, String name, String contact) {
        this.userEmail = userEmail;
        this.password = password;
        this.role = role;
        this.name = name;
        this.contact = contact;
        this.timeTable = new TimeTable(this);
    }

    public void update(String password,String name,String contact) {
        this.password = password;
        this.name = name;
        this.contact = contact;
    }
    public void initializeTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
    }

}
