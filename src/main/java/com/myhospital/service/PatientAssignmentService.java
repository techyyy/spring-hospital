package com.myhospital.service;

import com.myhospital.model.PatientAssignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PatientAssignmentService {
    List<PatientAssignment> getPatientAssignments(Long id);
    Page<PatientAssignment> getPaginatedPatientsAssignments(Long doctorId, Pageable pageable);
    List<PatientAssignment> searchForMyPatients(String query, Long doctorId);
    PatientAssignment getPatientAssignmentById(Long id, Long doctorId);
    void saveMyPatient(PatientAssignment pa);
}
