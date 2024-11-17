package com.peppo.tpstapi.model.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardResponse {

    @PositiveOrZero
    private Integer totalSuratMasuk;

    @PositiveOrZero
    private Integer totalSuratAtTPST;

    @PositiveOrZero
    private Long totalPetugas;

    @PositiveOrZero
    private Long totalSuratByPetugasTPST;

    @NotNull
    private List<ForListSuratResponse> newestSuratInbound;
}
