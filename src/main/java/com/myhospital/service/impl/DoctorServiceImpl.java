package com.myhospital.service.impl;

import com.myhospital.model.Doctor;
import com.myhospital.repository.DoctorRepository;
import com.myhospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public List<Doctor> searchByPosition(String position) {
        return doctorRepository.findByPosition(position);
    }

    @Override
    public Page<Doctor> getPaginatedDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable);
    }

    @Override
    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    @Override
    public Doctor getDoctorByUserId(Long id) {
        return doctorRepository.findDoctorByUser_Id(id);
    }

    @Override
    public void saveDoctor(Doctor doctor) {
        doctorRepository.save(doctor);
    }
}
