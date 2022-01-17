package com.ec.busgeomap.web.app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.ec.busgeomap.web.app.model.Assignes_Bus;
import com.ec.busgeomap.web.app.model.Bus;
import com.ec.busgeomap.web.app.model.Place;
import com.ec.busgeomap.web.app.model.Route;
import com.ec.busgeomap.web.app.model.Schedule;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class ServiceTravelSchedule {
	
private final Log log = LogFactory.getLog(getClass());
	
	public static final String COL_NAME_SCHEDULE = "Schedule";
	public static final String COL_NAME_PLACE = "Place";
	public static final String COL_NAME_ROUTE = "Route";
	public static final String COL_NAME_ASSIGNE_BUS = "Assignes_Bus";
	public static final String COL_NAME_BUS = "Bus";
	

	public static final String ROUTE_NAME = "BGM_ROUdBdgJateZe9";
	
	Firestore dbFirestore;
	
	Date currentDate = new Date();
	
	// Method to Find all Schedule
	public ArrayList<Schedule> readAllUpTravelSchedule() throws InterruptedException, ExecutionException {
			
		Schedule schedule = null;
			
		ArrayList<Schedule> arrayList = new ArrayList<>();
			
		dbFirestore = FirestoreClient.getFirestore();		
		
		// Buscar documentos de manera descendente y que encuentre desde la fecha actual
		ApiFuture<QuerySnapshot> query = dbFirestore.collection(COL_NAME_SCHEDULE).orderBy("sch_departure_time", Query.Direction.DESCENDING).whereGreaterThan("sch_departure_time", currentDate.getTime()).get();
		
					
		List<QueryDocumentSnapshot> documents = query.get().getDocuments();
		
		for (QueryDocumentSnapshot document : documents) {
			
			if (document.getString("sch_rou_id_name").equals(ROUTE_NAME)) {
				
				schedule = document.toObject(Schedule.class);
				
				schedule.setSch_asb_id_number_disc(numberAssigneBusDisc(schedule.getSch_asb_id_number_disc())); // OBTENER DISCO DEL BUS
				schedule.setSch_rou_id_name(routeName(schedule.getSch_rou_id_name())); // OBTENER LA RUTA
				
				System.out.println("> " + document.getId() + " \t" + document.getData());
				
				arrayList.add(schedule);
			}			
			
		}
			
		log.info("(HORARIOS) Nº DE REGISTROS: [" + arrayList.size() + "]");
			
		return arrayList;
	}	
	
	// Method to Find all Schedule
	public ArrayList<Schedule> readAllDownTravelSchedule() throws InterruptedException, ExecutionException {
			
		Schedule schedule = null;
			
		ArrayList<Schedule> arrayList = new ArrayList<>();
			
		dbFirestore = FirestoreClient.getFirestore();		
		
		// Buscar documentos de manera descendente y que encuentre desde la fecha actual
		ApiFuture<QuerySnapshot> query = dbFirestore.collection(COL_NAME_SCHEDULE).orderBy("sch_departure_time", Query.Direction.DESCENDING).whereGreaterThan("sch_departure_time", currentDate.getTime()).get();
					
		List<QueryDocumentSnapshot> documents = query.get().getDocuments();
		
		for (QueryDocumentSnapshot document : documents) {
			
			if (!document.getString("sch_rou_id_name").equals(ROUTE_NAME)) {
				
				schedule = document.toObject(Schedule.class);
				
				schedule.setSch_asb_id_number_disc(numberAssigneBusDisc(schedule.getSch_asb_id_number_disc())); // OBTENER DISCO DEL BUS
				schedule.setSch_rou_id_name(routeName(schedule.getSch_rou_id_name())); // OBTENER LA RUTA
				
				System.out.println("> " + document.getId() + " \t" + document.getData());
				
				arrayList.add(schedule);
			}			
		}
			
		log.info("(HORARIOS) Nº DE REGISTROS: [" + arrayList.size() + "]");
			
		return arrayList;
	}	
	
	private String numberAssigneBusDisc(String sch_asb_id_number_disc) throws InterruptedException, ExecutionException {
		Assignes_Bus aBus = null;
			
		DocumentReference docRef1 =  dbFirestore.collection(COL_NAME_ASSIGNE_BUS).document(sch_asb_id_number_disc);
		ApiFuture<DocumentSnapshot> future = docRef1.get();
		DocumentSnapshot document1 = future.get();
		
		if (document1.exists()) {
			aBus = document1.toObject(Assignes_Bus.class);
			if (sch_asb_id_number_disc.equals(document1.getId())) {
				return numberBusDis(aBus.getAsb_bus_id());
			} 
		}
		return null;
	}

	private String numberBusDis(String asb_bus_id) throws InterruptedException, ExecutionException {
		Bus bus = null;
		
		DocumentReference docRef1 =  dbFirestore.collection(COL_NAME_BUS).document(asb_bus_id);
		ApiFuture<DocumentSnapshot> future = docRef1.get();
		DocumentSnapshot document1 = future.get();
			
		if (document1.exists()) {
			bus = document1.toObject(Bus.class);
			if (asb_bus_id.equals(document1.getId())) {
				return String.valueOf(bus.getBus_number_disc());
			} 
		}
		
		return null;
	}

	private String routeName(String sch_rou_id_name) throws InterruptedException, ExecutionException {
		Route route = null;
		
		DocumentReference docRef1 =  dbFirestore.collection(COL_NAME_ROUTE).document(sch_rou_id_name);
		ApiFuture<DocumentSnapshot> future = docRef1.get();
		DocumentSnapshot document1 = future.get();
			
		if (document1.exists()) {
			route = document1.toObject(Route.class);
			if (sch_rou_id_name.equals(document1.getId())) {
				String nameRouteString = routePlace(route.getRou_place_starting()) + " - " + routePlace(route.getRou_place_destination());
				return nameRouteString;
			} 
		}
		return null;
	}

	private String routePlace(String rou_id) throws InterruptedException, ExecutionException {
		Place place= null;
		
		DocumentReference docRef1 =  dbFirestore.collection(COL_NAME_PLACE).document(rou_id);
		ApiFuture<DocumentSnapshot> future = docRef1.get();
		DocumentSnapshot document1 = future.get();
			
		if (document1.exists()) {
			place = document1.toObject(Place.class);
			if (rou_id.equals(document1.getId())) {
				return place.getPla_name();
			} 
		}
		
		return null;
	}
}