package com.peppo.tpstapi.controller;

import com.peppo.tpstapi.entity.User;
import com.peppo.tpstapi.model.request.CreateSuratRequest;
import com.peppo.tpstapi.model.request.SearchSuratByYearRequest;
import com.peppo.tpstapi.model.request.UpdateSuratRequest;
import com.peppo.tpstapi.model.response.ForListSuratResponse;
import com.peppo.tpstapi.model.response.PagingResponse;
import com.peppo.tpstapi.model.response.SuratResponse;
import com.peppo.tpstapi.model.response.WebResponse;
import com.peppo.tpstapi.service.surat.SuratServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
public class SuratController {

    @Autowired
    private SuratServiceImp suratServiceImp;

    @PostMapping(
        path = "/api/surat",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> createSurat(
        User user,
        @RequestPart(name = "data") CreateSuratRequest request,
        @RequestPart(name = "pdfFile", required = false) MultipartFile pdfFile
    ) {
        suratServiceImp.createSurat(user, request, pdfFile);
        return WebResponse.<String>builder()
            .data(request.getNomorSurat() + " berhasil ditambahkan")
            .build();
    }

    @GetMapping(
        path = "/api/surat",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<ForListSuratResponse>> listSuratByYear(
        User user,
        @RequestParam(value = "tahun", required = false) Integer tahun,
        @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "20") Integer size
    ) {
        SearchSuratByYearRequest request = SearchSuratByYearRequest.builder()
            .tahun(tahun)
            .page(page)
            .size(size)
            .build();

        Page<ForListSuratResponse> forListSuratResponses = suratServiceImp.listSuratByYear(user, request);
        return WebResponse.<List<ForListSuratResponse>>builder()
            .data(forListSuratResponses.getContent())
            .paging(PagingResponse.builder()
                .currentPage(forListSuratResponses.getNumber())
                .size(forListSuratResponses.getSize())
                .totalPage(forListSuratResponses.getTotalPages())
                .build())
            .build();
    }

    @GetMapping(
        path = "/api/surat/{idSurat}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<SuratResponse> getSurat(
        User user,
        @PathVariable("idSurat") Integer idSurat
    ) {
        SuratResponse suratResponse = suratServiceImp.getSurat(user, idSurat);
        return WebResponse.<SuratResponse>builder()
            .data(suratResponse)
            .build();
    }

    @PostMapping(
        path = "/api/surat/{idSurat}",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> updateSurat(
        User user,
        @RequestPart("data") UpdateSuratRequest request,
        @RequestPart("pdfFile") MultipartFile pdfFile,
        @PathVariable("idSurat") Integer idSurat
    ) {
        String response = suratServiceImp.updateSurat(user, request, pdfFile, idSurat);
        return WebResponse.<String>builder()
            .data(response+ " berhasil diubah")
            .build();
    }

    @DeleteMapping(
        path = "/api/surat/{idSurat}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> deleteSurat(
        User user,
        @PathVariable("idSurat") Integer idSurat
    ) {
        String response = suratServiceImp.deleteSurat(user, idSurat);
        return WebResponse.<String>builder()
            .data(response)
            .build();
    }
}
