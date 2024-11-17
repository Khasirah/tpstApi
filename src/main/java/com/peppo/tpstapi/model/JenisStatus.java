package com.peppo.tpstapi.model;

public enum JenisStatus {
    delete(0),
    active(1);

    public final Integer id;

    JenisStatus(Integer id) {
        this.id = id;
    }
}
