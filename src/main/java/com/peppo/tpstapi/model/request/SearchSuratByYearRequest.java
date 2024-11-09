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
public class SearchSuratByYearRequest {

    private Integer tahun;

    @NotNull
    private Integer page;

    @NotNull
    private Integer size;
}
