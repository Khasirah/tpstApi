package com.peppo.tpstapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "surat")
public class Surat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nomor_surat")
    private String nomorSurat;

    @ManyToOne
    @JoinColumn(name = "id_pengirim", referencedColumnName = "id")
    private Pengirim pengirim;

    private String perihal;

    @ManyToOne
    @JoinColumn(name = "id_bagian", referencedColumnName = "id")
    private Bagian bagian;

    @ManyToOne
    @JoinColumn(name = "id_posisi", referencedColumnName = "id")
    private PosisiSurat posisiSurat;

    @ManyToOne
    @JoinColumn(name = "id_ekspedisi", referencedColumnName = "id")
    private Ekspedisi ekspedisi;

    @Column(name = "nomor_seri_ekspedisi")
    private String nomorSeriEkspedisi;

    private String kontak;

    @ManyToOne
    @JoinColumn(name = "id_petugas_tpst", referencedColumnName = "id_user")
    private User petugasTPST;

    @Column(name = "tanggal_terima_bidang")
    private LocalDateTime tanggalTerimaBidang;

    @ManyToOne
    @JoinColumn(name = "id_petugas_bidang", referencedColumnName = "id_user")
    private User petugasBidang;

    @Column(name = "nama_berkas")
    private String namaBerkas;

    @ManyToOne
    @JoinColumn(name = "id_status", referencedColumnName = "id")
    private Status status;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
