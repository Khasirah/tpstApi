package com.peppo.tpstapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peppo.tpstapi.entity.DetailSurat;
import com.peppo.tpstapi.entity.Pengirim;
import com.peppo.tpstapi.entity.Surat;
import com.peppo.tpstapi.entity.User;
import com.peppo.tpstapi.model.JenisBidang;
import com.peppo.tpstapi.model.JenisKelompok;
import com.peppo.tpstapi.model.JenisKeterangan;
import com.peppo.tpstapi.model.PesanError;
import com.peppo.tpstapi.model.request.CreateSuratRequest;
import com.peppo.tpstapi.model.request.UpdateSuratRequest;
import com.peppo.tpstapi.model.response.ForListSuratResponse;
import com.peppo.tpstapi.model.response.SuratResponse;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class SuratControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BagianRepository bagianRepository;

    @Autowired
    private KelompokRepository kelompokRepository;

    @Autowired
    private SuratRepository suratRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PosisiSuratRepository posisiSuratRepository;

    @Autowired
    private EkspedisiRepository ekspedisiRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private PengirimRepository pengirimRepository;

    @Autowired
    private DetailSuratRepository detailSuratRepository;

    @BeforeEach
    void setUp() {
        detailSuratRepository.deleteAll();
        suratRepository.deleteAll();
        pengirimRepository.deleteAll();
        userRepository.deleteAll();

        createUserTest(null, null);
        createUserTestNoToken(null, null);
        createPengirimTest();
        createAdminTest();
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

    private void createSuratTest(Integer order) {
        Surat surat = new Surat();
        surat.setNomorSurat("S-98743"+order+"/BLA.08/2024");
        surat.setPengirim(pengirimRepository.findByNamaPengirim("kpp a").orElse(null));
        surat.setPerihal("penyampaian dokumen "+order);
        surat.setBagian(bagianRepository.findById(JenisBidang.umum.id).orElse(null));
        surat.setPosisiSurat(posisiSuratRepository.findById(1).orElse(null));
        surat.setEkspedisi(ekspedisiRepository.findById(1).orElse(null));
        surat.setPetugasTPST(userRepository.findById("222222222").orElse(null));
        surat.setStatus(statusRepository.findById(1).orElse(null));

        suratRepository.save(surat);
    }

    @Test
    void testCreateSuratFailedNotLogin() throws Exception {
        CreateSuratRequest request = CreateSuratRequest.builder()
            .nomorSurat("S-987434/BLA.08/2024")
            .namaPengirim("kantor a")
            .perihal("penyampaian dokumen b")
            .idEkspedisi(1)
            .idTujuanBagian(3)
            .build();

        mockMvc.perform(
            multipart("/api/surat")
                .file(new MockMultipartFile(
                    "pdfFile",
                    "cv.pdf",
                    "application/pdf",
                    getClass().getResourceAsStream("/pdfData/cv.pdf")
                ))
                .file(new MockMultipartFile(
                    "data",
                    null,
                    "application/json",
                    objectMapper.writeValueAsBytes(request)
                    )
                )
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
            log.info(response.getErrors());
        });
    }

    @Test
    void testCreateSuratFailedNotPdf() throws Exception {
        CreateSuratRequest request = CreateSuratRequest.builder()
            .nomorSurat("S-987434/BLA.08/2024")
            .namaPengirim("kantor a")
            .perihal("penyampaian dokumen b")
            .idEkspedisi(1)
            .idTujuanBagian(3)
            .build();

        mockMvc.perform(
            multipart("/api/surat")
                .file(new MockMultipartFile(
                    "pdfFile",
                    "users.csv",
                    "text/csv",
                    getClass().getResourceAsStream("/pdfData/users.csv")
                ))
                .file(new MockMultipartFile(
                    "data",
                    null,
                    "application/json",
                    objectMapper.writeValueAsBytes(request)
                ))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("X-API-TOKEN", "user2Test")
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
    void testCreateSuratFailedEkspedisiNotFound() throws Exception {
        CreateSuratRequest request = CreateSuratRequest.builder()
            .nomorSurat("S-987434/BLA.08/2024")
            .namaPengirim("kantor a")
            .perihal("penyampaian dokumen b")
            .idEkspedisi(100)
            .idTujuanBagian(3)
            .build();

        mockMvc.perform(
            multipart("/api/surat")
                .file(new MockMultipartFile(
                    "data",
                    null,
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(request)
                ))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("X-API-TOKEN", "user2Test")
        ).andExpectAll(
            status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
            );

            assertNotNull(response.getErrors());
            assertTrue(response.getErrors().contains(PesanError.ekspedisi.message));
            log.info(response.getErrors());
        });
    }

    @Test
    void testCreateSuratFailedBagianNotFound() throws Exception {
        CreateSuratRequest request = CreateSuratRequest.builder()
            .nomorSurat("S-987434/BLA.08/2024")
            .namaPengirim("kantor a")
            .perihal("penyampaian dokumen b")
            .idEkspedisi(1)
            .idTujuanBagian(100)
            .build();

        mockMvc.perform(
            multipart("/api/surat")
                .file(new MockMultipartFile(
                    "data",
                    null,
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(request)
                ))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("X-API-TOKEN", "user2Test")
        ).andExpectAll(
            status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
            );

            assertNotNull(response.getErrors());
            assertTrue(response.getErrors().contains(PesanError.bagian.message));
            log.info(response.getErrors());
        });
    }

    @Test
    void testCreateSuratFailedNomorSuratNotProvide() throws Exception {
        CreateSuratRequest request = CreateSuratRequest.builder()
            .namaPengirim("kantor a")
            .perihal("penyampaian dokumen b")
            .idEkspedisi(1)
            .idTujuanBagian(3)
            .build();

        mockMvc.perform(
            multipart("/api/surat")
                .file(new MockMultipartFile(
                    "data",
                    null,
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(request)
                ))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("X-API-TOKEN", "user2Test")
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
    void testCreateSuratFailedNamaPengirimNotProvide() throws Exception {
        CreateSuratRequest request = CreateSuratRequest.builder()
            .nomorSurat("S-987434/BLA.08/2024")
            .namaPengirim("")
            .perihal("penyampaian dokumen b")
            .idEkspedisi(1)
            .idTujuanBagian(3)
            .build();

        mockMvc.perform(
            multipart("/api/surat")
                .file(new MockMultipartFile(
                    "data",
                    null,
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(request)
                ))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("X-API-TOKEN", "user2Test")
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
    void testCreateSuratFailedPerihalNotProvide() throws Exception {
        CreateSuratRequest request = CreateSuratRequest.builder()
            .nomorSurat("S-987434/BLA.08/2024")
            .namaPengirim("kantor a")
            .perihal("")
            .idEkspedisi(1)
            .idTujuanBagian(3)
            .build();

        mockMvc.perform(
            multipart("/api/surat")
                .file(new MockMultipartFile(
                    "data",
                    null,
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(request)
                ))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("X-API-TOKEN", "user2Test")
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
    void testCreateSuratSuccess() throws Exception {
        CreateSuratRequest request = CreateSuratRequest.builder()
            .nomorSurat("S-987434/BLA.08/2024")
            .namaPengirim("kantor a")
            .perihal("penyampaian dokumen b")
            .idEkspedisi(1)
            .idTujuanBagian(3)
            .build();

        mockMvc.perform(
            multipart("/api/surat")
                .file(new MockMultipartFile(
                    "data",
                    null,
                    "application/json",
                    objectMapper.writeValueAsBytes(request)
                ))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("X-API-TOKEN", "user2Test")
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
            );

            assertNull(response.getErrors());
            assertTrue(response.getData().contains("berhasil"));
            log.info(response.getData());
        });
    }

    @Test
    void testCreateSuratSuccessWithPdf() throws Exception {
        CreateSuratRequest request = CreateSuratRequest.builder()
            .nomorSurat("S-987434/BLA.08/2024")
            .namaPengirim("kantor a")
            .perihal("penyampaian dokumen b")
            .idEkspedisi(1)
            .idTujuanBagian(3)
            .build();

        mockMvc.perform(
            multipart("/api/surat")
                .file(new MockMultipartFile(
                    "data",
                    null,
                    "application/json",
                    objectMapper.writeValueAsBytes(request)
                ))
                .file(new MockMultipartFile(
                    "pdfFile",
                    "cv.pdf",
                    "application/pdf",
                    getClass().getResourceAsStream("/pdfData/cv.pdf")
                ))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("X-API-TOKEN", "user2Test")
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
            );

            assertNull(response.getErrors());
            assertTrue(response.getData().contains("berhasil"));
            log.info(response.getData());
        });
    }

    @Test
    void testSearchSuratFailedNotLogin() throws Exception {
        mockMvc.perform(
            get("/api/surat")
                .param("year", "2024")
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
    void testSearchSuratSuccess() throws Exception {

        for (int i = 0; i < 100; i++) {
            createSuratTest(i);
        }

        mockMvc.perform(
            get("/api/surat")
                .param("tahun", "2024")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "user2Test")
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<List<ForListSuratResponse>> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
            );

            assertNull(response.getErrors());
            assertNotNull(response.getData());
            assertEquals(20, response.getData().size());
            assertEquals(5, response.getPaging().getTotalPage());
            assertEquals(0, response.getPaging().getCurrentPage());
            assertEquals(20, response.getPaging().getSize());
            log.info(response.getData().toString());
        });
    }

    @Test
    void testSearchSuratSuccessNotFound() throws Exception {

        for (int i = 0; i < 100; i++) {
            createSuratTest(i);
        }

        mockMvc.perform(
            get("/api/surat")
                .param("tahun", "2030")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "user2Test")
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<List<ForListSuratResponse>> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
            );

            assertNull(response.getErrors());
            assertNotNull(response.getData());
            assertEquals(0, response.getData().size());
            assertEquals(0, response.getPaging().getTotalPage());
            assertEquals(0, response.getPaging().getCurrentPage());
            assertEquals(20, response.getPaging().getSize());
        });
    }

    @Test
    void    testGetSuratFailedNotLogin() throws Exception {
        createSuratTest(2);
        mockMvc.perform(
            get("/api/surat/2")
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
    void testGetSuratFailedNotFound() throws Exception {
        createUserTest(null, null);
        createSuratTest(2);
        mockMvc.perform(
            get("/api/surat/100")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "user2Test")
        ).andExpectAll(
            status().isNotFound()
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
    void testGetSuratSuccess() throws Exception {
        createUserTest(null, null);
        createSuratTest(1);
        List<Surat> all = suratRepository.findAll();
        Surat first = all.getFirst();

        mockMvc.perform(
            get("/api/surat/"+first.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "user2Test")
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<SuratResponse> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
            );

            assertNull(response.getErrors());
            assertNotNull(response.getData());
            assertEquals(first.getId(), response.getData().getId());
            log.info(response.getData().toString());
        });
    }

    @Test
    void testUpdateSuratFailedNotLogin() throws Exception {
        createSuratTest(2);
        List<Surat> all = suratRepository.findAll();
        Surat first = all.getFirst();

        UpdateSuratRequest request = UpdateSuratRequest.builder()
            .idPosisi(2)
            .nomorSeriEkspedisi("6234728646242487")
            .kontak("08342745358")
            .tanggalTerimaBidang(LocalDateTime.now())
            .penerimaBidang("222222222")
            .build();

        mockMvc.perform(
            multipart("/api/surat/"+first.getId())
                .file(new MockMultipartFile(
                    "data",
                    null,
                    "application/json",
                    objectMapper.writeValueAsBytes(request)
                ))
                .file(new MockMultipartFile(
                    "pdfFile",
                    "cv.pdf",
                    "application/pdf",
                    getClass().getResourceAsStream("/pdfData/cv.pdf")
                ))
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
            assertTrue(response.getErrors().contains("You must login first"));
            log.info(response.getErrors());
        });
    }

    @Test
    void testUpdateSuratFailedNotFound() throws Exception {
        createSuratTest(2);
        List<Surat> all = suratRepository.findAll();
        Surat first = all.getFirst();

        UpdateSuratRequest request = UpdateSuratRequest.builder()
            .idPosisi(2)
            .nomorSeriEkspedisi("6234728646242487")
            .kontak("08342745358")
            .tanggalTerimaBidang(LocalDateTime.now())
            .penerimaBidang("222222222")
            .build();

        mockMvc.perform(
            multipart("/api/surat/3")
                .file(new MockMultipartFile(
                    "data",
                    null,
                    "application/json",
                    objectMapper.writeValueAsBytes(request)
                ))
                .file(new MockMultipartFile(
                    "pdfFile",
                    "cv.pdf",
                    "application/pdf",
                    getClass().getResourceAsStream("/pdfData/cv.pdf")
                ))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("X-API-TOKEN", "user2Test")
        ).andExpectAll(
            status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
            );

            assertNotNull(response.getErrors());
            assertTrue(response.getErrors().contains("surat not found"));
            log.info(response.getErrors());
        });
    }

    @Test
    void testUpdateSuratFailedNotPdf() throws Exception {
        createSuratTest(2);
        List<Surat> all = suratRepository.findAll();
        Surat first = all.getFirst();

        UpdateSuratRequest request = UpdateSuratRequest.builder()
            .idPosisi(2)
            .nomorSeriEkspedisi("6234728646242487")
            .kontak("08342745358")
            .tanggalTerimaBidang(LocalDateTime.now())
            .penerimaBidang("222222222")
            .build();

        mockMvc.perform(
            multipart("/api/surat/"+first.getId())
                .file(new MockMultipartFile(
                    "data",
                    null,
                    "application/json",
                    objectMapper.writeValueAsBytes(request)
                ))
                .file(new MockMultipartFile(
                    "pdfFile",
                    "users.csv",
                    "text/csv",
                    getClass().getResourceAsStream("/pdfData/users.csv")
                ))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("X-API-TOKEN", "user2Test")
        ).andExpectAll(
            status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
            );

            assertNotNull(response.getErrors());
            assertTrue(response.getErrors().contains("file type must be pdf"));
            log.info(response.getErrors());
        });
    }

    @Test
    void testUpdateSuratFailedPosisiNotFound() throws Exception {
        createSuratTest(2);
        List<Surat> all = suratRepository.findAll();
        Surat first = all.getFirst();

        UpdateSuratRequest request = UpdateSuratRequest.builder()
            .idPosisi(100)
            .nomorSeriEkspedisi("6234728646242487")
            .kontak("08342745358")
            .tanggalTerimaBidang(LocalDateTime.now())
            .penerimaBidang("222222222")
            .build();

        mockMvc.perform(
            multipart("/api/surat/"+first.getId())
                .file(new MockMultipartFile(
                    "data",
                    null,
                    "application/json",
                    objectMapper.writeValueAsBytes(request)
                ))
                .file(new MockMultipartFile(
                    "pdfFile",
                    "cv.pdf",
                    "application/pdf",
                    getClass().getResourceAsStream("/pdfData/cv.pdf")
                ))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("X-API-TOKEN", "user2Test")
        ).andExpectAll(
            status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
            );

            assertNotNull(response.getErrors());
            assertTrue(response.getErrors().contains(PesanError.posisiSurat.message));
            log.info(response.getErrors());
        });
    }

    @Test
    void testUpdateSuratSuccess() throws Exception {
        createSuratTest(2);
        List<Surat> all = suratRepository.findAll();
        Surat first = all.getFirst();

        UpdateSuratRequest request = UpdateSuratRequest.builder()
            .idPosisi(2)
            .nomorSeriEkspedisi("6234728646242487")
            .kontak("08342745358")
            .tanggalTerimaBidang(LocalDateTime.now())
            .penerimaBidang("222222222")
            .build();

        mockMvc.perform(
            multipart("/api/surat/"+first.getId())
                .file(new MockMultipartFile(
                    "data",
                    null,
                    "application/json",
                    objectMapper.writeValueAsBytes(request)
                ))
                .file(new MockMultipartFile(
                    "pdfFile",
                    null,
                    "application/pdf",
                    getClass().getResourceAsStream("/pdfData/cv.pdf")
                ))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("X-API-TOKEN", "user2Test")
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
            );

            assertNull(response.getErrors());
            assertTrue(response.getData().contains("berhasil diubah"));
            log.info(response.getData());
        });
    }

    @Test
    void testDeleteSuratFailedNotLogin() throws Exception {
        createSuratTest(2);
        List<Surat> all = suratRepository.findAll();
        Surat first = all.getFirst();

        mockMvc.perform(
            delete("/api/surat/"+first.getId())
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
            );

            assertNotNull(response.getErrors());
            assertTrue(response.getErrors().contains("You must login first"));
            log.info(response.getErrors());
        });
    }

    @Test
    void testDeleteSuratFailedNotFound() throws Exception {
        createSuratTest(2);
        List<Surat> all = suratRepository.findAll();
        Surat first = all.getFirst();

        mockMvc.perform(
            delete("/api/surat/10")
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
            assertTrue(response.getErrors().contains(PesanError.surat.message));
            log.info(response.getErrors());
        });
    }

    @Test
    void testDeleteSuratFailedNotAdmin() throws Exception {
        createSuratTest(2);
        List<Surat> all = suratRepository.findAll();
        Surat first = all.getFirst();

        mockMvc.perform(
            delete("/api/surat/"+first.getId())
                .header("X-API-TOKEN", "user2Test")
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
            );

            assertNotNull(response.getErrors());
            assertTrue(response.getErrors().contains("Unauthorized You are not allowed to access this resource"));
            log.info(response.getErrors());
        });
    }

    @Test
    void testDeleteSuratSuccess() throws Exception {
        createSuratTest(2);
        List<Surat> all = suratRepository.findAll();
        Surat first = all.getFirst();

        mockMvc.perform(
            delete("/api/surat/"+first.getId())
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
            Surat surat = suratRepository.findById(first.getId())
                    .orElse(null);

            assertNotNull(surat);
            assertEquals(0, surat.getStatus().getId());

            DetailSurat detailSurat = detailSuratRepository.findBySurat(first)
                    .orElse(null);

            assertNotNull(detailSurat);
            assertEquals(JenisKeterangan.dihapus.id, detailSurat.getKeterangan().getId());
        });
    }

    @Test
    void testUploadBerkasFailedNotLogin() throws Exception {
        createSuratTest(2);
        List<Surat> all = suratRepository.findAll();
        Surat first = all.getFirst();

        mockMvc.perform(
            multipart("/api/surat/"+first.getId()+"/upload")
                .file(new MockMultipartFile(
                    "pdfFile",
                    "cv.pdf",
                    "application/pdf",
                    getClass().getResourceAsStream("/pdfData/cv.pdf")
                ))
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
    void testUploadBerkasFailedNotPdf() throws Exception {
        createSuratTest(2);
        List<Surat> all = suratRepository.findAll();
        Surat first = all.getFirst();

        mockMvc.perform(
            multipart("/api/surat/"+first.getId()+"/upload")
                .file(new MockMultipartFile(
                    "pdfFile",
                    null,
                    "text/csv",
                    getClass().getResourceAsStream("/pdfData/users.csv")
                ))
                .header("X-API-TOKEN", "user2Test")
        ).andExpectAll(
            status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
            );

            assertNotNull(response.getErrors());
            assertTrue(response.getErrors().contains("file type must be pdf"));
            log.info(response.getErrors());
        });
    }

    @Test
    void testUploadBerkasSuccess() throws Exception {
        createSuratTest(2);
        List<Surat> all = suratRepository.findAll();
        Surat first = all.getFirst();

        mockMvc.perform(
            multipart("/api/surat/"+first.getId()+"/upload")
                .file(new MockMultipartFile(
                    "pdfFile",
                    "cv.pdf",
                    "application/pdf",
                    getClass().getResourceAsStream("/pdfData/cv.pdf")
                ))
                .header("X-API-TOKEN", "user2Test")
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
            assertTrue(response.getData().contains(first.getNomorSurat()));
            log.info(response.getData());
        });
    }

    @Test
    void testDownloadBerkasFailedNotLogin() throws Exception {
        createSuratTest(2);
        List<Surat> all = suratRepository.findAll();
        Surat first = all.getFirst();

        mockMvc.perform(
            multipart("/api/surat/"+first.getId()+"/upload")
                .file(new MockMultipartFile(
                    "pdfFile",
                    "cv.pdf",
                    "application/pdf",
                    getClass().getResourceAsStream("/pdfData/cv.pdf")
                ))
                .header("X-API-TOKEN", "user2Test")
        ).andDo(result -> {});

        mockMvc.perform(
            get("/api/surat/"+first.getId()+"/download")
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
            );

            assertNotNull(response.getErrors());
            assertTrue(response.getErrors().contains("Unauthorized You must login first"));
            log.info(response.getErrors());
        });
    }

    @Test
    void testDownloadBerkasFailedNotFound() throws Exception {
        createSuratTest(2);
        List<Surat> all = suratRepository.findAll();
        Surat first = all.getFirst();

        mockMvc.perform(
            multipart("/api/surat/"+first.getId()+"/upload")
                .file(new MockMultipartFile(
                    "pdfFile",
                    "cv.pdf",
                    "application/pdf",
                    getClass().getResourceAsStream("/pdfData/cv.pdf")
                ))
                .header("X-API-TOKEN", "user2Test")
        ).andDo(result -> {});

        mockMvc.perform(
            get("/api/surat/"+first.getId()+1+"/download")
                .header("X-API-TOKEN", "user2Test")
        ).andExpectAll(
            status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
            );

            assertNotNull(response.getErrors());
            assertTrue(response.getErrors().contains("not found"));
            log.info(response.getErrors());
        });
    }

    @Test
    void testDownloadBerkasSuccess() {

    }
}