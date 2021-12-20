package com.ec.busgeomap.web.app.controller;

import java.util.concurrent.ExecutionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ec.busgeomap.web.app.config.Pages;
import com.ec.busgeomap.web.app.model.CodeQR;

@Controller
@RequestMapping("/app_busgeomap")
public class ControllerCodeQR {

	private final Log log = LogFactory.getLog(getClass());
	private final String TAB_LIST_BGM = "listTabBgm";
	//private final String TAB_FORM_BGM = "formTabBgm";

	@GetMapping("/codeqr")
	public String viewRole(Model model) throws InterruptedException, ExecutionException {
		log.info("INICIAR MODULO GENERAR QR");
		
		addAttribute(model, new CodeQR(), TAB_LIST_BGM);
		
		return Pages.CODE_QR;
	}
	
	private void addAttribute(Model model, CodeQR codeQR, String tab)  throws InterruptedException, ExecutionException {
		model.addAttribute("codeqr", codeQR);
		//model.addAttribute("qrList", serviceRole.readAllRol());
		model.addAttribute(tab, "active"); 
	}
}