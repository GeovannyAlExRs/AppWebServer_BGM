package com.ec.busgeomap.web.app.controller;

import java.util.concurrent.ExecutionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ec.busgeomap.web.app.config.Pages;
import com.ec.busgeomap.web.app.model.Users;
import com.ec.busgeomap.web.app.service.ServiceUsers;

@Controller
@RequestMapping("/app_busgeomap")
public class ControllerUsers {
	
	private final Log log = LogFactory.getLog(getClass());
	private final String TAB_LIST_BGM = "listTabBgm";
	//private final String TAB_FORM_BGM = "formTabBgm";
	
	@Autowired
	ServiceUsers serviceUsers;

	@GetMapping("/users")
	public String viewUsers(Model model) throws InterruptedException, ExecutionException {
		log.info("PERSONAL DE LA COOPERATIVA");
		
		addAttribute(model, new Users(serviceUsers.autoIdDocumentUsers()), TAB_LIST_BGM);
		
		return Pages.USERS;
	}
	
	private void addAttribute(Model model, Users u, String tab)  throws InterruptedException, ExecutionException {
		model.addAttribute("Users", u);
		//model.addAttribute("userList", serviceRole.readAllRol());
		model.addAttribute(tab, "active"); 
	}
}
