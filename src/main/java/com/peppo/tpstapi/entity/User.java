package com.peppo.tpstapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id_user")
    @Size(min = 9, max = 9)
    private String idUser;

    @NotBlank
    @Column(name = "nama_user")
    private String namaUser;

    @NotBlank
    private String password;

    @Column(unique = true)
    private String token;

    @Column(name = "token_expired_at")
    private Long tokenExpiredAt;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @ManyToOne
    @JoinColumn(name = "id_bagian", referencedColumnName = "id")
    private Bagian bagian;

    @ManyToOne
    @JoinColumn(name = "id_kelompok", referencedColumnName = "id")
    private Kelompok kelompok;
}
