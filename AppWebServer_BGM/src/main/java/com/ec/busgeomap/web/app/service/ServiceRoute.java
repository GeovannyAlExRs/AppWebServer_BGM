package com.ec.busgeomap.web.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

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
	
	public static final String COL_NAME_ROUTE="Route";
	public static final String IDENTIFICATE="BGM_ROU";
	public static final int ID_LENGTH=10;
	
	Firestore dbFirestore;

	// Method to generate Random ID DOCUMENT
	public String autoIdDocumentRoute() {
			return IDENTIFICATE + RandomStringUtils.randomAlphanumeric(ID_LENGTH);
	}
	
	// Mapping the Object of the Route class
	private Route mapRoute(Route route) {
		
		Route r = new Route();
		
		r.setRou_id(route.getRou_id());
		r.setRou_name(route.getRou_name());
		r.setRou_registration_date(route.getRou_registration_date());
		r.setRou_status(route.getRou_status());
		
		return r;
	}
	
	// Method to create new Route record
	public String createRoute(Route r) throws InterruptedException, ExecutionException {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		Route route = mapRoute(r);
		
		dbFirestore.collection(COL_NAME_ROUTE).document(r.getRou_id()).set(route);
		
		return dbFirestore.toString();
	}
	
	// Method to Find all Route
	public ArrayList<Route> readAllRoute() throws InterruptedException, ExecutionException {
		
		Route route = null;
		
		ArrayList<Route> arrayList = new ArrayList<>();
		
		dbFirestore = FirestoreClient.getFirestore();
				
		ApiFuture<QuerySnapshot> query = dbFirestore.collection(COL_NAME_ROUTE).orderBy("rou_name", Query.Direction.ASCENDING).get();
				
		List<QueryDocumentSnapshot> documents = query.get().getDocuments();
		
		System.out.println("---- LISTA DE RUTAS ----\n ID Document \t\t| ROL \t| DESCRIPCION \t| ESTADO" );
		
		for (QueryDocumentSnapshot document : documents) {
		
			System.out.println("> " + document.getId() + " \t" + document.getString("rou_name"));
			
			route = document.toObject(Route.class);
			
			arrayList.add(route);
		}
		
		System.out.println("\n > LISTADO: " +arrayList);
		
		return arrayList;
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
		
		Route r = mapRoute(route);
			
		dbFirestore.collection(COL_NAME_ROUTE).document(r.getRou_id()).set(r);
		System.err.println(" RUTA Actualizado");
			
		return dbFirestore.toString();
	}
		
	// Method to Delete Ruta
	public String deleteRoute(String idDoc) throws InterruptedException, ExecutionException {
		Route route = readByIdDoc(idDoc);
			
		ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME_ROUTE).document(route.getRou_id()).delete();
			
		return writeResult.toString();
	}
}