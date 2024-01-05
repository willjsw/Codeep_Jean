package codeep.jean.domain;

import codeep.jean.domain.enums.TimeTableBlockID;
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
public class TimeTable extends BaseTimeEntity{
    @Id
    @Column(name = "time_table_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "timeTable")
    private User user;
    @OneToMany(mappedBy = "timeTable",cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "key")
    private List<TimeTableBlock> timeTableBlockSet = new ArrayList<>();

    public TimeTable(User user){
        this.user = user;
        this.timeTableBlockSet = new ArrayList<>();
    }

    public void setTimeTableBlock(List<TimeTableBlock> timeTableBlockSet){
        this.timeTableBlockSet = timeTableBlockSet;
    }

}
