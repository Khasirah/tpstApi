package com.peppo.tpstapi.controller;

import com.peppo.tpstapi.entity.User;
import com.peppo.tpstapi.model.request.RegisterUserRequest;
import com.peppo.tpstapi.model.request.SearchUserRequest;
import com.peppo.tpstapi.model.request.UpdateSpecificUserRequest;
import com.peppo.tpstapi.model.request.UpdateUserRequest;
import com.peppo.tpstapi.model.response.PagingResponse;
import com.peppo.tpstapi.model.response.UserResponse;
import com.peppo.tpstapi.model.response.WebResponse;
import com.peppo.tpstapi.service.user.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private UserServiceImp userServiceImp;

    @GetMapping(
            path = "/api/generateAdmin",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> generateAdmin() {
        userServiceImp.generateAdmin();
        return WebResponse.<String>builder()
                .data("Admin telah dibuat")
                .build();
    }

    @PostMapping(
            path = "/api/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> register(
            @RequestBody RegisterUserRequest request,
            User user
    ) {
        userServiceImp.register(request, user);
        return WebResponse.<String>builder()
                .data(request.getNamaUser()+" berhasil didaftarkan")
                .build();
    }

    @GetMapping(
            path = "/api/users/current",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> getCurrentUser(User user) {
        UserResponse response = userServiceImp.getCurrentUser(user);
        return WebResponse.<UserResponse>builder()
                .data(response)
                .build();
    }

    @PostMapping(
            path = "/api/users/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public WebResponse<String> handleCsvUpload(
            @RequestPart(name = "csvFile") MultipartFile csvFile, User user
    ) {
        userServiceImp.handleCsvUpload(csvFile, user);
        return WebResponse.<String>builder()
                .data(csvFile.getOriginalFilename()+" berhasil diupload")
                .build();
    }

    @GetMapping(
            path = "/api/users/{idUser}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> getSpecificUser(
            User user,
            @PathVariable("idUser") String idUser
    ) {
        UserResponse specificUser = userServiceImp.getSpecificUser(user, idUser);
        return WebResponse.<UserResponse>builder()
                .data(specificUser)
                .build();
    }

    @PatchMapping(
            path = "/api/users/current",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> updateCurrentUser(
            User user,
            @RequestBody UpdateUserRequest updateUserRequest
    ) {
        UserResponse response = userServiceImp.updateCurrentUser(user, updateUserRequest);
        return WebResponse.<UserResponse>builder()
                .data(response)
                .build();
    }

    @PatchMapping(
            path = "/api/users/{idUser}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> updateSpecificUser(
            User user,
            @RequestBody UpdateSpecificUserRequest updateSpecificUserRequest,
            @PathVariable("idUser") String idUser
    ) {
        updateSpecificUserRequest.setIdUser(idUser);
        UserResponse response = userServiceImp.updateSpecificUser(user, updateSpecificUserRequest);
        return WebResponse.<UserResponse>builder()
                .data(response)
                .build();
    }

    @DeleteMapping(
            path = "/api/users/{idUser}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> deleteUser(
            User user,
            @PathVariable("idUser") String idUser
    ) {
        String namaUser = userServiceImp.delete(user, idUser);
        return WebResponse.<String>builder()
                .data(namaUser+" berhasil dihapus")
                .build();
    }

    @GetMapping(
            path = "/api/users",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<UserResponse>> search(
            User user,
            @RequestParam(value = "idUser", required = false) String idUser,
            @RequestParam(value = "namaUser", required = false) String namaUser,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size
    ) {
        SearchUserRequest request = SearchUserRequest.builder()
                .idUser(idUser)
                .namaUser(namaUser)
                .page(page)
                .size(size)
                .build();

        Page<UserResponse> userResponses = userServiceImp.search(user, request);
        return WebResponse.<List<UserResponse>>builder()
                .data(userResponses.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(userResponses.getNumber())
                        .size(userResponses.getSize())
                        .totalPage(userResponses.getTotalPages())
                        .build())
                .build();
    }
}
