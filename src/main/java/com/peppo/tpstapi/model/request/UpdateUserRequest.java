package com.peppo.tpstapi.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {

    private String namaUser;

    private String password;

    private Integer idBagian;

    private Integer idKelompok;
}
