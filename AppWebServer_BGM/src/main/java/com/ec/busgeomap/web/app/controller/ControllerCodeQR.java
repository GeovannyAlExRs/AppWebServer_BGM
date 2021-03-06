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
import com.ec.busgeomap.web.app.model.CodeQR;
import com.ec.busgeomap.web.app.service.ServiceAsigneBus;
import com.ec.busgeomap.web.app.service.ServiceCodeQR;

@Controller
@RequestMapping("/app_busgeomap")
public class ControllerCodeQR {

	private final Log log = LogFactory.getLog(getClass());

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
	
	@PostMapping("/generator_qr") //codeqr
	public String generatorCodeQR(@Valid @ModelAttribute("codeqr") CodeQR codeqr, BindingResult result, Model model) throws InterruptedException, ExecutionException {
		log.info("GENERAR NUEVO CODIGO QR : " + codeqr.getGqr_code() + " " + codeqr.getGqr_description());
		
		if (result.hasErrors()) {
			addAttribute(model, codeqr);
			model.addAttribute("errorSave", "Error al guardar, complete los datos");
		} else {
			try {
				CodeQR qrID = serviceQR.generatorQR(codeqr);
				
				addAttribute(model, qrID);
				log.info("*** CODE QR GENERADO CON EXITO***");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				log.error("XXXXX ERROR AL GUARDAR CODE QR XXXXX");
				addAttribute(model, codeqr);
			}
		}
		
		return Pages.CODE_QR_GENERATOR;
	}

	@PostMapping("/codeqr")
	public String createCodeQR(@Valid @ModelAttribute("codeqr") CodeQR codeqr, BindingResult result, Model model) throws InterruptedException, ExecutionException {
		log.info("CREAR CODIGO QR : " + codeqr.getGqr_code() + " " + codeqr.getGqr_description());
		
		if (result.hasErrors()) {
			addAttribute(model, codeqr);
			model.addAttribute("errorSave", "Error al guardar, complete los datos");
		} else {
			try {
				serviceQR.createQR(codeqr);
				
				addAttribute(model, new CodeQR(serviceQR.autoIdDocument()));

			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				log.error("XXXXX ERROR AL GUARDAR CODE QR XXXXX");
				addAttribute(model, codeqr);
			}
		}
		
		log.info("*** GUARDAR CODE QR CON EXITO***");
		
		return viewCodeQR(model);
	}
	
	@GetMapping("/edit_qr/{gqr_code}")
	public String getEditCodeQR(@PathVariable(name = "gqr_code") String gqr_code, Model model) throws InterruptedException, ExecutionException {
		log.info("EDITAR QR : " + gqr_code);
		
		CodeQR qr = serviceQR.readByIdDoc(gqr_code);
		
		addAttribute(model, qr);
		
		model.addAttribute("editMode", "true");
		
		return Pages.CODE_QR_GENERATOR;
	}
	
	@PostMapping("/edit_qr")
	public String updateCodeQR(@Valid @ModelAttribute("codeqr") CodeQR qr, BindingResult result, Model model) throws InterruptedException, ExecutionException {
		log.info("ACTUALIZAR QR : " + qr);
		
		if (result.hasErrors()) {
			
			addAttribute(model, qr);
			model.addAttribute("editMode", "true");
			
			model.addAttribute("errorSave", "Error al guardar, complete los datos");
		}else {
			try {
				serviceQR.updateQR(qr);
				model.addAttribute("msgSuccess", "CODE QR"+ qr.getGqr_code() + " Actualizado.");
				
				// New Document ROL with auto ID (autoIdDocumentUser).
				addAttribute(model, new CodeQR(serviceQR.autoIdDocument()));
								
			} catch (Exception e1) {
				model.addAttribute("formErrorMessage", e1.getMessage());
				
				model.addAttribute("editMode", "true");
				addAttribute(model, qr);
			}
		}	

		return "redirect:codeqr";
	}
	
	@GetMapping("/delete_qr/{gqr_code}")
	public String deleteCodeQR(@PathVariable(name = "gqr_code") String gqr_code, Model model) throws InterruptedException, ExecutionException {
		try {
			serviceQR.deleteQR(gqr_code);
			log.info("(CODE QR) : REGISTRO ELIMINADO");
		} catch (Exception e) {
			model.addAttribute("deleteError", "El codigo QR no se pudo eliminar");
		}
		return viewCodeQR(model);
	}
	
	private void addAttribute(Model model, CodeQR codeQR)  throws InterruptedException, ExecutionException {
		log.info("HOLA CODE QR");
		
		model.addAttribute("codeqr", codeQR);
		model.addAttribute("itemcodeqr", serviceAsigneBus.readAssignesBusByDisc());
		model.addAttribute("qrList", serviceQR.readAllQR());
	}
}