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
import com.ec.busgeomap.web.app.model.Route;
import com.ec.busgeomap.web.app.service.ServiceRoute;

@Controller
@RequestMapping("/app_busgeomap")
public class ControllerRoute {

	private final Log log = LogFactory.getLog(getClass());
	private final String TAB_LIST_BGM = "listTabBgm";
	private final String TAB_FORM_BGM = "formTabBgm";
	
	@Autowired
	ServiceRoute serviceRoute;
	
	@GetMapping("/route")
	public String viewRoute(Model model) throws InterruptedException, ExecutionException {
		log.info("INICIAR MODULO RUTA");
		
		addAttributeRoute(model, new Route(serviceRoute.autoIdDocumentRoute()), TAB_LIST_BGM);
		
		return Pages.ROUTE;
	}
	
	@PostMapping("/route")
	public String saveRoute(@Valid @ModelAttribute("route") Route route, BindingResult result, Model model) throws InterruptedException, ExecutionException {
		log.info("CREAR NUEVA RUTA : " + route.getRou_name());
		
		if (result.hasErrors()) {
			addAttributeRoute(model, route, TAB_FORM_BGM);
			model.addAttribute("errorSave", "Error al guardar, complete los datos");
		} else {
			try {
				serviceRoute.createRoute(route);
				
				// New Document USERS with auto ID (autoIdDocumentUser).
				addAttributeRoute(model, new Route(serviceRoute.autoIdDocumentRoute()), TAB_LIST_BGM);
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				
				addAttributeRoute(model, route, TAB_FORM_BGM);
			}
			
		}
		
		log.info("*** GUARDAR ROUTE ***");
		
		return Pages.ROUTE;
	}
	
	@GetMapping("/edit_route/{rou_id}")
	public String getEditRole(@PathVariable(name = "rou_id") String rou_id, Model model) throws InterruptedException, ExecutionException {
		log.info("EDITAR RUTA : " + rou_id);
		
		Route route = serviceRoute.readByIdDoc(rou_id);
		
		addAttributeRoute(model, route, TAB_FORM_BGM);
		
		model.addAttribute("editMode", "true");
		
		return Pages.ROLES;
	}
	
	private void addAttributeRoute(Model model, Route route, String tab)  throws InterruptedException, ExecutionException {
		model.addAttribute("route", route);
		model.addAttribute("routeList", serviceRoute.readAllRoute());
		model.addAttribute(tab, "active"); 
	}
}
