package com.peppo.tpstapi.service.auth;

import com.peppo.tpstapi.entity.User;
import com.peppo.tpstapi.model.request.LoginUserRequest;
import com.peppo.tpstapi.model.response.TokenResponse;

public interface IAuthService {

    TokenResponse login(LoginUserRequest request);

    void logout(User user);
}
