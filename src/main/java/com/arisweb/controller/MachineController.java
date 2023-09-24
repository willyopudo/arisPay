package com.arisweb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.arisweb.model.Machine;
import com.arisweb.services.MachineService;

@Controller
@RequestMapping(value = "/machine")
public class MachineController {

	@Autowired
	MachineService machineService;


	@RequestMapping(value = "/listMachines", method = RequestMethod.GET)
	public ModelAndView listMachines() {

		ModelAndView model = new ModelAndView("machine_list");
		List<Machine> machineList = machineService.getAll();
		model.addObject("machineList", machineList);
		model.addObject("title", "List of Machines");

		//ObjectMapper objectMapper = new ObjectMapper();
		//System.out.println(objectMapper.writeValueAsString(machineList));
		return model;
	}


	@RequestMapping(value = "/addMachine/", method = RequestMethod.GET)
	public ModelAndView addMachine() {

		ModelAndView model = new ModelAndView();
		Machine machine = new Machine();
		model.addObject("machineForm", machine);
		model.addObject("title", "Add New Machine");
		model.setViewName("machine_form");

		return model;
	}


	@RequestMapping(value = "/editMachine/{id}", method = RequestMethod.GET)
	public ModelAndView editMachine(@PathVariable int id) {
		ModelAndView model = new ModelAndView();

		Machine machine = machineService.getById((long) id);
		model.addObject("machineForm", machine);
		model.addObject("title", "Edit Machine");
		model.setViewName("machine_form");

		return model;
	}


	@RequestMapping(value = "/addMachine", method = RequestMethod.POST)
	public ModelAndView add(@ModelAttribute("machineForm") Machine machine) {

		machineService.add(machine);
		return new ModelAndView("redirect:/machine/listMachines");

	}

	@RequestMapping(value = "/deleteMachine/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable("id") int id) {

		machineService.delete(id);
		return new ModelAndView("redirect:/machine/listMachines");

	}
}
