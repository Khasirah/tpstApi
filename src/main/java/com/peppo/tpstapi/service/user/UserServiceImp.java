package com.peppo.tpstapi.service.user;

import com.peppo.tpstapi.entity.Bagian;
import com.peppo.tpstapi.entity.Kelompok;
import com.peppo.tpstapi.entity.User;
import com.peppo.tpstapi.helper.CSVHelper;
import com.peppo.tpstapi.model.*;
import com.peppo.tpstapi.model.request.RegisterUserRequest;
import com.peppo.tpstapi.model.request.SearchUserRequest;
import com.peppo.tpstapi.model.request.UpdateSpecificUserRequest;
import com.peppo.tpstapi.model.request.UpdateUserRequest;
import com.peppo.tpstapi.model.response.UserResponse;
import com.peppo.tpstapi.repository.BagianRepository;
import com.peppo.tpstapi.repository.KelompokRepository;
import com.peppo.tpstapi.repository.UserRepository;
import com.peppo.tpstapi.security.BCrypt;
import com.peppo.tpstapi.service.validation.ValidationServiceImp;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class UserServiceImp implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BagianRepository bagianRepository;

    @Autowired
    private KelompokRepository kelompokRepository;

    @Autowired
    private ValidationServiceImp validationServiceImp;

    @Autowired
    private CSVHelper csvHelper;


    @Override
    @Transactional
    public void generateAdmin() {

        Bagian bagian = bagianRepository.findById(JenisBidang.admin.id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "bagian not found"));
        Kelompok kelompok = kelompokRepository.findById(JenisKelompok.admin.id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "kelompok not found"));

        User admin = new User();
        admin.setIdUser("111111111");
        admin.setNamaUser("admin");
        admin.setPassword(BCrypt.hashpw("admin123456789", BCrypt.gensalt()));
        admin.setBagian(bagian);
        admin.setKelompok(kelompok);

        if (userRepository.existsById(admin.getIdUser())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username already exist");
        }

        userRepository.save(admin);
    }

    @Override
    @Transactional
    public void register(RegisterUserRequest request, User user) {
        validationServiceImp.isAdmin(user);
        validationServiceImp.validate(request);

        if (userRepository.existsById(request.getIdUser())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username already exist");
        }

        Bagian bagian = bagianRepository.findById(request.getIdBagian()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "bagian not found"));
        Kelompok kelompok = kelompokRepository.findById(request.getIdKelompok()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "kelompok not found"));

        User newUser = new User();
        newUser.setIdUser(request.getIdUser());
        newUser.setNamaUser(request.getNamaUser());
        newUser.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        newUser.setBagian(bagian);
        newUser.setKelompok(kelompok);

        userRepository.save(newUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(User user) {
        return toUserResponse(user);
    }

    @Override
    @Transactional
    public void handleCsvUpload(MultipartFile csvFile, User user) {
        validationServiceImp.isAdmin(user);
        validationServiceImp.isCsv(csvFile);

        Set<User> users = csvHelper.parseCSV(csvFile);
        userRepository.saveAll(users);
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .idUser(user.getIdUser())
                .namaUser(user.getNamaUser())
                .bagian(user.getBagian())
                .kelompok(user.getKelompok())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getSpecificUser(User user, String idUser) {
        validationServiceImp.isAdmin(user);
        User specificUser = userRepository.findById(idUser)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        return toUserResponse(specificUser);
    }

    @Override
    @Transactional
    public UserResponse updateCurrentUser(User user, UpdateUserRequest updateUserRequest) {
        validationServiceImp.validate(updateUserRequest);

        User userToBeSave = checkNonNull(
            user,
            updateUserRequest.getNamaUser(),
            updateUserRequest.getPassword(),
            updateUserRequest.getIdBagian(),
            updateUserRequest.getIdKelompok()
        );

        userRepository.save(userToBeSave);
        return toUserResponse(userToBeSave);
    }

    @Override
    @Transactional
    public UserResponse updateSpecificUser(User user, UpdateSpecificUserRequest updateSpecificUserRequest) {
        validationServiceImp.isAdmin(user);
        validationServiceImp.validate(updateSpecificUserRequest);

        User userToBeChange = userRepository.findById(updateSpecificUserRequest.getIdUser())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        User userToBeSave =  checkNonNull(
                userToBeChange,
                updateSpecificUserRequest.getNamaUser(),
                updateSpecificUserRequest.getPassword(),
                updateSpecificUserRequest.getIdBagian(),
                updateSpecificUserRequest.getIdKelompok()
        );

        userRepository.save(userToBeSave);
        return toUserResponse(userToBeSave);
    }

    private User checkNonNull(
        User userToBeChange,
        String namaUser,
        String password,
        Integer idBagian,
        Integer idKelompok
    ) {
        if (Objects.nonNull(namaUser)) {
            userToBeChange.setNamaUser(namaUser);
        }

        if (Objects.nonNull(password)) {
            userToBeChange.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        }

        if (Objects.nonNull(idBagian)) {
            userToBeChange.setBagian(bagianRepository.findById(idBagian).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "bagian not found")));
        }

        if (Objects.nonNull(idKelompok)) {
            userToBeChange.setKelompok(kelompokRepository.findById(idKelompok).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "kelompok not found")));
        }
        userToBeChange.setUpdatedDate(LocalDateTime.now());

        return userToBeChange;
    }

    @Override
    @Transactional
    public String delete(User user, String idUser) {
        validationServiceImp.isAdmin(user);

        User userToBeDelete = userRepository.findById(idUser)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        userRepository.delete(userToBeDelete);
        return userToBeDelete.getNamaUser();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> search(User user, SearchUserRequest request) {
        validationServiceImp.isAdmin(user);
        Specification<User> specification = ((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (Objects.nonNull(request.getIdUser())) {
                predicates.add(builder.like(root.get("idUser"), "%" + request.getIdUser() + "%"));
            }
            if (Objects.nonNull(request.getNamaUser())) {
                predicates.add(builder.like(root.get("namaUser"), "%" + request.getNamaUser() + "%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        });

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<User> users = userRepository.findAll(specification, pageable);
        List<UserResponse> userResponses = users.getContent()
                .stream()
                .map(this::toUserResponse)
                .toList();

        return new PageImpl<>(userResponses, pageable, users.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Long getPetugasCount() {
        return userRepository.countByIdUserNot("111111111")
            .orElse(0L);
    }
}