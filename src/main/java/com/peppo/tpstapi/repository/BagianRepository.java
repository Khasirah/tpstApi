package com.peppo.tpstapi.repository;

import com.peppo.tpstapi.entity.Bagian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BagianRepository extends JpaRepository<Bagian, Integer> {

    Optional<List<Bagian>> findByIdNot(int id);
}
