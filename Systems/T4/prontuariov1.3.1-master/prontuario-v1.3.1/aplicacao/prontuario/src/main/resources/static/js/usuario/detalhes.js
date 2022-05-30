var _context = $("meta[name='_context']").attr("content");
if(_context == null){
    _context = "";
}

$(document).ready(function() {
	
	listenerSalvarSenha();
	validatorFormSenha();
	listenerMostrarOcultarFormSenha();
	
	$("#form-senha").hide();
	
	$("#form-dados").hide();
	
	$("#alterar-dados-button").on("click", function() {
		$("#form-dados").show();
		$("#opcoes-div").hide();
	});
	
	$("#cancelar-salvar-dados-button").on("click", function() {
		$("#form-dados").hide();
		$("#opcoes-div").show();
	});
});

function listenerSalvarSenha() {
	$("#salvar-senha-button").on("click", function() {
		if($("#form-alterar-senha").valid()){
			var url = _context + "/usuario/alterar-senha";
			
			var usuarioId = $("#id-usuario").val();
			var senhaAtual = $("#senha-atual").val();
			var novaSenha = $("#nova-senha-confirm").val();
			
			var data = {
				usuarioId : usuarioId,
				senhaAtual : senhaAtual,
				novaSenha : novaSenha
			}
			
			$.post(url, data, function() {
			})
			.done(function() {
				window.location.replace(_context + "/logout");
			})
			.fail(function(data) {
				$("#form-senha").html(data.responseText);
				listenerSalvarSenha();
				validatorFormSenha();
				listenerMostrarOcultarFormSenha();
			});
		}
	});
}

function validatorFormSenha() {
	$("#form-alterar-senha").validate({
		rules: {
			senhaAtual: {
				required: true
			},
			novaSenha: {
				required: true
			},
			novaSenhaConfirm: {
				required: true,
				equalTo: "#nova-senha"
			}
		},
		
		messages: {
			senhaAtual: {
				required: "Digite a senha atual"
			},
			novaSenha: {
				required: "Digite a nova senha"
			},
			novaSenhaConfirm: {
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
}

function listenerMostrarOcultarFormSenha() {
	$("#mudar-senha-button").on("click", function() {
		$("#opcoes-div").hide();
		$("#form-senha").show();
		$("#mensagem").hide();
	});
	
	$("#cancelar-salvar-senha-button").on("click", function() {
		$("#form-senha").hide();
		$("#opcoes-div").show();
	});
}