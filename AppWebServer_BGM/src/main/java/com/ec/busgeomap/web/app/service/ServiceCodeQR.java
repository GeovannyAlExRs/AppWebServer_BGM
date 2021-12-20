package com.ec.busgeomap.web.app.service;

import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.ec.busgeomap.web.app.model.CodeQR;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class ServiceCodeQR {
	
	public static final String COL_NAME_CODE="CodeQR";
	public static final String IDENTIFICATE="BGM_CODE";
	public static final int ID_LENGTH=10;
	
	Firestore dbFirestore;

	// Method to generate Random ID DOCUMENT
	public String autoIdDocument() {
		return IDENTIFICATE + RandomStringUtils.randomAlphanumeric(ID_LENGTH);
	}
	
	// Mapping the Object of the Roles class
	private CodeQR mapCodeQR(CodeQR code) {
		CodeQR qr = new CodeQR();
		
		qr.setGqr_code(code.getGqr_code());
		qr.setGqr_description(code.getGqr_description());
		qr.setGqr_image(code.getGqr_image());
		qr.setGqr_registration_date(code.getGqr_registration_date());
		qr.setGqr_asb_bus_id(code.getGqr_asb_bus_id());
		qr.setGqr_status(code.getGqr_status());
		
		return qr;
	}
	
	// Method to create new Roles record
	public String createQR(CodeQR qr) throws InterruptedException, ExecutionException {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		CodeQR code = mapCodeQR(qr);
		
		dbFirestore.collection(COL_NAME_CODE).document(qr.getGqr_code()).set(code);
		
		return dbFirestore.toString();
	}
}
