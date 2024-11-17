package com.peppo.tpstapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peppo.tpstapi.entity.Pengirim;
import com.peppo.tpstapi.entity.Surat;
import com.peppo.tpstapi.entity.User;
import com.peppo.tpstapi.model.JenisBidang;
import com.peppo.tpstapi.model.JenisKelompok;
import com.peppo.tpstapi.model.response.BagianAndEkspedisiResponse;
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
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class InputSuratControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SuratRepository suratRepository;

    @Autowired
    private BagianRepository bagianRepository;

    @Autowired
    private KelompokRepository kelompokRepository;

    @Autowired
    private PengirimRepository pengirimRepository;

    @BeforeEach
    void setUp() {
        suratRepository.deleteAll();
        pengirimRepository.deleteAll();
        userRepository.deleteAll();

        createUserTest(null, null);
        createUserTestNoToken(null, null);
        createPengirimTest();
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
        user.setToken("user2Test");
        user.setTokenExpiredAt(System.currentTimeMillis() + (1000 * 60 * 24));
        userRepository.save(user);
    }

    private void createUserTestNoToken(String idUser, String namaUser) {
        idUser = idUser == null ? "333333333" : idUser;
        namaUser = namaUser == null ? "user3" : namaUser;

        User user = new User();
        user.setIdUser(idUser);
        user.setNamaUser(namaUser);
        user.setPassword(BCrypt.hashpw("user123456", BCrypt.gensalt()));
        user.setBagian(bagianRepository.findById(JenisBidang.umum.id).orElse(null));
        user.setKelompok(kelompokRepository.findById(JenisKelompok.user.id).orElse(null));
        userRepository.save(user);
    }

    private void createPengirimTest() {
        Pengirim pengirim = new Pengirim();
        pengirim.setNamaPengirim("kpp a");
        pengirimRepository.save(pengirim);
    }

    @Test
    void testGetBagianAndEkspedisiFailedNotLogin() throws Exception {
        mockMvc.perform(
            get("/api/inputSurat/bagianAndEkspedisi")
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
            log.info(response.getErrors());
        });
    }

    @Test
    void testGetBagianAndEkspedisiSuccess() throws Exception {
        mockMvc.perform(
            get("/api/inputSurat/bagianAndEkspedisi")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "user2Test")
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<BagianAndEkspedisiResponse> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
            );

            assertNull(response.getErrors());
            assertNotNull(response.getData());
            assertEquals("Langsung", response.getData().getEkspedisiList().getFirst().getNamaEkspedisi());
            log.info(response.getData().toString());
        });
    }
}