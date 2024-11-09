package com.peppo.tpstapi.repository;

import com.peppo.tpstapi.entity.PosisiSurat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PosisiSuratRepository extends JpaRepository<PosisiSurat, Integer> {
}
