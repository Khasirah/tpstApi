package com.peppo.tpstapi.repository;

import com.peppo.tpstapi.entity.Keterangan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeteranganRepository extends JpaRepository<Keterangan, Integer> {
}
