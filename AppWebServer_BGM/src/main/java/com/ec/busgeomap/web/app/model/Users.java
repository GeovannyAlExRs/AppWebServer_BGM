package com.ec.busgeomap.web.app.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

@Component
public class Users {

	private String use_id;

	@NotBlank(message = "Ingrese apellidos") //no acepta Nulo (Solo texto)
	@Size(min = 4, max = 25, message = "Inserte texto mayor a 4 caracteres")
	private String use_first_name;
	
	@NotBlank(message = "Ingrese nombres") //no acepta Nulo (Solo texto)
	@Size(min = 4, max = 25, message = "Inserte texto mayor a 4 caracteres")
	private String use_last_name;
	
	@NotBlank(message = "Ingrese direccion") //no acepta Nulo (Solo texto)
	@Size(min = 5, max = 50, message = "Inserte texto mayor a 5 caracteres")
	private String use_address;
		
	@Email(message = "Email no valido")
	private String use_email;
	
	@NotBlank(message = "Ingrese numero celular") //no acepta Nulo (Solo texto)
	private String use_phone;
	
	@NotBlank(message = "Ingrese nombre de usuario") //no acepta Nulo (Solo texto)
	@Size(min = 4, max = 20, message = "Inserte texto mayor a 4 caracteres")
	private String use_name;
	
	@NotBlank(message = "Ingrese clave") //no acepta Nulo (Solo texto)
	@Size(min = 4, max = 20, message = "Inserte texto mayor a 4 caracteres")
	private String use_password;
	
	private String use_pass_crypt;
	
	//@NotBlank //no acepta Nulo (Solo texto)
	private String use_photo;
		
	private long use_registration_date;
	
	@NotBlank
	private String use_employment_id;
	
	@NotBlank
	private String use_roles_id;
	
	//@AssertFalse //Propiedad Boolean TRUE 
	private boolean use_status;
	
	public Users() {}
	
	public Users(String use_id) {
		this.use_id = use_id;
	}

	

	public Users(String use_id, String use_first_name, String use_last_name, String use_address, String use_email, 
			String use_phone, String use_name, String use_password, String use_pass_crypt, String use_photo, 
			long use_registration_date, String use_employment_id, String use_roles_id, boolean use_status) {
		this.use_id = use_id;
		this.use_first_name = use_first_name;
		this.use_last_name = use_last_name;
		this.use_address = use_address;
		this.use_email = use_email;
		this.use_phone = use_phone;
		this.use_name = use_name;
		this.use_password = use_password;
		this.use_pass_crypt = use_pass_crypt;
		this.use_photo = use_photo;
		this.use_registration_date = use_registration_date;
		this.use_employment_id = use_employment_id;
		this.use_roles_id = use_roles_id;
		this.use_status = use_status;
	}

	public String getUse_id() {
		return use_id;
	}

	public void setUse_id(String use_id) {
		this.use_id = use_id;
	}

	public String getUse_first_name() {
		return use_first_name;
	}

	public void setUse_first_name(String use_first_name) {
		this.use_first_name = use_first_name;
	}

	public String getUse_last_name() {
		return use_last_name;
	}

	public void setUse_last_name(String use_last_name) {
		this.use_last_name = use_last_name;
	}

	public String getUse_address() {
		return use_address;
	}

	public void setUse_address(String use_address) {
		this.use_address = use_address;
	}

	public String getUse_email() {
		return use_email;
	}

	public void setUse_email(String use_email) {
		this.use_email = use_email;
	}

	public String getUse_phone() {
		return use_phone;
	}

	public void setUse_phone(String use_phone) {
		this.use_phone = use_phone;
	}

	public String getUse_name() {
		return use_name;
	}

	public void setUse_name(String use_name) {
		this.use_name = use_name;
	}

	public String getUse_password() {
		return use_password;
	}

	public void setUse_password(String use_password) {
		this.use_password = use_password;
	}
	
	public String getUse_pass_crypt() {
		return use_pass_crypt;
	}

	public void setUse_pass_crypt(String use_pass_crypt) {
		this.use_pass_crypt = use_pass_crypt;
	}

	public String getUse_photo() {
		return use_photo;
	}

	public void setUse_photo(String use_photo) {
		this.use_photo = use_photo;
	}

	public long getUse_registration_date() {
		return use_registration_date;
	}

	public void setUse_registration_date(long use_registration_date) {
		this.use_registration_date = use_registration_date;
	}

	public String getUse_employment_id() {
		return use_employment_id;
	}

	public void setUse_employment_id(String use_employment_id) {
		this.use_employment_id = use_employment_id;
	}

	public String getUse_roles_id() {
		return use_roles_id;
	}

	public void setUse_roles_id(String use_roles_id) {
		this.use_roles_id = use_roles_id;
	}

	public boolean getUse_status() {
		return use_status;
	}

	public void setUse_status(boolean use_status) {
		this.use_status = use_status;
	}

	@Override
	public String toString() {
		return "Users [use_id=" + use_id + ", use_first_name=" + use_first_name + ", use_last_name=" + use_last_name
				+ ", use_address=" + use_address + ", use_email=" + use_email + ", use_phone=" + use_phone
				+ ", use_name=" + use_name + ", use_password=" + use_password + ", use_pass_crypt=" + use_pass_crypt
				+ ", use_photo=" + use_photo + ", use_registration_date=" + use_registration_date
				+ ", use_employment_id=" + use_employment_id + ", use_roles_id=" + use_roles_id + ", use_status="
				+ use_status + "]";
	}
}