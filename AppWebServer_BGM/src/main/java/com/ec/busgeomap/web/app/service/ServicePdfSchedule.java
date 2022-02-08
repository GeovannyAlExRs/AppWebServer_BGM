package com.ec.busgeomap.web.app.service;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.ec.busgeomap.web.app.model.Schedule;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class ServicePdfSchedule {
	
	@Autowired
	ServiceSchedule serviceSchedule;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	// Exportar la lista de Usuarios
	public void pdfReportSchedule(HttpServletResponse servletResponse) throws DocumentException, IOException, InterruptedException, ExecutionException, ParseException {
		
		Document doc = new Document(PageSize.LETTER.rotate());
		doc.setMargins(-50, -50, 5, 10);
		PdfWriter.getInstance(doc, servletResponse.getOutputStream());
		
		doc.open();
		
		Resource fileResourceHeader = resourceLoader.getResource("classpath:static/img/image/vector/bgm_encabezado.png");
				
		writeImage(doc, fileResourceHeader.getFile(),790, 60);
		
		writeEncabezado(doc, null, 1);

		writeTableReport(doc, null, 1);
		
		doc.close();
	}

	// Exportar la lista de Usuarios
	public void pdfReportFilterSchedule(HttpServletResponse servletResponse, String dateFilter) throws DocumentException, IOException, InterruptedException, ExecutionException, ParseException {
		
		Document doc = new Document(PageSize.LETTER.rotate());
		doc.setMargins(-50, -50, 5, 10);
		PdfWriter.getInstance(doc, servletResponse.getOutputStream());
		
		doc.open();
		
		Resource fileResourceHeader = resourceLoader.getResource("classpath:static/img/image/vector/bgm_encabezado.png");
				
		writeImage(doc, fileResourceHeader.getFile(),790, 60);
		
		writeEncabezado(doc, dateFilter, 2);

		writeTableReport(doc, dateFilter, 2);
		
		doc.close();
	}
		
	private void writeImage(Document doc, File file, int width, int heigth) throws IOException {
		Image img = Image.getInstance(file.toString());
		
		img.setAlignment(Element.ALIGN_CENTER);
		img.scaleAbsolute(width,heigth);
		
		doc.add(img);
	}
	private void writeEncabezado(Document doc, String dateFilter, int option) {
		
		PdfPCell cell = new PdfPCell();
		
		PdfPTable table = new PdfPTable(1);
		
		Font fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, new Color(17, 90, 135));
		
		String texto = "";
		
		if (option == 1) {
			texto = "Planificacion de horarios de transporte";
		} else {
			texto = "Planificacion de horarios de transporte correspondiente a " + dateFilter;
		}
				
		Color background = new Color(250, 250, 250);
		Color borderColor = new Color(255, 255, 255);
		
		cell =  textoEncabezado(cell, texto, fuente, 1, background, borderColor, 5);
		table.addCell(cell);
		doc.add(table);
		
		doc.add(new Phrase("\n"));
	}

	private void writeTableReport(Document doc, String dateFilter, int option) throws InterruptedException, ExecutionException, ParseException {
		// ENCABEZADO
		Font fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, new Color(17, 90, 135));
				
		PdfPCell cell = new PdfPCell();
					
		PdfPTable table = new PdfPTable(5);
		table.setWidths(new float[] {1.0f, 3.0f, 2.0f, 1.5f, 1.5f});
				
		Color background = new Color(245, 245, 245);
		Color borderColor = new Color(227, 227, 227);
				
		cell = textoEncabezado(cell, "Disco", fuente, 1, background, borderColor, 10);
		table.addCell(cell);
		cell = textoEncabezado(cell, "Ruta - Origen/Destino", fuente, 1, background, borderColor, 10);
		table.addCell(cell);
		cell = textoEncabezado(cell, "Fecha/Hora Registro", fuente, 1, background, borderColor, 10);
		table.addCell(cell);
		cell = textoEncabezado(cell, "Fecha Salida", fuente, 1, background, borderColor, 10);
		table.addCell(cell);
		cell = textoEncabezado(cell, "Hora Salida", fuente, 1, background, borderColor, 10);
		table.addCell(cell);

		if (option == 1) {
			writeTableReportDBFirebase(doc, table, cell, borderColor);
		} else {
			writeTableReportDBFirebaseFilter(doc, dateFilter,table, cell, borderColor);
		}
		
	}
	
	private void writeTableReportDBFirebase(Document doc, PdfPTable table, PdfPCell cell, Color border) throws InterruptedException, ExecutionException {
		
		Font fuenteData = FontFactory.getFont(FontFactory.HELVETICA, 10, new Color(25, 25, 25));
		Font fuenteDisco = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new Color(25, 25, 25));
		Color backgroundDate = new Color(251, 251, 251);
		
		ArrayList<Schedule> list = serviceSchedule.readAllSchedule();
		
		DateFormat formatRegistro = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		DateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat formatTime = new SimpleDateFormat("HH:mm");
		
		for (Schedule sch : list) {
			
			cell = textoEncabezado(cell, sch.getSch_asb_id_number_disc(), fuenteDisco, 1, backgroundDate, border, 3);
			table.addCell(cell);
			
			cell = textoTable(cell, sch.getSch_rou_id_name(), fuenteData, 1, backgroundDate, border, 3);
			table.addCell(cell);
			
			String fecharegistro = formatRegistro.format(sch.getSch_registration_date());
			cell = textoEncabezado(cell, fecharegistro, fuenteData, 1, backgroundDate, border, 3);
			table.addCell(cell);
						
			String fechaDate = formatDate.format(sch.getSch_departure_time());
			cell = textoEncabezado(cell, fechaDate, fuenteData, 1, backgroundDate, border, 3);
			table.addCell(cell);
			
			
			String fechaTime = formatTime.format(sch.getSch_departure_time());
			cell = textoEncabezado(cell, fechaTime, fuenteData, 1, backgroundDate, border, 3);
			table.addCell(cell);
		}
		doc.add(table);		
	}
	
	private void writeTableReportDBFirebaseFilter(Document doc, String departure_time, PdfPTable table, PdfPCell cell, Color border) throws InterruptedException, ExecutionException, ParseException {
		
		Font fuenteData = FontFactory.getFont(FontFactory.HELVETICA, 10, new Color(25, 25, 25));
		Font fuenteDisco = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new Color(25, 25, 25));
		Color backgroundDate = new Color(251, 251, 251);
		
		ArrayList<Schedule> list = serviceSchedule.readAllScheduleByDate(departure_time);
		
		DateFormat formatRegistro = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		DateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat formatTime = new SimpleDateFormat("HH:mm");
		
		for (Schedule sch : list) {
			
			cell = textoEncabezado(cell, sch.getSch_asb_id_number_disc(), fuenteDisco, 1, backgroundDate, border, 3);
			table.addCell(cell);
			
			cell = textoTable(cell, sch.getSch_rou_id_name(), fuenteData, 1, backgroundDate, border, 3);
			table.addCell(cell);
			
			String fecharegistro = formatRegistro.format(sch.getSch_registration_date());
			cell = textoEncabezado(cell, fecharegistro, fuenteData, 1, backgroundDate, border, 3);
			table.addCell(cell);
						
			String fechaDate = formatDate.format(sch.getSch_departure_time());
			cell = textoEncabezado(cell, fechaDate, fuenteData, 1, backgroundDate, border, 3);
			table.addCell(cell);
			
			
			String fechaTime = formatTime.format(sch.getSch_departure_time());
			cell = textoEncabezado(cell, fechaTime, fuenteData, 1, backgroundDate, border, 3);
			table.addCell(cell);
		}
		doc.add(table);		
	}
	
	private PdfPCell textoEncabezado(PdfPCell cell, String text, Font font, int border, Color background, Color borderColor, int padding) {
		
		cell.setPhrase(new Phrase(text, font));
		
		cell.setBorder(border);
		cell.setBorderColor(borderColor);
		cell.setBackgroundColor(background); 
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		cell.setPadding(padding);
		
		return cell;
	}

	private PdfPCell textoTable(PdfPCell cell, String text, Font font, int border, Color background, Color borderColor, int padding) {
	
		cell.setPhrase(new Phrase(text, font));
		
		cell.setBorder(border);
		cell.setBorderColor(borderColor);
		cell.setBackgroundColor(background); 
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		cell.setPadding(padding);
		
		return cell;
	}
}