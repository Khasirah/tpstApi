package com.peppo.tpstapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peppo.tpstapi.entity.Bagian;
import com.peppo.tpstapi.entity.User;
import com.peppo.tpstapi.model.JenisBidang;
import com.peppo.tpstapi.model.JenisKelompok;
import com.peppo.tpstapi.model.response.WebResponse;
import com.peppo.tpstapi.repository.*;
import com.peppo.tpstapi.security.BCrypt;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class BagianControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BagianRepository bagianRepository;

    @Autowired
    private KelompokRepository kelompokRepository;

    @Autowired
    private SuratRepository suratRepository;

    @Autowired
    private DetailSuratRepository detailSuratRepository;

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

    @Test
    void testGetAllBagianFailedNotLogin() throws Exception{

        mockMvc.perform(
            get("/api/bagian")
        ).andExpectAll(
            status().isUnauthorized()
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
    void testGetAllBagianSuccessWithAdmin() throws Exception {

        createAdminTest();
        mockMvc.perform(
            get("/api/bagian")
                .header(
                    "X-API-TOKEN", "test"
                )
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<List<Bagian>> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
            );

            assertNull(response.getErrors());
            assertNotNull(response.getData());

            Bagian bagianUmum = bagianRepository.findById(2).orElseThrow();
            assertEquals(bagianUmum.getNamaBagian(), response.getData().getFirst().getNamaBagian());
            log.info(response.getData().getFirst().getNamaBagian());
        });
    }

    @Test
    void testGetAllBagianSuccessWithUser() throws Exception {

        createUserTest(null, null);
        mockMvc.perform(
            get("/api/bagian")
                .header(
                    "X-API-TOKEN", "userTest"
                )
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<List<Bagian>> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
            );

            assertNull(response.getErrors());
            assertNotNull(response.getData());

            Bagian bagianUmum = bagianRepository.findById(2).orElseThrow();
            assertEquals(bagianUmum.getNamaBagian(), response.getData().getFirst().getNamaBagian());
            log.info(response.getData().getFirst().getNamaBagian());
        });
    }
}