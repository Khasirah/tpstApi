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
@Table(name = "detail_surat")
public class DetailSurat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_surat", referencedColumnName = "id")
    private Surat surat;

    @ManyToOne
    @JoinColumn(name = "id_keterangan", referencedColumnName = "id")
    private Keterangan keterangan;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    private User user;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
}
