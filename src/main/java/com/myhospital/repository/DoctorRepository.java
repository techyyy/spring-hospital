package com.myhospital.repository;

import com.myhospital.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public interface DoctorRepository extends PagingAndSortingRepository<Doctor, Long> {
    List<Doctor> findByPosition(String position);
    Page<Doctor> findAll(Pageable pageable);
    Optional<Doctor> findById(Long id);
    Doctor findDoctorByUser_Id(Long id);
}
