package com.ec.busgeomap.web.app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.ec.busgeomap.web.app.model.Assignes_Bus;
import com.ec.busgeomap.web.app.model.Bus;
import com.ec.busgeomap.web.app.model.Users;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class ServiceAsigneBus {
	
	public static final String COL_NAME_ASSIGNE_BUS="Assignes_Bus";
	public static final String COL_NAME_BUS="Bus";
	public static final String COL_NAME_USER="Users";
	public static final String IDENTIFICATE="BGM_BUSE";
	public static final int ID_LENGTH=10;

	Firestore dbFirestore;
		
	// Method to generate Random ID DOCUMENT
	public String autoIdDocument() {
		return IDENTIFICATE + RandomStringUtils.randomAlphanumeric(ID_LENGTH);
	}	
	
	private Assignes_Bus mapBus(Assignes_Bus assignes_Bus) {
		
		Assignes_Bus ab = new Assignes_Bus();
		
		ab.setAsb_bus_id(assignes_Bus.getAsb_bus_id());
		ab.setAsb_accompanist_id(assignes_Bus.getAsb_accompanist_id());
		ab.setAsb_driver_id(assignes_Bus.getAsb_driver_id());
		ab.setBus_registration_date(new Date().getTime());
		ab.setAsb_status(assignes_Bus.getAsb_status());
		
		return ab;
	}
	
	// Method to Find all BUS
	public ArrayList<Assignes_Bus> readAllAssignesBus() throws InterruptedException, ExecutionException {
		
		Assignes_Bus ab = null;
		
		ArrayList<Assignes_Bus> arrayList = new ArrayList<>();
		
		dbFirestore = FirestoreClient.getFirestore();
				
		ApiFuture<QuerySnapshot> query = dbFirestore.collection(COL_NAME_ASSIGNE_BUS).get();
				
		List<QueryDocumentSnapshot> documents = query.get().getDocuments();
		
		System.out.println("---- LISTA DE ASIGNACION DE BUSES ----\n ID Document \t\t| NºBUS" );
		
		for (QueryDocumentSnapshot document : documents) {
		
			System.out.println("> " + document.getId() + " \t" + document.getData());
			
			ab = document.toObject(Assignes_Bus.class);
			
			ab.setAsb_bus_id(readBusDoc(ab)); // Buscar Disco del Bus
			
			ab.setAsb_driver_id(readDriverDoc(ab)); // Buscar Conductor del Bus
			
			ab.setAsb_accompanist_id(readAccompanistDoc(ab)); // Buscar Acompañante del Bus
			
			arrayList.add(ab);
		}
		
		System.out.println("\n > LISTADO: " +arrayList);
		
		return arrayList;
	}

	// Buscar Documento DRIVER (USERS)
	private String readDriverDoc(Assignes_Bus ab) throws InterruptedException, ExecutionException {
		Users users = null; 
		
		DocumentReference docRef1 =  dbFirestore.collection(COL_NAME_USER).document(ab.getAsb_driver_id());
		ApiFuture<DocumentSnapshot> future = docRef1.get();
		
		DocumentSnapshot document1 = future.get();
		
		if (document1.exists()) {
			users = document1.toObject(Users.class);
			
			if (ab.getAsb_driver_id().equals(document1.getId())) {
				ab.setAsb_driver_id(users.getUse_last_name() + ' ' + users.getUse_first_name());
			} 
		}
		return users.getUse_last_name() + ' ' + users.getUse_first_name();
	}

	// Buscar Documento ACOMPAÑANTE (USERS)
	private String readAccompanistDoc(Assignes_Bus ab) throws InterruptedException, ExecutionException {
		Users users = null; 
		
		DocumentReference docRef1 =  dbFirestore.collection(COL_NAME_USER).document(ab.getAsb_accompanist_id());
		ApiFuture<DocumentSnapshot> future = docRef1.get();
		
		DocumentSnapshot document1 = future.get();
		
		if (document1.exists()) {
			users = document1.toObject(Users.class);
			
			if (ab.getAsb_accompanist_id().equals(document1.getId())) {
				ab.setAsb_accompanist_id(users.getUse_last_name() + ' ' + users.getUse_first_name());
			} 
		}
		return users.getUse_last_name() + ' ' + users.getUse_first_name();
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
	
	// Method to create new BUS record
	public String createAssignesBus(Assignes_Bus assignes_Bus) throws InterruptedException, ExecutionException {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		Assignes_Bus ab = mapBus(assignes_Bus);
		
		dbFirestore.collection(COL_NAME_ASSIGNE_BUS).document(assignes_Bus.getAsb_id()).set(ab);
		
		return dbFirestore.toString();
	}
	
	// Method to Find a specific role
	public Assignes_Bus readByIdDoc(String idDoc) throws InterruptedException, ExecutionException {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		DocumentReference docRef =  dbFirestore.collection(COL_NAME_ASSIGNE_BUS).document(idDoc);
		
		ApiFuture<DocumentSnapshot> future = docRef.get();
		
		DocumentSnapshot document = future.get();
		
		Assignes_Bus ab = null;
		
		if (document.exists()) {
			ab = document.toObject(Assignes_Bus.class);
			return ab;
		}else {
			return null;
		}
	}
	
	// Method to Update BUS
	public String updateAssigneBus(Assignes_Bus assignes_Bus) throws Exception {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		Assignes_Bus ab = mapBus(assignes_Bus);
		
		dbFirestore.collection(COL_NAME_ASSIGNE_BUS).document(ab.getAsb_id()).set(assignes_Bus);
		System.err.println("Actualizado");
		
		return dbFirestore.toString();
	}
	
	// Method to Delete BUS
	public String deleteAssigneBus(String idDoc) throws InterruptedException, ExecutionException {
		Assignes_Bus ab = readByIdDoc(idDoc);
		
		ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME_ASSIGNE_BUS).document(ab.getAsb_id()).delete();
		
		return writeResult.toString();
	}
	
	
}
