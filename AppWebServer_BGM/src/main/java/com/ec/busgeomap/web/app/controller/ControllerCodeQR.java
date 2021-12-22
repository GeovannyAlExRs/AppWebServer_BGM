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
import com.ec.busgeomap.web.app.model.CodeQR;
import com.ec.busgeomap.web.app.service.ServiceAsigneBus;
import com.ec.busgeomap.web.app.service.ServiceCodeQR;

@Controller
@RequestMapping("/app_busgeomap")
public class ControllerCodeQR {

	private final Log log = LogFactory.getLog(getClass());
	//private final String TAB_LIST_BGM = "listTabBgm";
	//private final String TAB_FORM_BGM = "formTabBgm";

	@Autowired
	ServiceAsigneBus serviceAsigneBus;

	@Autowired
	ServiceCodeQR serviceQR;
	
	@GetMapping("/codeqr")
	public String viewCodeQR(Model model) throws InterruptedException, ExecutionException {
		log.info("INICIAR MODULO GENERAR QR");
		
		addAttribute(model, new CodeQR(serviceQR.autoIdDocument()));
		
		return Pages.CODE_QR;
	}
	
	@PostMapping("/codeqr")
	public String saveCodeQR(@Valid @ModelAttribute("codeqr") CodeQR codeqr, BindingResult result, Model model) throws InterruptedException, ExecutionException {
		log.info("GENERAR NUEVO CODIGO QR : " + codeqr.getGqr_description());
		
		if (result.hasErrors()) {
			addAttribute(model, codeqr);
			model.addAttribute("errorSave", "Error al guardar, complete los datos");
		} else {
			try {
				serviceQR.createQR(codeqr);
				addAttribute(model, new CodeQR(serviceQR.autoIdDocument()));
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				
				addAttribute(model, codeqr);
			}
			
		}
		
		log.info("*** GUARDAR CODE QR***");
		
		return Pages.CODE_QR;
	}
	
	private void addAttribute(Model model, CodeQR codeQR)  throws InterruptedException, ExecutionException {
		model.addAttribute("qrList", serviceQR.readAllQR());
		model.addAttribute("codeqr", codeQR);
		model.addAttribute("itemcodeqr", serviceAsigneBus.readAssignesBusByDisc());
	}
}