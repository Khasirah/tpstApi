package com.peppo.tpstapi.service.user;

import com.peppo.tpstapi.entity.User;
import com.peppo.tpstapi.model.request.RegisterUserRequest;
import com.peppo.tpstapi.model.request.SearchUserRequest;
import com.peppo.tpstapi.model.request.UpdateSpecificUserRequest;
import com.peppo.tpstapi.model.request.UpdateUserRequest;
import com.peppo.tpstapi.model.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService {

    void generateAdmin();

    void register(RegisterUserRequest request, User user);

    UserResponse getCurrentUser(User user);

    void handleCsvUpload(MultipartFile csvFile, User user);

    UserResponse getSpecificUser(User user, String idUser);

    UserResponse updateCurrentUser(User user, UpdateUserRequest updateUserRequest);

    UserResponse updateSpecificUser(User user, UpdateSpecificUserRequest updateSpecificUserRequest);

    String delete(User user, String idUser);

    Page<UserResponse> search(User user, SearchUserRequest request);

    Long getPetugasCount();
}
