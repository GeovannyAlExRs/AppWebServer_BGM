package com.ec.busgeomap.web.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.ec.busgeomap.web.app.model.Employment;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class ServiceEmployment {
	
	public static final String COL_NAME_EMPLOYMENT = "Employment";
	public static final String IDENTIFICATE = "BGM_EMPL";
	public static final int ID_LENGTH = 10;
	
	Firestore dbFirestore;
	
	// Method to generate Random ID DOCUMENT
	public String autoIdDocumentEmployment() {
		return IDENTIFICATE + RandomStringUtils.randomAlphanumeric(ID_LENGTH);
	}
	
	// Method to Find all Employment
	public ArrayList<Employment> readAllEmployment() throws InterruptedException, ExecutionException {
		
		Employment employment = null;
		
		ArrayList<Employment> arrayList = new ArrayList<>();
		
		dbFirestore = FirestoreClient.getFirestore();
				
		ApiFuture<QuerySnapshot> query = dbFirestore.collection(COL_NAME_EMPLOYMENT).get();
				
		List<QueryDocumentSnapshot> documents = query.get().getDocuments();
		
		System.out.println("---- LISTA DE CARGOS ----\n" );
		
		for (QueryDocumentSnapshot document : documents) {
		
			System.out.println("> " + document.getId() + " \t" + document.getString("emp_name"));
			
			employment = document.toObject(Employment.class);
			
			arrayList.add(employment);
		}
		
		System.out.println("\n > LISTADO: " +arrayList);
		
		return arrayList;
	}
	
	

}
