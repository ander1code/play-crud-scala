$(function() {
    
    if(window.location.href.includes("add") || window.location.href.includes("save") || window.location.href.includes("edit") || window.location.href.includes("update") || window.location.href.includes("delete")){

        /***************************/

        $('#salary').maskMoney({
            thousands: '',
            decimal: '.',
            precision: 2,
            allowZero: true,
            allowNegative: false,
            allowEmpty: false
        });

        /***************************/

        var date = new Date();

        date.setFullYear(date.getFullYear() - 18);

        if(($('#birthday').val() == "") || ($('#birthday').val() == date.toString())){
            $('#birthday').val(date);
        }

        $('#birthday').datetimepicker({
            format: "MM/DD/YYYY"
        });

        $('#birthday').data("DateTimePicker").maxDate(date);

        /***************************/
    }

    if(window.location.href.includes("add") || window.location.href.includes("save") || window.location.href.includes("edit") || window.location.href.includes("update") || window.location.href.includes("delete") || window.location.href.includes("login")){
        if($(".div-errors").is(":visible")){
            $(".div-errors").fadeOut(10000);
        }
    }

    if(window.location.pathname == "/person"){
        $('#mytable').DataTable({
         "pageLength": 7
        });
        $("#mytable_length").remove();
    }

    if ($(".alert").is(":visible")) {
        $(".alert").fadeOut(10000);
    }

    $("#lnkLogoff").click(function(){
        $("#mdlLogout").modal("show");
    });
});

