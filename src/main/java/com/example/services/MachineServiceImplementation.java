package com.example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.Machine;
import com.example.repository.MachineRepository;
@Service
@Transactional
public class MachineServiceImplementation implements MachineService{

    @Autowired
    MachineRepository machineRepository;

    @Override
    public List<Machine> getAll() {
        return (List<Machine>) machineRepository.findAll();
    }

    @Override
    public Machine getById(Long id) {
        return machineRepository.findById(id).get();
    }

    @Override
    public void add(Machine machine) {
        machineRepository.save(machine);
    }

    @Override
    public void delete(int id) {
        machineRepository.deleteById((long) id);
    }
}
