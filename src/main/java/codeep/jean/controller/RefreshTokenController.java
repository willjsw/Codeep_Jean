package codeep.jean.controller;


import codeep.jean.domain.enums.Role;
import codeep.jean.security.auth.PrincipalDetails;
import codeep.jean.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @GetMapping("/refresh")
    public ResponseEntity<Void> refresh(HttpServletRequest request, @AuthenticationPrincipal PrincipalDetails userDetails) {
        String newAccessToken = refreshTokenService.refreshAccessToken(request,userDetails.getUserId(),userDetails.getUsername(), Role.valueOf(userDetails.getAuthorities().get(0).getAuthority()));
        return ResponseEntity.noContent()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken)
                .build();
    }
}
