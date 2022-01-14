package com.ec.busgeomap.web.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.ec.busgeomap.web.app.model.Place;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class ServicePlace {
	
	private final Log log = LogFactory.getLog(getClass());

	public static final String COL_NAME_PLACE="Place";
	public static final String IDENTIFICATE="BGM_PLAC";
	public static final int ID_LENGTH=10;
	
	Firestore dbFirestore;
	
	// Method to generate Random ID DOCUMENT
	public String autoIdDocumentPlace() {
		return IDENTIFICATE + RandomStringUtils.randomAlphanumeric(ID_LENGTH);
	}
	
	private Place mapPlace(Place place) {
		
		Place p = new Place();
		
		p.setPla_id(place.getPla_id());
		p.setPla_name(place.getPla_name());
		p.setPla_description(place.getPla_description());
		p.setPla_status(place.getPla_status());
		
		return p;
	}
	
	// Method to create new Place record
	public String createPlace(Place p) {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		Place place = mapPlace(p);
		
		dbFirestore.collection(COL_NAME_PLACE).document(p.getPla_id()).set(place);
				
		return dbFirestore.toString();
	}
	
	// Method to Find all Place
	public ArrayList<Place> readAllPlace() throws InterruptedException, ExecutionException {
		
		Place place = null;
		
		ArrayList<Place> arrayList= new ArrayList<>();
		
		dbFirestore = FirestoreClient.getFirestore();
		
		ApiFuture<QuerySnapshot> query = dbFirestore.collection(COL_NAME_PLACE).orderBy("pla_name", Query.Direction.ASCENDING).get();
		
		List<QueryDocumentSnapshot> documents = query.get().getDocuments();
		
		//System.out.println("---- LISTA DE LUGARES (PUNTO DE PARADAS) ----" );
		
		for (QueryDocumentSnapshot document : documents) {
			//System.out.println("> " + document.getId() + " \t" + document.getString("pla_name") + "\t " + document.getString("pla_description"));
			
			place = document.toObject(Place.class);
			
			arrayList.add(place);
		}

		log.info("(PLACE) Nº DE REGISTROS: [" + arrayList.size() + "]");
		
		return arrayList;
	}
	
	// Method to Find a specific Place
	public Place readByIdDoc(String idDoc) throws InterruptedException, ExecutionException {
		dbFirestore = FirestoreClient.getFirestore();
		
		DocumentReference docRef =  dbFirestore.collection(COL_NAME_PLACE).document(idDoc);
		
		ApiFuture<DocumentSnapshot> future = docRef.get();
		
		DocumentSnapshot document = future.get();
		
		Place place = null;
		
		if (document.exists()) {
			place = document.toObject(Place.class);
			
			return place;
		} else {
			return null;
		}
	}
	
	// Method to Update Place
	public String updatePlace(Place p) {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		Place place = mapPlace(p);
		
		dbFirestore.collection(COL_NAME_PLACE).document(p.getPla_id()).set(place);
		
		return dbFirestore.toString();
	}
	
	// Method to Delete Place
	public String deletePlace(String idDoc) throws InterruptedException, ExecutionException {
		Place place = readByIdDoc(idDoc);
		
		ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME_PLACE).document(place.getPla_id()).delete();
		
		return writeResult.toString();
	}
}