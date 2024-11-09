package com.peppo.tpstapi.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateSpecificUserRequest {

    @JsonIgnore
    @Size(min = 9, max = 9)
    private String idUser;

    private String namaUser;

    private String password;

    private Integer idBagian;

    private Integer idKelompok;
}
