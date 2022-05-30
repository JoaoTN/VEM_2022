$(document).ready(function() {

	mf_base.doAddDataTable($("#professores"), {
		order: [[ 0, 'asc' ]]
	});
	
	mf_base.doAddDataTable($("#turmas"), {
		order: [[ 1, 'desc' ],[0, 'asc']]
	});

    mf_base.doAddDataTable($("#atendimentos"), {
        order: [[ 4, 'asc' ]]
    });
	
});