package com.peppo.tpstapi.repository;

import com.peppo.tpstapi.entity.Ekspedisi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EkspedisiRepository extends JpaRepository<Ekspedisi, Integer> {
}
