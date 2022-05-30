$(document).ready(function() {
	
	var _context = $("meta[name='_context']").attr("content");
	if(_context == null){
	    _context = "";
	}

    mf_base.doAddDataTable($("table"), {
        order: [[ 0, 'asc' ]]
    });
	
	$("#status-turma").on("click", function(){
		var turma = $(this).attr("turma-id");
		var url = _context + "/turma/" + turma + "/status";
		
		var status_element = $(this);
		
		$.post(url, function(data) {
			console.log(data);
			status_element.html(data);
			location.reload();
		});
	});
	
	$("#btn-add-aluno").on("click", function() {
		$("#form-add-aluno").show();
	});
	
	$("#btn-cancel-add-aluno").on("click", function() {
		$("#form-add-aluno").hide();
	})
	
	$('#btn-add-professor').on("click", function(){
		$("#form-add-professor").show();
	});

	$("#btn-cancel-add-professor").on("click", function() {
		$("#form-add-professor").hide();
	});
});