package com.ec.busgeomap.web.app.service;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
