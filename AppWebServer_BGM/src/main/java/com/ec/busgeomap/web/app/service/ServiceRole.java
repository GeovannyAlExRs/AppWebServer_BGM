package com.ec.busgeomap.web.app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.ec.busgeomap.web.app.model.Roles;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class ServiceRole {

	public static final String COL_NAME_ROLE="Roles";
	public static final String IDENTIFICATE="BGM_ROLE";
	public static final int ID_LENGTH=10;
	
	Firestore dbFirestore;
	
	// Method to generate Random ID DOCUMENT
	public String autoIdDocumentRole() {
		return IDENTIFICATE + RandomStringUtils.randomAlphanumeric(ID_LENGTH);
	}
	
	// Mapping the Object of the Roles class
	private Roles mapRoles(Roles roles) {
		
		Roles r = new Roles();
		
		r.setId_rol(roles.getId_rol());
		r.setRol_name(roles.getRol_name());
		r.setRol_description(roles.getRol_description());
		r.setRol_status(roles.getRol_status());
		r.setTimestamp(new Date().getTime());
		
		return r;
	}
	
	// Method to create new Roles record
	public String createRoles(Roles r) throws InterruptedException, ExecutionException {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		Roles rol = mapRoles(r);
		
		dbFirestore.collection(COL_NAME_ROLE).document(r.getId_rol()).set(rol);
		
		return dbFirestore.toString();
	}
	
	public ArrayList<Roles> readAllRol() throws InterruptedException, ExecutionException {
		
		Roles rol = null;
		
		ArrayList<Roles> arrayListRol = new ArrayList<>();
		
		dbFirestore = FirestoreClient.getFirestore();
				
		ApiFuture<QuerySnapshot> query = dbFirestore.collection(COL_NAME_ROLE).orderBy("rol_name", Query.Direction.ASCENDING).get();
				
		List<QueryDocumentSnapshot> documents = query.get().getDocuments();
		
		System.out.println("---- LISTA DE ROLES ----\n ID Document \t\t| ROL \t| DESCRIPCION \t| ESTADO" );
		
		for (QueryDocumentSnapshot document : documents) {
		
			System.out.println("> " + document.getId() + " \t" + document.getString("rol_name") + "\t " + document.getString("rol_description") + " \t" +  document.getBoolean("rol_status"));
			
			rol = document.toObject(Roles.class);
			
			arrayListRol.add(rol);
		}
		
		System.out.println("\n > LISTADO: " +arrayListRol);
		
		return arrayListRol;
	}
}
