package com.peppo.tpstapi.helper;

import com.peppo.tpstapi.entity.User;
import com.peppo.tpstapi.repository.BagianRepository;
import com.peppo.tpstapi.repository.KelompokRepository;
import com.peppo.tpstapi.security.BCrypt;
import com.peppo.tpstapi.service.validation.ValidationServiceImp;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class CSVHelper {

    @Autowired
    private BagianRepository bagianRepository;

    @Autowired
    private KelompokRepository kelompokRepository;

    @Autowired
    private ValidationServiceImp validationServiceImp;

    public enum Headers{
        idUser, namaUser, idBagian, idKelompok
    }

    public Set<User> parseCSV(MultipartFile csvFile) {
        try(Reader reader = new InputStreamReader(csvFile.getInputStream())) {
            Iterable<CSVRecord> records = CSVFormat.EXCEL.builder()
                    .setHeader(Headers.class)
                    .setSkipHeaderRecord(true)
                    .setIgnoreEmptyLines(true)
                    .setIgnoreSurroundingSpaces(true)
                    .build()
                    .parse(reader);

            return StreamSupport.stream(records.spliterator(), false)
                    .map(record -> {
                        User user = new User();
                        user.setIdUser(record.get(Headers.idUser));
                        user.setNamaUser(record.get(Headers.namaUser));
                        user.setPassword(BCrypt.hashpw(record.get(Headers.idUser), BCrypt.gensalt()));
                        user.setBagian(bagianRepository.findById(Integer.parseInt(record.get(Headers.idBagian))).orElseThrow());
                        user.setKelompok(kelompokRepository.findById(Integer.parseInt(record.get(Headers.idKelompok))).orElseThrow());

                        validationServiceImp.validate(user);
                        return user;
                    })
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
