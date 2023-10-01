package com.arisweb.iservices;

import java.util.List;

import com.arisweb.model.Machine;

public interface MachineService {

	public List<Machine> getAll();

	public Machine getById(Long id);

	public void add(Machine machine);

	public void delete(int id);
}
