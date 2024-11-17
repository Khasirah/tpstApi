package com.peppo.tpstapi.service.surat;

import com.peppo.tpstapi.entity.*;
import com.peppo.tpstapi.model.JenisKeterangan;
import com.peppo.tpstapi.model.JenisPosisiSurat;
import com.peppo.tpstapi.model.JenisStatus;
import com.peppo.tpstapi.model.PesanError;
import com.peppo.tpstapi.model.request.CreateSuratRequest;
import com.peppo.tpstapi.model.request.SearchSuratByYearRequest;
import com.peppo.tpstapi.model.request.UpdateSuratRequest;
import com.peppo.tpstapi.model.response.ForListSuratResponse;
import com.peppo.tpstapi.model.response.SuratResponse;
import com.peppo.tpstapi.repository.*;
import com.peppo.tpstapi.service.validation.ValidationServiceImp;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class SuratServiceImp implements ISuratService {

    @Autowired
    private SuratRepository suratRepository;

    @Autowired
    private PengirimRepository pengirimRepository;

    @Autowired
    private ValidationServiceImp validationServiceImp;

    @Autowired
    private BagianRepository bagianRepository;

    @Autowired
    private PosisiSuratRepository posisiSuratRepository;

    @Autowired
    private EkspedisiRepository ekspedisiRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private DetailSuratRepository detailSuratRepository;

    @Autowired
    private KeteranganRepository keteranganRepository;

    final String UPLOADPATH = "upload/dokumen/";

    @Override
    @Transactional
    public void createSurat(
        User user,
        CreateSuratRequest createSuratRequest,
        MultipartFile pdfFile
    ) {
        validationServiceImp.validate(createSuratRequest);

        Pengirim pengirim = searchOrSavePengirim(createSuratRequest.getNamaPengirim());

        Surat surat = new Surat();
        surat.setNomorSurat(createSuratRequest.getNomorSurat());
        surat.setPengirim(pengirim);
        surat.setPerihal(createSuratRequest.getPerihal());
        surat.setPosisiSurat(posisiSuratRepository.findById(1)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PesanError.posisiSurat.message)));
        surat.setBagian(bagianRepository.findById(createSuratRequest.getIdTujuanBagian())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PesanError.bagian.message)));
        surat.setEkspedisi(ekspedisiRepository.findById(createSuratRequest.getIdEkspedisi())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PesanError.ekspedisi.message)));
        surat.setNomorSeriEkspedisi(createSuratRequest.getNomorSeriEkspedisi());
        surat.setKontak(createSuratRequest.getKontakPengirim());
        surat.setPetugasTPST(user);
        surat.setStatus(statusRepository.findById(1)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PesanError.status.message)));

        Surat suratToBeSave = processPdfFile(surat, pdfFile);

        suratRepository.save(suratToBeSave);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ForListSuratResponse> listSuratByYear(
        User user, SearchSuratByYearRequest request
    ) {
        validationServiceImp.validate(request);
        Specification<Surat> specification = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (Objects.nonNull(request.getTahun())) {
                predicates.add(
                    criteriaBuilder.equal(
                        criteriaBuilder.function(
                            "YEAR",
                            Integer.class,
                            root.get("createdDate")
                        ),
                        request.getTahun()
                    )
                );
            }
            if (Objects.isNull(request.getTahun())) {
                predicates.add(
                    criteriaBuilder.equal(
                        criteriaBuilder.function(
                            "YEAR",
                            Integer.class,
                            root.get("createdDate")
                        ),
                        LocalDate.now().getYear()
                    )
                );
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        });

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Surat> surats = suratRepository.findAll(specification, pageable);
        List<ForListSuratResponse> forListSuratResponses = surats.getContent()
            .stream()
            .map(this::toForListSuratResponse)
            .toList();

        return new PageImpl<>(forListSuratResponses, pageable, surats.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public SuratResponse getSurat(User user, Integer idSurat) {
        Surat surat = suratRepository.findById(idSurat)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PesanError.surat.message));

        String petugasBidang = "";
        if (!Objects.isNull(surat.getPetugasBidang())) {
            petugasBidang = surat.getPetugasBidang().getNamaUser();
        }

        return SuratResponse.builder()
            .id(surat.getId())
            .nomorSurat(surat.getNomorSurat())
            .namaPengirim(surat.getPengirim().getNamaPengirim())
            .perihal(surat.getPerihal())
            .namaBagian(surat.getBagian().getNamaBagian())
            .posisiSurat(surat.getPosisiSurat().getKeteranganPosisi())
            .namaEkspedisi(surat.getEkspedisi().getNamaEkspedisi())
            .nomorSeriEkspedisi(surat.getNomorSeriEkspedisi())
            .kontak(surat.getKontak())
            .namaPetugasTpst(surat.getPetugasTPST().getNamaUser())
            .tanggalTerimaBidang(surat.getTanggalTerimaBidang())
            .penerimaBagian(petugasBidang)
            .namaBerkas(surat.getNamaBerkas())
            .status(surat.getStatus().getStatus())
            .tanggalTerimaSurat(surat.getCreatedDate())
            .build();
    }

    @Override
    @Transactional
    public String updateSurat(
        User user,
        UpdateSuratRequest request,
        MultipartFile pdfFile,
        Integer idSurat
    ) {
        validationServiceImp.validate(request);
        Surat surat = suratRepository.findById(idSurat)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PesanError.surat.message));

        surat.setNomorSeriEkspedisi(request.getNomorSeriEkspedisi());
        surat.setKontak(request.getKontak());
        surat.setTanggalTerimaBidang(request.getTanggalTerimaBidang());
        surat.setPetugasBidang(user);

        if (Objects.nonNull(request.getIdPosisi())) {
            surat.setPosisiSurat(posisiSuratRepository.findById(request.getIdPosisi())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PesanError.posisiSurat.message)));
        }

        Surat suratToBeSave = processPdfFile(surat, pdfFile);
        suratToBeSave.setUpdatedDate(LocalDateTime.now());

        createDetailSurat(suratToBeSave, user, JenisKeterangan.diubah.id);
        suratRepository.save(suratToBeSave);
        return suratToBeSave.getNomorSurat();
    }

    @Override
    @Transactional
    public String deleteSurat(User user, Integer idSurat) {
        validationServiceImp.isAdmin(user);
        Surat surat = suratRepository.findById(idSurat)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PesanError.surat.message));

        surat.setStatus(statusRepository.findById(0)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PesanError.status.message)));
        surat.setUpdatedDate(LocalDateTime.now());
        log.info(surat.toString());

        suratRepository.save(surat);
        createDetailSurat(surat, user, JenisKeterangan.dihapus.id);
        return surat.getNomorSurat()+" berhasil dihapus";
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getTotalActiveSurat() {
        Status status = statusRepository.findById(JenisStatus.active.id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PesanError.status.message)
            );
        return suratRepository.countSuratByStatus(status)
            .orElse(0);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getTotalSuratAnTPST() {
        Status status = statusRepository.findById(JenisStatus.active.id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PesanError.status.message));

        PosisiSurat posisiSurat = posisiSuratRepository.findById(JenisPosisiSurat.tpst.id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PesanError.posisiSurat.message));

        return suratRepository.countSuratByPosisiSuratAndStatus(posisiSurat, status)
            .orElse(0);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ForListSuratResponse> getNewestSurat() {
        Pageable pageable = PageRequest.of(
            0, 10, Sort.by(Sort.Direction.DESC, "createdDate")
        );
        Page<Surat> surats = suratRepository.findAll(pageable);
        return surats.getContent()
            .stream()
            .map(this::toForListSuratResponse)
            .toList();
    }

    @Transactional
    protected void createDetailSurat(
        Surat suratToBeSave,
        User user,
        Integer keterangan
    ) {
        DetailSurat detailSurat = new DetailSurat();
        detailSurat.setSurat(suratToBeSave);
        detailSurat.setUser(user);
        detailSurat.setKeterangan(keteranganRepository.findById(keterangan)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PesanError.keterangan.message)));

        detailSuratRepository.save(detailSurat);
    }

    private Surat processPdfFile(Surat surat, MultipartFile pdfFile) {
        if (Objects.nonNull(pdfFile)) {
            validationServiceImp.isPdf(pdfFile);
            String fileName = generateFileName(surat.getNomorSurat(),
                Objects.requireNonNull(pdfFile.getOriginalFilename()));
            surat.setNamaBerkas(fileName);
            Path path = Path.of(UPLOADPATH + fileName);

            try {
                pdfFile.transferTo(path);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }

        return surat;
    }

    private ForListSuratResponse toForListSuratResponse(Surat surat) {
        return ForListSuratResponse.builder()
            .idSurat(surat.getId())
            .nomorSurat(surat.getNomorSurat())
            .namaPengirim(surat.getPengirim().getNamaPengirim())
            .perihal(surat.getPerihal())
            .tanggalTerima(surat.getCreatedDate())
            .idPetugasTpst(surat.getPetugasTPST().getIdUser())
            .namaPetugasTpst(surat.getPetugasTPST().getNamaUser())
            .build();
    }

    private String generateFileName(String nomorSurat, String originalFileName) {

        return nomorSurat
            .toLowerCase()
            .replaceAll("[\\s\\\\/;]", "_")
            .concat(Long.toString(System.currentTimeMillis()))
            .concat(".")
            .concat(
                originalFileName
                    .substring(originalFileName.lastIndexOf(".") + 1)
            );
    }

    @Transactional
    protected Pengirim searchOrSavePengirim(String namaPengirim) {
        Pengirim pengirim = pengirimRepository.findByNamaPengirim(namaPengirim)
            .orElse(null);

        if (Objects.nonNull(pengirim)) {
            return pengirim;
        }

        Pengirim newPengirim = new Pengirim();
        newPengirim.setNamaPengirim(namaPengirim);
        pengirimRepository.save(newPengirim);

        return pengirimRepository.findByNamaPengirim(namaPengirim)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PesanError.pengirim.message));
    }

    @Override
    public Long getTotalSuratByUser(User user) {
        return suratRepository.countSuratByPetugasTPST(user).orElse(0L);
    }
}
