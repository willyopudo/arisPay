package com.example.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.model.Machine;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineRepository extends CrudRepository<Machine, Long>{
}
