package codeep.jean.service;

import codeep.jean.controller.dtos.FamilyJoinResponseDTO;
import codeep.jean.domain.Family;
import codeep.jean.exception.AuthException;
import codeep.jean.exception.ErrorCode;
import codeep.jean.repository.FamilyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;

    //Family Join
    public FamilyJoinResponseDTO joinFamily(String familyName,String familyId, String familyPassword){
        validateFamilyNameDuplication(familyName);
        Family joinedFamily = familyRepository.save(new Family(familyName,familyId, familyPassword));
        return new FamilyJoinResponseDTO(joinedFamily.getId(),joinedFamily.getFamilyName());
    }

    private void validateFamilyNameDuplication(String familyName) throws AuthException{
        Optional.ofNullable(familyRepository.findByFamilyName(familyName))
                .ifPresent(family->{
                    throw new AuthException(ErrorCode.FAMILY_NAME_DUPLICATED, familyName+" is already exists");
                });
    }
}
