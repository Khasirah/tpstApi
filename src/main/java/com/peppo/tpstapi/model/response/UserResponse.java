package com.peppo.tpstapi.model.response;

import com.peppo.tpstapi.entity.Bagian;
import com.peppo.tpstapi.entity.Kelompok;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private String idUser;

    private String namaUser;

    private Bagian bagian;

    private Kelompok kelompok;
}
