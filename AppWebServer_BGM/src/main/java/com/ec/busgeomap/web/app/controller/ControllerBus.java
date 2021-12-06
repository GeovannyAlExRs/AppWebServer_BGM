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
import com.ec.busgeomap.web.app.model.Bus;
import com.ec.busgeomap.web.app.service.ServiceBus;

@Controller
@RequestMapping("/app_busgeomap")
public class ControllerBus {

	private final Log log = LogFactory.getLog(getClass());
	private final String TAB_LIST_BGM = "listTabBgm";
	//private final String TAB_FORM_BGM = "formTabBgm";
	
	@Autowired
	ServiceBus serviceBus;
	
	@GetMapping("/bus")
	public String viewBus(Model model) throws InterruptedException, ExecutionException {
		log.info("INICIAR MODULO UNIDADES DE TRANSPORTE");
		
		addAttributeBus(model, new Bus(serviceBus.autoIdDocument()), TAB_LIST_BGM);
		
		return Pages.COLECTIVE;
	}
	
	private void addAttributeBus(Model model, Bus bus, String tab)  throws InterruptedException, ExecutionException {
		//model.addAttribute("bus", bus);
		model.addAttribute("busList", serviceBus.readAllBus());
		model.addAttribute(tab, "active"); 
	}
}
