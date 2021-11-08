$(document).ready(function(){
    console.log("Vamos a concatenar")
    // Concatenar input Date con Text (Timestamp)    
    $(document).on("click", "#submitSch", function(){
        console.log("clic...");
        
        varDate=document.getElementById('dateID').value;
        varHour=document.getElementById('hourID').value;
    
        finalID = varDate+ ' ' +varHour;
        timestamp = Date.parse(finalID)
        document.getElementById('finalID').value=timestamp;
        console.log(timestamp);
        console.log(finalID);
    });

//     document.getElementById("selectBus").onchange = function() {
        
//         var link = /*[[@{/app_busgeomap/assigned_bus}]]*/ 'test'; 
//             document.getElementById("user_form").action = link; 

//         var select = $('#nameDrive').load('app_busgeomap/assigned_bus', "numDic"=$('#selectBus').val);
//         $("input[name='asb_driver_firtsname']").val(select);

//    };
    
    // function mostrarDato() {
    //     $ajax({
    //         type : "get",
    //         url : "app_busgeomap/assigned_bus",
    //         data : {
    //             assBus : $('#selectBus').val()
    //         },
    //         success : function(html) {
    //             $('#nameDrive').html(html);
    //         },
    //         error : function(e){
    //             console.error(e);
    //         }
    //     });
    //     return false;
    // }
    /*
    document.getElementById("selectBus").onchange = function() {
         //Referencia al option seleccionado 
        var option = this.value;
        window.location = "assigned_bus/"+option; 
        var disco = document.getElementById('nameDrive');
       
        disco.innerHTML = '<input th:text="${a_bus.asb_accompanist_firtsname } ${a_bus.asb_accompanist_lastname}" type="text" class="form-control"/>';
    };*/
    
    /*function myFunction(id) {
        var disc = $("#userIdHiddenInput").val(id);
        //var disc = document.getElementById("selectBus").value;
               
        window.location = "assigned_bus/"+disc;
        
        document.getElementById('nameDrive').innerHTML = 
                    '<input th:text="${a_bus.asb_accompanist_firtsname } ${a_bus.asb_accompanist_lastname}" type="text" class="form-control"/>';
                    
                    
      }*/
});