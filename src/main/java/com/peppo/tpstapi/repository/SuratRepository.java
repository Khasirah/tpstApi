package com.peppo.tpstapi.repository;

import com.peppo.tpstapi.entity.Surat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SuratRepository extends JpaRepository<Surat, Integer>, JpaSpecificationExecutor<Surat> {
}
