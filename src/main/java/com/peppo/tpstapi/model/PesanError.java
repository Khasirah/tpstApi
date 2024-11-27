package com.peppo.tpstapi.model;

public enum PesanError {
    status("status not found"),
    ekspedisi("ekspedisi not found"),
    posisiSurat("posisi surat not found"),
    user("user not found"),
    bagian("bagian not found"),
    pengirim("pengirim not found"),
    surat("surat not found"),
    keterangan("keterangan not found"),
    userExist("user exist"),
    pdfNotFound("pdf not found"),
    kelompok("kelompok not found");

    public final String message;

    PesanError(String message) {
        this.message = message;
    }
}
