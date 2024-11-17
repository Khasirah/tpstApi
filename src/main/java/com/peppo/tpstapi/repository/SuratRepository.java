package com.peppo.tpstapi.repository;

import com.peppo.tpstapi.entity.PosisiSurat;
import com.peppo.tpstapi.entity.Status;
import com.peppo.tpstapi.entity.Surat;
import com.peppo.tpstapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuratRepository extends JpaRepository<Surat, Integer>, JpaSpecificationExecutor<Surat> {

    Optional<Integer> countSuratByStatus(Status status);

    Optional<Integer> countSuratByPosisiSuratAndStatus(PosisiSurat posisiSurat, Status status);

    Optional<Long> countSuratByPetugasTPST(User user);

}
