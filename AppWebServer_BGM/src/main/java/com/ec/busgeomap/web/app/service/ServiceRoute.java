package com.ec.busgeomap.web.app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.ec.busgeomap.web.app.model.Place;
import com.ec.busgeomap.web.app.model.Route;
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
public class ServiceRoute {
	
	private final Log log = LogFactory.getLog(getClass());
	
	public static final String COL_NAME_ROUTE="Route";
	public static final String COL_NAME_PLACE="Place";
	public static final String IDENTIFICATE="BGM_ROU";
	
	public static final int ID_LENGTH=10;
	
	public static final int OPTION_CREATE = 1;
	public static final int OPTION_UPDATE = 2;

	Firestore dbFirestore;

	// Method to generate Random ID DOCUMENT
	public String autoIdDocumentRoute() {
			return IDENTIFICATE + RandomStringUtils.randomAlphanumeric(ID_LENGTH);
	}
	
	// Mapping the Object of the Route class
	private Route mapRoute(Route route, int option) {
		
		Route r = new Route();
		
		r.setRou_id(route.getRou_id());
		r.setRou_name(route.getRou_name());
		r.setRou_registration_date(new Date().getTime());
		r.setRou_place_destination(route.getRou_place_destination());
		r.setRou_place_starting(route.getRou_place_starting());
		
		if (option == 1 ) {
			r.setRou_status(true);
		} else {
			r.setRou_status(route.getRou_status());
		}
		
		
		return r;
	}
	
	// Method to create new Route record
	public String createRoute(Route r) throws Exception {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		Route route = mapRoute(r, OPTION_CREATE);
		
		if (validatePlaceRoute(route)) {
			log.info("LUGARES SON IGUALES - Origen: " + route.getRou_place_starting() + " Destino: " + route.getRou_place_destination());
		}
		
		dbFirestore.collection(COL_NAME_ROUTE).document(r.getRou_id()).set(route);
				
		return dbFirestore.toString();
	}
	
	private boolean validatePlaceRoute(Route route) throws Exception {
		
		if (route.getRou_place_destination().equals(route.getRou_place_starting())) {
			throw new Exception("Lugares asignados no deben ser iguales");
		}
		
		return true;
	}
	
	// Method to Find all Route
	public ArrayList<Route> readAllRoute() throws InterruptedException, ExecutionException {
		
		Route route = null;
		
		ArrayList<Route> arrayList = new ArrayList<>();
		
		dbFirestore = FirestoreClient.getFirestore();
				
		ApiFuture<QuerySnapshot> query = dbFirestore.collection(COL_NAME_ROUTE).orderBy("rou_name", Query.Direction.ASCENDING).get();
				
		List<QueryDocumentSnapshot> documents = query.get().getDocuments();
		
		for (QueryDocumentSnapshot document : documents) {
		
			//System.out.println("> " + document.getId() + " \t" + document.getString("rou_name"));
			
			route = document.toObject(Route.class);
			route.setRou_place_starting(readPlace(route.getRou_place_starting()));
			route.setRou_place_destination(readPlace(route.getRou_place_destination()));
			arrayList.add(route);
		}
		
		log.info("(ROUTE) Nº DE REGISTROS: [" + arrayList.size() + "]");
		
		return arrayList;
	}
	
	private String readPlace(String placeID) throws InterruptedException, ExecutionException {
		Place place = null;
		
		DocumentReference docRef1 =  dbFirestore.collection(COL_NAME_PLACE).document(placeID);
		
		ApiFuture<DocumentSnapshot> future = docRef1.get();
		
		DocumentSnapshot document = future.get();
		
		if (document.exists()) {
			place = document.toObject(Place.class);

			if (placeID.equals(document.getId())) {
				return place.getPla_name();
			} 
		}
		
		return null;
	}

	// Method to Find a specific route
	public Route readByIdDoc(String idDoc) throws InterruptedException, ExecutionException {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		DocumentReference docRef =  dbFirestore.collection(COL_NAME_ROUTE).document(idDoc);
		
		ApiFuture<DocumentSnapshot> future = docRef.get();
		
		DocumentSnapshot document = future.get();
		
		Route route = null;
		
		if (document.exists()) {
			route = document.toObject(Route.class);
			return route;
		}else {
			return null;
		}
	}
	
	// Method to Update Ruta
	public String updateRoute(Route route) throws Exception {
			
		dbFirestore = FirestoreClient.getFirestore();
		
		Route r = mapRoute(route, OPTION_UPDATE);
		
		if (validatePlaceRoute(route)) {
			log.info("LUGARES SON IGUALES - Origen: " + route.getRou_place_starting() + " Destino: " + route.getRou_place_destination());
		}
			
		dbFirestore.collection(COL_NAME_ROUTE).document(r.getRou_id()).set(r);
			
		return dbFirestore.toString();
	}
		
	// Method to Delete Ruta
	public String deleteRoute(String idDoc) throws InterruptedException, ExecutionException {
		Route route = readByIdDoc(idDoc);
			
		ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME_ROUTE).document(route.getRou_id()).delete();
			
		return writeResult.toString();
	}
}