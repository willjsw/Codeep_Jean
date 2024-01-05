package codeep.jean.domain;

import codeep.jean.domain.enums.TimeTableBlockID;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.awt.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeTableBlock extends BaseTimeEntity{
    @Id
    @Column(name = "timetable_block_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "time_table_id")
    private TimeTable timeTable;
    //table property
    private TimeTableBlockID timeBlockID;

    //table content
    @Nullable
    private Color color;
    @Nullable
    private String timetableContent;

    public TimeTableBlock(TimeTable timeTable, TimeTableBlockID timeBlockID){
        this.timeTable = timeTable;
        this.timeBlockID = timeBlockID;
    }
}
