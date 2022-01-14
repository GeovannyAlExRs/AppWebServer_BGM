package com.ec.busgeomap.web.app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

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
public class ServiceBus {
	
	private final Log log = LogFactory.getLog(getClass());
	
	public static final String COL_NAME_BUS="Bus";
	public static final String COL_NAME_USER="Users";
	public static final String IDENTIFICATE="BGM_BUSE";
	public static final int ID_LENGTH=10;

	Firestore dbFirestore;
	
	// Method to generate Random ID DOCUMENT
	public String autoIdDocument() {
		return IDENTIFICATE + RandomStringUtils.randomAlphanumeric(ID_LENGTH);
	}
	
	// Mapping the Object of the Bus class
	private Bus mapBus(Bus bus) {
		
		Bus b = new Bus();
		
		b.setBus_id(bus.getBus_id());
		b.setBus_make(bus.getBus_make());
		b.setBus_model(bus.getBus_model());
		b.setBus_number_disc(bus.getBus_number_disc());
		b.setBus_propietor_id(bus.getBus_propietor_id());
		b.setBus_registration_date(new Date().getTime());
		b.setBus_registration_number(bus.getBus_registration_number());
		b.setBus_size(bus.getBus_size());
		b.setBus_status(bus.getBus_status());
		
		return b;
	}

	// Method to create new BUS record
	public String createBus(Bus b) throws InterruptedException, ExecutionException {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		Bus bus = mapBus(b);
		
		dbFirestore.collection(COL_NAME_BUS).document(bus.getBus_id()).set(bus);
		
		return dbFirestore.toString();
	}
	
	// Method to Find all BUS
	public ArrayList<Bus> readAllBus() throws InterruptedException, ExecutionException {
		
		Bus bus = null;
		
		ArrayList<Bus> arrayList = new ArrayList<>();
		
		dbFirestore = FirestoreClient.getFirestore();
				
		ApiFuture<QuerySnapshot> query = dbFirestore.collection(COL_NAME_BUS).whereEqualTo("bus_status", true).get();
				
		List<QueryDocumentSnapshot> documents = query.get().getDocuments();
		
		for (QueryDocumentSnapshot document : documents) {
		
			//System.out.println("> " + document.getId() + " \t" + document.getData());
			
			bus = document.toObject(Bus.class);
			
			bus.setBus_propietor_id(readUserDoc(bus));
			
			arrayList.add(bus);
		}
		
		log.info("(BUS) Nº DE REGISTROS: [" + arrayList.size() + "]");
		
		return arrayList;
	}
	
	// Method to Find a USER PROPIETOR Doc
	private String readUserDoc(Bus b) throws InterruptedException, ExecutionException{
		Users user = null;
		
		DocumentReference docRef1 =  dbFirestore.collection(COL_NAME_USER).document(b.getBus_propietor_id());
		
		ApiFuture<DocumentSnapshot> future = docRef1.get();
		
		DocumentSnapshot document = future.get();
		
		if (document.exists()) {
			user = document.toObject(Users.class);

			if (b.getBus_propietor_id().equals(document.getId())) {
				b.setBus_propietor_id(user.getUse_last_name() + " " + user.getUse_first_name());
			} 
		}
		
		return user.getUse_last_name() + " " + user.getUse_first_name();
	}
	
	// Method to Find a specific role
	public Bus readByIdDoc(String idDoc) throws InterruptedException, ExecutionException {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		DocumentReference docRef =  dbFirestore.collection(COL_NAME_BUS).document(idDoc);
		
		ApiFuture<DocumentSnapshot> future = docRef.get();
		
		DocumentSnapshot document = future.get();
		
		Bus bus = null;
		
		if (document.exists()) {
			bus = document.toObject(Bus.class);
			return bus;
		}else {
			return null;
		}
	}
	
	// Method to Update BUS
	public String updateBus(Bus bus) throws Exception {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		Bus b = mapBus(bus);
		
		dbFirestore.collection(COL_NAME_BUS).document(b.getBus_id()).set(bus);
		
		return dbFirestore.toString();
	}
	
	// Method to Delete BUS
	public String deleteBus(String idDoc) throws InterruptedException, ExecutionException {
		Bus bus = readByIdDoc(idDoc);
		
		ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME_BUS).document(bus.getBus_id()).delete();
		
		return writeResult.toString();
	}
}