package codeep.jean.service;

import codeep.jean.domain.TimeTable;
import codeep.jean.domain.TimeTableBlock;
import codeep.jean.domain.User;
import codeep.jean.domain.enums.TimeTableBlockID;
import codeep.jean.repository.TimeTableBlockRepository;
import codeep.jean.repository.TimeTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TimeTableService {
    private final TimeTableRepository timeTableRepository;
    private final TimeTableBlockRepository timeTableBlockRepository;

    public TimeTable createTimeTable(User user){
        TimeTable timeTable = timeTableRepository.save(new TimeTable(user));
        timeTable.setTimeTableBlock(timeTableInitializer(timeTable));
        return timeTable;
    }


    //현재 시간 가족 구성원 타임블럭 컨텐츠
    public void getNowMembersTimeTableBlock(){
        nowTimeBlockID();
    }



    public void updateTimeTable(Long userId, TimeTableBlock timeTableBlock){
    }


    private List<TimeTableBlock> timeTableInitializer(TimeTable timeTable) {
        List<TimeTableBlock> timeTableBlockSet = new ArrayList<>();
        TimeTableBlockID[] arr = TimeTableBlockID.values();

        for (TimeTableBlockID tb : arr) {
            TimeTableBlock timeTableBlock = timeTableBlockRepository.save(new TimeTableBlock(timeTable,tb));
            timeTableBlockSet.add(timeTableBlock);
        }
        return timeTableBlockSet;
    }
    private String nowTimeBlockID(){
        LocalDateTime now = LocalDateTime.now();
        String day = now.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US).toUpperCase();
        String hour = String.valueOf(now.getHour());
        String min = (now.getMinute() >= 30) ? "B" : "A";
        return day+"_"+hour+min;
    }
}
