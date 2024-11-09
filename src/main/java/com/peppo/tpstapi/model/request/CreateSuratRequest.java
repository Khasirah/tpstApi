package com.peppo.tpstapi.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @NotEmpty
    private String nomorSurat;

    @NotBlank
    @NotNull
    @NotEmpty
    private String namaPengirim;

    @NotBlank
    @NotNull
    @NotEmpty
    private String perihal;

    @NotNull
    private Integer idEkspedisi;

    private String nomorSeriEkspedisi;

    private String kontakPengirim;

    @NotNull
    private Integer idTujuanBagian;
}
