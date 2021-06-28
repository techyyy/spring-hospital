package com.myhospital.service;

import com.myhospital.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    List<Doctor> searchByPosition(String fullName);
    Page<Doctor> getPaginatedDoctors(Pageable pageable);
    Optional<Doctor> getDoctorById(Long id);
    Doctor getDoctorByUserId(Long id);
    void saveDoctor(Doctor doctor);
}
