package com.peppo.tpstapi.model;

public enum JenisKeterangan {
    cetakTT(1),
    uploadBerkas(2),
    diubah(3),
    dihapus(4),
    diterimaBidang(5);

    public final Integer id;

    JenisKeterangan(Integer id) {
        this.id = id;
    }
}
