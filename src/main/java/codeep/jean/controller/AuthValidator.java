
package codeep.jean.controller;

import codeep.jean.domain.User;
import codeep.jean.domain.enums.Role;
import codeep.jean.exception.AuthException;
import codeep.jean.exception.ErrorCode;
import codeep.jean.repository.UserRepository;

import codeep.jean.security.auth.PrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthValidator {

    private final PrincipalDetailsService principalDetailsService;
    private final UserRepository userRepository;
    public void validateUpdatingUser(Long userId) throws AuthException {
        //수정 권한자로 등록된 유저 확인
        User registeredUser = Optional.of(userRepository.findById(userId).get())
                .orElseThrow(() ->
                        new AuthException(ErrorCode.USER_NOT_FOUND, "no existing user"));
        //업데이트 대상 계정과 로그인된 계정 일치 여부 확인
        if (!getUserDetails().getUsername().equals(registeredUser.getUserEmail())) {
            //관리자에 의한 직권 수정 허용(ADMIN)
            String nowUserAuthority = getUserAuthority(getUserDetails());
            if (nowUserAuthority.equals(String.valueOf(Role.ROLE_ADMIN))) {
                log.info("now updating with authority of administrator({}): {}", nowUserAuthority, getUserDetails().getUsername());
            } else {
                throw new AuthException(ErrorCode.INVALID_AUTHORITY, "no authority");
            }
        }
    }


    private UserDetails getUserDetails() throws AuthException{
        if(SecurityContextHolder.getContext().getAuthentication()==null) {
            throw new AuthException(ErrorCode.NO_AUTHORIZED_USER,"no userdetails in securityContext");
        }
        return principalDetailsService.loadUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
    }

    private String getUserAuthority(UserDetails userDetails) throws AuthException{
        List<GrantedAuthority> authList = (List<GrantedAuthority>) userDetails.getAuthorities();
        if(authList.size()==0){
            throw new AuthException(ErrorCode.NO_AUTHORITY,"no granted authorities for user");
        }
        return authList.get(0).getAuthority();
    }

}
