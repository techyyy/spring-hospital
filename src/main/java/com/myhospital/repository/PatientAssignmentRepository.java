package com.myhospital.repository;

import com.myhospital.model.entity.PatientAssignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientAssignmentRepository extends PagingAndSortingRepository<PatientAssignment, Long> {
    List<PatientAssignment> findPatientAssignmentByPatientId(Long id);
    Page<PatientAssignment> findAllByDoctor_IdAndPatient_IsDischarged(Long doctorId, Pageable pageable, boolean isDischarged);

    @Query(value = "SELECT * FROM patient_has_doctor" +
            " INNER JOIN doctor ON patient_has_doctor.doctor_id = doctor.id" +
            " INNER JOIN patient ON patient_has_doctor.patient_id = patient.id" +
            " WHERE CONCAT(patient.first_name, ' ', patient.last_name)" +
            " LIKE :fullName" +
            " AND doctor.id = :doctorId AND patient.is_discharged = false", nativeQuery = true)
    List<PatientAssignment> findPatientAssignmentsLike(@Param("fullName") String fullName, @Param("doctorId") Long doctorId);

    PatientAssignment getPatientAssignmentByIdAndDoctorId(Long id, Long doctorId);
}
