package com.myhospital.service.impl;

import com.myhospital.model.Patient;
import com.myhospital.repository.PatientRepository;
import com.myhospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public List<Patient> searchForPatientLike(String fullName) {
        return patientRepository.searchByFullNameLike(fullName);
    }

    @Override
    public Page<Patient> getPaginatedPatients(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }

    @Override
    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    @Override
    public void savePatient(Patient patient) {
        patientRepository.save(patient);
    }

}
