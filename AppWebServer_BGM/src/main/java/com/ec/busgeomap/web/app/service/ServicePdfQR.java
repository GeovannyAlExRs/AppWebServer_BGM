package com.ec.busgeomap.web.app.service;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.ec.busgeomap.web.app.model.Assignes_Bus;
import com.ec.busgeomap.web.app.model.Bus;
import com.ec.busgeomap.web.app.model.CodeQR;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.lowagie.text.BadElementException;
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
public class ServicePdfQR {
	
	public static final String COL_NAME_ASSIGNE_BUS="Assignes_Bus";
	public static final String COL_NAME_BUS="Bus";
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	Firestore dbFirestore;
	
	@Autowired
	ServiceCodeQR serviceCodeQR;
	
	// Exportar la lista de Codigos QR
	public void pdfReportListQR(HttpServletResponse servletResponse) throws DocumentException, IOException, InterruptedException, ExecutionException {
	
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
		
		String texto = "Lista de Codigo QR";
		Color background = new Color(250, 250, 250);
		Color borderColor = new Color(255, 255, 255);
		
		cell =  textoEncabezado(cell, texto, fuente, 1, background, borderColor, 5);
		table.addCell(cell);
		doc.add(table);
		
		doc.add(new Phrase("\n"));
	}

	private void writeTableReport(Document doc) throws InterruptedException, ExecutionException, BadElementException, IOException {
		// ENCABEZADO
		Font fuente = FontFactory.getFont(FontFactory.COURIER_BOLD, 10, new Color(17, 90, 135));
				
		PdfPCell cell = new PdfPCell();
					
		PdfPTable table = new PdfPTable(5);
		table.setWidths(new float[] {1.0f, 4.0f, 2.0f, 1.5f, 1.5f});
				
		Color background = new Color(245, 245, 245);
		Color borderColor = new Color(227, 227, 227);
				
		cell = textoEncabezado(cell, "Disco", fuente, 1, background, borderColor, 5);
		table.addCell(cell);
		cell = textoEncabezado(cell, "Descripcion", fuente, 1, background, borderColor, 5);
		table.addCell(cell);
		cell = textoEncabezado(cell, "Fecha", fuente, 1, background, borderColor, 5);
		table.addCell(cell);
		cell = textoEncabezado(cell, "Hora", fuente, 1, background, borderColor, 5);
		table.addCell(cell);
		cell = textoEncabezado(cell, "Codigo QR", fuente, 1, background, borderColor, 5);
		table.addCell(cell);
		
		writeTableReportDBFirebase(doc, table, cell, borderColor);
	}

	private void writeTableReportDBFirebase(Document doc, PdfPTable table, PdfPCell cell, Color border) throws InterruptedException, ExecutionException, BadElementException, IOException {
		
		Font fuenteData = FontFactory.getFont(FontFactory.COURIER, 10, new Color(17, 90, 135));
		Font fuenteDisco = FontFactory.getFont(FontFactory.COURIER_BOLD, 14, new Color(17, 90, 135));
		Color backgroundDate = new Color(251, 251, 251);
		
		ArrayList<CodeQR> list = serviceCodeQR.readAllQR();
		
		DateFormat dateFormatDate = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat dateFormatTime = new SimpleDateFormat("HH:mm");
		
		for (CodeQR qr : list) {
			String numDisco = String.valueOf(qr.getGqr_asb_bus_id());
			cell = textoEncabezado(cell, numDisco, fuenteDisco, 1, backgroundDate, border, 2);
			table.addCell(cell);
			
			cell = textoTable(cell, qr.getGqr_description(), fuenteData, 1, backgroundDate, border, 2);
			table.addCell(cell);
			
			String fechaQR = dateFormatDate.format(qr.getGqr_registration_date());
			cell = textoTable(cell, fechaQR, fuenteData, 1, backgroundDate, border, 2);
			table.addCell(cell);
			
			String horaQR = dateFormatTime.format(qr.getGqr_registration_date());
			cell = textoEncabezado(cell, horaQR, fuenteData, 1, backgroundDate, border, 2);
			table.addCell(cell);
			
			table.addCell(imageQRTable(qr.getGqr_image(), 1, border));			
		}
		
		doc.add(table);		
	}

	private Image imageQRTable(String gqr_image, int border, Color borderColor) throws BadElementException, IOException {
		
		byte[] bytesImg = Base64.getDecoder().decode(gqr_image);
		
		Image img = Image.getInstance(bytesImg);
		
		img.setAlignment(Element.ALIGN_RIGHT);
		img.setBorder(border);
		img.setBorderColor(borderColor);
		img.scaleAbsolute(1,1);

		return img;
		
	}

	public void exportPDF(CodeQR qr, HttpServletResponse servletResponse) throws DocumentException, IOException, InterruptedException, ExecutionException {
		
		Document doc = new Document(PageSize.A4);
		
		PdfWriter.getInstance(doc, servletResponse.getOutputStream());
		
		doc.open();
		
		Resource fileResourceHeader = resourceLoader.getResource("classpath:static/img/image/vector/rpt_Encabezado.png"); 
		Resource fileResourceFooter = resourceLoader.getResource("classpath:static/img/image/vector/rpt_pie.png");
		
		writeImage(doc, fileResourceHeader.getFile(),525, 70);
		
		writeHeaderPdf(doc);
		writeBodyQRPdf(doc, qr.getGqr_image());
		writeBodyDescriptionPdf(doc, qr);
		
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
		
		Font fuenteTitulo = FontFactory.getFont(FontFactory.COURIER_BOLD, 12, new Color(17, 90, 135));

		String texto = "Sirviendo a la comunidad \n Chanduy - Santa Elena - Santa Elena";

		Color background = new Color(255, 255, 255);
		Color borderColor = new Color(255, 255, 255);
		
		textoCell(doc, texto, fuenteTitulo, 1, background, borderColor, 0, 5, 20);
	}

	private void writeBodyQRPdf(Document doc, String gqr_image) throws IOException {

		byte[] bytesImg = Base64.getDecoder().decode(gqr_image);
		
		Image img = Image.getInstance(bytesImg);
		img.setAlignment(Element.ALIGN_CENTER);
		
		doc.add(img);
		
		Font fuenteTitulo = FontFactory.getFont(FontFactory.COURIER_BOLD, 12, new Color(17, 90, 135));
		
		String texto = "Codigo QR";
		Color background = new Color(245, 245, 245);
		Color borderColor = new Color(227, 227, 227);
		
		textoCell(doc, texto, fuenteTitulo, 1, background, borderColor, 1, 5, 0);
	}

	private void writeBodyDescriptionPdf(Document doc, CodeQR qr) throws InterruptedException, ExecutionException {

		Font fuente = FontFactory.getFont(FontFactory.COURIER, 10, new Color(17, 90, 135));

		PdfPCell cell = new PdfPCell();
		
		PdfPTable table = new PdfPTable(2);
		
		Color background = new Color(251, 251, 251);
		Color borderColor = new Color(227, 227, 227);
		
		dbFirestore = FirestoreClient.getFirestore();
		CodeQR code = new CodeQR();
		
		code.setGqr_asb_bus_id(readAssignesBusByDisc(qr));
		
		cell = textoEncabezado(cell, "Disco Nº " + code.getGqr_asb_bus_id(), fuente, 1, background, borderColor, 0);
		table.addCell(cell);
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String fecha = dateFormat.format(qr.getGqr_registration_date());
		
		System.out.println("FECHA conversion : " + fecha);
		
		cell = textoEncabezado(cell, qr.getGqr_description() + " - " + fecha, fuente, 1, background, borderColor, 0);
		table.addCell(cell);
		
		doc.add(table);
		doc.add(new Phrase("\n \n \n"));
	}
	
	// Method to Find DISC BUS
	public String readAssignesBusByDisc(CodeQR qr) throws InterruptedException, ExecutionException {
		
		Assignes_Bus ab = null;
		String numDisc = "";
				
		DocumentReference docRef1 =  dbFirestore.collection(COL_NAME_ASSIGNE_BUS).document(qr.getGqr_asb_bus_id());
		ApiFuture<DocumentSnapshot> future = docRef1.get();
		DocumentSnapshot document = future.get();
		
		if (document.exists()) {
			ab = document.toObject(Assignes_Bus.class);
			if (qr.getGqr_asb_bus_id().equals(document.getId())) {
				ab.setAsb_bus_id(readBusDoc(ab)); // Buscar Disco del Bus
				numDisc = ab.getAsb_bus_id();
			}
		}
		
		return numDisc;
	}
	
	// Method to Find a DISC BUS Doc
	private String readBusDoc(Assignes_Bus ab) throws InterruptedException, ExecutionException {
		Bus bus = null;
		
		DocumentReference docRef1 =  dbFirestore.collection("Bus").document(ab.getAsb_bus_id());
		ApiFuture<DocumentSnapshot> future = docRef1.get();
		DocumentSnapshot document1 = future.get();
		
		if (document1.exists()) {
			bus = document1.toObject(Bus.class);
			
			if (ab.getAsb_bus_id().equals(document1.getId())) {
				ab.setAsb_bus_id(String.valueOf(bus.getBus_number_disc()));
			} 
		}
		
		return String.valueOf(bus.getBus_number_disc());
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
		table.setSpacingBefore(spacing);	//table.setSpacingAfter(20);
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
