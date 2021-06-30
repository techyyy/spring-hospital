package com.myhospital.service.impl;

import com.myhospital.model.entity.Nurse;
import com.myhospital.repository.NurseRepository;
import com.myhospital.service.NurseService;
import org.springframework.stereotype.Service;

@Service
public class NurseServiceImpl implements NurseService {

    private final NurseRepository nurseRepository;

    public NurseServiceImpl(NurseRepository nurseRepository) {
        this.nurseRepository = nurseRepository;
    }

    @Override
    public void saveNurse(Nurse nurse) {
        nurseRepository.save(nurse);
    }
}
