package com.peppo.tpstapi.controller;

import com.peppo.tpstapi.entity.User;
import com.peppo.tpstapi.model.response.DashboardResponse;
import com.peppo.tpstapi.model.response.WebResponse;
import com.peppo.tpstapi.service.surat.SuratServiceImp;
import com.peppo.tpstapi.service.user.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class DashboardController {

    @Autowired
    private UserServiceImp userServiceImp;

    @Autowired
    private SuratServiceImp suratServiceImp;

    @GetMapping(
        path = "/api/dashboard",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<DashboardResponse> getDashboard(User user) {
        return WebResponse.<DashboardResponse>builder()
            .data(DashboardResponse.builder()
                .newestSuratInbound(suratServiceImp.getNewestSurat())
                .totalPetugas(userServiceImp.getPetugasCount())
                .totalSuratAtTPST(suratServiceImp.getTotalSuratAnTPST())
                .totalSuratMasuk(suratServiceImp.getTotalActiveSurat())
                .totalSuratByPetugasTPST(suratServiceImp.getTotalSuratByUser(user))
                .build())
            .build();
    }
}
