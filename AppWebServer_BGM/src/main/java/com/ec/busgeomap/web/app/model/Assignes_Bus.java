package com.ec.busgeomap.web.app.model;

import javax.validation.constraints.NotBlank;

public class Assignes_Bus {

private String asb_id;
	
	@NotBlank
	private String asb_accompanist_id;
	
	@NotBlank
	private String asb_driver_id;
	
	@NotBlank
	private String asb_bus_id;
	
	@NotBlank
	private long bus_registration_date;

	private boolean asb_status;

	public Assignes_Bus() {}

	public Assignes_Bus(String asb_id) {
		this.asb_id = asb_id;
	}
	
	public Assignes_Bus(String asb_id, String asb_accompanist_id, String asb_driver_id, String asb_bus_id, @NotBlank long bus_registration_date, boolean asb_status) {
		this.asb_id = asb_id;
		this.asb_accompanist_id = asb_accompanist_id;
		this.asb_driver_id = asb_driver_id;
		this.asb_bus_id = asb_bus_id;
		this.bus_registration_date = bus_registration_date;
		this.asb_status = asb_status;
	}

	public String getAsb_id() {
		return asb_id;
	}

	public void setAsb_id(String asb_id) {
		this.asb_id = asb_id;
	}

	public String getAsb_accompanist_id() {
		return asb_accompanist_id;
	}

	public void setAsb_accompanist_id(String asb_accompanist_id) {
		this.asb_accompanist_id = asb_accompanist_id;
	}

	public String getAsb_driver_id() {
		return asb_driver_id;
	}

	public void setAsb_driver_id(String asb_driver_id) {
		this.asb_driver_id = asb_driver_id;
	}

	public String getAsb_bus_id() {
		return asb_bus_id;
	}

	public void setAsb_bus_id(String asb_bus_id) {
		this.asb_bus_id = asb_bus_id;
	}

	public long getBus_registration_date() {
		return bus_registration_date;
	}

	public void setBus_registration_date(long bus_registration_date) {
		this.bus_registration_date = bus_registration_date;
	}

	public boolean getAsb_status() {
		return asb_status;
	}

	public void setAsb_status(boolean asb_status) {
		this.asb_status = asb_status;
	}

	@Override
	public String toString() {
		return "Assignes_Bus [asb_id=" + asb_id + ", asb_accompanist_id=" + asb_accompanist_id + ", asb_driver_id="
				+ asb_driver_id + ", asb_bus_id=" + asb_bus_id + ", bus_registration_date=" + bus_registration_date
				+ ", asb_status=" + asb_status + "]";
	}	
}