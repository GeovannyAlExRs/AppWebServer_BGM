package com.ec.busgeomap.web.app.controller;

import java.util.concurrent.ExecutionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ec.busgeomap.web.app.config.Pages;
import com.ec.busgeomap.web.app.model.Assignes_Bus;
import com.ec.busgeomap.web.app.service.ServiceAsigneBus;
import com.ec.busgeomap.web.app.service.ServiceBus;

@Controller
@RequestMapping("/app_busgeomap")
public class ControllerAsigneBus {

	private final Log log = LogFactory.getLog(getClass());
	private final String TAB_LIST_BGM = "listTabBgm";
	private final String TAB_FORM_BGM = "formTabBgm";
	
	@Autowired
	ServiceAsigneBus serviceAssigneBus;
	
	@Autowired
	ServiceBus serviceBus;
	
	@GetMapping("/assigne_bus")
	public String viewAssigneBus(Model model) throws InterruptedException, ExecutionException {
		log.info("Ingreso al registro de RUTAS");
		
		addAttributeAssigneBus(model, new Assignes_Bus(serviceAssigneBus.autoIdDocument()), TAB_LIST_BGM);
		
		return Pages.ASSIGNE_BUS;
	}
	
	@GetMapping("/edit_assigne_bus/{asb_id}")
	public String getEditAssigneBus(@PathVariable(name = "asb_id") String asb_id, Model model) throws InterruptedException, ExecutionException {
		log.info("EDITAR COLECTIVO : " + asb_id);
		
		Assignes_Bus assignes_Bus = serviceAssigneBus.readByIdDoc(asb_id);
				
		addAttributeAssigneBus(model, assignes_Bus, TAB_FORM_BGM);
		
		model.addAttribute("editMode", "true");
		
		return Pages.ASSIGNE_BUS;
	}
	
	@GetMapping("/delete_assignebus/{bus_id}")
	public String deleteBus(@PathVariable(name = "bus_id") String bus_id, Model model) throws InterruptedException, ExecutionException {
		
		try {
			System.err.println("ELIMINAR ASSIGNE BUS : " + bus_id);

			serviceAssigneBus.deleteAssigneBus(bus_id);
							
		} catch (Exception e1) {
			//model.addAttribute("deleteError", e1.getMessage());
			model.addAttribute("deleteError","La ASIGNACN no se pudo eliminar");
		}
		return viewAssigneBus(model);
	}
	
	private void addAttributeAssigneBus(Model model, Assignes_Bus bus, String tab) throws InterruptedException, ExecutionException {
		model.addAttribute("bus", bus);
		
		model.addAttribute("busList", serviceAssigneBus.readAllAssignesBus());
		model.addAttribute("busDiscList", serviceBus.readAllBus());
		model.addAttribute("driversList", serviceAssigneBus.readDriversOrAccompany("BGM_EMPL0yM3nTeR05"));
		model.addAttribute("accompanyList", serviceAssigneBus.readDriversOrAccompany("BGM_EMPL0yM3nTeR06"));
		
		model.addAttribute(tab, "active");
	}
}
