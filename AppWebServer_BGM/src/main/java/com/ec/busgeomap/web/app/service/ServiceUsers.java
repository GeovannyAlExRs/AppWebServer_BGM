package com.ec.busgeomap.web.app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.ec.busgeomap.web.app.model.Employment;
import com.ec.busgeomap.web.app.model.Users;
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
public class ServiceUsers {

	public static final String COL_NAME_USER="Users";	
	public static final String IDENTIFICATE_USERS="BGM_USER";
	public static final String COL_NAME_EMPLOYMENT = "Employment";
	
	public static final int ID_LENGTH=10;
	
	Firestore dbFirestore;
	
	// Method to generate Random ID DOCUMENT
	public String autoIdDocumentUsers() {
		return IDENTIFICATE_USERS + RandomStringUtils.randomAlphanumeric(ID_LENGTH);		}

	private Users mapUsers(Users users) {
		
		Users u = new Users();
		
		u.setUse_id(users.getUse_id());
		u.setUse_first_name(users.getUse_first_name());
		u.setUse_last_name(users.getUse_last_name());
		u.setUse_address(users.getUse_address());
		u.setUse_phone(users.getUse_phone());
		u.setUse_email(users.getUse_email());
		u.setUse_photo("NULL");
		u.setUse_name(users.getUse_name());
		u.setUse_password(users.getUse_password());
		u.setUse_registration_date(new Date().getTime());
		u.setUse_employment_id(users.getUse_employment_id());
		u.setUse_status(users.getUse_status());
		u.setUse_roles_id(users.getUse_roles_id());
		
		return u;
	}

	//Method to create new Users record
	public String createUsers(Users u) throws InterruptedException, ExecutionException {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		Users users = mapUsers(u);
		
		dbFirestore.collection(COL_NAME_USER).document(u.getUse_id()).set(users);
		
		return dbFirestore.toString();
	}

	// Method to Find all users
	public ArrayList<Users> readAllUsers() throws InterruptedException, ExecutionException {
		
		Users users = null;
		
		ArrayList<Users> arrayList = new ArrayList<>();
		
		dbFirestore = FirestoreClient.getFirestore();
				
		ApiFuture<QuerySnapshot> query = dbFirestore.collection(COL_NAME_USER).orderBy("use_last_name", Query.Direction.ASCENDING).get();
				
		List<QueryDocumentSnapshot> documents = query.get().getDocuments();
		
		System.out.println("---- LISTA DE USERS ----\n ID Document \t\t| ROL \t| DESCRIPCION \t| ESTADO" );
		
		for (QueryDocumentSnapshot document : documents) {
		
			System.out.println("> " + document.getId() + " \t" + document.getString("use_name") + "\t " + document.getString("use_last_name") + " \t" +  document.getString("use_first_name"));
			
			users = document.toObject(Users.class);
			
			users.setUse_employment_id(readEmploymentDoc(users)); // Buscar DOC EMPLOYMENT 
			
			arrayList.add(users);
		}

		return arrayList;
	}

	// Method to Find a Employment Doc
	private String readEmploymentDoc(Users u) throws InterruptedException, ExecutionException{
		Employment employment = null;
		
		DocumentReference docRef1 =  dbFirestore.collection(COL_NAME_EMPLOYMENT).document(u.getUse_employment_id());
		
		ApiFuture<DocumentSnapshot> future = docRef1.get();
		
		DocumentSnapshot document = future.get();
		
		if (document.exists()) {
			employment = document.toObject(Employment.class);

			if (u.getUse_employment_id().equals(document.getId())) {
				u.setUse_employment_id(employment.getEmp_name());
			} 
		}
		
		return employment.getEmp_name();
	}
	
	// Method to Find a specific user
	public Users readByIdDoc(String idDoc) throws InterruptedException, ExecutionException {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		DocumentReference docRef =  dbFirestore.collection(COL_NAME_USER).document(idDoc);
		
		ApiFuture<DocumentSnapshot> future = docRef.get();
		
		DocumentSnapshot document = future.get();
		
		Users users = null;
		
		if (document.exists()) {
			users = document.toObject(Users.class);
			return users;
		}else {
			return null;
		}
	}
	
	// Method to Update Users
	public String updateUsers(Users u) throws Exception {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		Users users = mapUsers(u);
		
		dbFirestore.collection(COL_NAME_USER).document(users.getUse_id()).set(users);
		System.err.println("USUARIO Actualizado");
		
		return dbFirestore.toString();
	}
	
	// Method to Delete USERS
	public String deleteUsers(String idDoc) throws InterruptedException, ExecutionException {
		Users users = readByIdDoc(idDoc);
		
		ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME_USER).document(users.getUse_id()).delete();
		
		return writeResult.toString();
	}
}