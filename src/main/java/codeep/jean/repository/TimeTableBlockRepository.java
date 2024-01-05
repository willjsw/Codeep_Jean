package codeep.jean.repository;

import codeep.jean.domain.TimeTableBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeTableBlockRepository extends JpaRepository<TimeTableBlock,Long> {
}
