package com.peppo.tpstapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "posisi_surat")
public class PosisiSurat {

    @Id
    private Integer id;

    @NotBlank
    @Column(name = "keterangan_posisi", unique = true)
    private String keteranganPosisi;

    @Column(name = "created_date")
    @NotBlank
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
