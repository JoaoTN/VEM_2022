$(document).ready(function(){
	var form = $("#pergunta").serialize();
	var anamnese = $("#idAnamnese").serialize();
	
	$.ajax({
		type:"post",
		data:{form: form, anamnese: anamnese},
		url: _context + "/anamnese/salvar-pergunta",
		async: false,
		dataType: "json",
		success: function(data){
		}
	});
});