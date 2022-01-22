package com.ec.busgeomap.web.app.controller;

import java.util.concurrent.ExecutionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ec.busgeomap.web.app.config.Pages;


@Controller
@RequestMapping("/app_busgeomap")
public class ControllerNavegation {
	
	private final Log log = LogFactory.getLog(getClass());
	
	@GetMapping("/navegation")
	public String viewNavegation(Model model) throws InterruptedException, ExecutionException {
		log.info("::: MODULO DE NAVEGACION :::");
		
		
		return Pages.HEREMAPS;
	}

}
