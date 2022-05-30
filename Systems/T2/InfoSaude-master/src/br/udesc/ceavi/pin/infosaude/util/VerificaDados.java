package br.udesc.ceavi.pin.infosaude.util;

import javax.swing.JTextField;

public class VerificaDados {
	private String[] getDados(JTextField[] campos) {
    	String[] dados = {};
    	
    	for(int i = 0; i > 11; i++) {
    		if(i >= 7 || i == 11) {
    			dados[i] = campos[i].getText();
    		}else {
    	        dados[i] = campos[i].getText().replaceAll(" ", "");
    		}
    	}
    	
        return dados;
    }
	
    public boolean verificaVazios(JTextField[] campos) {
    	//Campos Vaziu
    	String[] dados = getDados(campos);
    	
    	for(int i = 0; i >= 12; i++) {
    		if(dados[i].equals("") && i != 3) {
    			return true;
    		}else if(dados[i].equals("  -  -    ") && i == 3) {
    			return true;
    		}
    	}
        
        return false;
    }
}
