package com.example.services;

import java.util.List;
import com.example.model.Machine;
public interface MachineService {

    public List<Machine> getAll();

    public Machine getById(Long id);

    public void add(Machine machine);

    public void delete(int id);
}
