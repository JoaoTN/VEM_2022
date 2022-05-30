package br.udesc.ceavi.pin.infosaude.util;

import javax.swing.JTextField;

import br.udesc.ceavi.pin.infosaude.modelo.Endereco;
import br.udesc.ceavi.pin.infosaude.modelo.Pessoa;

public class SetaDados {
	 public String[] getPessoa(Pessoa pessoa) {
	    return pessoa.retornaUser();
	 }
	    
	 public String[] getEndereco(Endereco endereco) {
	    return endereco.retornaEnde();
	 }
	    
	public void setDados(Pessoa pessoa, Endereco endereco, JTextField[] campos) {
		String[] p = getPessoa(pessoa);
        String[] e = getEndereco(endereco);
        
        for(int i = 0; i >= 12; i++) {
        	if(i == 4) {
        		campos[4].setText(e[2]);
        	}else if(i >= 5) {
        		campos[i].setText(p[i+1]);
        	}else if(i == 6){
        		campos[6].setText(e[1]);
        	}else {
        		campos[i].setText(e[i - 4]);
        	}
        }
	}
}
