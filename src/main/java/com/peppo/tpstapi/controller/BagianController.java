package com.peppo.tpstapi.controller;

import com.peppo.tpstapi.entity.Bagian;
import com.peppo.tpstapi.entity.User;
import com.peppo.tpstapi.model.response.WebResponse;
import com.peppo.tpstapi.repository.BagianRepository;
import com.peppo.tpstapi.service.bagian.BagianServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class BagianController {

    @Autowired
    private BagianServiceImp bagianServiceImp;

    @GetMapping(
        path = "/api/bagian",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<Bagian>> getAllBagian(User use) {
        return WebResponse.<List<Bagian>>builder()
            .data(bagianServiceImp.getAllBagian()).build();
    }
}
