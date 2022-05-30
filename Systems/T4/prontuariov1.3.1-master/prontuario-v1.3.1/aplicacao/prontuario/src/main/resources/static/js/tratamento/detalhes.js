$(document).ready(function() {
	var _context = $("meta[name='_context']").attr("content");
	if(_context == null){
	    _context = "";
	}
	
	$("#finalizar-atendimento").on("click", function() {
		var idAtendimento = $(this).data("atendimento");
		
		$.post(_context + "/atendimento/" + idAtendimento + "/finalizar", function(data) {
			$("#finalizar-atendimento").mouseleave();
			$("#status-atendimento").html(data);
		});
	});
	
	$("#validar-atendimento").on("click", function() {
		var idAtendimento = $(this).data("atendimento");
		
		$.post(_context + "/atendimento/" + idAtendimento + "/validar", function(data) {
			$("#validar-atendimento").mouseleave();
			$("#status-atendimento").html(data);
		});
	});
});