package com.myhospital.repository;

import com.myhospital.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends PagingAndSortingRepository<Patient, Long> {
    @Query(value = "SELECT * FROM patient WHERE CONCAT(first_name, ' ', last_name) LIKE :fullName", nativeQuery = true)
    List<Patient> searchByFullNameLike(@Param("fullName") String fullName);

    Page<Patient> findAll(Pageable pageable);
    Optional<Patient> findById(Long id);
}
