package com.peppo.tpstapi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuratResponse {

    private Integer id;

    private String nomorSurat;

    private String namaPengirim;

    private String perihal;

    private String namaBagian;

    private String posisiSurat;

    private String namaEkspedisi;

    private String nomorSeriEkspedisi;

    private String kontak;

    private String namaPetugasTpst;

    private LocalDateTime tanggalTerimaBidang;

    private String penerimaBagian;

    private String namaBerkas;

    private String status;

    private LocalDateTime tanggalTerimaSurat;
}
