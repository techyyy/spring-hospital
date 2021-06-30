package com.myhospital.repository;

import com.myhospital.model.entity.Doctor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public interface DoctorRepository extends PagingAndSortingRepository<Doctor, Long> {

    List<Doctor> findByPosition(String position);

    @NotNull
    Page<Doctor> findAll(@NotNull Pageable pageable);

    @NotNull
    List<Doctor> findAll();

    @NotNull
    Optional<Doctor> findById(@NotNull Long id);

    Doctor findDoctorByUser_Id(Long id);
}
