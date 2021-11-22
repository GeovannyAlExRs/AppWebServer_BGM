package com.ec.busgeomap.web.app.model;

import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Component;

@Component
public class Route {

	private String rou_id;

	@NotBlank(message = "Ingrese la ruta")
	private String rou_name;
	
	private long rou_registration_date;
	
	private boolean rou_status;

	public Route() {}

	public Route(String rou_id, String rou_name, long rou_registration_date, boolean rou_status) {
		this.rou_id = rou_id;
		this.rou_name = rou_name;
		this.rou_registration_date = rou_registration_date;
		this.rou_status = rou_status;
	}

	public Route(String rou_id) {
		this.rou_id = rou_id;
	}

	public String getRou_id() {
		return rou_id;
	}

	public void setRou_id(String rou_id) {
		this.rou_id = rou_id;
	}

	public String getRou_name() {
		return rou_name;
	}

	public void setRou_name(String rou_name) {
		this.rou_name = rou_name;
	}

	public long getRou_registration_date() {
		return rou_registration_date;
	}

	public void setRou_registration_date(long rou_registration_date) {
		this.rou_registration_date = rou_registration_date;
	}

	public boolean getRou_status() {
		return rou_status;
	}

	public void setRou_status(boolean rou_status) {
		this.rou_status = rou_status;
	}

	@Override
	public String toString() {
		return "Route [rou_id=" + rou_id + ", rou_name=" + rou_name + ", rou_registration_date=" + rou_registration_date + ", rou_status=" + rou_status + "]";
	}
}