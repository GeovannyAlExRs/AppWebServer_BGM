$(document).ready(function(){
    let hola = "Geovanny Alexander"
    console.log(hola);
    /* registerUser.html - previsualizar archivo (foto png, jpg, jpge) */
    //Examinar archivo
    $(document).on("click", "#add-file", function(){
        console.log("clic...");
        $("#fotoUser").click();
    });

    //Accionar el evento
    $(document).on("change", "#fotoUser", function(){
        var fileInput = document.getElementById('fotoUser');
        var directorio = fileInput.value;
        var formato = /(.png|.jpg|.jpeg)$/i;
        if(!formato.exec(directorio)){
            alert('Seleccione la imagen correcta');
            showMessage("Archivo no valido");
            fileInput.value = '';
            return false;
        }
        else{
            //PRevio de la img
            if (fileInput.files && fileInput.files[0]) 
            {
                var visor = new FileReader();
                visor.onload = function(e) 
                {
                    document.getElementById('add-file').innerHTML = 
                    '<img class="foto" src="' + e.target.result + '"alt="Usuario"/><input th:value="${}" th:field="${u.use_photo}" type="text"/>';
                };
                visor.readAsDataURL(fileInput.files[0]);
            }
        }
    });

    /*function createPreview(file){
        var imagen = URL.createObjectURL(file);
        var img = $('<div class="add-photo-container"><div class="fotoUser" id="add-file"><img class="camara" src="' + imagen + '"alt="Usuario"/> </div> </div>');
        $(img).insertBefore("#add-photo");
    }*/
    
    function showMessage(message){
        $("#Message.tag").text(message);
        showModal("Message");
    }

    function showModal(card){
        $("#" + card).show();
        $(".modal").addClass("show");
    }    
}); 
