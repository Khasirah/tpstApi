package com.peppo.tpstapi.model;

public enum JenisKelompok {
    admin(1),
    user(2);

    public final Integer id;

    JenisKelompok(Integer id) {
        this.id = id;
    }
}
