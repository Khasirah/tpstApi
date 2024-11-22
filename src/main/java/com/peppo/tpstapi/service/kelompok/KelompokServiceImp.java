package com.peppo.tpstapi.service.kelompok;

import com.peppo.tpstapi.entity.Kelompok;
import com.peppo.tpstapi.repository.KelompokRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KelompokServiceImp implements IKelompokService{

    @Autowired
    KelompokRepository kelompokRepository;

    @Override
    public List<Kelompok> getKelompoks() {
        return kelompokRepository.findAll();
    }
}
