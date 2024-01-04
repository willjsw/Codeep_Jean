package codeep.jean.service;

import codeep.jean.controller.dtos.FamilyJoinResponseDTO;
import codeep.jean.controller.dtos.UserJoinResponseDTO;
import codeep.jean.domain.Family;
import codeep.jean.domain.User;
import codeep.jean.domain.enums.Role;
import codeep.jean.exception.AuthException;
import codeep.jean.exception.ErrorCode;
import codeep.jean.repository.FamilyRepository;
import codeep.jean.repository.UserRepository;
import codeep.jean.security.jwt.TokenProvider;
import codeep.jean.security.jwt.dto.JwtDTO;
import codeep.jean.security.redis.RedisUtil;
import codeep.jean.service.dto.UserLoginDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FamilyService {
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RedisUtil redisUtil;


    //User Join
    public FamilyJoinResponseDTO joinFamily(String familyName){
        validateFamilyNameDuplication(familyName);
        Family joinedFamily = familyRepository.save(new Family(familyName,new ArrayList<>()));
        return new FamilyJoinResponseDTO(joinedFamily.getId(),joinedFamily.getFamilyName());
    }

    private void validateFamilyNameDuplication(String familyName) throws AuthException{
        Optional.ofNullable(familyRepository.findByFamilyName(familyName))
                .ifPresent(family->{
                    throw new AuthException(ErrorCode.FAMILY_NAME_DUPLICATED, familyName+" is already exists");
                });
    }
}
