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
@Table(name = "bagian")
public class Bagian {

    @Id
    private Integer id;

    @NotBlank
    @Column(name = "nama_bagian")
    private String namaBagian;

    @NotBlank
    @Column(name = "singkatan_bagian", unique = true)
    private String singkatanBagian;

    @NotBlank
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
