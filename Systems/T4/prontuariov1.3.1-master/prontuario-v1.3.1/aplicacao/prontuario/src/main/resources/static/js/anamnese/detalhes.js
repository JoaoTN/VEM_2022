$(document).ready(function() {
	
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");

	var _context = $("meta[name='_context']").attr("content");
	if(_context == null){
    	_context = "";
	}
	
	var dropzone = document.getElementById("table-detalhes-anamnese");
	
	if (dropzone != null) {
		var table = document.getElementById("table-detalhes-anamnese");
		var rows = document.getElementById("table-detalhes-anamnese");
		if (rows != null) {
			rows = rows.getElementsByTagName("tr").length;
		}
		table.rows[1].classList.add("primeiro");
		table.rows[rows - 1].classList.add("ultimo");
		
		dropzone.ondrop = function drop(e, ui) 
		{
			var perguntaId = ui.draggable.attr("id");
			var index = 0;
			var id = $('body').attr("anamnese");
			setTimeout(function() {
				var table = document.getElementById("table-detalhes-anamnese");
				for (var i = 0, row; row = table.rows[i]; i++) {
					if (table.rows[i].id == perguntaId) {
						index = i;
					}
				}
				$.ajax({
					url : _context + "/anamnese/altera-ordem-pergunta/" + id,
					beforeSend : function(request) {
						request.setRequestHeader(header, token);
					},
					async : false,
					type : 'GET',
					data : {
						pergunta : perguntaId,
						index : index
					},
					success : function(result) {
						window.location.reload();
					}
				});
			}, 10);
		}
	}

});
