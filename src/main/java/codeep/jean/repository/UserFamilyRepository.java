package codeep.jean.repository;

import codeep.jean.domain.Family;
import codeep.jean.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyRepository extends JpaRepository<Family,Long> {
    Family findByFamilyName(String familyName);
}
