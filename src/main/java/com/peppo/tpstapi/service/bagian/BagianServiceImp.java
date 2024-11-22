package com.peppo.tpstapi.service.bagian;

import com.peppo.tpstapi.entity.Bagian;
import com.peppo.tpstapi.repository.BagianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BagianServiceImp implements IBagianService{

    @Autowired
    BagianRepository bagianRepository;

    @Override
    public List<Bagian> getAllBagian() {
        return bagianRepository.findAll();
    }
}
