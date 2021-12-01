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
import com.ec.busgeomap.web.app.model.Place;
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
	public String viewPlace(Model model) throws InterruptedException, ExecutionException {
		log.info("INICIAR MODULO PLACE");
		
		addAttributePlace(model, new Place(servicePlace.autoIdDocumentPlace()), TAB_LIST_BGM);
		
		return Pages.PLACE;
	}
	
	@PostMapping("/place")
	public String savePlace(@Valid @ModelAttribute("place") Place place, BindingResult result, Model model) throws InterruptedException, ExecutionException {
		log.info("CREAR NUEVO REGISTRO PLACE : " + place.getPla_name());
		
		if (result.hasErrors()) {
			addAttributePlace(model, place, TAB_FORM_BGM);
			
			model.addAttribute("errorSave", "Error al guardar, complete los datos");
		} else {
			try {
				servicePlace.createPlace(place);
				addAttributePlace(model, new Place(servicePlace.autoIdDocumentPlace()), TAB_LIST_BGM);
			}catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				
				addAttributePlace(model, place, TAB_FORM_BGM);
			}
		}
		
		log.info("*** GUARDAR PLACE ***");
		
		return Pages.PLACE;
	}
	
	@GetMapping("/edit_place/{pla_id}")
	public String getEditPlace(@PathVariable(name = "pla_id") String pla_id, Model model) throws InterruptedException, ExecutionException {
		log.info("EDITAR PLACE : " + pla_id);
		
		Place place = servicePlace.readByIdDoc(pla_id);
		
		addAttributePlace(model, place, TAB_FORM_BGM);
		
		model.addAttribute("editMode", "true");
		
		return Pages.PLACE;
	}
	
	@PostMapping("/edit_place")
	public String updatePlace(@Valid @ModelAttribute("rol") Place place, BindingResult result, Model model) throws InterruptedException, ExecutionException {
		log.info("ACTUALIZAR ROL : " + place.getPla_id());
		
		if (result.hasErrors()) {
			addAttributePlace(model, place, TAB_FORM_BGM);
			model.addAttribute("editMode", "true");
			
			model.addAttribute("errorSave", "Error al guardar, complete los datos");
			System.err.println("**** ERROR... CAMPOS VACIOS *** ");
		} else {
			try {
				servicePlace.updatePlace(place);
				model.addAttribute("msgSuccess", "El ROL "+ place.getPla_name() + " fue Actualizado correctamente.");
				
				addAttributePlace(model, new Place(servicePlace.autoIdDocumentPlace()), TAB_LIST_BGM);
			} catch (Exception e1) {
				model.addAttribute("formErrorMessage", e1.getMessage());
				
				model.addAttribute("editMode", "true");
				addAttributePlace(model, place, TAB_FORM_BGM);
			}
		}
		return "redirect:place";
	}
	
	@GetMapping("/delete_place/{pla_id}")
	public String deletePlace(@PathVariable(name = "pla_id") String pla_id, Model model) throws InterruptedException, ExecutionException {
		try {
			servicePlace.deletePlace(pla_id);
		}catch (Exception e1) {
			model.addAttribute("deleteError", "El Rol no se pudo eliminar");
		}
		return viewPlace(model);
	}
	
	private void addAttributePlace(Model model, Place place, String tab)  throws InterruptedException, ExecutionException {
		model.addAttribute("place", place);
		model.addAttribute("placeList", servicePlace.readAllPlace());
		model.addAttribute(tab, "active"); 
	}
}