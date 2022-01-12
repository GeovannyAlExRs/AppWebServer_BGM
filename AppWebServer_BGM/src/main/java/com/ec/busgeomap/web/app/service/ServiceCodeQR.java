package com.ec.busgeomap.web.app.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.ec.busgeomap.web.app.model.Assignes_Bus;
import com.ec.busgeomap.web.app.model.Bus;
import com.ec.busgeomap.web.app.model.CodeQR;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
public class ServiceCodeQR {
	
	/*@Autowired
	private ResourceLoader resourceLoader;*/
	
	public static final String COL_NAME_CODE="CodeQR";
	public static final String COL_NAME_ASSIGNE_BUS="Assignes_Bus";
	public static final String IDENTIFICATE="BGM_CODE";
	public static final int ID_LENGTH=10;
	public static final int OP_GENERATE=1;
	public static final int OP_CREATE=2;
	public static final int OP_UPDATE=3;
	
	Firestore dbFirestore;
	
	// Method to generate Random ID DOCUMENT
	public String autoIdDocument() {
		return IDENTIFICATE + RandomStringUtils.randomAlphanumeric(ID_LENGTH);
	}
	
	// Mapping the Object of the CodeQR class
	private CodeQR mapCodeQR(CodeQR code, int option) throws WriterException, IOException {
		
		CodeQR qr = new CodeQR();
		
		if (option == 1) {
			
			System.out.println("ENTRO A LA OPCION[1] GENERAR");
			qr.setGqr_code(code.getGqr_code());
			qr.setGqr_description(code.getGqr_description());
			qr.setGqr_registration_date(new Date().getTime());
			qr.setGqr_asb_bus_id(code.getGqr_asb_bus_id());
			
			byte [] imgQR = generatorQRCode(qr.getGqr_code(), 500, 500);		
			String qrcodeIMG = Base64.getEncoder().encodeToString(imgQR);
			qr.setGqr_image(qrcodeIMG);
			
			qr.setGqr_status(true);
			
		} 
		if (option == 2 || option == 3) {
			
			System.out.println("ENTRO A LA OPCION[2] CREAR - OPCION[3] ACTUALIZAR");
			qr.setGqr_code(code.getGqr_code());
			qr.setGqr_description(code.getGqr_description());
			qr.setGqr_registration_date(new Date().getTime());
			qr.setGqr_asb_bus_id(code.getGqr_asb_bus_id());
			byte [] imgQR = generatorQRCode(qr.getGqr_code(), 500, 500);		
			String qrcodeIMG = Base64.getEncoder().encodeToString(imgQR);
			qr.setGqr_image(qrcodeIMG);
			//qr.setGqr_image(code.getGqr_image());
			qr.setGqr_status(code.getGqr_status());
			
		} 
		
		System.out.println(" OBJETO DE LA OPCION [" + option + "] : " + qr);
		
		return qr;
	}
	
	// Generator Code QR a Memory
	public CodeQR generatorQR(CodeQR qr) throws WriterException, IOException {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		CodeQR code = mapCodeQR(qr, OP_GENERATE);
		
		System.out.println(" CODE QR GENERATOR> " + code);
		
		dbFirestore.collection(COL_NAME_CODE).document(qr.getGqr_code()).set(code);
		
		return code;
	}
	
	// Method to create new QR record
	public String createQR(CodeQR qr) throws InterruptedException, ExecutionException, WriterException, IOException {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		CodeQR code = mapCodeQR(qr, OP_CREATE);
		
		System.out.println("OBJETO QR ANTES DE GUARDAR > " + code);
		dbFirestore.collection(COL_NAME_CODE).document(qr.getGqr_code()).set(code);
		
		return dbFirestore.toString();
	}
	
	// Method to Find all CODE QR
	public ArrayList<CodeQR> readAllQR() throws InterruptedException, ExecutionException {
		
		CodeQR qr = null;
		
		ArrayList<CodeQR> arrayList = new ArrayList<>();
		
		dbFirestore = FirestoreClient.getFirestore();
				
		ApiFuture<QuerySnapshot> query = dbFirestore.collection(COL_NAME_CODE).get();
				
		List<QueryDocumentSnapshot> documents = query.get().getDocuments();
		
		System.out.println("---- LISTA DE QR ----\n ID Document \t\t| Code QR");
		
		for (QueryDocumentSnapshot document : documents) {
		
			System.out.println("> " + document.getId() + " \t" + document.getData());
			
			qr = document.toObject(CodeQR.class);
			
			qr.setGqr_asb_bus_id(readAssignesBusByDisc(qr));
			
			arrayList.add(qr);
		}
		
		System.out.println("\n > LISTADO: " +arrayList);
		
		return arrayList;
	}

	
	// Method to Find DISC BUS
	public String readAssignesBusByDisc(CodeQR qr) throws InterruptedException, ExecutionException {
		
		Assignes_Bus ab = null;
		String numDisc = "";
				
		DocumentReference docRef1 =  dbFirestore.collection(COL_NAME_ASSIGNE_BUS).document(qr.getGqr_asb_bus_id());
		ApiFuture<DocumentSnapshot> future = docRef1.get();
		DocumentSnapshot document = future.get();
		
		if (document.exists()) {
			ab = document.toObject(Assignes_Bus.class);
			if (qr.getGqr_asb_bus_id().equals(document.getId())) {
				ab.setAsb_bus_id(readBusDoc(ab)); // Buscar Disco del Bus
				numDisc = ab.getAsb_bus_id();
			}
		}
		
		return numDisc;
	}
	
	// Method to Find a DISC BUS Doc
	private String readBusDoc(Assignes_Bus ab) throws InterruptedException, ExecutionException {
		Bus bus = null;
		
		DocumentReference docRef1 =  dbFirestore.collection("Bus").document(ab.getAsb_bus_id());
		ApiFuture<DocumentSnapshot> future = docRef1.get();
		DocumentSnapshot document1 = future.get();
		
		if (document1.exists()) {
			bus = document1.toObject(Bus.class);
			
			if (ab.getAsb_bus_id().equals(document1.getId())) {
				ab.setAsb_bus_id(String.valueOf(bus.getBus_number_disc()));
			} 
		}
		
		return String.valueOf(bus.getBus_number_disc());
	}

	// Method to Update CODE QR
	public String updateQR(CodeQR codeQR) throws Exception {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		CodeQR qr = mapCodeQR(codeQR, OP_UPDATE);
		
		dbFirestore.collection(COL_NAME_CODE).document(qr.getGqr_code()).set(qr);
		System.err.println(" QR Actualizado");
		
		return dbFirestore.toString();
	}
	
	// Method to Delete CODE QR
	public String deleteQR(String idDoc) throws InterruptedException, ExecutionException {
		CodeQR qr = readByIdDoc(idDoc);
		
		ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME_CODE).document(qr.getGqr_code()).delete();
		
		return writeResult.toString();
	}

	// Method to Find a specific CODE QR
	public CodeQR readByIdDoc(String idDoc) throws InterruptedException, ExecutionException {
		
		dbFirestore = FirestoreClient.getFirestore();
		
		DocumentReference docRef =  dbFirestore.collection(COL_NAME_CODE).document(idDoc);
		
		ApiFuture<DocumentSnapshot> future = docRef.get();
		
		DocumentSnapshot document = future.get();
		
		CodeQR qr = null;
		
		if (document.exists()) {
			qr = document.toObject(CodeQR.class);
			System.out.println("OBJETO QR RECUPERADO : "+ qr);
			return qr;
		}else {
			return null;
		}
	}
		
	private byte[] generatorQRCode(String code, int width, int height) throws WriterException, IOException {

		/*String nameCodeQR = code + ".png";
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(System.getProperty("user.home"));
		builder.append(File.separator);
		builder.append("codeqr");
		builder.append(File.separator);
		builder.append(nameCodeQR);
		
		Path path = Paths.get(builder.toString());*/
		
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		
		BitMatrix bitMatrix = qrCodeWriter.encode(code, BarcodeFormat.QR_CODE, width, height);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream);
		
		byte[] qrData = outputStream.toByteArray();
		
		//MatrixToImageWriter.writeToPath(bitMatrix, "png", path);
		
		return qrData;
	}
	
	public String readCodeQRImg(String code) throws Exception {
		
		String nameCodeQR = code + ".png";
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(System.getProperty("user.home"));
		builder.append(File.separator);
		builder.append("codeqr");
		builder.append(File.separator);
		builder.append(nameCodeQR);
		
		File file = new File(builder.toString());
		
		BufferedImage bufferedImage = ImageIO.read(file);
		
		LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
		
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		
		Result result = new MultiFormatReader().decode(bitmap);
		
		System.out.println("NOMBRE DE LA IMAGEN READ" + result.getText());
		System.out.println("NOMBRE DE LA IMAGEN RUTA" + builder.toString());
		
		return builder.toString();
	}
}