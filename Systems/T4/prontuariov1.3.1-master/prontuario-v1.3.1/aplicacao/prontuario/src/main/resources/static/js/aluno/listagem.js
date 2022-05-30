$(document).ready(function(){
    $('.modal-trigger').leanModal();
    mf_base.doAddDataTable($("#alunos"), {
        order: [[ 0, 'asc' ]]
    });

    mf_base.doAddDataTable($("#atendimentos"), {
        order: [[ 4, 'asc' ]]
    });
});




  