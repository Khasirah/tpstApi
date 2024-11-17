package com.peppo.tpstapi.controller;

import com.peppo.tpstapi.entity.Bagian;
import com.peppo.tpstapi.entity.Ekspedisi;
import com.peppo.tpstapi.entity.User;
import com.peppo.tpstapi.model.response.BagianAndEkspedisiResponse;
import com.peppo.tpstapi.model.JenisBidang;
import com.peppo.tpstapi.model.PesanError;
import com.peppo.tpstapi.model.response.WebResponse;
import com.peppo.tpstapi.repository.BagianRepository;
import com.peppo.tpstapi.repository.EkspedisiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
public class InputSuratController {

    @Autowired
    private BagianRepository bagianRepository;

    @Autowired
    private EkspedisiRepository ekspedisiRepository;

    @GetMapping(
        path = "/api/inputSurat/bagianAndEkspedisi",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<BagianAndEkspedisiResponse> getBagianAndEkspedisi(User user) {
        List<Bagian> bagianList = bagianRepository.findByIdNot(JenisBidang.admin.id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PesanError.bagian.message));

        List<Ekspedisi> ekspedisiList = ekspedisiRepository.findAll();

        return WebResponse.<BagianAndEkspedisiResponse>builder()
            .data(BagianAndEkspedisiResponse.builder()
                .bagianList(bagianList)
                .ekspedisiList(ekspedisiList)
                .build())
            .build();
    }
}
