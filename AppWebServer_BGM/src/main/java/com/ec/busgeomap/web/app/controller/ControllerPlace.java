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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ec.busgeomap.web.app.config.Pages;
import com.ec.busgeomap.web.app.model.Place;
import com.ec.busgeomap.web.app.model.Roles;
import com.ec.busgeomap.web.app.service.ServicePlace;

@Controller
@RequestMapping("/app_busgeomap")
public class ControllerPlace {
	
	private final Log log = LogFactory.getLog(getClass());
	private final String TAB_LIST_BGM = "listTabBgm";
	private final String TAB_FORM_BGM = "formTabBgm";
	
	@Autowired
	ServicePlace servicePlace;

	@GetMapping("/place")
	public String viewRole(Model model) throws InterruptedException, ExecutionException {
		log.info("INICIAR MODULO ROLES");
		
		addAttributePlace(model, new Place(servicePlace.autoIdDocumentPlace()), TAB_LIST_BGM);
		
		return Pages.PLACE;
	}
	
	@PostMapping("/place")
	public String savePlace(@Valid @ModelAttribute("place") Place place, BindingResult result, Model model) throws InterruptedException, ExecutionException {
		log.info("CREAR NUEVO REGISTRO PLACE : " + place.getPla_name());
		
		
		log.info("*** GUARDAR PLACE ***");
		
		return Pages.PLACE;
	}
	
	
	private void addAttributePlace(Model model, Place place, String tab)  throws InterruptedException, ExecutionException {
		model.addAttribute("place", place);
		model.addAttribute("userList", servicePlace.readAllPlace());
		model.addAttribute(tab, "active"); 
	}
}
