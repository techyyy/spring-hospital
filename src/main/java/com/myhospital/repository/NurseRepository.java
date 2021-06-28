package com.myhospital.repository;

import com.myhospital.model.Nurse;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface NurseRepository extends CrudRepository<Nurse, Long> {
}
