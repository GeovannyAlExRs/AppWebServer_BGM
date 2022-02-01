package com.ec.busgeomap.web.app.controller;

import java.util.concurrent.ExecutionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.ec.busgeomap.web.app.config.Pages;
import com.ec.busgeomap.web.app.model.ReCaptchaResponse;
import com.ec.busgeomap.web.app.service.ServiceUsers;

@Controller
@RequestMapping("/app_busgeomap")
public class ControllerHome {
	
	private final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	ServiceUsers serviceUsers;

	@Value("${recaptcha.secret}")
	private String recaptchaSecret;
	
	@Value("${recaptcha.secret}")
	private String recaptchaServiceURL;
	
	@Autowired
	private RestTemplate restTemplate;
	
	String messageCaptcha;
	
	@GetMapping("/home")
	public String viewHome(Model model) throws InterruptedException, ExecutionException {
		log.info("PANTALLA PRINCIPAL");
		
		return Pages.HOME;
	}
	
	@GetMapping("/login")
	public String viewLogin(Model model) throws InterruptedException, ExecutionException {
		log.info("INICIAR SESION LOGIN");		
		
		//restTemplate
		return Pages.LOGIN;
	}
	
	/*@PostMapping("/login")
	public String viewToLogin(Model model, @RequestParam(name = "g-recaptcha-response") String captchaResponse) throws InterruptedException, ExecutionException {
		log.info("INICIAR SESION LOGIN");		
		String params = recaptchaSecret +"&response="+ captchaResponse;
		ReCaptchaResponse reCaptchaResponse = restTemplate.exchange(recaptchaServiceURL+params, HttpMethod.POST, null, ReCaptchaResponse.class).getBody();
		
		if (!reCaptchaResponse.isSuccess()) {
			try {
				 
				System.out.println("CAPTCHA " + reCaptchaResponse);
				
			} catch (Exception e1) {
				messageCaptcha = " Por favor, seleccione capthca";
				model.addAttribute("messageCaptcha", messageCaptcha);
			}
		}
		return Pages.LOGIN;
	}*/	
}