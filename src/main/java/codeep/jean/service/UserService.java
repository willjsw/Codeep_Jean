package codeep.jean.service;

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

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RedisUtil redisUtil;
    private final TimeTableService timeTableService;

    private BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }
    //User Join
    public UserJoinResponseDTO joinUser(String userEmail, String password, Role role, String name, String contact){
        validateUsernameDuplication(userEmail);
        User joinedUser = userRepository.save(new User(userEmail, encodePwd().encode(password),role,name,contact));
        joinedUser.initializeTimeTable(timeTableService.createTimeTable(joinedUser));

        return new UserJoinResponseDTO(joinedUser.getId(), joinedUser.getUserEmail());
    }


    //User Login
    public UserLoginDTO loginUser(String userEmail, String password){

        //기본 username, password 인증
        //1. 일치하는 username 없음
        User user = Optional.ofNullable(userRepository.findByUserEmail(userEmail))
                .orElseThrow(()->
                        new AuthException(ErrorCode.USER_NOT_FOUND, "invalid user account"));
        //2. password 불일치
        if(!bCryptPasswordEncoder.matches(password,user.getPassword())){
            throw new AuthException(ErrorCode.USER_NOT_FOUND, "invalid user account");
        }
        //토큰 발급
        JwtDTO tokens = TokenProvider.createTokens(user.getId(),userEmail,user.getRole());
        String refreshToken = tokens.getRefreshToken();
        String key = "RT:" + userEmail;
        //Redis 저장된 refreshToken 확인 -> 업데이트
        if(redisUtil.hasKeyRefreshToken(key)) {
            redisUtil.deleteRefreshToken(key);
        }redisUtil.setRefreshToken(key, refreshToken, TokenProvider.getExpiration(refreshToken), TimeUnit.MILLISECONDS);
        //정상 로그인 완료 -> JWT 반환
        return new UserLoginDTO(user.getId(), user.getUserEmail(), user.getRole(), tokens.getAccessToken(), tokens.getRefreshToken());
    }

    //User Update
    public Optional<User> updateUser(Long userId, String password, String name, String contact, String accessToken){
        //update 진행
        String username = userRepository.findById(userId).get().getUserEmail();
        String encPwd = bCryptPasswordEncoder.encode(password);

        return userRepository.findById(userId)
                .map(baseUser -> {
                    baseUser.update(encPwd, name, contact);
                    invalidateUserAccessToken(username, accessToken);
                    log.info("user updated -> userId : {}, username : {}", userId, username);
                    return baseUser;
                });
    }

    //User Logout
    public String logoutUser(String username,String accessToken) {
        invalidateUserAccessToken(username, accessToken);
        return username;
    }

    private void invalidateUserAccessToken(String username, String accessToken) {
        //Redis 에 저장된 refresh token 있을 경우 삭제
        if (redisUtil.hasKeyRefreshToken("RT:" + username)) {
            redisUtil.deleteRefreshToken("RT:" + username);
        }
        //해당 AccessToken 유효시간 반영해서 BlackList 저장
        String bannedToken = TokenProvider.resolveToken(accessToken);
        redisUtil.setBlackList(bannedToken, "AT:" + username, TokenProvider.getExpiration(bannedToken) + (60 * 10 * 1000), TimeUnit.MILLISECONDS);
    }

    private void validateUsernameDuplication(String userEmail) throws AuthException{
        Optional.ofNullable(userRepository.findByUserEmail(userEmail))
                .ifPresent(user->{
                    throw new AuthException(ErrorCode.USERNAME_DUPLICATED, userEmail+" is already exists");
                });
    }

}
