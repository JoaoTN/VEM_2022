$(document).ready(function() {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	var _context = $("meta[name='_context']").attr("content");
	if(_context == null){
	    _context = "";
	}
	
	var odontogramaId = $("#odontograma-id").val();
	
	$("#odonto-procedimentos").hide();
	$("#odonto-procedimentos-existentes").hide();
	$("#procedimento-geral").hide();
	$("#procedimento-existente-geral").hide();
	$("#label-resultado-busca-patologia").hide();
	$("#label-patologias-selecionadas").hide();
	$("#label-resultado-busca-novo-procedimento").hide();
	$("#label-novos-procedimentos-selecionados").hide();
	$("#label-resultado-busca-procedimento-existente").hide();
	$("#label-procedimentos-selecionados-existentes").hide();
	$("#patologias-dente-busca").hide();
	
	
	carregarTablePatologias();
	carregarTableProcedimentos();
	marcarPatologias();
	marcarProcedimentos();
	marcarProcedimentosExistentes();
	
	$("#tab-patologias").click(function() {
		$(".arcadas-container").hide();
		$("#odonto-patologias").fadeIn(500);
		
		$(".geral").hide();
		$("#patologia-geral").show();
	});
	
	$("#tab-procedimentos").click(function() {
		$(".arcadas-container").hide();
		$("#odonto-procedimentos").fadeIn(500);
		
		$(".geral").hide();
		$("#procedimento-geral").show();
	});
	
	$("#tab-procedimentos-existentes").click(function() {
		$(".arcadas-container").hide();
		$("#odonto-procedimentos-existentes").fadeIn(500);
		
		$(".geral").hide();
		$("#procedimento-existente-geral").show();
	});
	
	$("#select-arcada").change(function() {
		var arcada = $("#select-arcada").val();
		
		if(arcada == "permanente") {
			$(".arcada-permanente").show();
			$(".arcada-decidua").hide();
			
		} else if(arcada == "decidua") {
			$(".arcada-decidua").show();
			$(".arcada-permanente").hide();
		} else {
			$(".arcada-permanente").show();
			$(".arcada-decidua").show();
		}
	});
	
	$(".face, .txt-svg").on("mouseenter", function(){
		// ...
		$(this).addClass("focus-face");
	});

	$(".face, .txt-svg").on("mouseleave", function(){
		$(this).removeClass("focus-face");
	});
	
	$(".face").on("click", function(){
		var tipo = $(this).attr("tipo");
		
		//Decidindo qual modal deve ser aberto de acordo com o tipo do odontograma
		switch (tipo) {
			case "PA":
				
				if($("#modal-patologias").length){
					$("#modal-patologias").openModal();
					$("#id-face-dente-patologia").val($(this).attr("face"));
					$("#id-dente-patologia").val("");
				}
				
				break;
				
			case "PR":
					
				if($("#modal-procedimentos").length){
					$("#modal-procedimentos").openModal();
					$("#id-face-dente-procedimento").val($(this).attr("face"));
					$("#id-dente-procedimento").val("");
					
					var dente = $("#id-face-dente-procedimento").val();					
					
					$('#patologias-dente-selecionado-1').empty();
					$('#patologias-dente-selecionado-2').empty();
					$('#patologias-dente-selecionado').empty();
					$('#data-patologia-tratamento').val("");
					
					$.ajax({
						url : _context + "/odontograma/buscar-patologia/"+odontogramaId,
						beforeSend : function(request) {
							request.setRequestHeader(header, token);
						},
						async : false,
						type : 'GET',
						data : {
							face : dente
						},
						success : function(data) {
							if(data.length > 0){
								$("#patologias-dente-busca").show();
								var index = 2;
								$.each(data, function(key, value) {
										adicionarResultado(value.id, "patologias-dente", '#patologias-dente-selecionado-1', '#patologias-dente-selecionado-2', index, value.tipo.nome, "patologias-dente");
										index++;
								});
							}else{
								$("#patologias-dente-busca").hide();
							}
						}
					});
				}				
				
				break;
				
			case "PE":
				
				if($("#modal-procedimentos-existentes").length){
					$("#modal-procedimentos-existentes").openModal();
					$("#id-face-dente-proced-exist").val($(this).attr("face"));
					$("#id-dente-proced-exist").val("");
				}
				
				break;
				
			default:
				break;
		}
	});
	
	$(".txt-dente").on("click", function() {		
		var tipo = $(this).attr("tipo");
		
		//Decidindo qual modal deve ser aberto de acordo com o tipo do odontograma
		switch (tipo) {
			case "PA":
				
				if($("#modal-patologias").length){
					$("#modal-patologias").openModal();
					$("#id-face-dente-patologia").val("");
					$("#id-dente-patologia").val(this.textContent);
				}
				
				break;
				
			case "PR":
					
				if($("#modal-procedimentos").length){
					$("#modal-procedimentos").openModal();
					$("#id-face-dente-procedimento").val("");
					$("#id-dente-procedimento").val(this.textContent);
					
					var dente = $(this).attr("dente");
					
					$('#patologias-dente-selecionado-1').empty();
					$('#patologias-dente-selecionado-2').empty();
					$('#patologias-dente-selecionado').empty();
					$('#data-patologia-tratamento').val("");
					
					$.ajax({
						url : _context + "/odontograma/buscar-patologia/"+odontogramaId,
						beforeSend : function(request) {
							request.setRequestHeader(header, token);
						},
						async : false,
						type : 'GET',
						data : {
							dente : dente
						},
						success : function(data) {
							if(data.length > 0){
								$("#patologias-dente-busca").show();
								var index = 2;
								$.each(data, function(key, value) {
										adicionarResultado(value.id, "patologias-dente", '#patologias-dente-selecionado-1', '#patologias-dente-selecionado-2', index, value.tipo.nome, "patologias-dente");
										index++;
								});
							}else{
								$("#patologias-dente-busca").hide();
							}
						}
					});
				}
				
				break;
				
			case "PE":
				
				if($("#modal-procedimentos-existentes").length){
					$("#modal-procedimentos-existentes").openModal();
					$("#id-face-dente-proced-exist").val("");
					$("#id-dente-proced-exist").val(this.textContent);
				}
				
				break;
				
			default:
				break;
		}
	});
	
	$(".geral").on("click", function() {
		var tipo = $(this).attr("tipo");
		
		//Decidindo qual modal deve ser aberto de acordo com o tipo do odontograma
		switch (tipo) {
			case "PA":
				
				if($("#modal-patologias").length){
					$("#modal-patologias").openModal();
					$("#id-face-dente-patologia").val("");
					$("#id-dente-patologia").val("");
				}
				
				break;
				
			case "PR":
					
				if($("#modal-procedimentos").length){
					$("#modal-procedimentos").openModal();
					$("#id-face-dente-procedimento").val("");
					$("#id-dente-procedimento").val("");	
					
				}
				
				$('#patologias-dente-selecionado-1').empty();
				$('#patologias-dente-selecionado-2').empty();
				$('#patologias-dente-selecionado').empty();
				$('#data-patologia-tratamento').val("");
				
				$.ajax({
					url : _context + "/odontograma/buscar-patologia/"+odontogramaId,
					beforeSend : function(request) {
						request.setRequestHeader(header, token);
					},
					async : false,
					type : 'GET',
					success : function(data) {
						if(data.length > 0){
							$("#patologias-dente-busca").show();
							var index = 2;
							$.each(data, function(key, value) {
									adicionarResultado(value.id, "patologias-dente", '#patologias-dente-selecionado-1', '#patologias-dente-selecionado-2', index, value.tipo.nome, "patologias-dente");
									index++;
							});
						}else{
							$("#patologias-dente-busca").hide();
						}
					}
				});
				
				break;
				
			case "PE":
				
				if($("#modal-procedimentos-existentes").length){
					$("#modal-procedimentos-existentes").openModal();
					$("#id-face-dente-proced-exist").val("");
					$("#id-dente-proced-exist").val("");
				}
				
				break;
				
			default:
				break;
		}
	});
	
	$("#confirm-patologia").click(function() {
		var idLocal;
		var local;
		
		if($("#id-face-dente-patologia").val() != "") {
			local = "FACE";
			idLocal = $("#id-face-dente-patologia").val();
		} else if($("#id-dente-patologia").val() != "") {
			local = "DENTE";
			idLocal = $("#id-dente-patologia").val();
		} else {
			local = "GERAL";
			idLocal = null;
		}
		
		if($("input[name=patologia]").length > 0 && $("input[name=patologia]").is(":checked")) {
			adicionarPatologia(idLocal, local);
		}
	});
	
	$("#confirm-procedimento").click(function() {
		var idLocal;
		var local;
		
		if($("#id-face-dente-procedimento").val() != "") {
			local = "FACE";
			idLocal = $("#id-face-dente-procedimento").val();
		} else if($("#id-dente-procedimento").val() != "") {
			local = "DENTE";
			idLocal = $("#id-dente-procedimento").val();
		} else {
			local = "GERAL";
			idLocal = null;
		}
		
		if($("input[name=procedimento]").length > 0 && $("input[name=procedimento]").is(":checked")) {
			adicionarProcedimento(idLocal, local);
		}
	});
	
	$("#confirm-proced-exist").click(function() {
		var idLocal;
		var local;
		
		if($("#id-face-dente-proced-exist").val() != "") {
			local = "FACE";
			idLocal = $("#id-face-dente-proced-exist").val();
		} else if($("#id-dente-proced-exist").val() != "") {
			local = "DENTE";
			idLocal = $("#id-dente-proced-exist").val();
		} else {
			local = "GERAL";
			idLocal = null;
		}
		
		if($("input[name=proced-exist]").length > 0 && $("input[name=proced-exist]").is(":checked")) {
			adicionarProcedimentoExistente(idLocal, local);
		}
	});
	
	$("#confirm-patologia").click(function() {
		$("input[name=patologia]").attr("checked", false);
		$("#modal-patologias").closeModal();
		$("#descricao-patologia").val("");
	});
	
	$("#confirm-procedimento").click(function() {
		$("input[name=procedimento]").attr("checked", false);
		$("#modal-procedimentos").closeModal();
		$("#descricao-procedimento").val("");
	});
	
	$("#confirm-proced-exist").click(function() {
		$("input[name=proced-exist]").attr("checked", false);
		$("#modal-procedimentos-existentes").closeModal();
		$("#descricao-proced-exist").val("");
	});
	
	$("#cancelar-patologia").click(function() {
		$("#modal-patologias").closeModal();
	});
	
	$("#cancelar-procedimento").click(function() {
		$("#modal-procedimentos").closeModal();
	});
	
	$("#cancelar-proced-exist").click(function() {
		$("#modal-procedimentos-existentes").closeModal();
	});
	
	$("#confirm-tratamento-patologia").click(function() {
		var idPatologia = $("#id-patologia").val();
		
		$.post("../tratar/" + idPatologia, $("#form-tratamento-patologia").serialize(), function() {
			$("#modal-tratamento-patologia").closeModal();
		})
		.done(function(patologias) {
			marcarPatologias();
		    carregarTablePatologias();
		    
		    $("#data-tratamento").val(null);
		    $("#descricao-tratamento").val(null);
		});
	});
	
	$("#cancelar-tratamento-patologia").click(function() {
		$("#modal-tratamento-patologia").closeModal();
		
		$("#data-tratamento").val(null);
	    $("#descricao-tratamento").val(null);
	});
	
	
	
	function adicionarPatologia(idLocal, local){
		var patologias = $("input[name=patologia]:checked").map(function() {
			return this.value;
		}).get().join(",");
		
		var descricao = $("#descricao-patologia").val();
	
		$.ajax({
			url : "../adicionarPatologia",
			beforeSend: function (request) {
 				request.setRequestHeader(header, token);
 		    },
		    async: false,
		    type : 'POST',
		    data : {	
			   	patologias : patologias,
			   	faceDente : idLocal,
			   	local : local,
			   	idOdontograma : odontogramaId,
		 	   	descricao : descricao,
		    },
		    success : function(result) {
		    	marcarPatologias();
			    carregarTablePatologias();
			    $("#resultado-busca-patologia-1").empty();
			    $("#resultado-busca-patologia-2").empty();
				$("#selecionados-busca-patologia").empty();
				$("#label-resultado-busca-patologia").hide();
				$("#label-patologias-selecionadas").hide();
				document.getElementById("busca-patologia-input").value = "";
		    }
		});
	}
	
	function adicionarProcedimento(idLocal, local) {
		
		var procedimentos = $("input[name=procedimento]:checked").map(function() {
			return this.value;
		}).get().join(",");
		
		var patologias = $("input[name=patologias-dente]:checked").map(function() {
			return this.value;
		}).get().join(",");
		
		var data = $("#data-patologia-tratamento").val();
		
		var descricao = $("#descricao-procedimento").val();
		
		$.ajax({
			url : "../adicionarProcedimento",
			beforeSend: function (request){
				request.setRequestHeader(header, token);
			},
			async: true,
			type : 'POST',
			data : {	
				data: data,
				patologias: patologias,
				procedimentos : procedimentos,
			   	faceDente : idLocal,
			   	local : local,
			   	idOdontograma : odontogramaId,
			   	descricao : descricao,
			   	preExistente : false
			},
			success : function(result) {
			   classeProcedimento(result.procedimentos);
			   carregarTableProcedimentos();
			   carregarTablePatologias();
			   marcarPatologias();
			   $("#resultado-busca-procedimento-1").empty();
			   $("#resultado-busca-procedimento-2").empty();
			   $("#label-resultado-busca-novo-procedimento").hide();
			   $("#label-novos-procedimentos-selecionados").hide();
			   $("#selecionados-busca-procedimento").empty();
			   document.getElementById("busca-novo-procedimento-input").value = "";
		   }
		});
	}
	
	function adicionarProcedimentoExistente(idLocal, local) {
		
		var procedimentos = $("input[name=proced-exist]:checked").map(function() {
			return this.value;
		}).get().join(",");
		
		var descricao = $("#descricao-proced-exist").val();
		
		$.ajax({
			url : "../adicionarProcedimento",
			beforeSend: function (request){
				request.setRequestHeader(header, token);
			},
			async: true,
			type : 'POST',
			data : {	
				procedimentos : procedimentos,
			   	faceDente : idLocal,
			   	local : local,
			   	idOdontograma : odontogramaId,
			   	descricao : descricao,
			   	preExistente : true
			},
			success : function(result) {
			   classeProcedimentoExistente(result.procedimentos)
			   carregarTableProcedimentos();
			   $("#resultado-busca-procedimento-existente-1").empty();
			   $("#resultado-busca-procedimento-existente-2").empty();
			   $("#label-resultado-busca-procedimento-existente").hide();
			   $("#label-procedimentos-selecionados-existentes").hide();
			   $("#selecionados-busca-procedimento-existente").empty();
			   document.getElementById("busca-procedimento-existente-input").value = "";
		   }
		});
	}
	
	function marcarPatologias() {
		$(".patologia").attr("data-tooltip", "");
		$(".face").removeClass("patologia");
		$(".face").removeClass("patologia-ativa");
		var urlAux = "../buscarPatologias/" + odontogramaId;
		$.ajax({
			url: urlAux,
			type : 'GET',
			success : function(data) {
				classePatologia(data.patologiasOdontograma);
			}
		});
	}
	
	function marcarProcedimentos() {
		var urlAux = "../buscarProcedimentos/" + odontogramaId;
		$.ajax({
			url: urlAux,
			type : 'GET',
			success : function(data) {
				classeProcedimento(data.procedimentosOdontograma);
			}
		});
	}
	
	function marcarProcedimentosExistentes() {
		var urlAux = "../buscarProcedimentosExistentes/" + odontogramaId;
		$.ajax({
			url: urlAux,
			type : 'GET',
			success : function(data) {
				classeProcedimentoExistente(data.procedimentosExistentesOdontograma);
			}
		});
	}
	
	var faces = ["R", "L", "D", "O", "M", "V"];
	
	function classePatologia(patologias) {
		var id;
		
		for(i = 0; i < patologias.length; i++) {
			if(patologias[i].local == "FACE") {
				id = "PA_" + patologias[i].dente.substring(1) + "_" + patologias[i].face;
				$("#" + id).addClass("patologia");
				if(patologias[i].tratamento == null){
					$("#" + id).addClass("patologia-ativa");
				}
				addTextTooltip(id, patologias[i].tipo.nome);
				
			} else if(patologias[i].local == "DENTE") {
				id = "PA_" + patologias[i].dente.substring(1);
				
				for(j = 0; j < faces.length; j++){
					var idAux = id + "_" + faces[j];
					$("#" + idAux).addClass("patologia");
					if(patologias[i].tratamento == null){
						$("#" + idAux).addClass("patologia-ativa");
					} 
					addTextTooltip(idAux, patologias[i].tipo.nome);
				}
			}
		}
	}
	
	function classeProcedimento(procedimentos) {
		var id;
		
		for(i = 0; i < procedimentos.length; i++) {
			if(procedimentos[i].local == "FACE") {
				id = "PR_" + procedimentos[i].dente.substring(1) + "_" + procedimentos[i].face;
				$("#" + id).addClass("procedimento");
				addTextTooltip(id, procedimentos[i].tipoProcedimento.nome);
				
			} else if(procedimentos[i].local == "DENTE") {
				id = "PR_" + procedimentos[i].dente.substring(1);
				
				for(j = 0; j < faces.length; j++){
					var idAux = id + "_" + faces[j];
					$("#" + idAux).addClass("procedimento");
					addTextTooltip(idAux, procedimentos[i].tipoProcedimento.nome);
				}
			}
		}
	}
	
	function classeProcedimentoExistente(procedimentos) {
		var id;
		
		for(i = 0; i < procedimentos.length; i++) {
			if(procedimentos[i].local == "FACE") {
				id = "PE_" + procedimentos[i].dente.substring(1) + "_" + procedimentos[i].face;
				$("#" + id).addClass("proced-exist");
				addTextTooltip(id, procedimentos[i].tipoProcedimento.nome);
				
			} else if(procedimentos[i].local == "DENTE") {
				id = "PE_" + procedimentos[i].dente.substring(1);
				
				for(j = 0; j < faces.length; j++){
					var idAux = id + "_" + faces[j];
					$("#" + idAux).addClass("proced-exist");
					addTextTooltip(idAux, procedimentos[i].tipoProcedimento.nome);
				}
			}
		}
	}
	
	
	function carregarTablePatologias() {
		var url = "../tablePatologias/" + odontogramaId;
		
		$("#result-patologias").load(url, function() {
            /*mf_base.doAddDataTable($("#table-patologias"), {
                order: [[ 0, 'asc' ]],
				paging : false,
				searching: false,
				info: false
            });*/
		});
	}
	
	function carregarTableProcedimentos() {
		var url = "../tableProcedimentos/" + odontogramaId;
		
		$("#result-procedimentos").load(url, function() {
            /*mf_base.doAddDataTable($("#table-procedimentos"), {
                order: [[ 0, 'asc' ]],
                paging : false,
                searching: false,
                info: false
            });*/
		});
	}
	
	function limparPatologias(patologias) {
		var id;
		
		for(i = 0; i < patologias.length; i++) {
			if(patologias[i].local == "FACE") {
				id = "PA_" + patologias[i].dente.substring(1) + "_" + patologias[i].face;
				$("#" + id).removeClass("patologia");
				$("#" + id).removeClass("patologia-ativa");
				
			} else if(patologias[i].local == "DENTE") {
				id = patologias[i].dente.substring(1);
				
				for(j = 0; j < faces.length; j++){
					var idAux = "PA_" + id + "_" + faces[j];
					$("#" + idAux).removeClass("patologia");
					$("#" + idAux).removeClass("patologia-ativa");
				}
			}
		}
	}
	
	function limparProcedimentos(procedimentos) {
		var id;
		
		for(i = 0; i < procedimentos.length; i++) {
			if(procedimentos[i].local == "FACE") {
				id = "PE_" + procedimentos[i].dente.substring(1) + "_" + procedimentos[i].face;
				$("#" + id).removeClass("proced-exist");
				
			} else if(procedimentos[i].local == "DENTE") {
				id = procedimentos[i].dente.substring(1);
				
				for(j = 0; j < faces.length; j++){
					var idAux = "PE_" + id + "_" + faces[j];
					$("#" + idAux).removeClass("proced-exist");
				}
			}
		}
	}
	
	function addTextTooltip(id, descricao) {
		if(!$("#" + id).attr("data-tooltip")){
			$("#" + id).attr("data-tooltip", descricao);
			$("#" + id).tooltip();
		} else {
			var tooltip = $("#" + id).attr("data-tooltip") + ", " + descricao;
			$("#" + id).attr("data-tooltip", tooltip);
			$("#" + id).tooltip();
		}
	}
	
	$("#busca-novo-procedimento-input").keyup(function() {
		$('#resultado-busca-procedimento-1').empty();
		$('#resultado-busca-procedimento-2').empty();
		var valor = $('#busca-novo-procedimento-input').val();
		if (this.value.length > 2) {
			$("#label-resultado-busca-novo-procedimento").show();
			$.ajax({
				url : _context + "/odontograma/buscar-procedimento",
				beforeSend : function(request) {
					request.setRequestHeader(header, token);
				},
				async : false,
				type : 'GET',
				data : {
					query : valor
				},
				success : function(data) {
					var index = 2;
					$.each(data, function(key, value) {
						if(!existeSelecionado(value.id, "selecionados-busca-procedimento")){
							adicionarResultado(value.id, "procedimento", '#resultado-busca-procedimento-1', '#resultado-busca-procedimento-2', index, value.nome, "resultado-busca-procedimento");
							index++;
						}
					});
				}
			});
		}else{
			$("#label-resultado-busca-novo-procedimento").hide();
		}
	});
	
	$("#busca-procedimento-existente-input").keyup(function() {
		$('#resultado-busca-procedimento-existente-1').empty();
		$('#resultado-busca-procedimento-existente-2').empty();
		var valor = $('#busca-procedimento-existente-input').val();
		if (this.value.length > 2) {
			$("#label-resultado-busca-procedimento-existente").show();
			$.ajax({
				url : _context + "/odontograma/buscar-procedimento",
				beforeSend : function(request) {
					request.setRequestHeader(header, token);
				},
				async : false,
				type : 'GET',
				data : {
					query : valor
				},
				success : function(data) {
					var index = 2;
					$.each(data, function(key, value) {
						if(!existeSelecionado(value.id, "selecionados-busca-procedimento-existente")){
							
							adicionarResultado(value.id, "proced-exist", '#resultado-busca-procedimento-existente-1', '#resultado-busca-procedimento-existente-2', index, value.nome, "resultado-busca-procedimento-existente");
							index++;
							
							
						}
					});
				}
			});
		}else{
			$("#label-resultado-busca-procedimento-existente").hide();
		}
	});
	
	$("#busca-patologia-input").keyup(function() {
				$('#resultado-busca-patologia-1').empty();
				$('#resultado-busca-patologia-2').empty();
				var valor = $('#busca-patologia-input').val();
				if (this.value.length > 2) {
					$("#label-resultado-busca-patologia").show();
					$.ajax({
						url : _context + "/odontograma/buscar-patologia",
						beforeSend : function(request) {
							request.setRequestHeader(header, token);
						},
						async : false,
						type : 'GET',
						data : {
							query : valor
						},
						success : function(data) {
							var index = 2;
							$.each(data, function(key, value) {
								if(!existeSelecionado(value.id, "selecionados-busca-patologia")){
									
									adicionarResultado(value.id, "patologia", '#resultado-busca-patologia-1', '#resultado-busca-patologia-2', index, value.nome, "resultado-busca-patologia");
									index++;
									
									
								}
							});
						}
					});
				}else{
					$("#label-resultado-busca-patologia").hide();
				}
			});
	
	$('body').on('click','.resultado-busca-patologia', function() {
						if (this.checked) {
							$("#label-patologias-selecionadas").show();
							var $lb = $("label[for='" + this.id + "']");
							if ($lb != null) {
								this.remove();
								$lb.remove();
								adicionarSelecionado(this.id, "patologia", "selecionados-busca-patologia", $(this).attr('nome'), '#selecionados-busca-patologia', $(this).attr('col'));
								
							}
						}
				});
	
	$('body').on('click','.selecionados-busca-patologia', function() {
					if (!this.checked) {
						var $lb = $("label[for='" + this.id + "']");
						this.remove();
						$lb.remove();
						removerSelecionado(this.id, "patologia", "resultado-busca-patologia", $(this).attr('nome'), $(this).attr('col'), '#resultado-busca-patologia-1', '#resultado-busca-patologia-2');
						
					}	
	});
	
	$('body').on('click','.resultado-busca-procedimento', function() {
		if (this.checked) {
			$("#label-novos-procedimentos-selecionados").show();
			var $lb = $("label[for='" + this.id + "']");
			if ($lb != null) {
				this.remove();
				$lb.remove();
				adicionarSelecionado(this.id, "procedimento", "selecionados-busca-procedimento", $(this).attr('nome'), '#selecionados-busca-procedimento', $(this).attr('col'));
				
					
			}
		}
	});
	
	$('body').on('click','.selecionados-busca-procedimento', function() {
		if (!this.checked) {
			var $lb = $("label[for='" + this.id + "']");
			this.remove();
			$lb.remove();
			
			removerSelecionado(this.id, "procedimento", "resultado-busca-procedimento", $(this).attr('nome'), $(this).attr('col'), '#resultado-busca-procedimento-1', '#resultado-busca-procedimento-2');
			
		}	
	});
	
	
	
	$('body').on('click','.resultado-busca-procedimento-existente', function() {
		if (this.checked) {
			$("#label-procedimentos-selecionados-existentes").show();
			var $lb = $("label[for='" + this.id + "']");
			if ($lb != null) {
				this.remove();
				$lb.remove();
				adicionarSelecionado(this.id, "proced-exist", "selecionados-busca-procedimento-existente", $(this).attr('nome'), '#selecionados-busca-procedimento-existente', $(this).attr('col'));
			}
		}
	});
	
	$('body').on('click','.selecionados-busca-procedimento-existente', function() {
		if (!this.checked) {
			var $lb = $("label[for='" + this.id + "']");
			this.remove();
			$lb.remove();
			removerSelecionado(this.id, "proced-exist", "resultado-busca-procedimento-existente", $(this).attr('nome'), $(this).attr('col'), '#resultado-busca-procedimento-existente-1', '#resultado-busca-procedimento-existente-2');
		}	
	});
	
	$('body').on('click','.patologias-dente', function() {
		if (this.checked) {
			var $lb = $("label[for='" + this.id + "']");
			this.remove();
			$lb.remove();
			adicionarSelecionado(this.id, "patologias-dente", "patologias-dente-selecionadas", $(this).attr('nome'), '#patologias-dente-selecionado', $(this).attr('col'));
		}	
	});
	
	$('body').on('click','.patologias-dente-selecionadas', function() {
		if (!this.checked) {
			var $lb = $("label[for='" + this.id + "']");
			this.remove();
			$lb.remove();
			removerSelecionado(this.id, "patologias-dente", "patologias-dente", $(this).attr('nome'), $(this).attr('col'), '#patologias-dente-selecionado-1', '#patologias-dente-selecionado-2');
		}	
	});
	
	
	function adicionarResultado(id, name, coluna1, coluna2, index, nome, classe){
		var checkbox = document.createElement('input');
		checkbox.className = classe;
		checkbox.type = "checkbox";
		checkbox.name = name;
		checkbox.value = id;
		checkbox.id = id;
		checkbox.setAttribute("nome", nome);
		var label = document.createElement('label')
		label.htmlFor = id;
		label.appendChild(document.createTextNode(nome));
		if(index % 2 == 0){
			checkbox.setAttribute("col", 1);
			$(coluna1).append(checkbox);
			$(coluna1).append(label);
			$(coluna1).append(' ');
		}else{
			checkbox.setAttribute("col", 2);
			$(coluna2).append(checkbox);
			$(coluna2).append(label);
			$(coluna2).append(' ');
		}
		
	}
	
	function adicionarSelecionado(id, name, classe, nome, div, col){
		var checkbox = document.createElement('input');
		checkbox.type = "checkbox";
		checkbox.name = name;
		checkbox.className = classe;
		checkbox.value = id;
		checkbox.id = id;
		checkbox.checked = true;
		checkbox.setAttribute("nome", nome);
		checkbox.setAttribute("col", col);
		var label = document.createElement('label')
		label.htmlFor = id;
		label.appendChild(document.createTextNode(nome));
		$(div).append(checkbox);
		$(div).append(label);
		$(div).append(' ');
	}
	
	function removerSelecionado(id, name, classe, nome, col, div1, div2){
		var checkbox = document.createElement('input');
		checkbox.className = classe;
		checkbox.type = "checkbox";
		checkbox.name = name;
		checkbox.value = id;
		checkbox.id = id;
		checkbox.setAttribute("nome", nome);
		checkbox.setAttribute("col", col);
		var label = document.createElement('label')
		label.htmlFor = id;
		label.appendChild(document.createTextNode(nome));
		
		if(col == 1){
			$(div1).append(checkbox);
			$(div1).append(label);
			$(div1).append(' ');
		}else{
			$(div2).append(checkbox);
			$(div2).append(label);
			$(div2).append(' ');
		}
	}
	
	
	
	function existeSelecionado(idSelecionado, elemento){
		var pai = document.getElementById(elemento).childNodes;
		var i = 0;
		var existe = false;
		while (i != pai.length) {
		    if(pai[i].id == idSelecionado){
		    	existe = true;
		    }
			i++;
		}
		return existe;
	}
})