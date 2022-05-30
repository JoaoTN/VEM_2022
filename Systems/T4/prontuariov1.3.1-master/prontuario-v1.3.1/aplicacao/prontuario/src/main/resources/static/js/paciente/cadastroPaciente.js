function validarCNS() {
    var cns = $("#cns").val().trim();
    if ((cns.length) == 0) {
        return true;
    } else {
        var soma = new Number;
        var resto = new Number;
        var dv = new Number;
        var pis = new String;
        var resultado = new String;
        pis = cns.substring(0, 11);
        soma = (((Number(pis.substring(0, 1))) * 15) +
            ((Number(pis.substring(1, 2))) * 14) +
            ((Number(pis.substring(2, 3))) * 13) +
            ((Number(pis.substring(3, 4))) * 12) +
            ((Number(pis.substring(4, 5))) * 11) +
            ((Number(pis.substring(5, 6))) * 10) +
            ((Number(pis.substring(6, 7))) * 9) +
            ((Number(pis.substring(7, 8))) * 8) +
            ((Number(pis.substring(8, 9))) * 7) +
            ((Number(pis.substring(9, 10))) * 6) +
            ((Number(pis.substring(10, 11))) * 5));
        resto = soma % 11;
        dv = 11 - resto;
        if (dv == 11) {
            dv = 0;
        }
        if (dv == 10) {
            soma = (((Number(pis.substring(0, 1))) * 15) +
                ((Number(pis.substring(1, 2))) * 14) +
                ((Number(pis.substring(2, 3))) * 13) +
                ((Number(pis.substring(3, 4))) * 12) +
                ((Number(pis.substring(4, 5))) * 11) +
                ((Number(pis.substring(5, 6))) * 10) +
                ((Number(pis.substring(6, 7))) * 9) +
                ((Number(pis.substring(7, 8))) * 8) +
                ((Number(pis.substring(8, 9))) * 7) +
                ((Number(pis.substring(9, 10))) * 6) +
                ((Number(pis.substring(10, 11))) * 5) + 2);
            resto = soma % 11;
            dv = 11 - resto;
            resultado = pis + "001" + String(dv);
        } else {
            resultado = pis + "000" + String(dv);
        }
        if (cns != resultado) {
            $("#cns").addClass("invalid");
            var toastHTML = '<span style="color: #ffffff; font-weight: bold;">Atenção! Numero de CNS invalido!</span>';
            Materialize.toast(toastHTML, 3000, "red");
            return false;
        }
        return true;
    }
}

$('#cns').mask('000000000000000', {reverse: false});

$("select[required]").css({display: "block", height: 0, padding: 0, width: 0, position: 'absolute'});