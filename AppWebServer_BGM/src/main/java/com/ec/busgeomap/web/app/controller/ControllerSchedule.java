package com.ec.busgeomap.web.app.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String saveSchedule(@Valid @ModelAttribute("schedule") Schedule sch, BindingResult result, Model model, @RequestParam @DateTimeFormat(pattern="DD/MM/YYYY HH:mm") String sch_departure_time) throws InterruptedException, ExecutionException {
		log.info("CREAR NUEVA PLANIFICACION : " + sch.getSch_id());
		
		long datelong = convertDateTime(sch_departure_time);
		
		if (result.hasErrors()) {
			addAttribute(model, sch, TAB_FORM_BGM);
			model.addAttribute("errorSave", "Error al guardar, complete los datos");
		} else {
			try {
				serviceSchedule.createSchedule(sch, datelong);
				// New Document USERS with auto ID (autoIdDocumentUser).
				addAttribute(model, new Schedule(serviceSchedule.autoIdDocument()), TAB_LIST_BGM);
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				
				addAttribute(model, sch, TAB_FORM_BGM);
			}
			
		}
		
		log.info("*** GUARDAR PLANIFICACION ***");
		
		return Pages.SCHEDULE;
	}
	
	@GetMapping("/edit_schedule/{sch_id}")
	public String getEditSchedule(@PathVariable(name = "sch_id") String sch_id, Model model) throws InterruptedException, ExecutionException {
		log.info("EDITAR PLANIFICACION : " + sch_id);
		
		Schedule schedule = serviceSchedule.readByIdDoc(sch_id);
		
		addAttribute(model, schedule, TAB_FORM_BGM);
		
		model.addAttribute("editMode", "true");
		
		return Pages.SCHEDULE;
	}
	
	@PostMapping("/edit_schedule")
	public String updateSchedule(@Valid @ModelAttribute("schedule") Schedule schedule, BindingResult result, Model model, @RequestParam @DateTimeFormat(pattern="DD/MM/YYYY HH:mm") String sch_departure_time) throws InterruptedException, ExecutionException {
		log.info("ACTUALIZAR PLANIFICACION : " + schedule.getSch_id());
		
		long datelong = convertDateTime(sch_departure_time);
		
		if (result.hasErrors()) {
			
			addAttribute(model, schedule, TAB_FORM_BGM);
			model.addAttribute("editMode", "true");
			
			model.addAttribute("errorSave", "Error al guardar, complete los datos");
		}else {
			try {
				serviceSchedule.updateSchedule(schedule, datelong);
				model.addAttribute("msgSuccess", "PLANIFICACION "+ schedule.getSch_id() + " fue Actualizado correctamente.");
				
				// New Document ROL with auto ID (autoIdDocumentUser).
				//addAttribute(model, new Schedule(serviceSchedule.autoIdDocument()), TAB_LIST_BGM);
								
			} catch (Exception e1) {
				model.addAttribute("formErrorMessage", e1.getMessage());
				
				model.addAttribute("editMode", "true");
				addAttribute(model, schedule, TAB_FORM_BGM);
			}
		}	

		return "redirect:schedule";
	}
	
	private long convertDateTime(String departure_time) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			
			Date fecha = format.parse(departure_time);
			
			return fecha.getTime();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return 0;
	}

	private void addAttribute(Model model, Schedule schedule, String tab)  throws InterruptedException, ExecutionException {
		model.addAttribute("schedule", schedule);
		model.addAttribute(tab, "active"); 
		model.addAttribute("iRoute", serviceRoute.readAllRoute()); 
		model.addAttribute("iDisco", serviceAsigneBus.readAssignesBusByDisc());
		
		// cambiar el estado (FALSE) de las planificaciones desde la fecha actual
		try {
			serviceSchedule.changeSchedule();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Mostrar la lista actualizada
		model.addAttribute("schList", serviceSchedule.readAllSchedule());
	}
}