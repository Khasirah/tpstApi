package com.peppo.tpstapi.service.validation;

import com.peppo.tpstapi.entity.Surat;
import com.peppo.tpstapi.entity.User;
import com.peppo.tpstapi.model.JenisKelompok;
import com.peppo.tpstapi.model.PesanError;
import com.peppo.tpstapi.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
public class ValidationServiceImp implements IValidationService {

    @Autowired
    private Validator validator;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void validate(Object request) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    @Override
    public boolean isAdmin(User user) {
        if (!Objects.equals(user.getKelompok().getId(), JenisKelompok.admin.id)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized You are not allowed to access this resource");
        }
        return true;
    }

    @Override
    public void isCsv(MultipartFile csvFile) {
        String TYPE = "text/csv";
        if (!TYPE.equals(csvFile.getContentType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "file type must be csv");
        }
    }

    @Override
    public void isPdf(MultipartFile pdfFile) {
        String TYPE = "application/pdf";
        if (!TYPE.equals(pdfFile.getContentType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "file type must be pdf");
        }
    }

    @Override
    public boolean isUserExist(String idUser) {
        User user = userRepository.findById(idUser).orElse(null);
        if (user == null) {
            return false;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, idUser + " " + PesanError.userExist.message);
    }

    @Override
    public void isArchive(Surat surat) {
        if (!Objects.isNull(surat.getPetugasBidang())) {
            throw new ResponseStatusException(
                HttpStatus.NOT_ACCEPTABLE,
                surat.getNomorSurat() + " " + PesanError.alreadyArchived.message
            );
        }
    }

    @Override
    public void isArchiveByStaff(Surat surat, User user) {
        if (surat.getBagian() != user.getBagian()) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                user.getNamaUser() + " " + PesanError.unauthorizedToArchive.message + " " + surat.getNomorSurat());
        }
    }
}
