package com.ec.busgeomap.web.app.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

@Component
public class Bus {

	private String bus_id;
	
	@NotBlank
	@Size(min=5, max=25, message="Inserte texto mayor a 4 caracteres")
	private String bus_make;

	@NotBlank
	@Size(min=5, max=25, message="Inserte texto mayor a 4 caracteres")
	private String bus_model;

	@NotNull
	private int bus_number_disc;

	private long bus_registration_date;

	@NotBlank
	@Size(min=5, max=10, message="Inserte texto mayor a 5 caracteres")
	private String bus_registration_number;

	@NotNull
	private int bus_size;

	//@NotBlank
	@Size(min=5, max=25, message="Inserte texto mayor a 5 caracteres")
	private String bus_propietor_id;
	
	//@NotNull
	private boolean bus_status;

	public Bus() {}

	public Bus(String bus_id) {
		this.bus_id = bus_id;
	}

	public Bus(String bus_id, String bus_make, String bus_model, int bus_number_disc, long bus_registration_date, 
			String bus_registration_number, int bus_size, String bus_propietor_id, boolean bus_status) {
		this.bus_id = bus_id;
		this.bus_make = bus_make;
		this.bus_model = bus_model;
		this.bus_number_disc = bus_number_disc;
		this.bus_registration_date = bus_registration_date;
		this.bus_registration_number = bus_registration_number;
		this.bus_size = bus_size;
		this.bus_propietor_id = bus_propietor_id;
		this.bus_status = bus_status;
	}

	public String getBus_id() {
		return bus_id;
	}

	public void setBus_id(String bus_id) {
		this.bus_id = bus_id;
	}

	public String getBus_make() {
		return bus_make;
	}

	public void setBus_make(String bus_make) {
		this.bus_make = bus_make;
	}

	public String getBus_model() {
		return bus_model;
	}

	public void setBus_model(String bus_model) {
		this.bus_model = bus_model;
	}

	public int getBus_number_disc() {
		return bus_number_disc;
	}

	public void setBus_number_disc(int bus_number_disc) {
		this.bus_number_disc = bus_number_disc;
	}

	public long getBus_registration_date() {
		return bus_registration_date;
	}

	public void setBus_registration_date(long bus_registration_date) {
		this.bus_registration_date = bus_registration_date;
	}

	public String getBus_registration_number() {
		return bus_registration_number;
	}

	public void setBus_registration_number(String bus_registration_number) {
		this.bus_registration_number = bus_registration_number;
	}

	public int getBus_size() {
		return bus_size;
	}

	public void setBus_size(int bus_size) {
		this.bus_size = bus_size;
	}

	public String getBus_propietor_id() {
		return bus_propietor_id;
	}

	public void setBus_propietor_id(String bus_propietor_id) {
		this.bus_propietor_id = bus_propietor_id;
	}

	public boolean getBus_status() {
		return bus_status;
	}

	public void setBus_status(boolean bus_status) {
		this.bus_status = bus_status;
	}

	@Override
	public String toString() {
		return "Bus [bus_id=" + bus_id + ", bus_make=" + bus_make + ", bus_model=" + bus_model + ", bus_number_disc="
				+ bus_number_disc + ", bus_registration_date=" + bus_registration_date + ", bus_registration_number="
				+ bus_registration_number + ", bus_size=" + bus_size + ", bus_propietor_id=" + bus_propietor_id
				+ ", bus_status=" + bus_status + "]";
	}
}