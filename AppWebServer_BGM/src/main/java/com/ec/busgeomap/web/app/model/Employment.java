package com.ec.busgeomap.web.app.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

@Component
public class Employment {

	private String emp_id;

	@NotBlank //no acepta Nulo (Solo texto)
	@Size(min = 4, max = 15, message = "Inserte texto mayor a 4 caracteres")
	private String emp_name;
	
	@NotBlank //no acepta Nulo (Solo texto)
	@Size(min = 4, max = 30, message = "Inserte texto mayor a 4 caracteres")
	private String emp_description;
	
	//@AssertFalse //Propiedad Boolean TRUE 
	private boolean emp_status;
	
	public Employment() {}

	public Employment(String emp_id, String emp_name, String emp_description, boolean emp_status) {
		this.emp_id = emp_id;
		this.emp_name = emp_name;
		this.emp_description = emp_description;
		this.emp_status = emp_status;
	}

	public String getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(String emp_id) {
		this.emp_id = emp_id;
	}

	public String getEmp_name() {
		return emp_name;
	}

	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}

	public String getEmp_description() {
		return emp_description;
	}

	public void setEmp_description(String emp_description) {
		this.emp_description = emp_description;
	}

	public boolean isEmp_status() {
		return emp_status;
	}

	public void setEmp_status(boolean emp_status) {
		this.emp_status = emp_status;
	}

	@Override
	public String toString() {
		return "Employment [emp_id=" + emp_id + ", emp_name=" + emp_name + ", emp_description=" + emp_description + ", emp_status=" + emp_status + "]";
	}
	
	
}