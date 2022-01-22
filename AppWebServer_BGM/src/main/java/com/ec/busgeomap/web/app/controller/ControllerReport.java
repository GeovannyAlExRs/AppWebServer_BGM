package com.ec.busgeomap.web.app.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ec.busgeomap.web.app.config.Pages;
import com.ec.busgeomap.web.app.model.Bus;
import com.ec.busgeomap.web.app.model.CodeQR;
import com.ec.busgeomap.web.app.model.Schedule;
import com.ec.busgeomap.web.app.model.Users;
import com.ec.busgeomap.web.app.service.ServiceBus;
import com.ec.busgeomap.web.app.service.ServiceCodeQR;
import com.ec.busgeomap.web.app.service.ServicePdfBus;
import com.ec.busgeomap.web.app.service.ServicePdfSchedule;
import com.ec.busgeomap.web.app.service.ServicePdfUser;
import com.ec.busgeomap.web.app.service.ServiceSchedule;
import com.ec.busgeomap.web.app.service.ServiceUsers;
import com.lowagie.text.DocumentException;

@Controller
@RequestMapping("/app_busgeomap")
public class ControllerReport {
	
	private final Log log = LogFactory.getLog(getClass());
	
	private final String TAB_BGM_USE = "userTabBgm";
	
	@Autowired
	ServiceUsers serviceUser;
	
	@Autowired
	ServiceBus serviceBus;
	
	@Autowired
	ServiceCodeQR serviceCodeQR;
	
	@Autowired
	ServiceSchedule serviceSchedule;
	
	@Autowired
	ServicePdfUser servicePdfUSer;
	
	@Autowired
	ServicePdfBus servicePdfBus;
	
	@Autowired
	ServicePdfSchedule servicePdfSchedule;
	
	@GetMapping("/report")
	public String viewReportPDF(Model model) throws Exception {
		log.info("INICIAR REPORTES");
		
		addAttribute(model, TAB_BGM_USE);
		
		return Pages.REPORT;
	}
	
	private void addAttribute(Model model, String tab) throws Exception {
		model.addAttribute(tab, "active"); 
		
		ArrayList<Users> listUser = serviceUser.readAllUsers();
		model.addAttribute("userList", listUser);//idUserCount
		model.addAttribute("idUserCount", listUser.size());
		
		ArrayList<Bus> listBus = serviceBus.readAllBus();
		model.addAttribute("busList", listBus);
		model.addAttribute("idBusCount", listBus.size());
		
		ArrayList<CodeQR> listQR = serviceCodeQR.readAllQR();
		model.addAttribute("qrList", listQR);
		model.addAttribute("idQRCount", listQR.size());

		ArrayList<Schedule> listSch = serviceSchedule.readAllSchedule();
		model.addAttribute("schList", listSch);
		model.addAttribute("idSchCount", listSch.size());
	}
	
	
	@GetMapping(path = {"/pdf_user"}, name = "use_id")
	public void exporPdfUser(@RequestParam(defaultValue = "1", name = "use_id", required = false) String use_id, HttpServletResponse servletResponse) throws DocumentException, IOException, InterruptedException, ExecutionException {
		servletResponse.setContentType("application/pdf");
		
		Users u = serviceUser.readByIdDoc(use_id);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDate = dateFormat.format(new Date());
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename="+ use_id +"-" + currentDate +".pdf";
		
		servletResponse.setHeader(headerKey, headerValue);
	
		servicePdfUSer.exportPDF(u, servletResponse);
	}
	
	@GetMapping("/pdf_report_user")  
	public void exportListUSerPdf(HttpServletResponse servletResponse) throws InterruptedException, ExecutionException, DocumentException, IOException {
		
		servletResponse.setContentType("application/pdf");
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDate = dateFormat.format(new Date());
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename= Report_USERS_" + currentDate +".pdf";
		
		servletResponse.setHeader(headerKey, headerValue);
		
		servicePdfUSer.pdfReportListUser(servletResponse);
	}
	
	@GetMapping(path = {"/pdf_bus"}, name = "bus_id")
	public void exporPdfBus(@RequestParam(defaultValue = "1", name = "bus_id", required = false) String bus_id, HttpServletResponse servletResponse) throws DocumentException, IOException, InterruptedException, ExecutionException {
		servletResponse.setContentType("application/pdf");
		
		Bus bus = serviceBus.readByIdDoc(bus_id);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDate = dateFormat.format(new Date());
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename="+ bus_id +"-" + currentDate +".pdf";
		
		servletResponse.setHeader(headerKey, headerValue);
	
		servicePdfBus.exportPDF(bus, servletResponse);
	}
	
	@GetMapping("/pdf_report_bus")  
	public void exportListBusPdf(HttpServletResponse servletResponse) throws InterruptedException, ExecutionException, DocumentException, IOException {
		
		servletResponse.setContentType("application/pdf");
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDate = dateFormat.format(new Date());
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename= Report_BUS_" + currentDate +".pdf";
		
		servletResponse.setHeader(headerKey, headerValue);
		
		servicePdfBus.pdfReportBus(servletResponse);

	}
	
	@GetMapping("/pdf_report_schedule")  
	public void exportListSchPdf(HttpServletResponse servletResponse) throws InterruptedException, ExecutionException, DocumentException, IOException {
		
		servletResponse.setContentType("application/pdf");
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDate = dateFormat.format(new Date());
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename= Report_SCHEDULE_" + currentDate +".pdf";
		
		servletResponse.setHeader(headerKey, headerValue);
		
		servicePdfSchedule.pdfReportSchedule(servletResponse);;

	}
}