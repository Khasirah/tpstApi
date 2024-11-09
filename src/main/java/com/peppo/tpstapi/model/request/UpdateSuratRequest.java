package com.peppo.tpstapi.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateSuratRequest {

    private Integer idPosisi;

    private String nomorSeriEkspedisi;

    private String kontak;

    private LocalDateTime tanggalTerimaBidang;

    private String penerimaBidang;

    private String namaBerkas;

}
