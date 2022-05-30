$(document).ready(function() {

    $('#btn_cadastro').css('background-color', '#616161');


    var password = document.getElementById("password_cadastro");
    var password_confirm = document.getElementById("password_confirm");


    function validatePassword(){

        if(password.value == password_confirm.value) {
            $("#btn_cadastro").prop("disabled", false);
            $('#btn_cadastro').css('background-color', '#4a148c');

        }else{
            $('#btn_cadastro').css('background-color', '#616161');
            $("#btn_cadastro").prop("disabled", true);
        }
    }


    $("#password_cadastro").on('input',function() {
        if(password.value != ""){
            validatePassword();
        }else if(password_confirm != ""){
            validatePassword();
        }
    });

    $("#password_confirm").on('input',function() {
        if(password.value != ""){
            validatePassword();
        }else if(password_confirm != ""){
            validatePassword();
        }
    });


    $("#password_cadastro").on('input',function() {
        if(password.value == ""){
            $('#btn_cadastro').css('background-color', '#616161');
            $("#btn_cadastro").prop("disabled", true);
        }else if(password_confirm.value == ""){
            $('#btn_cadastro').css('background-color', '#616161');
            $("#btn_cadastro").prop("disabled", true);
        }
    });

    $("#password_confirm").on('input',function() {
        if(password.value == ""){
            $('#btn_cadastro').css('background-color', '#616161');
            $("#btn_cadastro").prop("disabled", true);
        }else if(password_confirm.value == ""){
            $('#btn_cadastro').css('background-color', '#616161');
            $("#btn_cadastro").prop("disabled", true);
        }
    });

});