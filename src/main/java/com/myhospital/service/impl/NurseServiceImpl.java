package com.myhospital.service.impl;

import com.myhospital.model.Nurse;
import com.myhospital.repository.NurseRepository;
import com.myhospital.service.NurseService;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class NurseServiceImpl implements NurseService {

    private final NurseRepository nurseRepository;

    public NurseServiceImpl(NurseRepository nurseRepository) {
        this.nurseRepository = nurseRepository;
    }

    @Override
    @Transactional
    public void saveNurse(Nurse nurse) {
        String password = nurse.getUser().getPassword();
        nurse.getUser().setPassword(new Pbkdf2PasswordEncoder().encode(password));
        nurseRepository.save(nurse);
    }
}
