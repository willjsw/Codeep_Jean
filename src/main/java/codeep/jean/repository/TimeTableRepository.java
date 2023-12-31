package codeep.jean.repository;

import codeep.jean.domain.TimeTable;
import codeep.jean.domain.TimeTableBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable,Long> {
}
