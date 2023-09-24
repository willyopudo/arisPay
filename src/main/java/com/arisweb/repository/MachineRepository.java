package com.arisweb.repository;

import org.springframework.data.repository.CrudRepository;
import com.arisweb.model.Machine;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineRepository extends CrudRepository<Machine, Long> {
}
