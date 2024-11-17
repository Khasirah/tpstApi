package com.peppo.tpstapi.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateSuratRequest {

    @NotBlank
    private String nomorSurat;

    @NotBlank
    private String namaPengirim;

    @NotBlank
    private String perihal;

    @Positive
    @NotNull
    private Integer idEkspedisi;

    private String nomorSeriEkspedisi;

    private String kontakPengirim;

    @Positive
    @NotNull
    private Integer idTujuanBagian;
}
