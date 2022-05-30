$(document).ready(function() {
	
	mf_base.doAddDataTable($("table"), {
		order: [[ 0, 'asc' ]],
		paging: false,
        ordering: false,
        info: false, 
        searching: false
	});
	
	$(".bottom-row").parent().hide();
	
});