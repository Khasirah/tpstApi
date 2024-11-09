package com.peppo.tpstapi.repository;

import com.peppo.tpstapi.entity.DetailSurat;
import com.peppo.tpstapi.entity.Surat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DetailSuratRepository extends JpaRepository<DetailSurat, Integer> {

    Optional<DetailSurat> findBySurat(Surat surat);
}
