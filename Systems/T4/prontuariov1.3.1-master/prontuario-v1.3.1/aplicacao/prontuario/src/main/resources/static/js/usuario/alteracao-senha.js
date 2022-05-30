$(document).ready(function() {
	
	$("#form-nova-senha").validate({
		rules: {
			senha: {
				required: true
			},
			senhaConfirm: {
				required: true,
				equalTo: "#nova-senha"
			}
		},
		
		messages: {
			senha: {
				required: "Digite a nova senha"
			},
			senhaConfirm: {
				required: "Confirme a nova senha",
				equalTo: "As senhas não correspondem"
			},
			required: "Este campo não pode ficar vazio"
		},
		
		errorElement : 'span',
        errorPlacement: function(error, element) {
          var placement = $(element).data('error');
          if (placement) {
            $(placement).append(error)
          } else {
            error.insertAfter(element);
          }
        }
	});
	
});