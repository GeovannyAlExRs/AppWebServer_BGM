package com.ec.busgeomap.web.app.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ec.busgeomap.web.app.model.CodeQR;
import com.ec.busgeomap.web.app.service.ServiceCodeQR;
import com.ec.busgeomap.web.app.service.ServicePdfQR;
import com.lowagie.text.DocumentException;

@Controller
@RequestMapping("/app_busgeomap")
public class ControllerPdfQR {
	
	@Autowired
	ServicePdfQR servicePdf;

	@Autowired
	ServiceCodeQR serviceQR;
	
	@GetMapping(path = {"/pdf"}, name = "gqr_code")
	public void exporPdfQR(@RequestParam(defaultValue = "1", name = "gqr_code", required = false) String gqr_code, HttpServletResponse servletResponse) throws DocumentException, IOException, InterruptedException, ExecutionException {
		servletResponse.setContentType("application/pdf");
		
		System.err.println(" *** *** BUSCA EL ID DOCUMENT  *** ***\n >>> ID DOCUMENT : " + gqr_code);
		
		CodeQR qr = serviceQR.readByIdDoc(gqr_code);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDate = dateFormat.format(new Date());
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename="+ gqr_code +"-" + currentDate +".pdf";
		
		servletResponse.setHeader(headerKey, headerValue);
		
		servicePdf.exportPDF(qr, servletResponse);
	}
}
