package com.peppo.tpstapi.controller;

import com.peppo.tpstapi.entity.Kelompok;
import com.peppo.tpstapi.entity.User;
import com.peppo.tpstapi.model.response.WebResponse;
import com.peppo.tpstapi.service.kelompok.KelompokServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class KelompokController {

    @Autowired
    private KelompokServiceImp kelompokServiceImp;

    @GetMapping(
        path = "/api/kelompok",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<Kelompok>> getAllKelompok(User user) {
        return WebResponse.<List<Kelompok>>builder()
            .data(kelompokServiceImp.getKelompoks()).build();
    }
}
