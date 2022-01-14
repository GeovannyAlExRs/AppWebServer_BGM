package com.ec.busgeomap.web.app.controller;

import java.util.Timer;
import java.util.TimerTask;
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
import com.ec.busgeomap.web.app.model.Bus;
import com.ec.busgeomap.web.app.service.ServiceBus;
import com.ec.busgeomap.web.app.service.ServiceUsers;

@Controller
@RequestMapping("/app_busgeomap")
public class ControllerBus {

	private final Log log = LogFactory.getLog(getClass());
	
	private final String TAB_LIST_BGM = "listTabBgm";
	private final String TAB_FORM_BGM = "formTabBgm";
	
	private static final long LOAD_TIME = 2500;
	
	@Autowired
	ServiceBus serviceBus;
	
	@Autowired
	ServiceUsers serviceUsers;
	
	@GetMapping("/bus")
	public String viewBus(Model model) throws InterruptedException, ExecutionException {
		log.info("INICIAR MODULO UNIDADES DE TRANSPORTE");
		
		addAttributeBus(model, new Bus(serviceBus.autoIdDocument()), TAB_LIST_BGM);
		
		return Pages.COLECTIVE;
	}
	
	@PostMapping("/bus")
	public String saveBus(@Valid @ModelAttribute("bus") Bus bus, BindingResult result, Model model) throws InterruptedException, ExecutionException {
		log.info("CREAR NUEVO REGISTRO BUS DISCO : " + bus.getBus_number_disc() + " - ID : " +bus.getBus_id());
		
		if (result.hasErrors()) {
			addAttributeBus(model, bus, TAB_FORM_BGM);
			model.addAttribute("errorSave", "Error al guardar, complete los datos");
		} else {
			try {
				serviceBus.createBus(bus);
				loadTime();
				// New Document BUS with auto ID (autoIdDocumentUser).
				addAttributeBus(model, new Bus(serviceBus.autoIdDocument()), TAB_LIST_BGM);
				
				//model.addAttribute("msgSuccess", "El Colectivo " + bus.getBus_make() + " "+ bus.getBus_model() + " fue guardado correctamente.");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				
				addAttributeBus(model, bus, TAB_FORM_BGM);
			}
		}
		
		log.info("*** GUARDAR BUS ***");
		
		return Pages.COLECTIVE;
	}
	
	@GetMapping("/edit_bus/{bus_id}")
	public String getEditBus(@PathVariable(name = "bus_id") String bus_id, Model model) throws InterruptedException, ExecutionException {
		log.info("EDITAR COLECTIVO : " + bus_id);
		
		Bus bus = serviceBus.readByIdDoc(bus_id);
		
		addAttributeBus(model, bus, TAB_FORM_BGM);
		
		model.addAttribute("editMode", "true");
		
		return Pages.COLECTIVE;
	}
	
	@PostMapping("/edit_bus")
	public String updateBus(@Valid @ModelAttribute("bus") Bus bus, BindingResult result, Model model) throws InterruptedException, ExecutionException {
		log.info("ACTUALIZAR BUS DISCO : " + bus.getBus_number_disc());
		
		if (result.hasErrors()) {
			
			addAttributeBus(model, bus, TAB_FORM_BGM);
			model.addAttribute("editMode", "true");
			
			model.addAttribute("errorSave", "Error al guardar, complete los datos");
		}else {
			try {
				serviceBus.updateBus(bus);
				model.addAttribute("msgSuccess", "EL BUS DISCO"+ bus.getBus_number_disc() + " fue Actualizado correctamente.");
				
				// New Document ROL with auto ID (autoIdDocumentUser).
				addAttributeBus(model, new Bus(serviceBus.autoIdDocument()), TAB_LIST_BGM);
								
			} catch (Exception e1) {
				model.addAttribute("formErrorMessage", e1.getMessage());
				
				model.addAttribute("editMode", "true");
				addAttributeBus(model, bus, TAB_FORM_BGM);
			}
		}	

		return "redirect:bus";
	}
	
	@GetMapping("/delete_bus/{bus_id}")
	public String deleteRole(@PathVariable(name = "bus_id") String bus_id, Model model) throws InterruptedException, ExecutionException {
		
		try {
			serviceBus.deleteBus(bus_id);
			log.info("(BUS) : REGISTRO ELIMINADO");
		} catch (Exception e1) {
			//model.addAttribute("deleteError", e1.getMessage());
			model.addAttribute("deleteError","El COLECTIVO no se pudo eliminar");
		}

		return viewBus(model);
	}
	
	private void addAttributeBus(Model model, Bus bus, String tab)  throws InterruptedException, ExecutionException {
		log.info(" HOLA BUS");
		model.addAttribute("bus", bus);
		model.addAttribute("iUsers", serviceUsers.readAllUsers());
		model.addAttribute("busList", serviceBus.readAllBus());
		model.addAttribute(tab, "active"); 
	}
	
	private void loadTime() {
	    TimerTask timerTask = new TimerTask() {
	        @Override
	        public void run() {
	        	log.info("HICISTE UNA PAUSA DE " + LOAD_TIME);
	        }
	    };
	
	    Timer timer = new Timer();
	    timer.schedule(timerTask, LOAD_TIME);
		
	}
}
