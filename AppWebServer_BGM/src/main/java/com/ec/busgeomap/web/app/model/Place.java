package com.ec.busgeomap.web.app.model;

import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Component;

import com.google.cloud.firestore.GeoPoint;

@Component
public class Place {

	private String pla_id;
	
	@NotBlank(message = "Ingrese el lugar")
	private String pla_name;

	@NotBlank(message = "Ingrese la descripcion del lugar")
	private String pla_description;
	
	private GeoPoint pla_ubication;
	    
	private boolean pla_status;

	public Place() {}
	
	public Place(String pla_id) {
		this.pla_id = pla_id;
	}

	public Place(String pla_id, String pla_name, String pla_description, GeoPoint pla_ubication, boolean pla_status) {
		this.pla_id = pla_id;
		this.pla_name = pla_name;
		this.pla_description = pla_description;
		this.pla_ubication = pla_ubication;
		this.pla_status = pla_status;
	}

	public String getPla_id() {
		return pla_id;
	}

	public void setPla_id(String pla_id) {
		this.pla_id = pla_id;
	}

	public String getPla_name() {
		return pla_name;
	}

	public void setPla_name(String pla_name) {
		this.pla_name = pla_name;
	}

	public String getPla_description() {
		return pla_description;
	}

	public void setPla_description(String pla_description) {
		this.pla_description = pla_description;
	}

	public GeoPoint getPla_ubication() {
		return pla_ubication;
	}

	public void setPla_ubication(GeoPoint pla_ubication) {
		this.pla_ubication = pla_ubication;
	}

	public boolean getPla_status() {
		return pla_status;
	}

	public void setPla_status(boolean pla_status) {
		this.pla_status = pla_status;
	}

	@Override
	public String toString() {
		return "Place [pla_id=" + pla_id + ", pla_name=" + pla_name + ", pla_description=" + pla_description
				+ ", pla_ubication=" + pla_ubication + ", pla_status=" + pla_status + "]";
	}
}