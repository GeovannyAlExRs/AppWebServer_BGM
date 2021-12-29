package com.ec.busgeomap.web.app.service;

import java.io.IOException;

import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

/**
 * 
 * @author Geovanny AlEx Rs
 * @description: This class is used to establish a connection to the firebase database.
 * PATH_RESOURCE: indicate the path of the json 
 *
 */
@Service
public class FirebaseInitialize {
	
	private static final String PATH_RESOURCE = "./practica1-busgeomap-firebase-adminsdk-nryva-299a7fccde.json";
	
	@SuppressWarnings("deprecation")
	@PostConstruct
	private void initialDBFirebase() throws IOException{
		
		InputStream serviceAccount = this.getClass().getClassLoader().getResourceAsStream(PATH_RESOURCE);
		
		FirebaseOptions options = new FirebaseOptions.Builder()
		  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
		  .setDatabaseUrl("https://practica1-busgeomap.firebaseio.com")
		  .setStorageBucket("practica1-busgeomap.appspot.com/") //gs://practica1-busgeomap.appspot.com/
		  .build();
		 
		if (FirebaseApp.getApps().isEmpty()) {
			FirebaseApp.initializeApp(options);
		}
	}

	public Firestore getFirebase() {
		return FirestoreClient.getFirestore();	
	}
}
