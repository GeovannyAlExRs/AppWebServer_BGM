package com.ec.busgeomap.web.app.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import com.ec.busgeomap.web.app.model.Assignes_Bus;
import com.ec.busgeomap.web.app.model.Bus;
import com.ec.busgeomap.web.app.model.CodeQR;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class ServiceCodeQR {
	
	public static final String COL_NAME_CODE="CodeQR";
	public static final String COL_NAME_ASSIGNE_BUS="Assignes_Bus";
	public static final String IDENTIFICATE="BGM_CODE";
	public static final int ID_LENGTH=10;
	
	Firestore dbFirestore;

	// Method to generate Random ID DOCUMENT
	public String autoIdDocument() {
		return IDENTIFICATE + RandomStringUtils.randomAlphanumeric(ID_LENGTH);
	}
	
	// Mapping the Object of the CodeQR class
	private CodeQR mapCodeQR(CodeQR code) {
		CodeQR qr = new CodeQR();
		
		qr.setGqr_code(code.getGqr_code());
		qr.setGqr_description(code.getGqr_description());
		qr.setGqr_image(code.getGqr_image());
		qr.setGqr_registration_date(new Date().getTime());
		qr.setGqr_asb_bus_id(code.getGqr_asb_bus_id());
		qr.setGqr_status(code.getGqr_status());
		
		return qr;
	}
	
	// Method to create new QR record
	public String createQR(CodeQR qr) throws InterruptedException, ExecutionException {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		CodeQR code = mapCodeQR(qr);
		
		System.out.println("OBJETO QR ANTES DE GUARDAR > " + code);
		dbFirestore.collection(COL_NAME_CODE).document(qr.getGqr_code()).set(code);
		
		return dbFirestore.toString();
	}
	
	// Method to Find all CODE QR
	public ArrayList<CodeQR> readAllQR() throws InterruptedException, ExecutionException {
		
		CodeQR qr = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		ArrayList<CodeQR> arrayList = new ArrayList<>();
		
		dbFirestore = FirestoreClient.getFirestore();
				
		ApiFuture<QuerySnapshot> query = dbFirestore.collection(COL_NAME_CODE).get();
				
		List<QueryDocumentSnapshot> documents = query.get().getDocuments();
		
		System.out.println("---- LISTA DE QR ----\n ID Document \t\t| Code QR");
		
		for (QueryDocumentSnapshot document : documents) {
		
			System.out.println("> " + document.getId() + " \t" + document.getData());
			
			qr = document.toObject(CodeQR.class);
			qr.setGqr_asb_bus_id(readAssignesBusByDisc(qr));
			//qr.setGqr_registration_date(new Date(qr.getGqr_registration_date()* 1000).getTime());
			//qr.setGqr_registration_date(new Date().parse(qr.getGqr_registration_date(), DateTimeFormat.ISO));
			//qr.setGqr_registration_date(sdf.parse(String.valueOf(qr.getGqr_registration_date())));
			arrayList.add(qr);
		}
		
		System.out.println("\n > LISTADO: " +arrayList);
		
		return arrayList;
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
}