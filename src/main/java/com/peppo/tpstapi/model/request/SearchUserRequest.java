package com.peppo.tpstapi.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchUserRequest {

    private String idUser;

    private String namaUser;

    @NotNull
    private Integer page;

    @NotNull
    private Integer size;
}
