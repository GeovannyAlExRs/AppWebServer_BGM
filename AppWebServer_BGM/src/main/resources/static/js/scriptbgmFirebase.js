$(document).ready(function(){
  
    // For Firebase JS SDK v7.20.0 and later, measurementId is optional
    const firebaseConfig = {
        apiKey: "AIzaSyDv7BMFxvm_cn_QkSnSyLR5mbDO7vXmEzE",
        authDomain: "practica1-busgeomap.firebaseapp.com",
        databaseURL: "https://practica1-busgeomap.firebaseio.com",
        projectId: "practica1-busgeomap",
        storageBucket: "practica1-busgeomap.appspot.com",
        messagingSenderId: "796864090025",
        appId: "1:796864090025:web:7a7f69ecb0147727b73eb8",
        measurementId: "G-DN151XPGTT"
    };

    // Initialize Firebase
    firebase.initializeApp(firebaseConfig);
    firebase.analytics();

    const storage = firebase.storage();
    console.log(storage)
    const FileUpload = document.getElementById('fotoUser');
    const progressBar = document.getElementById('progressBar');

    FileUpload.addEventListener('change', (e) => {
        console.log(e);
        const file = e.target.files[0];
        const storageRef  = storage.ref('gallery/'+ file.name);
        const task = storageRef.put(file);
        
        task.on(
            firebase.storage.TaskEvent.STATE_CHANGED, 
            loadFile, errorFile, completeFile)

        function loadFile(data) {
            const loadBar = (data.bytesTransferred/data.totalBytes) * 100
            console.log('Load : ' + loadBar);
            progressBar.value = loadBar
        }
        function errorFile(data) {
            console.log('error : ' + data);
        }
        function completeFile(data) {
            console.log('success : ' + data);
            storageRef.getDownloadURL().then(
                    url => {
                        console.log('download Img : ', url);
                })
        }
    });
});