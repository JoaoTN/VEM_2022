package ufc.npi.prontuario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProntuarioController {

	@GetMapping("/")
	public String getIndex() {
		return "index";
	}
	
	@GetMapping("/sobre")
	public String getSobre() {
		return "sobre";
	}
}
