package com.ec.busgeomap.web.app.service;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.ec.busgeomap.web.app.model.Bus;
import com.ec.busgeomap.web.app.model.Users;
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
public class ServicePdfBus {
	
	@Autowired
	ServiceBus serviceBus;
	
	@Autowired
	ServiceUsers serviceUser;
	
	@Autowired
	private ResourceLoader resourceLoader;

	// Exportar la lista de Usuarios
	public void pdfReportBus(HttpServletResponse servletResponse) throws DocumentException, IOException, InterruptedException, ExecutionException {
		
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
		
		String texto = "Lista de Unidades de Transporte";
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
		table.setWidths(new float[] {1f, 1.5f, 1.5f, 2.0f, 2.0f, 1.5f});
				
		Color background = new Color(245, 245, 245);
		Color borderColor = new Color(227, 227, 227);
				
		cell = textoEncabezado(cell, "Disco", fuente, 1, background, borderColor, 5);
		table.addCell(cell);
		cell = textoEncabezado(cell, "Placa", fuente, 1, background, borderColor, 5);
		table.addCell(cell);
		cell = textoEncabezado(cell, "Capacidad", fuente, 1, background, borderColor, 5);
		table.addCell(cell);
		cell = textoEncabezado(cell, "Modelo/Marca", fuente, 1, background, borderColor, 5);
		table.addCell(cell);
		cell = textoEncabezado(cell, "Propietario", fuente, 1, background, borderColor, 5);
		table.addCell(cell);
		cell = textoEncabezado(cell, "Fec.Registro", fuente, 1, background, borderColor, 5);
		table.addCell(cell);	

		writeTableReportDBFirebase(doc, table, cell, borderColor);
	}
	
	private void writeTableReportDBFirebase(Document doc, PdfPTable table, PdfPCell cell, Color border) throws InterruptedException, ExecutionException {
		
		Font fuenteData = FontFactory.getFont(FontFactory.COURIER, 10, new Color(17, 90, 135));
		Font fuenteDisco = FontFactory.getFont(FontFactory.COURIER_BOLD, 14, new Color(17, 90, 135));
		Color backgroundDate = new Color(251, 251, 251);
		
		ArrayList<Bus> list = serviceBus.readAllBus();
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		for (Bus bus : list) {
			String numDisco = String.valueOf(bus.getBus_number_disc());
			cell = textoEncabezado(cell, numDisco, fuenteDisco, 1, backgroundDate, border, 2);
			table.addCell(cell);
			
			cell = textoEncabezado(cell, bus.getBus_registration_number(), fuenteData, 1, backgroundDate, border, 2);
			table.addCell(cell);
			
			String capadidad = String.valueOf(bus.getBus_size());
			cell = textoEncabezado(cell, capadidad, fuenteData, 1, backgroundDate, border, 2);
			table.addCell(cell);
			
			String marca = bus.getBus_make() + " " + bus.getBus_model();
			cell = textoTable(cell, marca, fuenteData, 1, backgroundDate, border, 2);
			table.addCell(cell);
			
			cell = textoTable(cell, bus.getBus_propietor_id(), fuenteData, 1, backgroundDate, border, 2);
			table.addCell(cell);
						
			String fecha = dateFormat.format(bus.getBus_registration_date());
			cell = textoTable(cell, fecha, fuenteData, 1, backgroundDate, border, 2);
			table.addCell(cell);
		}
		doc.add(table);		
	}
	
	// Exportar Un Usuario
	public void exportPDF(Bus bus, HttpServletResponse servletResponse) throws DocumentException, IOException, InterruptedException, ExecutionException {
		
		Document doc = new Document(PageSize.A4);
		
		PdfWriter.getInstance(doc, servletResponse.getOutputStream());
		
		doc.open();
		
		Resource fileResourceHeader = resourceLoader.getResource("classpath:static/img/image/vector/rpt_Encabezado.png"); 
		Resource fileResourceFooter = resourceLoader.getResource("classpath:static/img/image/vector/rpt_pie.png");
		
		writeImage(doc, fileResourceHeader.getFile(),525, 70);
		
		writeHeaderPdf(doc);
		//writeBodyQRPdf(doc);
		writeBodyDescriptionPdf(doc, bus);
		
		writeImage(doc, fileResourceFooter.getFile(),525, 50);
		
		doc.close();
	}
	
	private void writeImage(Document doc, File file, int width, int heigth) throws IOException {
		
		Image img = Image.getInstance(file.toString());
		
		img.setAlignment(Element.ALIGN_RIGHT);
		img.scaleAbsolute(width,heigth);
		
		doc.add(img);
	}
	
	private void writeHeaderPdf(Document doc) {
		
		Font fuenteTitulo = FontFactory.getFont(FontFactory.COURIER_BOLD, 12, new Color(0, 40, 45));

		String texto = "DATOS DE LA UNIDAD DE TRANSPORTE";

		Color background = new Color(255, 255, 255);
		Color borderColor = new Color(255, 255, 255);
		
		textoCell(doc, texto, fuenteTitulo, 1, background, borderColor, 0, 5, 20);
	}
	
	private void writeBodyDescriptionPdf(Document doc, Bus bus) throws InterruptedException, ExecutionException {

		Font fuenteTitulo = FontFactory.getFont(FontFactory.COURIER_BOLD, 12, new Color(10, 60, 125));
		Font fuente = FontFactory.getFont(FontFactory.COURIER, 11, new Color(17, 90, 135));

		PdfPCell cell = new PdfPCell();
		
		PdfPTable table = new PdfPTable(2);
		
		Color background = new Color(251, 251, 251);
		Color borderColor = new Color(227, 227, 227);

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String fecha = dateFormat.format(bus.getBus_registration_date());
		
		Users u = serviceUser.readByIdDoc(bus.getBus_propietor_id()); // Extra el nombre del propietario
		
		cell = textoEncabezado(cell, "Marca", fuenteTitulo, 0, background, borderColor, 10);
		table.setSpacingBefore(25);
		table.addCell(cell);	
		
		cell = textoEncabezado(cell, bus.getBus_make() , fuente, 0, background, borderColor, 10);
		table.setSpacingBefore(25);
		table.addCell(cell);
		
		cell = textoEncabezado(cell, "Modelo", fuenteTitulo, 0, background, borderColor, 10);
		table.addCell(cell);
		
		cell = textoEncabezado(cell,  bus.getBus_model(), fuente, 0, background, borderColor, 10);
		table.addCell(cell);
		
		cell = textoEncabezado(cell, "Disco", fuenteTitulo, 0, background, borderColor, 10);
		table.addCell(cell);
		
		String numDisco = String.valueOf(bus.getBus_number_disc());
		cell = textoEncabezado(cell, numDisco, fuente, 0, background, borderColor, 10);
		table.addCell(cell);
		
		cell = textoEncabezado(cell, "Placa", fuenteTitulo, 0, background, borderColor, 10);
		table.addCell(cell);
		
		cell = textoEncabezado(cell, bus.getBus_registration_number(), fuente, 0, background, borderColor, 10);
		table.addCell(cell);
		
		cell = textoEncabezado(cell, "Propietario", fuenteTitulo, 0, background, borderColor, 10);
		table.addCell(cell);
		
		String propietor = u.getUse_last_name() + " " + u.getUse_first_name();
		cell = textoEncabezado(cell, propietor, fuente, 0, background, borderColor, 10);
		table.addCell(cell);
		
		cell = textoEncabezado(cell, "Fecha de registro", fuenteTitulo, 0, background, borderColor, 10);
		table.addCell(cell);
		
		cell = textoEncabezado(cell, fecha, fuente, 0, background, borderColor, 10);
		table.addCell(cell);
		
		doc.add(table);
		doc.add(new Phrase("\n \n \n"));
	}

	private void textoCell(Document doc, String texto, Font font, int numTable, Color background, Color borderColor, int border, int padding, int spacing) {
	
		PdfPTable table = new PdfPTable(numTable);
		
		PdfPCell cell = new PdfPCell();
		
		cell.setPhrase(new Phrase(texto, font));
		cell.setBorder(border);
		cell.setBackgroundColor(background);
		cell.setBorderColor(borderColor);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		cell.setPadding(padding);
		
		table.addCell(cell);
		table.setSpacingBefore(spacing);	
		doc.add(table);
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

}
