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
import com.ec.busgeomap.web.app.model.Roles;
import com.ec.busgeomap.web.app.service.ServiceRole;

@Controller
@RequestMapping("/app_busgeomap")
public class ControllerRole {
	
	private final Log log = LogFactory.getLog(getClass());
	private final String TAB_LIST_BGM = "listTabBgm";
	private final String TAB_FORM_BGM = "formTabBgm";
	
	@Autowired
	ServiceRole serviceRole;

	@GetMapping("/role")
	public String viewRole(Model model) throws InterruptedException, ExecutionException {
		log.info("INICIAR MODULO ROLES");
		
		addAttributeRoles(model, new Roles(serviceRole.autoIdDocumentRole()), TAB_LIST_BGM);
		
		return Pages.ROLES;
	}

	@PostMapping("/role")
	public String saveRoles(@Valid @ModelAttribute("rol") Roles roles, BindingResult result, Model model) throws InterruptedException, ExecutionException {
		log.info("CREAR NUEVO REGISTRO ROL : " + roles.getRol_name());
		
		if (result.hasErrors()) {
			addAttributeRoles(model, roles, TAB_FORM_BGM);
			model.addAttribute("errorSave", "Error al guardar, complete los datos");
		} else {
			try {
				serviceRole.createRole(roles);
				
				// New Document USERS with auto ID (autoIdDocumentUser).
				addAttributeRoles(model, new Roles(serviceRole.autoIdDocumentRole()), TAB_LIST_BGM);
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				
				addAttributeRoles(model, roles, TAB_FORM_BGM);
			}
			
		}
		
		log.info("*** GUARDAR ROLE ***");
		
		return Pages.ROLES;
	}
	
	@GetMapping("/edit_role/{id_rol}")
	public String getEditRole(@PathVariable(name = "id_rol") String id_rol, Model model) throws InterruptedException, ExecutionException {
		log.info("EDITAR ROL : " + id_rol);
		
		Roles roles = serviceRole.readByIdDoc(id_rol);
		
		addAttributeRoles(model, roles, TAB_FORM_BGM);
		
		model.addAttribute("editMode", "true");
		
		return Pages.ROLES;
	}
	
	@PostMapping("/edit_role")
	public String updateRole(@Valid @ModelAttribute("rol") Roles roles, BindingResult result, Model model) throws InterruptedException, ExecutionException {
		log.info("ACTUALIZAR ROL : " + roles.getId_rol());
		
		if (result.hasErrors()) {
			
			addAttributeRoles(model, roles, TAB_FORM_BGM);
			model.addAttribute("editMode", "true");
			
			model.addAttribute("errorSave", "Error al guardar, complete los datos");
			System.err.println("**** ERROR... CAMPOS VACIOS *** ");
			
			// model.addAttribute("formErrorMessage", "Por favor seleccione la imagen");
			
		}else {
			try {
				serviceRole.updateRole(roles);
				model.addAttribute("msgSuccess", "El ROL "+ roles.getRol_name()+ " fue Actualizado correctamente.");
				
				// New Document ROL with auto ID (autoIdDocumentUser).
				addAttributeRoles(model, new Roles(serviceRole.autoIdDocumentRole()), TAB_LIST_BGM);
								
			} catch (Exception e1) {
				model.addAttribute("formErrorMessage", e1.getMessage());
				
				model.addAttribute("editMode", "true");
				addAttributeRoles(model, roles, TAB_FORM_BGM);
			}
		}	

		return "redirect:role";
	}
	
	@GetMapping("/delete_role/{id_rol}")
	public String deleteRole(@PathVariable(name = "id_rol") String id_rol, Model model) throws InterruptedException, ExecutionException {
		
		try {
			serviceRole.deleteRole(id_rol);
							
		} catch (Exception e1) {
			//model.addAttribute("deleteError", e1.getMessage());
			model.addAttribute("deleteError","El Rol no se pudo eliminar");
		}

		return viewRole(model);
	}
	
	private void addAttributeRoles(Model model, Roles roles, String tab)  throws InterruptedException, ExecutionException {
		model.addAttribute("rol", roles);
		model.addAttribute("userList", serviceRole.readAllRol());
		model.addAttribute(tab, "active"); 
	}
}