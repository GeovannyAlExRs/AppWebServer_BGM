package com.ec.busgeomap.web.app.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

@Component
public class CodeQR {
	
	private String gqr_code;
	
	@NotBlank //no acepta Nulo (Solo texto)
	@Size(min = 8, max = 50, message = "Inserte texto mayor a 5 caracteres")
	private String gqr_description;
	
	@NotNull //Valor no nulo
	private String gqr_asb_bus_id;

	private long gqr_registration_date;
	
	private String gqr_image;

	//@AssertFalse //Propiedad Boolean TRUE 
	private boolean gqr_status;

	public CodeQR() {}
	
	public CodeQR(String gqr_code) {
		this.gqr_code = gqr_code;
	}

	public CodeQR(String gqr_code, String gqr_description, String gqr_asb_bus_id, 
			long gqr_registration_date, String gqr_image, boolean gqr_status) {
		this.gqr_code = gqr_code;
		this.gqr_description = gqr_description;
		this.gqr_asb_bus_id = gqr_asb_bus_id;
		this.gqr_registration_date = gqr_registration_date;
		this.gqr_image = gqr_image;
		this.gqr_status = gqr_status;
	}

	public String getGqr_code() {
		return gqr_code;
	}

	public void setGqr_code(String gqr_code) {
		this.gqr_code = gqr_code;
	}

	public String getGqr_description() {
		return gqr_description;
	}

	public void setGqr_description(String gqr_description) {
		this.gqr_description = gqr_description;
	}

	public String getGqr_asb_bus_id() {
		return gqr_asb_bus_id;
	}

	public void setGqr_asb_bus_id(String gqr_asb_bus_id) {
		this.gqr_asb_bus_id = gqr_asb_bus_id;
	}

	public long getGqr_registration_date() {
		return gqr_registration_date;
	}

	public void setGqr_registration_date(long gqr_registration_date) {
		this.gqr_registration_date = gqr_registration_date;
	}

	public String getGqr_image() {
		return gqr_image;
	}

	public void setGqr_image(String gqr_image) {
		this.gqr_image = gqr_image;
	}

	public boolean getGqr_status() {
		return gqr_status;
	}

	public void setGqr_status(boolean gqr_status) {
		this.gqr_status = gqr_status;
	}

	@Override
	public String toString() {
		return "CodeQR [gqr_code=" + gqr_code + ", gqr_description=" + gqr_description + ", gqr_asb_bus_id="
				+ gqr_asb_bus_id + ", gqr_registration_date=" + gqr_registration_date + ", gqr_image=" + gqr_image
				+ ", gqr_status=" + gqr_status + "]";
	}	
}