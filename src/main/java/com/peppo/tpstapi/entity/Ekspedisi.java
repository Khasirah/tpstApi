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
@Table(name = "ekspedisi")
public class Ekspedisi {

    @Id
    private Integer id;

    @Column(name = "nama_ekspedisi", unique = true)
    @NotBlank
    private String namaEkspedisi;

    @Column(name = "created_date")
    @NotBlank
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
