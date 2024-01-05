package codeep.jean.controller;


import codeep.jean.controller.dtos.*;
import codeep.jean.domain.enums.Role;
import codeep.jean.exception.UserUpdateFailedException;
import codeep.jean.security.jwt.TokenProvider;
import codeep.jean.service.FamilyService;
import codeep.jean.service.UserService;
import codeep.jean.service.dto.UserLoginDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FamilyController {

    private final FamilyService familyService;
    @PostMapping(value = "/family/join")
    public ResponseEntity<FamilyJoinResponseDTO> familyJoin(@ModelAttribute FamilyJoinRequestDTO familyJoinRequestDTO) {
        log.info("familyName :{}", familyJoinRequestDTO.getFamilyName());
        FamilyJoinResponseDTO joinedFamily = familyService.joinFamily(familyJoinRequestDTO.getFamilyName(),familyJoinRequestDTO.getFamilyId(),familyJoinRequestDTO.getFamilyPassword());
        return ResponseEntity.ok().body(joinedFamily);
    }


}
