$(document).ready(function() {
	
	var _context = $("meta[name='_context']").attr("content");
	if(_context == null){
	    _context = "";
	}

	$("#turma").on("change", function() {
		var turma = $(this).find(":selected").val();
		var responsavel = $("#responsavel").val();
			$("#ajudante").load(_context + "/turma/ajudantes/?idResponsavel=" + responsavel + "&idTurma=" + turma, function(){
				$("#ajudante").material_select();
			});
			$("#professor").load(_context + "/turma/professores/?idTurma=" + turma, function(){
				$("#professor").material_select();
			});
	});
	
});