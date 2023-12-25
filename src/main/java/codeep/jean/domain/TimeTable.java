package codeep.jean.domain;

import codeep.jean.domain.enums.Day;
import codeep.jean.domain.enums.TimeBlockID;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeTable {
    @Id
    @Column(name = "timetable_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_user_id")
    private User user;
    //table property
    private Day day;
    private TimeBlockID timeBlockID;
    //table content
    @Nullable
    private String timetableContent;

}
