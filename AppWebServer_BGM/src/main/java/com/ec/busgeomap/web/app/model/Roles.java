package com.ec.busgeomap.web.app.model;

import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Component;

@Component
public class Roles {
	
	private String id_rol;

	@NotBlank(message = "Ingrese el rol")
	private String rol_name;

	@NotBlank(message = "Ingrese la descripcion del rol")
	private String rol_description;
	
    private long timestamp;
	
    
	private boolean rol_status;

	public Roles() {}

	public Roles(String id_rol) {
		this.id_rol = id_rol;
	}
	
	public Roles(String id_rol, String rol_name, String rol_description, long timestamp, boolean rol_status) {
		this.id_rol = id_rol;
		this.rol_name = rol_name;
		this.rol_description = rol_description;
		this.timestamp = timestamp;
		this.rol_status = rol_status;
	}

	public String getId_rol() {
		return id_rol;
	}

	public void setId_rol(String id_rol) {
		this.id_rol = id_rol;
	}

	public String getRol_name() {
		return rol_name;
	}

	public void setRol_name(String rol_name) {
		this.rol_name = rol_name;
	}

	public String getRol_description() {
		return rol_description;
	}

	public void setRol_description(String rol_description) {
		this.rol_description = rol_description;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public boolean getRol_status() {
		return rol_status;
	}

	public void setRol_status(boolean rol_status) {
		this.rol_status = rol_status;
	}

	@Override
	public String toString() {
		return "Roles [id_rol=" + id_rol + ", rol_name=" + rol_name + ", rol_description=" + rol_description
				+ ", timestamp=" + timestamp + ", rol_status=" + rol_status + "]";
	}
}
