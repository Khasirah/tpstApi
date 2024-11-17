package com.peppo.tpstapi.model;

public enum JenisPosisiSurat {
    tpst(1),
    bidang(2);

    public final Integer id;

    JenisPosisiSurat(Integer id) {
        this.id = id;
    }
}
