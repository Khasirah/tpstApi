package com.peppo.tpstapi.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserRequest {

    @NotBlank
    @Size(min = 9, max = 9)
    private String idUser;

    @NotBlank
    @Size(max = 100)
    private String namaUser;

    @NotBlank
    @Size(max = 100)
    private String password;

    @Positive
    private Integer idBagian;

    @Positive
    private Integer idKelompok;
}
