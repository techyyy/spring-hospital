package com.myhospital.service;

import com.myhospital.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    List<Patient> searchForPatientLike(String fullName);
    Page<Patient> getPaginatedPatients(Pageable pageable);
    Optional<Patient> getPatientById(Long id);
    void savePatient(Patient patient);
}
