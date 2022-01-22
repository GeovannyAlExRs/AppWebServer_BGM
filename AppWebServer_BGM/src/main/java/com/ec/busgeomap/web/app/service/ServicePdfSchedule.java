package com.ec.busgeomap.web.app.service;

import java.awt.Color;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ec.busgeomap.web.app.model.Schedule;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class ServicePdfSchedule {
	
	@Autowired
	ServiceSchedule serviceSchedule;
	
	// Exportar la lista de Usuarios
	public void pdfReportSchedule(HttpServletResponse servletResponse) throws DocumentException, IOException, InterruptedException, ExecutionException {
		
		Document doc = new Document(PageSize.LETTER.rotate());
		doc.setMargins(-30, -30, 40, 20);
		PdfWriter.getInstance(doc, servletResponse.getOutputStream());
		
		doc.open();
				
		writeEncabezado(doc);

		writeTableReport(doc);
		
		doc.close();
	}
	
	private void writeEncabezado(Document doc) {
		
		PdfPCell cell = new PdfPCell();
		
		PdfPTable table = new PdfPTable(1);
		
		Font fuente = FontFactory.getFont(FontFactory.COURIER_BOLD, 12, new Color(17, 90, 135));
		
		String texto = "Planificacion de horarios de transporte";
		Color background = new Color(250, 250, 250);
		Color borderColor = new Color(255, 255, 255);
		
		cell =  textoEncabezado(cell, texto, fuente, 1, background, borderColor, 5);
		table.addCell(cell);
		doc.add(table);
		
		doc.add(new Phrase("\n"));
	}

	private void writeTableReport(Document doc) throws InterruptedException, ExecutionException {
		// ENCABEZADO
		Font fuente = FontFactory.getFont(FontFactory.COURIER_BOLD, 10, new Color(17, 90, 135));
				
		PdfPCell cell = new PdfPCell();
					
		PdfPTable table = new PdfPTable(6);
		table.setWidths(new float[] {1.0f, 2.5f, 1.5f, 1.5f, 1.5f, 1.0f});
				
		Color background = new Color(245, 245, 245);
		Color borderColor = new Color(227, 227, 227);
				
		cell = textoEncabezado(cell, "Disco", fuente, 1, background, borderColor, 5);
		table.addCell(cell);
		cell = textoEncabezado(cell, "Ruta - Origen/Destino", fuente, 1, background, borderColor, 5);
		table.addCell(cell);
		cell = textoEncabezado(cell, "Fecha Registro", fuente, 1, background, borderColor, 5);
		table.addCell(cell);
		cell = textoEncabezado(cell, "Fecha Salida", fuente, 1, background, borderColor, 5);
		table.addCell(cell);
		cell = textoEncabezado(cell, "Hora Salida", fuente, 1, background, borderColor, 5);
		table.addCell(cell);
		cell = textoEncabezado(cell, "Estado", fuente, 1, background, borderColor, 5);
		table.addCell(cell);	

		writeTableReportDBFirebase(doc, table, cell, borderColor);
	}
	
	private void writeTableReportDBFirebase(Document doc, PdfPTable table, PdfPCell cell, Color border) throws InterruptedException, ExecutionException {
		
		Font fuenteData = FontFactory.getFont(FontFactory.COURIER, 10, new Color(17, 90, 135));
		Font fuenteDisco = FontFactory.getFont(FontFactory.COURIER_BOLD, 14, new Color(17, 90, 135));
		Color backgroundDate = new Color(251, 251, 251);
		
		ArrayList<Schedule> list = serviceSchedule.readAllSchedule();
		
		DateFormat formatRegistro = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		DateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat formatTime = new SimpleDateFormat("HH:mm");
		
		for (Schedule sch : list) {
			
			cell = textoEncabezado(cell, sch.getSch_asb_id_number_disc(), fuenteDisco, 1, backgroundDate, border, 2);
			table.addCell(cell);
			
			cell = textoTable(cell, sch.getSch_rou_id_name(), fuenteData, 1, backgroundDate, border, 2);
			table.addCell(cell);
			
			String fecharegistro = formatRegistro.format(sch.getSch_registration_date());
			cell = textoEncabezado(cell, fecharegistro, fuenteData, 1, backgroundDate, border, 2);
			table.addCell(cell);
						
			String fechaDate = formatDate.format(sch.getSch_departure_time());
			cell = textoEncabezado(cell, fechaDate, fuenteData, 1, backgroundDate, border, 2);
			table.addCell(cell);
			
			
			String fechaTime = formatTime.format(sch.getSch_departure_time());
			cell = textoEncabezado(cell, fechaTime, fuenteData, 1, backgroundDate, border, 2);
			table.addCell(cell);
			
			cell = textoTable(cell, sch.getSch_state(), fuenteData, 1, backgroundDate, border, 2);
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