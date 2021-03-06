package com.myhospital.service.impl;

import com.myhospital.model.entity.PatientAssignment;
import com.myhospital.repository.PatientAssignmentRepository;
import com.myhospital.service.PatientAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientAssignmentServiceImpl implements PatientAssignmentService {

    private final PatientAssignmentRepository patientAssignmentRepository;

    @Autowired
    public PatientAssignmentServiceImpl(PatientAssignmentRepository patientAssignmentRepository) {
        this.patientAssignmentRepository = patientAssignmentRepository;
    }

    @Override
    public List<PatientAssignment> getPatientAssignments(Long id) {
        return patientAssignmentRepository.findPatientAssignmentByPatientId(id);
    }

    @Override
    public Page<PatientAssignment> getPaginatedPatientsAssignments(Long doctorId, Pageable pageable, boolean isDischarged) {
        return patientAssignmentRepository.findAllByDoctor_IdAndPatient_IsDischarged(doctorId, pageable, isDischarged);
    }

    @Override
    public List<PatientAssignment> searchForMyPatients(String query, Long doctorId) {
        return patientAssignmentRepository.findPatientAssignmentsLike(query, doctorId);
    }

    @Override
    public PatientAssignment getPatientAssignmentById(Long id, Long doctorId) {
        return patientAssignmentRepository.getPatientAssignmentByIdAndDoctorId(id, doctorId);
    }

    @Override
    public void save(PatientAssignment patientAssignment) {
        patientAssignmentRepository.save(patientAssignment);
    }

}
