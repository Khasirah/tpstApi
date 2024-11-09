package com.peppo.tpstapi.controller;

import com.peppo.tpstapi.entity.User;
import com.peppo.tpstapi.model.request.LoginUserRequest;
import com.peppo.tpstapi.model.response.TokenResponse;
import com.peppo.tpstapi.model.response.WebResponse;
import com.peppo.tpstapi.service.auth.AuthServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthServiceImp authServiceImp;

    @PostMapping(
            path = "/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TokenResponse> login(@RequestBody LoginUserRequest request) {
        TokenResponse tokenResponse = authServiceImp.login(request);
        return WebResponse.<TokenResponse>builder()
                .data(tokenResponse)
                .build();
    }

    @DeleteMapping(
            path = "/api/auth/logout",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> logout(User user) {
        authServiceImp.logout(user);
        return WebResponse.<String>builder()
                .data("berhasil keluar")
                .build();
    }
}
