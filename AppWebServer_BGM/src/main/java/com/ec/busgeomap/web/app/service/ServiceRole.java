package com.ec.busgeomap.web.app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.ec.busgeomap.web.app.model.Roles;
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
public class ServiceRole {

	private final Log log = LogFactory.getLog(getClass());
	
	public static final String COL_NAME_ROLE="Roles";
	public static final String IDENTIFICATE="BGM_ROLE";
	public static final int ID_LENGTH=10;
	
	Firestore dbFirestore;
	
	// Method to generate Random ID DOCUMENT
	public String autoIdDocumentRole() {
		return IDENTIFICATE + RandomStringUtils.randomAlphanumeric(ID_LENGTH);
	}
	
	// Mapping the Object of the Roles class
	private Roles mapRole(Roles roles) {
		
		Roles r = new Roles();
		
		r.setId_rol(roles.getId_rol());
		r.setRol_name(roles.getRol_name());
		r.setRol_description(roles.getRol_description());
		r.setRol_status(roles.getRol_status());
		r.setTimestamp(new Date().getTime());
		
		return r;
	}
	
	// Method to create new Roles record
	public String createRole(Roles r) throws InterruptedException, ExecutionException {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		Roles rol = mapRole(r);
		
		dbFirestore.collection(COL_NAME_ROLE).document(r.getId_rol()).set(rol);
		
		return dbFirestore.toString();
	}
	
	// Method to Find all roles
	public ArrayList<Roles> readAllRol() throws InterruptedException, ExecutionException {
		
		Roles rol = null;
		
		ArrayList<Roles> arrayList = new ArrayList<>();
		
		dbFirestore = FirestoreClient.getFirestore();
				
		ApiFuture<QuerySnapshot> query = dbFirestore.collection(COL_NAME_ROLE).orderBy("rol_name", Query.Direction.ASCENDING).get();
				
		List<QueryDocumentSnapshot> documents = query.get().getDocuments();
		
		for (QueryDocumentSnapshot document : documents) {
		
			//System.out.println("> " + document.getId() + " \t" + document.getString("rol_name") + "\t " + document.getString("rol_description") + " \t" +  document.getBoolean("rol_status"));
			rol = document.toObject(Roles.class);
			
			arrayList.add(rol);
		}
		
		log.info("(ROLE) Nº DE REGISTROS: [" + arrayList.size() + "]");
		
		return arrayList;
	}
	
	// Method to Find a specific role
	public Roles readByIdDoc(String idDoc) throws InterruptedException, ExecutionException {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		DocumentReference docRef =  dbFirestore.collection(COL_NAME_ROLE).document(idDoc);
		
		ApiFuture<DocumentSnapshot> future = docRef.get();
		
		DocumentSnapshot document = future.get();
		
		Roles rol = null;
		
		if (document.exists()) {
			rol = document.toObject(Roles.class);
			return rol;
		}else {
			return null;
		}
	}

	// Method to Find a Role Doc
	public Roles readRoleDoc(String idDoc) throws InterruptedException, ExecutionException {
		
		Roles roles = null;
		
		dbFirestore = FirestoreClient.getFirestore();
				
		ApiFuture<QuerySnapshot> query = dbFirestore.collection(COL_NAME_ROLE).whereEqualTo("id_rol", idDoc).get();
				
		List<QueryDocumentSnapshot> documents = query.get().getDocuments();
		
		System.out.println("---- LISTA DE ROLES ----\n ID Document \t\t| ROL \t| DESCRIPCION \t| ESTADO" );
		
		for (QueryDocumentSnapshot document : documents) {
		
			System.out.println("> " + document.getId() + " \t" + document.getString("use_name") + "\t " + document.getString("use_last_name") + " \t" +  document.getString("use_first_name"));
			
			roles = document.toObject(Roles.class);
		}

		return roles;
	}
	
	// Method to Update Rol
	public String updateRole(Roles roles) throws Exception {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		Roles rol = mapRole(roles);
		
		dbFirestore.collection(COL_NAME_ROLE).document(rol.getId_rol()).set(rol);
		System.err.println(" ROL Actualizado");
		
		return dbFirestore.toString();
	}
	
	// Method to Delete Rol
	public String deleteRole(String idDoc) throws InterruptedException, ExecutionException {
		Roles role = readByIdDoc(idDoc);
		
		ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME_ROLE).document(role.getId_rol()).delete();
		
		return writeResult.toString();
	}
}
