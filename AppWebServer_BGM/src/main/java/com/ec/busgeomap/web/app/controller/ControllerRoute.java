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
import com.ec.busgeomap.web.app.model.Route;
import com.ec.busgeomap.web.app.service.ServiceRoute;

@Controller
@RequestMapping("/app_busgeomap")
public class ControllerRoute {

	private final Log log = LogFactory.getLog(getClass());
	private final String TAB_LIST_BGM = "listTabBgm";
	//private final String TAB_FORM_BGM = "formTabBgm";
	
	@Autowired
	ServiceRoute serviceRoute;
	
	@GetMapping("/route")
	public String viewRole(Model model) throws InterruptedException, ExecutionException {
		log.info("INICIAR MODULO R");
		
		addAttributeRoute(model, new Route(serviceRoute.autoIdDocumentRoute()), TAB_LIST_BGM);
		
		return Pages.ROUTE;
	}
	
	private void addAttributeRoute(Model model, Route route, String tab)  throws InterruptedException, ExecutionException {
		model.addAttribute("route", route);
		model.addAttribute("routeList", serviceRoute.readAllRoute());
		model.addAttribute(tab, "active"); 
	}
}
