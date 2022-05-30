$(document).ready(function(){
	$.attrHooks['viewbox'] = {
		set : function(elem, value, name) {
			elem.setAttributeNS(null, 'viewBox', value + '');
			return value;
		}
	};
	
	//Setando atributos de configuração do svg
	var permanentePatologias = $("#arcada-permanente-patologias");
	var deciduaPatologias =  $("#arcada-decidua-patologias");
	gerarArcadaPermanente(permanentePatologias, "PA");
	gerarArcadaDecidua(deciduaPatologias, "PA");
	
	var permanenteProcedimentos = $("#arcada-permanente-procedimentos");
	var deciduaProcedimentos =  $("#arcada-decidua-procedimentos");
	gerarArcadaPermanente(permanenteProcedimentos, "PR");
	gerarArcadaDecidua(deciduaProcedimentos, "PR");
	
	var permanenteProcedimentosExistentes = $("#arcada-permanente-procedimentos-existentes");
	var deciduaProcedimentosExistentes =  $("#arcada-decidua-procedimentos-existentes");
	gerarArcadaPermanente(permanenteProcedimentosExistentes, "PE");
	gerarArcadaDecidua(deciduaProcedimentosExistentes, "PE");
});

var faceSuperior = "<path d='m342 740c443,-116 951,-92 1377,75 82,32 149,68 226,99l30 -49c64,-121 418,-784 429,-825l-2982 0c10,45 292,568 337,653 21,40 36,72 57,112l51 93c7,10 8,11 12,16 202,-81 214,-109 463,-174z'/>";
var faceLingual = "<path d='m1405 850c-316,-82 -661,-82 -978,0 -108,28 -323,101 -397,153l395 747c176,-68 252,-124 491,-124 237,0 317,55 494,126l393 -749c-79,-54 -286,-124 -398,-153z'/>";
var faceDistal = "<path  d='m300 1850l-410 -778c-67,29 -252,214 -297,271 -52,64 -74,81 -127,161 -457,677 -454,1481 3,2157 77,113 318,382 424,428l404 -781c-79,-118 -116,-120 -196,-312 -114,-272 -110,-568 2,-838 74,-178 112,-189 197,-308z'/>";
var faceOclusal = "<path  d='m820 1850c-400,53 -661,434 -615,826 46,394 398,694 798,641 399,-54 662,-432 617,-824 -46,-395 -398,-697 -800,-643z'/>";
var faceMesial = "<path  d='m1700 3050c-25,56 -66,128 -103,176 -63,83 -98,74 -49,164 83,149 317,626 378,716 158,-72 439,-435 532,-603 308,-561 308,-1249 0,-1810 -93,-168 -375,-531 -532,-603 -67,99 -292,564 -381,722 -43,77 -37,37 59,167 228,307 245,729 96,1071z'/>";
var faceVestibular = "<path d='m1800 4200l-397 -749c-394,170 -588,172 -986,-2l-392 751c578,285 1195,290 1775,0z'/>";
var faceInferior = "<path d='m1945 4300c-205,82 -218,112 -470,176 -368,95 -765,94 -1133,-2 -248,-64 -261,-92 -463,-174l-120 221c-44,81 -328,611 -337,653l2982 0 -459 -874z'/>";

var facesSuperiores = new Map();
facesSuperiores.set(0, faceSuperior);
facesSuperiores.set(1, faceLingual);
facesSuperiores.set(2, faceDistal);
facesSuperiores.set(3, faceOclusal);
facesSuperiores.set(4, faceMesial);
facesSuperiores.set(5, faceVestibular);

var facesInferiores = new Map();
facesInferiores.set(0, faceInferior);
facesInferiores.set(1, faceLingual);
facesInferiores.set(2, faceDistal);
facesInferiores.set(3, faceOclusal);
facesInferiores.set(4, faceMesial);
facesInferiores.set(5, faceVestibular);

var espaco = 4500;

//Recebe o elemento no qual será injetado o svg e o tipo do odontograma
function gerarArcadaPermanente(elem, tipo) {
	elem.attr("viewBox", "-1500 0 74500 14500");
	elem.attr("width", "230mm");
	elem.attr("height", "50mm");
	elem.attr("version", "1.1");
	
	elem.svg();
	var svg = elem.svg("get");

	//GERANDO A PARTE SUPERIOR DA ARCADA
	var posAux = 0;

	//Dentes de 18 a 11
	var facesId = ["R", "V", "D", "O", "M", "L"];
	
	var g1 = svg.group();
	for (var dente = 18; dente >= 11; dente--) {
		var g = svg.group(g1, {id:tipo + "_" + dente, transform:"translate(" + posAux + ")"});

		facesSuperiores.forEach(function(valor, chave){
			var aux = svg.group(g, {id:tipo + "_" + dente + "_" + facesId[chave], class:"face", face: dente + "_" + facesId[chave], tipo: tipo, dente:dente});
			svg.add(aux, valor);
		}, facesSuperiores);
		
		svg.text(g, 250, 7000, dente.toString(), {fontFamily: 'inherit', fontSize: '1250', class:"txt-dente", style:"cursor: pointer;", tipo: tipo, dente:dente});
		
		posAux+=espaco;
	}
	
	posAux+=2000;

	//Dentes de 21 a 28
	var facesId = ["R", "V", "M", "O", "D", "L"];
	
	for (var dente = 21; dente <= 28; dente++) {
		var g = svg.group(g1, {id:tipo + "_" + dente, transform:"translate(" + posAux + ")"});

		facesSuperiores.forEach(function(valor, chave){
			var aux = svg.group(g, {id:tipo + "_" + dente + "_" + facesId[chave], class:"face", face: dente + "_" + facesId[chave], tipo: tipo, dente:dente});
			svg.add(aux, valor);
		}, facesSuperiores);
		
		svg.text(g, 250, 7000, dente.toString(), {fontFamily: 'inherit', fontSize: '1250', class:"txt-dente", style:"cursor: pointer;", tipo: tipo, dente:dente});
		
		posAux+=espaco;
	}


	//GERANDO A PARTE INFERIOR DA ARCADA
	posAux = 0;

	//Dentes de 48 a 41
	var facesId = ["R", "L", "D", "O", "M", "V"];
	
	var g2 = svg.group({transform:"translate(0, 7500)"});
	for (var dente = 48; dente >= 41; dente--) {
		var g = svg.group(g2, {id:tipo + "_" + dente, transform:"translate(" + posAux + ", 2000)"});

		facesInferiores.forEach(function(valor, chave){
			var aux = svg.group(g, {id:tipo + "_" + dente + "_" + facesId[chave], class:"face", face: dente + "_" + facesId[chave], tipo: tipo, dente:dente});
			svg.add(aux, valor);
		}, facesInferiores);
		
		svg.text(g, 250, -600, dente.toString(), {fontFamily: 'inherit', fontSize: '1250', class:"txt-dente", style:"cursor: pointer;", tipo: tipo, dente:dente});
		
		posAux+=espaco;
	}

	posAux+=2000;
	
	//Dentes de 31 a 38
	var facesId = ["R", "L", "M", "O", "D", "V"];
	
	for (var dente = 31; dente <= 38; dente++) {
		var g = svg.group(g2, {id:tipo + "_" + dente, transform:"translate(" + posAux + " , 2000)"});

		facesInferiores.forEach(function(valor, chave){
			var aux = svg.group(g, {id:tipo + "_" + dente + "_" + facesId[chave], class:"face", face: dente + "_" + facesId[chave], tipo: tipo, dente:dente});
			svg.add(aux, valor);
		}, facesInferiores);
		
		svg.text(g, 250, -600, dente.toString(), {fontFamily: 'inherit', fontSize: '1250', class:"txt-dente", style:"cursor: pointer;", tipo: tipo, dente:dente});
		
		posAux+=espaco;
	}
}

//Recebe o elemento no qual será injetado o svg e o tipo do odontograma
function gerarArcadaDecidua(elem, tipo) {
	elem.attr("viewBox", "1200 -900 42200 16000");
	elem.attr("width", "150mm");
	elem.attr("height", "50mm");
	elem.attr("version", "1.1");
	
	elem.svg();
	var svg = elem.svg("get");


	//GERANDO A PARTE SUPERIOR DA ARCADA
	var posAux = 0;

	//Dentes de 55 a 51
	var facesId = ["R", "V", "D", "O", "M", "L"];
	
	var g1 = svg.group();
	for (var dente = 55; dente >= 51; dente--) {
		var g = svg.group(g1, {id:tipo + "_" + dente, transform:"translate(" + posAux + ")"});

		facesSuperiores.forEach(function(valor, chave){
			var aux = svg.group(g, {id:tipo + "_" + dente + "_" + facesId[chave], class:"face", face: dente + "_" + facesId[chave], tipo: tipo, dente:dente});
			svg.add(aux, valor);
		}, facesSuperiores);
		
		svg.text(g, 250, 7000, dente.toString(), {fontFamily: 'inherit', fontSize: '1250', class:"txt-dente", style:"cursor: pointer;", tipo: tipo, dente:dente});
		
		posAux+=espaco;
	}
	
	posAux+=2000;

	//Dentes de 61 a 65
	var facesId = ["R", "V", "M", "O", "D", "L"];
	
	for (var dente = 61; dente <= 65; dente++) {
		var g = svg.group(g1, {id:tipo + "_" + dente, transform:"translate(" + posAux + ")"});

		facesSuperiores.forEach(function(valor, chave){
			var aux = svg.group(g, {id:tipo + "_" + dente + "_" + facesId[chave], class:"face", face: dente + "_" + facesId[chave], tipo: tipo, dente:dente});
			svg.add(aux, valor);
		}, facesSuperiores);
		
		svg.text(g, 250, 7000, dente.toString(), {fontFamily: 'inherit', fontSize: '1250', class:"txt-dente", style:"cursor: pointer;", tipo: tipo, dente:dente});
		
		posAux+=espaco;
	}


	//GERANDO A PARTE INFERIOR DA ARCADA
	posAux = 0;

	//Dentes de 85 a 81
	var facesId = ["R", "L", "D", "O", "M", "V"];
	
	var g2 = svg.group({transform:"translate(0, 7500)"});
	for (var dente = 85; dente >= 81; dente--) {
		var g = svg.group(g2, {id:tipo + "_" + dente, transform:"translate(" + posAux + ", 2000)"});

		facesInferiores.forEach(function(valor, chave){
			var aux = svg.group(g, {id:tipo + "_" + dente + "_" + facesId[chave], class:"face", face: dente + "_" + facesId[chave], tipo: tipo, dente:dente});
			svg.add(aux, valor);
		}, facesInferiores);
		
		svg.text(g, 250, -600, dente.toString(), {fontFamily: 'inherit', fontSize: '1250', class:"txt-dente", style:"cursor: pointer;", tipo: tipo, dente:dente});
		
		posAux+=espaco;
	}
	
	posAux+=2000;

	//Dentes de 71 a 75
	var facesId = ["R", "L", "M", "O", "D", "V"];
	
	for (var dente = 71; dente <= 75; dente++) {
		var g = svg.group(g2, {id:tipo + "_" + dente, transform:"translate(" + posAux + ", 2000)"});

		facesInferiores.forEach(function(valor, chave){
			var aux = svg.group(g, {id:tipo + "_" + dente + "_" + facesId[chave], class:"face", face: dente + "_" + facesId[chave], tipo: tipo, dente:dente});
			svg.add(aux, valor);
		}, facesInferiores);
		
		svg.text(g, 250, -600, dente.toString(), {fontFamily: 'inherit', fontSize: '1250', class:"txt-dente", style:"cursor: pointer;", tipo: tipo, dente:dente });
		
		posAux+=espaco;
	}
}