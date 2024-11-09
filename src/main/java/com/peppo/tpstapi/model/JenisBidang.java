package com.peppo.tpstapi.model;

public enum JenisBidang {
    admin(1),
    umum(2),
    dp3(3),
    kbp(4),
    ppip(5),
    p2humas(6),
    pep(7);

    public final Integer id;

    JenisBidang(Integer id) {
        this.id = id;
    }
}
