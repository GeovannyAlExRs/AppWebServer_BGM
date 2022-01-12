package com.ec.busgeomap.web.app.config;

import java.awt.Color;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.ec.busgeomap.web.app.model.CodeQR;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Component("qr/codeqr")
public class ReportPdf extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		document.setMargins(-15, -15, 10, 10);
		document.open();
		
		CodeQR qr = (CodeQR) model.get("qrList");
		
		Font fuenteTitulo = FontFactory.getFont("Baloo Tammudu 2", 15, Color.white);
		
		PdfPCell cell = null;
		
		cell = new PdfPCell(new Phrase("System BusGeoMap", fuenteTitulo));
		cell.setBorder(0);
		cell.setBackgroundColor(new Color(17, 90, 135)); // linear-gradient(to right, #115a87, #212529);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		cell.setPadding(25);
		
		PdfPTable table =new PdfPTable(2);
		
		table.addCell(qr.getGqr_description());
		
		/*long fechaLong = qr.getGqr_registration_date();
		System.out.println("FECHA DE LA BD : " + fechaLong);
		String fechaString = new SimpleDateFormat("MM-dd-yy: HH:mm").format(String.valueOf(fechaLong));
		System.out.println("FECHA conversion : " + fechaString);
		
		table.addCell(fechaString);*/
		
		document.add(table);
	}
}
