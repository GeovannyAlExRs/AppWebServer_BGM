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
import com.ec.busgeomap.web.app.model.Schedule;
import com.ec.busgeomap.web.app.service.ServiceAsigneBus;
import com.ec.busgeomap.web.app.service.ServiceRoute;
import com.ec.busgeomap.web.app.service.ServiceSchedule;

@Controller
@RequestMapping("/app_busgeomap")
public class ControllerSchedule {
	
	private final Log log = LogFactory.getLog(getClass());
	
	private final String TAB_LIST_BGM = "listTabBgm";
	private final String TAB_FORM_BGM = "formTabBgm";
	
	@Autowired
	ServiceSchedule serviceSchedule;
	
	@Autowired
	ServiceRoute serviceRoute;
	
	@Autowired
	ServiceAsigneBus serviceAsigneBus;
	@GetMapping("/schedule")
	public String viewSchedule(Model model) throws InterruptedException, ExecutionException {
		log.info("INICIAR MODULO PLANIFICACION DE HORARIOS");
		
		addAttribute(model, new Schedule(serviceSchedule.autoIdDocument()), TAB_LIST_BGM);
		
		return Pages.SCHEDULE;
	}
	
	@PostMapping("/schedule")
	public String saveSchedule(@Valid @ModelAttribute("sch") Schedule sch, BindingResult result, Model model) throws InterruptedException, ExecutionException {
		log.info("CREAR NUEVA PLANIFICACION : " + sch.getSch_id());
		
		if (result.hasErrors()) {
			addAttribute(model, sch, TAB_FORM_BGM);
			model.addAttribute("errorSave", "Error al guardar, complete los datos");
		} else {
			try {
				serviceSchedule.createSchedule(sch);
				// New Document USERS with auto ID (autoIdDocumentUser).
				addAttribute(model, new Schedule(serviceSchedule.autoIdDocument()), TAB_LIST_BGM);
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				
				addAttribute(model, sch, TAB_FORM_BGM);
			}
			
		}
		
		log.info("*** GUARDAR ROUTE ***");
		
		return Pages.SCHEDULE;
	}
	
	private void addAttribute(Model model, Schedule schedule, String tab)  throws InterruptedException, ExecutionException {
		model.addAttribute("schedule", schedule);
		model.addAttribute("iRoute", serviceRoute.readAllRoute()); //
		model.addAttribute("iDisco", serviceAsigneBus.readAssignesBusByDisc());
		model.addAttribute("schList", serviceSchedule.readAllSchedule());
		model.addAttribute(tab, "active"); 
	}

}
