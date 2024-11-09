package com.peppo.tpstapi.service.auth;

import com.peppo.tpstapi.entity.User;
import com.peppo.tpstapi.model.request.LoginUserRequest;
import com.peppo.tpstapi.model.response.TokenResponse;
import com.peppo.tpstapi.repository.UserRepository;
import com.peppo.tpstapi.security.BCrypt;
import com.peppo.tpstapi.service.validation.ValidationServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthServiceImp implements IAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationServiceImp validationServiceImp;

    @Override
    @Transactional
    public TokenResponse login(LoginUserRequest request) {
        validationServiceImp.validate(request);

        User user = userRepository.findById(request.getIdUser())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username atau password salah"));

        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(next1Day());
            userRepository.save(user);

            return TokenResponse.builder()
                    .token(user.getToken())
                    .expiredAt(user.getTokenExpiredAt())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username atau password salah");
        }

    }

    private Long next1Day() {
        return System.currentTimeMillis() + (1000 * 60 * 24);
    }

    @Override
    @Transactional
    public void logout(User user) {
        user.setToken(null);
        user.setTokenExpiredAt(null);

        userRepository.save(user);
    }
}
