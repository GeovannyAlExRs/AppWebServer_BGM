$(document).ready(function() {
    $('#placeList').DataTable({
        "language": {
            "lengthMenu": "Mostrar _MENU_ registros",
            "zeroRecords": "No se encontro resultados",
            "info": "Registro del _START_ al _END_ - Pagina _PAGES_",
            "infoEmpty": "Registro del 0 al 0 - Pagina 0",
            "infoFiltered": "(Registros total _MAX_)",
            "sSearch": "Buscar: ",
            "oPaginate": {
                "sFirst": "Primero",
                "sLast": "Ultimo",
                "sNext": "Siguiente",
                "sPrevious": "Anterior"
            },
            "sProcessing": "Procesando..."
        }
    });
} );