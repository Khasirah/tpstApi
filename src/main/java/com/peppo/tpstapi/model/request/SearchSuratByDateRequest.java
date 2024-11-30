package com.peppo.tpstapi.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchSuratByDateRequest {

    @NotNull
    private Date tanggalTerimaSurat;

    @NotNull
    private Integer page;

    @NotNull
    private Integer size;
}
