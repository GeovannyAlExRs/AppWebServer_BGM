$(document).ready(function(){
    $('.reloj').mdtimepicker({

        timeFormat: 'hh:mm:ss.000', // format of the time value (data-time attribute)

        format: 'h:mm tt', // format of the input value

        theme: 'dark', // 'red', 'purple', 'indigo', 'teal', 'green', 'dark'

        readOnly: true, // determina que el input sea de lectura

        // determines if display value has zero padding for hour value less than 10 (i.e. 05:30 PM); 24-hour format has padding by default
        hourPadding: false,

        // determines if clear button is visible  
        clearBtn: true

    });

     //Display Only Date till today // 
    var dtToday = new Date();
    var month = dtToday.getMonth() + 1;     // getMonth() is zero-based
    var day = dtToday.getDate();
    var year = dtToday.getFullYear();
    if(month < 10)
        month = '0' + month.toString();
    if(day < 10)
        day = '0' + day.toString();

    var maxDate = year + '-' + month + '-' + day;
    $('#dateID').attr('min', maxDate);
});