package codeep.jean.repository;

import codeep.jean.domain.Family;
import codeep.jean.domain.UserFamily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFamilyRepository extends JpaRepository<UserFamily,Long> {
    List<UserFamily>findAllByFamilyId(Family family);
}
