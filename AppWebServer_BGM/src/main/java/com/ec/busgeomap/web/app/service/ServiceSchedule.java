package com.ec.busgeomap.web.app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.RandomStringUtils;
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
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class ServiceSchedule {
	
	private final Log log = LogFactory.getLog(getClass());
	
	public static final String COL_NAME_SCHEDULE="Schedule";
	public static final String COL_NAME_PLACE="Place";
	public static final String COL_NAME_ROUTE="Route";
	public static final String COL_NAME_ASSIGNE_BUS="Assignes_Bus";
	public static final String COL_NAME_BUS="Bus";
	public static final String IDENTIFICATE="BGM_SCH";
	public static final int ID_LENGTH=10;
	
	Firestore dbFirestore;

	// Method to generate Random ID DOCUMENT
	public String autoIdDocument() {
			return IDENTIFICATE + RandomStringUtils.randomAlphanumeric(ID_LENGTH);
	}

	// Mapping the Object of the Schedule class
	private Schedule mapSchedule(Schedule schedule) {
		
		Schedule sch = new Schedule();
		
		sch.setSch_id(schedule.getSch_id());
		sch.setSch_asb_id_number_disc(schedule.getSch_asb_id_number_disc());
		sch.setSch_rou_id_name(schedule.getSch_rou_id_name());
		sch.setSch_registration_date(new Date().getTime());
		sch.setSch_departure_time(schedule.getSch_departure_time());
		sch.setSch_state(schedule.getSch_state());
		sch.setSch_status(schedule.getSch_status());
		
		return sch;
	}

	// Method to create new Schedule record
	public String createSchedule(Schedule schedule) throws InterruptedException, ExecutionException {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		Schedule sch = mapSchedule(schedule);
		
		dbFirestore.collection(COL_NAME_SCHEDULE).document(sch.getSch_id()).set(sch);
		
		return dbFirestore.toString();
	}
	

	// Method to Find all Schedule
	public ArrayList<Schedule> readAllSchedule() throws InterruptedException, ExecutionException {
		
		Schedule schedule = null;
		
		ArrayList<Schedule> arrayList = new ArrayList<>();
		
		dbFirestore = FirestoreClient.getFirestore();
				
		ApiFuture<QuerySnapshot> query = dbFirestore.collection(COL_NAME_SCHEDULE).orderBy("sch_departure_time", Query.Direction.ASCENDING).get();
				
		List<QueryDocumentSnapshot> documents = query.get().getDocuments();
		
		for (QueryDocumentSnapshot document : documents) {
		
			//System.out.println("> " + document.getId() + " \t" + document.getString("rou_name"));
			
			schedule = document.toObject(Schedule.class);
			
			schedule.setSch_asb_id_number_disc(numberAssigneBusDisc(schedule.getSch_asb_id_number_disc())); // OBTENER DISCO DEL BUS
			schedule.setSch_rou_id_name(routeName(schedule.getSch_rou_id_name())); // OBTENER LA RUTA
			arrayList.add(schedule);
		}
		
		log.info("(SCHEDULE) Nº DE REGISTROS: [" + arrayList.size() + "]");
		
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
				String nameRouteString = route.getRou_name() + " (" + routePlace(route.getRou_place_starting()) + " - " + routePlace(route.getRou_place_destination()) + ")";
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

	// Method to Find a specific Schedule
	public Schedule readByIdDoc(String idDoc) throws InterruptedException, ExecutionException {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		DocumentReference docRef =  dbFirestore.collection(COL_NAME_SCHEDULE).document(idDoc);
		
		ApiFuture<DocumentSnapshot> future = docRef.get();
		
		DocumentSnapshot document = future.get();
		
		Schedule sch = null;
		
		if (document.exists()) {
			sch = document.toObject(Schedule.class);
			return sch;
		}else {
			return null;
		}
	}
	
	// Method to Update Schedule
	public String updateRoute(Schedule schedule) throws Exception {
			
		dbFirestore = FirestoreClient.getFirestore();
		
		Schedule sch = mapSchedule(schedule);
			
		dbFirestore.collection(COL_NAME_SCHEDULE).document(sch.getSch_id()).set(sch);
			
		return dbFirestore.toString();
	}
		
	// Method to Delete Schedule
	public String deleteRoute(String idDoc) throws InterruptedException, ExecutionException {
		Schedule sch = readByIdDoc(idDoc);
			
		ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME_SCHEDULE).document(sch.getSch_id()).delete();
			
		return writeResult.toString();
	}
}