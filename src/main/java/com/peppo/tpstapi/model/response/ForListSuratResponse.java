package com.peppo.tpstapi.model.response;

import com.peppo.tpstapi.entity.PosisiSurat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForListSuratResponse {

    private Integer idSurat;

    private String nomorSurat;

    private String namaPengirim;

    private String perihal;

    private LocalDateTime tanggalTerima;

    private String idPetugasTpst;

    private String namaPetugasTpst;

    private PosisiSurat posisiSurat;
}
