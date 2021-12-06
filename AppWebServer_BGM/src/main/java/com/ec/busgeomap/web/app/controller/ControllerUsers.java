package com.ec.busgeomap.web.app.controller;

import java.util.concurrent.ExecutionException;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ec.busgeomap.web.app.config.Pages;
import com.ec.busgeomap.web.app.model.Users;
import com.ec.busgeomap.web.app.service.ServiceEmployment;
import com.ec.busgeomap.web.app.service.ServiceRole;
import com.ec.busgeomap.web.app.service.ServiceUsers;

@Controller
@RequestMapping("/app_busgeomap")
public class ControllerUsers {
	
	private final Log log = LogFactory.getLog(getClass());
	private final String TAB_LIST_BGM = "listTabBgm";
	private final String TAB_FORM_BGM = "formTabBgm";
	
	@Autowired
	ServiceUsers serviceUsers;
	
	@Autowired
	ServiceRole serviceRole;

	@Autowired
	ServiceEmployment serviceEmployment;
	
	@GetMapping("/users")
	public String viewUsers(Model model) throws InterruptedException, ExecutionException {
		log.info("PERSONAL DE LA COOPERATIVA");
		
		addAttribute(model, new Users(serviceUsers.autoIdDocumentUsers()), TAB_LIST_BGM);
		
		return Pages.USERS;
	}
	
	@PostMapping("/users")
	public String saveUser(@Valid @ModelAttribute("users") Users users, BindingResult result, Model model) throws InterruptedException, ExecutionException {
		log.info("CREAR NUEVO REGISTRO USERS : " + users.getUse_name());
		
		if (result.hasErrors()) {
			addAttribute(model, users, TAB_FORM_BGM);
			model.addAttribute("errorSave", "Error al guardar, complete los datos");
		} else {
			try {
				serviceUsers.createUsers(users);
				
				// New Document USERS with auto ID (autoIdDocumentUser).
				addAttribute(model, new Users(serviceUsers.autoIdDocumentUsers()), TAB_LIST_BGM);
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				
				addAttribute(model, users, TAB_FORM_BGM);
			}
			
		}
		
		log.info("*** GUARDAR USUARIO ***");
		
		return Pages.USERS;
	}
	
	@GetMapping("/edit_users/{use_id}")
	public String getEditUsers(@PathVariable(name = "use_id") String use_id, Model model) throws InterruptedException, ExecutionException {
		log.info("EDITAR ROL : " + use_id);
		
		Users users = serviceUsers.readByIdDoc(use_id);
		
		addAttribute(model, users, TAB_FORM_BGM);
		
		model.addAttribute("editMode", "true");
		
		return Pages.USERS;
	}
	
	@PostMapping("/edit_users")
	public String updateUsers(@Valid @ModelAttribute("users") Users users, BindingResult result, Model model) throws InterruptedException, ExecutionException {
		log.info("ACTUALIZAR ROL : " + users.getUse_id());
		
		if (result.hasErrors()) {
			
			addAttribute(model, users, TAB_FORM_BGM);
			model.addAttribute("editMode", "true");
			
			model.addAttribute("errorSave", "Error al guardar, complete los datos");
			System.err.println("**** ERROR... CAMPOS VACIOS *** ");
			
		}else {
			try {
				serviceUsers.updateUsers(users);
				model.addAttribute("msgSuccess", "El USUARIO "+ users.getUse_name() + " fue Actualizado correctamente.");
				
				// New Document ROL with auto ID (autoIdDocumentUser).
				addAttribute(model, new Users(serviceUsers.autoIdDocumentUsers()), TAB_LIST_BGM);
								
			} catch (Exception e1) {
				model.addAttribute("formErrorMessage", e1.getMessage());
				
				model.addAttribute("editMode", "true");
				addAttribute(model, users, TAB_FORM_BGM);
			}
		}	

		return "redirect:users";
	}
	
	@GetMapping("/delete_users/{use_id}")
	public String deleteUsers(@PathVariable(name = "use_id") String use_id, Model model) throws InterruptedException, ExecutionException {
		
		try {
			serviceUsers.deleteUsers(use_id);
							
		} catch (Exception e1) {
			//model.addAttribute("deleteError", e1.getMessage());
			model.addAttribute("deleteError","El USUARIO no se pudo eliminar");
		}

		return viewUsers(model);
	}
	
	private void addAttribute(Model model, Users u, String tab)  throws InterruptedException, ExecutionException {
		model.addAttribute("u", u);
		model.addAttribute("userList", serviceUsers.readAllUsers());
		model.addAttribute("iRoles", serviceRole.readAllRol());
		model.addAttribute("iEmployment", serviceEmployment.readAllEmployment());
		model.addAttribute(tab, "active"); 
	}
}
