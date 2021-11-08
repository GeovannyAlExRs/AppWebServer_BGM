package com.ec.busgeomap.web.app.controller;

import java.util.concurrent.ExecutionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ec.busgeomap.web.app.config.Pages;
import com.ec.busgeomap.web.app.model.Roles;

@Controller
@RequestMapping("/app_busgeomap")
public class ControllerRole {
	
	private final Log log = LogFactory.getLog(getClass());
	private final String TAB_LIST_BGM = "listTabBgm";
	//private final String TAB_FORM_BGM = "formTabBgm";

	@GetMapping("/role")
	public String viewRoles(Model model) throws InterruptedException, ExecutionException {
		log.info("INICIAR MODULO ROLES");
		
		addAttributeRoles(model, new Roles("ID_DOCUMENT"), TAB_LIST_BGM);
		
		return Pages.ROLES;
	}

	private void addAttributeRoles(Model model, Roles roles, String tab)  throws InterruptedException, ExecutionException {
		model.addAttribute("rol", roles);
		//model.addAttribute("userList", "lista"); // lista: serviceRoles.readAllRol()  
		model.addAttribute(tab, "active"); 
	}
}