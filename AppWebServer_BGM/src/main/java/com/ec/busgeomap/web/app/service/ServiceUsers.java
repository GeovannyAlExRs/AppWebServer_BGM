package com.ec.busgeomap.web.app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.auth.UserRecord.UpdateRequest;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class ServiceUsers {
	
	private final Log log = LogFactory.getLog(getClass());
	
	public static final String COL_NAME_USER="Users";	
	public static final String IDENTIFICATE_USERS="BGM_USER";
	public static final String COL_NAME_EMPLOYMENT = "Employment";
	
	public static final int ID_LENGTH=10;
	
	public static final int OPTION_CREATE = 1;
	public static final int OPTION_UPDATE = 2;
	
	Firestore dbFirestore;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	// Method to generate Random ID DOCUMENT
	public String autoIdDocumentUsers() {
		return IDENTIFICATE_USERS + RandomStringUtils.randomAlphanumeric(ID_LENGTH);		
	}

	private Users mapUsers(Users users, int option) throws FirebaseAuthException {
		
		Users u = new Users();
		
		String uid = "";
		String email = "";
		String password = users.getUse_password();
		boolean estado = false;
		
		String passwordCrypt = passwordEncoder.encode(users.getUse_password());
		
		log.info("USERNAME " + users.getUse_name() + ", PASSWORD: (" + password + "), PASSWORD ENCRIPTADO : " + passwordCrypt);
		
		if (option == 1) {
			
			CreateRequest createUsers = new CreateRequest()
					.setUid(users.getUse_id())
					.setEmail(users.getUse_email())
					.setPassword(password);
			
			UserRecord userRecord = FirebaseAuth.getInstance().createUser(createUsers);
			
			uid = userRecord.getUid();
			email = userRecord.getEmail();
			estado = true;
			
			log.info("CREATE FIREBASE AUTH - UID : " + uid + " - Email : " + email);
			
		} else if (option == 2) {
			
			UpdateRequest updateUsers = new UpdateRequest(users.getUse_id())
					.setEmail(users.getUse_email())
					.setPassword(password);
			
			UserRecord userRecord = FirebaseAuth.getInstance().updateUser(updateUsers);
			
			uid = userRecord.getUid();
			email = userRecord.getEmail();
			estado = users.getUse_status();
			log.info("UPDATE FIREBASE AUTH - UID : " + uid + " - Email : " + email);
		}
		
		u.setUse_id(uid);
		u.setUse_email(email);
		u.setUse_password(password);
		u.setUse_pass_crypt(passwordCrypt);
		
		u.setUse_first_name(users.getUse_first_name());
		u.setUse_last_name(users.getUse_last_name());
		u.setUse_address(users.getUse_address());
		u.setUse_phone(users.getUse_phone());
		u.setUse_photo("NULL");
		u.setUse_name(users.getUse_name());		
		u.setUse_registration_date(new Date().getTime());
		u.setUse_employment_id(users.getUse_employment_id());
		u.setUse_status(estado);
		u.setUse_roles_id(users.getUse_roles_id());
		
		return u;
	}

	//Method to create new Users record
	public String createUsers(Users u) throws InterruptedException, ExecutionException, FirebaseAuthException {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		Users users = mapUsers(u, OPTION_CREATE);
		
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
		
		for (QueryDocumentSnapshot document : documents) {
		
			//System.out.println(document.getId() + " \t"+ document.getString("use_last_name") + " " +  document.getString("use_first_name"));
			
			users = document.toObject(Users.class);
			
			users.setUse_employment_id(readEmploymentDoc(users)); // Buscar DOC EMPLOYMENT 
			
			arrayList.add(users);
		}

		log.info("(USERS) Nº DE REGISTROS: [" + arrayList.size() + "]");
		
		return arrayList;
	}

	// Method to Find a Users Doc
	public Users readUsersDoc(String username) throws InterruptedException, ExecutionException {
		
		Users users = null;
		
		dbFirestore = FirestoreClient.getFirestore();
				
		ApiFuture<QuerySnapshot> query = dbFirestore.collection(COL_NAME_USER).whereEqualTo("use_name", username).get();
				
		List<QueryDocumentSnapshot> documents = query.get().getDocuments();
		
		for (QueryDocumentSnapshot document : documents) {
		
			//System.out.println("> " + document.getId() + " \t" + document.getString("use_name") + "\t " + document.getString("use_last_name") + " \t" +  document.getString("use_first_name"));
			users = document.toObject(Users.class);
		}

		log.info("(USERS) USERNAME: " + users );
		
		return users;
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
		
		Users users = mapUsers(u, OPTION_UPDATE);
		
		dbFirestore.collection(COL_NAME_USER).document(users.getUse_id()).set(users);
		
		return dbFirestore.toString();
	}
	
	// Method to Delete USERS
	public String deleteUsers(String idDoc) throws InterruptedException, ExecutionException, FirebaseAuthException {
		Users users = readByIdDoc(idDoc);
		
		ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME_USER).document(users.getUse_id()).delete();
		FirebaseAuth.getInstance().deleteUser(users.getUse_id());
		return writeResult.toString();
	}
}