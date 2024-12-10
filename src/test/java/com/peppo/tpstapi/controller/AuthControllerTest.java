package com.peppo.tpstapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peppo.tpstapi.entity.User;
import com.peppo.tpstapi.model.*;
import com.peppo.tpstapi.model.request.LoginUserRequest;
import com.peppo.tpstapi.model.response.TokenResponse;
import com.peppo.tpstapi.model.response.WebResponse;
import com.peppo.tpstapi.repository.*;
import com.peppo.tpstapi.security.BCrypt;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BagianRepository bagianRepository;

    @Autowired
    private KelompokRepository kelompokRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DetailSuratRepository detailSuratRepository;

    @Autowired
    private SuratRepository suratRepository;

    @BeforeEach
    void setUp() {
        detailSuratRepository.deleteAll();
        suratRepository.deleteAll();
        userRepository.deleteAll();
    }

    private void createAdminTest() {
        User user = new User();
        user.setIdUser("111111111");
        user.setNamaUser("admin");
        user.setPassword(BCrypt.hashpw("admin123456", BCrypt.gensalt()));
        user.setBagian(bagianRepository.findById(JenisBidang.admin.id).orElse(null));
        user.setKelompok(kelompokRepository.findById(JenisKelompok.admin.id).orElse(null));
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + (1000 * 60 * 24));
        userRepository.save(user);
    }

    @Test
    void testLoginFailedUserNotFound() throws Exception {
        LoginUserRequest request = new LoginUserRequest();
        request.setIdUser("111111111");
        request.setPassword("admin123456");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper
                    .readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testLoginFailedWrongPassword() throws Exception {
        User user = new User();
        user.setIdUser("111111111");
        user.setNamaUser("admin");
        user.setPassword(BCrypt.hashpw("admin123456", BCrypt.gensalt()));
        user.setBagian(bagianRepository.findById(1).get());
        user.setKelompok(kelompokRepository.findById(1).get());
        userRepository.save(user);

        LoginUserRequest request = new LoginUserRequest();
        request.setIdUser("111111111");
        request.setPassword("salah");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper
                    .readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testLoginSuccess() throws Exception {
        User user = new User();
        user.setIdUser("111111111");
        user.setNamaUser("admin");
        user.setPassword(BCrypt.hashpw("admin123456", BCrypt.gensalt()));
        user.setBagian(bagianRepository.findById(1).get());
        user.setKelompok(kelompokRepository.findById(1).get());
        userRepository.save(user);

        LoginUserRequest request = new LoginUserRequest();
        request.setIdUser("111111111");
        request.setPassword("admin123456");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<TokenResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>(){}
            );
            assertNull(response.getErrors());
            assertNotNull(response.getData().getToken());
            assertNotNull(response.getData().getExpiredAt());

            User userDb = userRepository.findById("111111111").orElse(null);

            assertNotNull(userDb);
            assertEquals(response.getData().getToken(), userDb.getToken());
            assertEquals(response.getData().getExpiredAt(), userDb.getTokenExpiredAt());
        });
    }

    @Test
    void testLogoutFailedNotLogin() throws Exception {
        mockMvc.perform(
                delete("/api/auth/logout")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>(){}
            );

            assertNotNull(response.getErrors());
            log.info(response.getErrors());
        });
    }

    @Test
    void testLogoutSuccess() throws Exception {
        createAdminTest();

        mockMvc.perform(
                delete("/api/auth/logout")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>(){}
            );

            assertNull(response.getErrors());
            assertNotNull(response.getData());
            log.info(response.getData());
        });
    }
}