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
		log.info("INICIAR MODULO RUTAS");
		
		addAttributeAssigneBus(model, new Assignes_Bus(serviceAssigneBus.autoIdDocument()), TAB_LIST_BGM);
		
		return Pages.ASSIGNE_BUS;
	}
	
	@PostMapping("/assigne_bus")
	public String saveRoles(@Valid @ModelAttribute("bus") Assignes_Bus bus, BindingResult result, Model model) throws InterruptedException, ExecutionException {
		log.info("ASIGNAR DE BUS: " + bus.getAsb_id());
		
		if (result.hasErrors()) {
			addAttributeAssigneBus(model, bus, TAB_FORM_BGM);
			model.addAttribute("errorSave", "Error al guardar, complete los datos");
		} else {
			try {
				serviceAssigneBus.createAssignesBus(bus);
				// New Document USERS with auto ID (autoIdDocumentUser).
				addAttributeAssigneBus(model, new Assignes_Bus(serviceAssigneBus.autoIdDocument()), TAB_LIST_BGM);
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				
				addAttributeAssigneBus(model, bus, TAB_FORM_BGM);
			}
		}
		
		log.info("*** GUARDAR ASIGNACION ***");
		
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
	
	@PostMapping("/edit_assigne_bus")
	public String updateAssigneBus(@Valid @ModelAttribute("bus") Assignes_Bus bus, BindingResult result, Model model) throws InterruptedException, ExecutionException {
		log.info("ACTUALIZAR COLECTIVO : " + bus.getAsb_id());
		
		if (result.hasErrors()) {
			
			addAttributeAssigneBus(model, bus, TAB_FORM_BGM);
			model.addAttribute("editMode", "true");
			
			model.addAttribute("errorSave", "Error al guardar, complete los datos");
			
		}else {
			try {
				serviceAssigneBus.updateAssigneBus(bus);
				model.addAttribute("msgSuccess", "El COLECTIVO fue Actualizado correctamente.");
				
				addAttributeAssigneBus(model, new Assignes_Bus(serviceAssigneBus.autoIdDocument()), TAB_LIST_BGM);
								
			} catch (Exception e1) {
				model.addAttribute("formErrorMessage", e1.getMessage());
				
				model.addAttribute("editMode", "true");
				addAttributeAssigneBus(model, bus, TAB_FORM_BGM);
			}
		}	

		return "redirect:assigne_bus";
	}
	
	@GetMapping("/delete_assignebus/{bus_id}")
	public String deleteBus(@PathVariable(name = "bus_id") String bus_id, Model model) throws InterruptedException, ExecutionException {
		
		try {
			serviceAssigneBus.deleteAssigneBus(bus_id);
			log.info("(PLACE) : REGISTRO ELIMINADO");
		} catch (Exception e1) {
			//model.addAttribute("deleteError", e1.getMessage());
			model.addAttribute("deleteError","La ASIGNACN no se pudo eliminar");
		}
		return viewAssigneBus(model);
	}
	
	private void addAttributeAssigneBus(Model model, Assignes_Bus bus, String tab) throws InterruptedException, ExecutionException {
		log.info("HOLA ASIGNAR BUS");
		model.addAttribute("bus", bus);
		
		model.addAttribute("busDiscList", serviceBus.readAllBus());
		model.addAttribute("driversList", serviceAssigneBus.readDriversOrAccompany("BGM_EMPL0yM3nTeR05"));
		model.addAttribute("accompanyList", serviceAssigneBus.readDriversOrAccompany("BGM_EMPL0yM3nTeR06"));
		model.addAttribute("busList", serviceAssigneBus.readAllAssignesBus());
		
		model.addAttribute(tab, "active");
	}
}
