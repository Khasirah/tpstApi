package com.peppo.tpstapi.service.validation;

import com.peppo.tpstapi.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface IValidationService {

    void validate(Object request);

    void isAdmin(User user);

    void isCsv(MultipartFile csvFile);

    void isPdf (MultipartFile pdfFile);
}
