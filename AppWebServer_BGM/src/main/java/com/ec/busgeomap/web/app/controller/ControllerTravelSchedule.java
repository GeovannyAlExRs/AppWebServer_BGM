package com.ec.busgeomap.web.app.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ec.busgeomap.web.app.config.Pages;
import com.ec.busgeomap.web.app.service.ServiceTravelSchedule;

@Controller
@RequestMapping("/app_busgeomap")
public class ControllerTravelSchedule {
	
	private final Log log = LogFactory.getLog(getClass());
	
	private final String TAB_LIST_BGM = "listTabBgm";
	
	@Autowired
	ServiceTravelSchedule serviceTravelSchedule;
	
	@GetMapping("/travel")
	public String viewSchedule(Model model) throws Exception {
		log.info("INICIAR HORARIOS DE TRANSPORTE");
		
		addAttribute(model, TAB_LIST_BGM);
		
		return Pages.TRAVEL;
	}

	private void addAttribute(Model model, String tab) throws Exception {
		model.addAttribute(tab, "active"); 
		model.addAttribute("upList", serviceTravelSchedule.readAllUpTravelSchedule());
		model.addAttribute("downList", serviceTravelSchedule.readAllDownTravelSchedule());
	}

}
