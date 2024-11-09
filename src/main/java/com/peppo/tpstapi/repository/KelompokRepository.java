package com.peppo.tpstapi.repository;

import com.peppo.tpstapi.entity.Kelompok;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KelompokRepository extends JpaRepository<Kelompok, Integer> {
}
