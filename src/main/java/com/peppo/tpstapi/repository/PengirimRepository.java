package com.peppo.tpstapi.repository;

import com.peppo.tpstapi.entity.Pengirim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PengirimRepository extends JpaRepository<Pengirim, Integer> {

    Optional<Pengirim> findByNamaPengirim(String namaPengirim);
}
