package com.arisweb.services;

import java.util.List;

import com.arisweb.iservices.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arisweb.model.Machine;
import com.arisweb.repository.MachineRepository;

@Service
@Transactional
public class MachineServiceImplementation implements MachineService {

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
