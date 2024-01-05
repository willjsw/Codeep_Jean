package codeep.jean.service;

import codeep.jean.controller.dtos.FamilyJoinResponseDTO;
import codeep.jean.domain.Family;
import codeep.jean.domain.User;
import codeep.jean.domain.UserFamily;
import codeep.jean.exception.AuthException;
import codeep.jean.exception.ErrorCode;
import codeep.jean.repository.FamilyRepository;
import codeep.jean.repository.UserFamilyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FamilyService {
    private final UserFamilyRepository userFamilyRepository;

    private final FamilyRepository familyRepository;

    //Family Join
    public FamilyJoinResponseDTO joinFamily(String familyName,String familyId, String familyPassword){
        validateFamilyNameDuplication(familyName);
        Family joinedFamily = familyRepository.save(new Family(familyName,familyId, familyPassword));
        return new FamilyJoinResponseDTO(joinedFamily.getId(),joinedFamily.getFamilyName());
    }

    public void addMemberToFamily(User user, Family family){
        userFamilyRepository.save(new UserFamily(user, family));
    }

    public void deleteMemberFromFamily(User user, Family family){
    }

    public List<User> getFamilyMembers(Family family){
        List<UserFamily> userFamilies = userFamilyRepository.findAllByFamilyId(family);
        return userFamilies.stream().map(userFamily -> userFamily.getUserId())
                .collect(Collectors.toList());
    }

    private void validateFamilyNameDuplication(String familyName) throws AuthException{
        Optional.ofNullable(familyRepository.findByFamilyName(familyName))
                .ifPresent(family->{
                    throw new AuthException(ErrorCode.FAMILY_NAME_DUPLICATED, familyName+" is already exists");
                });
    }
}
