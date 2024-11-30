package com.peppo.tpstapi.controller;

import com.peppo.tpstapi.entity.User;
import com.peppo.tpstapi.model.request.*;
import com.peppo.tpstapi.model.response.ForListSuratResponse;
import com.peppo.tpstapi.model.response.PagingResponse;
import com.peppo.tpstapi.model.response.SuratResponse;
import com.peppo.tpstapi.model.response.WebResponse;
import com.peppo.tpstapi.service.surat.SuratServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
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
    public WebResponse<List<ForListSuratResponse>> searchSurat(
        User user,
        @RequestParam(value = "nomorSurat", required = false) String nomorSurat,
        @RequestParam(value = "tahun", required = false) Integer tahun,
        @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "20") Integer size
    ) {
        SearchSuratByYearRequest request = SearchSuratByYearRequest.builder()
            .nomorSurat(nomorSurat)
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
            .data(response + " berhasil diubah")
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

    @PostMapping(
        path = "/api/surat/{idSurat}/upload",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> uploadBerkas(
        User user,
        @RequestPart("pdfFile") MultipartFile pdfFile,
        @PathVariable("idSurat") Integer idSurat
    ) {
        String response = suratServiceImp.handleUploadBerkas(user, idSurat, pdfFile);
        return WebResponse.<String>builder()
            .data(response).build();
    }

    @GetMapping(
        path = "/api/surat/{idSurat}/download",
        produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<?> downloadBerkas(
        User user,
        @PathVariable("idSurat") Integer idSurat
    ) {
        Resource resource = suratServiceImp.handleDownloadBerkas(user, idSurat);

        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                .body(resource);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping(
        path = "/api/surat/getSuratByDate",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<ForListSuratResponse>> listSuratByDateAndBagian(
        User user,
        @RequestParam(value = "tanggalTerima", required = false) Date tanggalTerima,
        @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "20") Integer size
    ) {
        SearchSuratByDateRequest request = SearchSuratByDateRequest.builder()
            .tanggalTerimaSurat(tanggalTerima)
            .page(page)
            .size(size).build();

        Page<ForListSuratResponse> forListSuratResponses = suratServiceImp.listSuratByDateAndBagian(user, request);
        return WebResponse.<List<ForListSuratResponse>>builder()
            .data(forListSuratResponses.getContent())
            .paging(PagingResponse.builder()
                .currentPage(forListSuratResponses.getNumber())
                .size(forListSuratResponses.getSize())
                .totalPage(forListSuratResponses.getTotalPages())
                .build())
            .build();
    }

    @PostMapping(
        path = "/api/surat/archive",
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> archiveSurats(
        User user,
        @RequestBody ArchiveSuratsRequest request
    ) {
        String response = suratServiceImp.archiveSurats(user, request);
        return WebResponse.<String>builder()
            .data(response).build();
    }
}