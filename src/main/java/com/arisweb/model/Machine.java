package com.arisweb.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "machinedetails")
public class Machine {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;

	@Column(name = "machinecode")
	private String machinecode;

	@Column(name = "milktemp")
	private String milktemp;

	@Column(name = "milkvolume")
	private String milkvolume;

	@Column(name = "oilvolume")
	private String oilvolume;

	@Column(name = "machinelatitude")
	private String machinelatitude;

	@Column(name = "machinelongitude")
	private String machinelongitude;

	@Column(name = "machinerent")
	private String machinerent;

	@Column(name = "createddatetime")
	private Date createddatetime = new java.util.Date();

	//getters and setters


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMachinecode() {
		return machinecode;
	}

	public void setMachinecode(String machinecode) {
		this.machinecode = machinecode;
	}

	public String getMilktemp() {
		return milktemp;
	}

	public void setMilktemp(String milktemp) {
		this.milktemp = milktemp;
	}

	public String getMilkvolume() {
		return milkvolume;
	}

	public void setMilkvolume(String milkvolume) {
		this.milkvolume = milkvolume;
	}

	public String getOilvolume() {
		return oilvolume;
	}

	public void setOilvolume(String oilvolume) {
		this.oilvolume = oilvolume;
	}

	public String getMachinelatitude() {
		return machinelatitude;
	}

	public void setMachinelatitude(String machinelatitude) {
		this.machinelatitude = machinelatitude;
	}

	public String getMachinelongitude() {
		return machinelongitude;
	}

	public void setMachinelongitude(String machinelongitude) {
		this.machinelongitude = machinelongitude;
	}

	public String getMachinerent() {
		return machinerent;
	}

	public void setMachinerent(String machinerent) {
		this.machinerent = machinerent;
	}

	public Date getCreateddatetime() {
		return createddatetime;
	}

	public void setCreateddatetime(Date createddatetime) {
		this.createddatetime = createddatetime;
	}
}
