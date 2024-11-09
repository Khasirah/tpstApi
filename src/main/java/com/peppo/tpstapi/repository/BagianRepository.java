package com.peppo.tpstapi.repository;

import com.peppo.tpstapi.entity.Bagian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BagianRepository extends JpaRepository<Bagian, Integer> {
}
