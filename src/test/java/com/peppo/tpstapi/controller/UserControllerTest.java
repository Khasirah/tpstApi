package com.peppo.tpstapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peppo.tpstapi.entity.User;
import com.peppo.tpstapi.model.*;
import com.peppo.tpstapi.model.request.RegisterUserRequest;
import com.peppo.tpstapi.model.request.UpdateSpecificUserRequest;
import com.peppo.tpstapi.model.request.UpdateUserRequest;
import com.peppo.tpstapi.model.response.UserResponse;
import com.peppo.tpstapi.model.response.WebResponse;
import com.peppo.tpstapi.repository.BagianRepository;
import com.peppo.tpstapi.repository.KelompokRepository;
import com.peppo.tpstapi.repository.SuratRepository;
import com.peppo.tpstapi.repository.UserRepository;
import com.peppo.tpstapi.security.BCrypt;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class UserControllerTest {

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
    private SuratRepository suratRepository;

    @BeforeEach
    void setUp() {
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

    private void createUserTest(String idUser, String namaUser) {
        idUser = idUser == null ? "222222222" : idUser;
        namaUser = namaUser == null ? "user2" : namaUser;

        User user = new User();
        user.setIdUser(idUser);
        user.setNamaUser(namaUser);
        user.setPassword(BCrypt.hashpw("user123456", BCrypt.gensalt()));
        user.setBagian(bagianRepository.findById(JenisBidang.umum.id).orElse(null));
        user.setKelompok(kelompokRepository.findById(JenisKelompok.user.id).orElse(null));
        user.setToken("userTest");
        user.setTokenExpiredAt(System.currentTimeMillis() + (1000 * 60 * 24));
        userRepository.save(user);
    }

    private void createUserTestNoToken(String idUser, String namaUser) {
        idUser = idUser == null ? "222222222" : idUser;
        namaUser = namaUser == null ? "user2" : namaUser;

        User user = new User();
        user.setIdUser(idUser);
        user.setNamaUser(namaUser);
        user.setPassword(BCrypt.hashpw("user123456", BCrypt.gensalt()));
        user.setBagian(bagianRepository.findById(JenisBidang.umum.id).orElse(null));
        user.setKelompok(kelompokRepository.findById(JenisKelompok.user.id).orElse(null));
        userRepository.save(user);
    }

    @Test
    void testGenerateAdminSuccess() throws Exception {
        mockMvc.perform(
                get("/api/generateAdmin")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNull(response.getErrors());
            assertNotNull(response.getData());
            System.out.println(response.getData());
        });
    }

    @Test
    void testGenerateAdminFailedDuplicate() throws Exception {
        mockMvc.perform(
                get("/api/generateAdmin")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNull(response.getErrors());
            assertNotNull(response.getData());
            System.out.println(response.getData());
        });

        mockMvc.perform(
                get("/api/generateAdmin")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNotNull(response.getErrors());
            System.out.println(response.getErrors());
        });
    }

    @Test
    void testRegisterSuccess() throws Exception {
        createAdminTest();

        RegisterUserRequest request = new RegisterUserRequest();
        request.setIdUser("111111112");
        request.setNamaUser("user2");
        request.setPassword("user23456789");
        request.setIdBagian(2);
        request.setIdKelompok(2);

        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertEquals(request.getNamaUser() + " berhasil didaftarkan", response.getData());
        });
    }

    @Test
    void testRegisterBadRequest() throws Exception {
        createAdminTest();

        RegisterUserRequest request = new RegisterUserRequest();
        request.setIdUser("");
        request.setNamaUser("");
        request.setPassword("");

        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testRegisterDuplicate() throws Exception {
        createAdminTest();

        User user = new User();
        user.setIdUser("111111112");
        user.setNamaUser("admin");
        user.setPassword(BCrypt.hashpw("admin123456", BCrypt.gensalt()));
        user.setBagian(bagianRepository.findById(1).get());
        user.setKelompok(kelompokRepository.findById(1).get());
        userRepository.save(user);

        RegisterUserRequest request = new RegisterUserRequest();
        request.setIdUser("111111112");
        request.setNamaUser("admin");
        request.setPassword("admin123456");
        request.setIdBagian(1);
        request.setIdKelompok(1);

        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testRegisterUnauthorized() throws Exception {
        User user = new User();
        user.setIdUser("111111111");
        user.setNamaUser("admin");
        user.setPassword(BCrypt.hashpw("admin123456", BCrypt.gensalt()));
        user.setBagian(bagianRepository.findById(JenisBidang.umum.id).get());
        user.setKelompok(kelompokRepository.findById(JenisKelompok.user.id).get());
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + (1000 * 60 * 24));
        userRepository.save(user);

        RegisterUserRequest request = new RegisterUserRequest();
        request.setIdUser("111111112");
        request.setNamaUser("admin");
        request.setPassword("admin123456");
        request.setIdBagian(2);
        request.setIdKelompok(2);

        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetUserUnauthorized() throws Exception {

        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "notfound")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetUserUnauthorizedTokenNotSend() throws Exception {

        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetUserSuccess() throws Exception {
        User user = new User();
        user.setIdUser("111111111");
        user.setNamaUser("admin");
        user.setPassword(BCrypt.hashpw("admin123456", BCrypt.gensalt()));
        user.setBagian(bagianRepository.findById(1).get());
        user.setKelompok(kelompokRepository.findById(1).get());
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + (1000 * 60 * 24));
        userRepository.save(user);

        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNull(response.getErrors());
            assertNotNull(response.getData().getKelompok().getNamaKelompok());
        });
    }

    @Test
    void testGetUserTokenExpired() throws Exception {
        User user = new User();
        user.setIdUser("111111111");
        user.setNamaUser("admin");
        user.setPassword(BCrypt.hashpw("admin123456", BCrypt.gensalt()));
        user.setBagian(bagianRepository.findById(1).get());
        user.setKelompok(kelompokRepository.findById(1).get());
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() - (1000 * 60 * 24));
        userRepository.save(user);

        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testHandleCsvUploadNotLogin() throws Exception {
        mockMvc.perform(
                multipart("/api/users/upload")
                        .file(new MockMultipartFile(
                                "csvFile",
                                "users.csv",
                                "text/csv",
                                getClass().getResourceAsStream("/csvData/users.csv")))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testHandleCsvUploadNotAdmin() throws Exception {
        User user = new User();
        user.setIdUser("111111111");
        user.setNamaUser("admin");
        user.setPassword(BCrypt.hashpw("admin123456", BCrypt.gensalt()));
        user.setBagian(bagianRepository.findById(JenisBidang.umum.id).get());
        user.setKelompok(kelompokRepository.findById(JenisKelompok.user.id).get());
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + (1000 * 60 * 24));
        userRepository.save(user);

        mockMvc.perform(
                multipart("/api/users/upload")
                        .file(new MockMultipartFile(
                                "csvFile",
                                "users.csv",
                                "text/csv",
                                getClass().getResourceAsStream("/csvData/users.csv")))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testHandleCsvUploadNotCsv() throws Exception {
        createAdminTest();

        mockMvc.perform(
                multipart("/api/users/upload")
                        .file(new MockMultipartFile(
                                "csvFile",
                                "cv.pdf",
                                "application/pdf",
                                getClass().getResourceAsStream("/csvData/cv.pdf")))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNotNull(response.getErrors());
            log.info(response.getErrors());
        });
    }

    @Test
    void testHandleCsvUploadFailedColumnIdBagianNotMatch() throws Exception {
        createAdminTest();

        mockMvc.perform(
                multipart("/api/users/upload")
                        .file(new MockMultipartFile(
                                "csvFile",
                                "usersError.csv",
                                "text/csv",
                                getClass().getResourceAsStream("/csvData/usersError.csv")))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testHandleCsvUploadFailedColumnIdUserNotMatch() throws Exception {
        createAdminTest();

        mockMvc.perform(
                multipart("/api/users/upload")
                        .file(new MockMultipartFile(
                                "csvFile",
                                "usersErrorIdUser.csv",
                                "text/csv",
                                getClass().getResourceAsStream("/csvData/usersErrorIdUser.csv")))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testHandleCsvUploadSuccess() throws Exception {
        createAdminTest();

        mockMvc.perform(
                multipart("/api/users/upload")
                        .file(new MockMultipartFile(
                                "csvFile",
                                "users.csv",
                                "text/csv",
                                getClass().getResourceAsStream("/csvData/users.csv")))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNull(response.getErrors());
            assertNotNull(response.getData());
        });
    }

    @Test
    void testGetSpecificUserFailedNotLogin() throws Exception {
        mockMvc.perform(
                get("/api/users/222222222")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetSpecificUserFailedNotAdmin() throws Exception {
        createUserTest(null, null);

        mockMvc.perform(
                get("/api/users/222222222")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetSpecificUserFailedNotFound() throws Exception {
        createAdminTest();

        mockMvc.perform(
                get("/api/users/222222222")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetSpecificUserSuccess() throws Exception {
        createAdminTest();
        createUserTest(null, null);

        mockMvc.perform(
                get("/api/users/222222222")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNull(response.getErrors());
            assertNotNull(response.getData());
            assertEquals("222222222", response.getData().getIdUser());
            assertEquals("user2", response.getData().getNamaUser());
        });
    }

    @Test
    void testUpdateCurrentUserFailedNotLogin() throws Exception {

        UpdateUserRequest request = new UpdateUserRequest();
        request.setNamaUser("user10");
        request.setPassword("user10");
        request.setIdBagian(JenisBidang.dp3.id);
        request.setIdKelompok(JenisKelompok.user.id);

        mockMvc.perform(
                patch("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateCurrentUserFailedBidangNotFound() throws Exception {
        createUserTest(null, null);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setNamaUser("user10");
        request.setPassword("user10");
        request.setIdBagian(10);
        request.setIdKelompok(JenisKelompok.user.id);

        mockMvc.perform(
                patch("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "userTest")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateCurrentUserFailedKelompokNotFound() throws Exception {
        createUserTest(null, null);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setNamaUser("user10");
        request.setPassword("user10");
        request.setIdBagian(JenisBidang.dp3.id);
        request.setIdKelompok(10);

        mockMvc.perform(
                patch("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "userTest")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateCurrentUserSuccess() throws Exception {
        createUserTest(null, null);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setNamaUser("user10");
        request.setPassword("user10");
        request.setIdBagian(JenisBidang.dp3.id);

        mockMvc.perform(
                patch("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "userTest")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertNull(response.getErrors());
            assertNotNull(response.getData());
            assertEquals("user10", response.getData().getNamaUser());
            assertEquals(JenisBidang.dp3.id, response.getData().getBagian().getId());

            User userDb = userRepository.findById("222222222").orElse(null);
            assertNotNull(userDb);
            assertTrue(BCrypt.checkpw("user10", userDb.getPassword()));
        });
    }

    @Test
    void testUpdateSpecificUserFailedNotLogin() throws Exception {

        UpdateSpecificUserRequest request = new UpdateSpecificUserRequest();
        request.setIdUser("222222222");
        request.setNamaUser("user10");
        request.setPassword("user10");
        request.setIdBagian(JenisBidang.ppip.id);

        mockMvc.perform(
                patch("/api/users/222222222")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {}
            );

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateSpecificUserFailedNotAdmin() throws Exception {
        createUserTest(null, null);

        UpdateSpecificUserRequest request = new UpdateSpecificUserRequest();
        request.setIdUser("222222222");
        request.setNamaUser("user10");
        request.setPassword("user10");
        request.setIdBagian(JenisBidang.ppip.id);

        mockMvc.perform(
                patch("/api/users/222222222")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "userTest")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {}
            );

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateSpecificUserFailedUserNotFound() throws Exception {
        createAdminTest();
        createUserTest(null, null);

        UpdateSpecificUserRequest request = new UpdateSpecificUserRequest();
        request.setIdUser("222222223");
        request.setNamaUser("user10");
        request.setPassword("user10");
        request.setIdBagian(JenisBidang.ppip.id);

        mockMvc.perform(
                patch("/api/users/222222223")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {}
            );

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateSpecificUserFailedBagianNotFound() throws Exception {
        createAdminTest();
        createUserTest(null, null);

        UpdateSpecificUserRequest request = new UpdateSpecificUserRequest();
        request.setIdUser("222222222");
        request.setNamaUser("user10");
        request.setPassword("user10");
        request.setIdBagian(10);

        mockMvc.perform(
                patch("/api/users/222222222")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {}
            );

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateSpecificUserFailedKelompokNotFound() throws Exception {
        createAdminTest();
        createUserTest(null, null);

        UpdateSpecificUserRequest request = new UpdateSpecificUserRequest();
        request.setIdUser("222222222");
        request.setNamaUser("user10");
        request.setPassword("user10");
        request.setIdBagian(JenisBidang.ppip.id);
        request.setIdKelompok(10);

        mockMvc.perform(
                patch("/api/users/222222222")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {}
            );

            assertNotNull(response.getErrors());
            log.info(response.getErrors());
        });
    }

    @Test
    void testUpdateSpecificUserSuccess() throws Exception {
        createAdminTest();
        createUserTest(null, null);

        UpdateSpecificUserRequest request = new UpdateSpecificUserRequest();
        request.setPassword("user10");
        request.setIdBagian(JenisBidang.ppip.id);

        mockMvc.perform(
                patch("/api/users/222222222")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {}
            );

            assertNull(response.getErrors());
            assertNotNull(response.getData());
            assertEquals("user2", response.getData().getNamaUser());
            assertEquals(JenisBidang.ppip.id, response.getData().getBagian().getId());

            User userDb = userRepository.findById("222222222").orElse(null);
            assertNotNull(userDb);
            assertTrue(BCrypt.checkpw("user10", userDb.getPassword()));
            log.info(response.getData().toString());
        });
    }

    @Test
    void testDeleteUserFailedNotLogin() throws Exception {

        mockMvc.perform(
                delete("/api/users/222222222")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {}
            );

            assertNotNull(response.getErrors());
            log.info(response.getErrors());
        });
    }

    @Test
    void testDeleteUserFailedNotAdmin() throws Exception {
        createUserTest(null, null);

        mockMvc.perform(
                delete("/api/users/222222222")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "userTest")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {}
            );

            assertNotNull(response.getErrors());
            log.info(response.getErrors());
        });
    }

    @Test
    void testDeleteUserFailedUserNotFound() throws Exception {
        createAdminTest();

        mockMvc.perform(
                delete("/api/users/222222222")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {}
            );

            assertNotNull(response.getErrors());
            log.info(response.getErrors());
        });
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        createAdminTest();
        createUserTest(null, null);

        mockMvc.perform(
                delete("/api/users/222222222")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {}
            );

            assertNull(response.getErrors());
            assertNotNull(response.getData());
            log.info(response.getData());
        });
    }

    @Test
    void testSearchFailedNotLogin() throws Exception {

        for (int i = 0; i < 100; i++) {
            String idUser = Integer.toString(111_111_113 + i);
            createUserTestNoToken(idUser, "user 1"+i);
        }

        mockMvc.perform(
                get("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {}
            );

            assertNotNull(response.getErrors());
            log.info(response.getErrors());
        });
    }

    @Test
    void testSearchFailedNotAdmin() throws Exception {
        createUserTest(null, null);

        for (int i = 0; i < 100; i++) {
            String idUser = Integer.toString(111_111_115 + i);
            createUserTestNoToken(idUser, "user 1"+i);
        }

        mockMvc.perform(
                get("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "userTest")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {}
            );

            assertNotNull(response.getErrors());
            log.info(response.getErrors());
        });
    }

    @Test
    void testSearchSuccessNotFound() throws Exception {
        createAdminTest();

        for (int i = 0; i < 100; i++) {
            String idUser = Integer.toString(111_111_115 + i);
            createUserTestNoToken(idUser, "user 1"+i);
        }

        mockMvc.perform(
                get("/api/users")
                        .param("idUser", "12300000000000")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<UserResponse>> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {}
            );

            assertNull(response.getErrors());
            assertEquals(0, response.getData().size());
            assertEquals(0, response.getPaging().getTotalPage());
            assertEquals(0, response.getPaging().getCurrentPage());
            assertEquals(20, response.getPaging().getSize());
        });
    }

    @Test
    void testSearchSuccessEmptyParam() throws Exception {
        createAdminTest();

        for (int i = 0; i < 100; i++) {
            String idUser = Integer.toString(111_111_113 + i);
            createUserTestNoToken(idUser, "user 1"+i);
        }

        mockMvc.perform(
                get("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<UserResponse>> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {}
            );

            assertNull(response.getErrors());
            assertNotNull(response.getData());
            assertEquals(20, response.getData().size());
            log.info(response.getData().toString());
        });
    }

    @Test
    void testSearchSuccessWithParam() throws Exception {
        createAdminTest();

        for (int i = 0; i < 100; i++) {
            String idUser = Integer.toString(111_111_113 + i);
            createUserTestNoToken(idUser, "user 1"+i);
        }

        mockMvc.perform(
                get("/api/users")
                        .param("namaUser", "user")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<UserResponse>> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {}
            );

            assertNull(response.getErrors());
            assertEquals(20, response.getData().size());
            assertEquals(5, response.getPaging().getTotalPage());
            assertEquals(0, response.getPaging().getCurrentPage());
            assertEquals(20, response.getPaging().getSize());
        });
    }

    @Test
    void testSearchSuccessWithIdUserParam() throws Exception {
        createAdminTest();

        for (int i = 0; i < 100; i++) {
            String idUser = Integer.toString(111_111_113 + i);
            createUserTestNoToken(idUser, "user 1"+i);
        }

        mockMvc.perform(
                get("/api/users")
                        .param("idUser", "123")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<UserResponse>> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {}
            );

            assertNull(response.getErrors());
            assertEquals(1, response.getData().size());
            assertEquals(1, response.getPaging().getTotalPage());
            assertEquals(0, response.getPaging().getCurrentPage());
            assertEquals(20, response.getPaging().getSize());
        });
    }
}