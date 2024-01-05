package codeep.jean.controller;


import codeep.jean.controller.dtos.*;
import codeep.jean.domain.enums.Role;
import codeep.jean.exception.UserUpdateFailedException;
import codeep.jean.repository.FamilyRepository;
import codeep.jean.security.jwt.TokenProvider;
import codeep.jean.service.UserService;
import codeep.jean.service.dto.UserLoginDTO;
import jakarta.servlet.http.HttpServletRequest;
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
public class UserController {
    private final FamilyRepository familyRepository;

    private final UserService userService;
    private final AuthValidator authValidator;
    private static final Role role = Role.valueOf("ROLE_USER");


    @PostMapping(value = "/user/join")
    public ResponseEntity<UserJoinResponseDTO> join(@ModelAttribute UserJoinRequestDTO userJoinRequestDTO) {
        log.info("username : {}, password : {}", userJoinRequestDTO.getUserEmail(), userJoinRequestDTO.getPassword());
        UserJoinResponseDTO joinedUser = userService.joinUser(
                userJoinRequestDTO.getUserEmail(),
                userJoinRequestDTO.getPassword(),
                role,
                userJoinRequestDTO.getName(),
                userJoinRequestDTO.getContact(),
                userJoinRequestDTO.getFamilyId());
        return ResponseEntity.ok().body(joinedUser);
    }

    //login
    @PostMapping(value = "/user/login2")
    public ResponseEntity<UserLoginResponseDTO> login(@ModelAttribute UserLoginRequestDTO userLoginRequestDTO) {
        log.info("username : {}, password : {}", userLoginRequestDTO.getUserEmail(), userLoginRequestDTO.getPassword());
        UserLoginDTO userLoginDTO = userService.loginUser(userLoginRequestDTO.getUserEmail(), userLoginRequestDTO.getPassword());
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION,"Bearer " + userLoginDTO.getAccessToken());
        headers.set("Refresh-Token","Bearer " + userLoginDTO.getRefreshToken());
        return new ResponseEntity<>(
                new UserLoginResponseDTO(userLoginDTO.getId(), userLoginDTO.getUserEmail(), userLoginDTO.getRole()), headers, HttpStatus.valueOf(200));
    }


    //update
    @PatchMapping(value = "/user/{userId}")
    public ResponseEntity<UserUpdateResponseDTO> update(HttpServletRequest request, @PathVariable Long userId, @ModelAttribute UserUpdateRequestDTO userUpdateRequestDTO){
        authValidator.validateUpdatingUser(userId);
        return userService.updateUser(userId, userUpdateRequestDTO.getPassword(),
                        userUpdateRequestDTO.getName(),
                        userUpdateRequestDTO.getContact()
                        ,request.getHeader(AUTHORIZATION)
                      )
                .map(updatedUser -> new UserUpdateResponseDTO(userId, updatedUser.getUserEmail()))
                .map(ResponseEntity::ok)
                .orElseThrow(UserUpdateFailedException::new);
    }

    //logout
    @PostMapping("/user/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userService.logoutUser(
                TokenProvider.getAuthentication(userDetails).getName(),
                request.getHeader(AUTHORIZATION)
        );
        String msg = username+ " logged out successfully";
        log.info(msg);
        return ResponseEntity.ok(msg);
    }

}
