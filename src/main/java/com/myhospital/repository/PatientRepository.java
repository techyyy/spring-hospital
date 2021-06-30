package com.myhospital.repository;

import com.myhospital.model.entity.Patient;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends PagingAndSortingRepository<Patient, Long> {
    @Query(value = "SELECT * FROM patient WHERE CONCAT(first_name, ' ', last_name) LIKE :fullName", nativeQuery = true)
    List<Patient> searchByFullNameLike(@Param("fullName") String fullName);

    @NotNull
    List<Patient> findAll();

    @NotNull
    Page<Patient> findAll(@NotNull Pageable pageable);
    @NotNull
    Optional<Patient> findById(@NotNull Long id);

    @Modifying
    @Query(value = "UPDATE patient SET is_discharged = true WHERE id = :patientId", nativeQuery = true)
    @Transactional
    void dischargePatient(@Param("patientId") Long patientId);
}
