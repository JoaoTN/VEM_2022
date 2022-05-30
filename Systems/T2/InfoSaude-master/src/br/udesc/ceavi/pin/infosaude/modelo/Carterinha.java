package br.udesc.ceavi.pin.infosaude.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta Classe define os atributos de Endereço
 *
 * @author Erick William Henschel
 * @author Fabio Frare
 * @author Gustavo de Carvalho Santos
 * @author Lucas Eduardo Nogueira
 * @author Ricardo José Pinto
 * @version 1.0
 * @since 15/11/2018
 */
public class Carterinha {

    private final Usuario usuario;
    private List<Vacina> listaDeVacinaTomadas = new ArrayList<>();
    private List<Campanha> listaDeCampanhaParticipadas = new ArrayList<>();

    public Carterinha(Usuario usuario) {
        this.usuario = usuario;
    }

    public Pessoa getUsuario() {
        return usuario;
    }
    
    public boolean vacinasTomadasVazias() {
    	return this.listaDeVacinaTomadas.isEmpty();
    }
    
    public int quantidadeVacinasTomadas() {
    	return listaDeVacinaTomadas.size();
    }
    
    public Vacina buscaVacina(int i) {
    	return listaDeVacinaTomadas.get(i);
    }

    public Vacina getVacinaPorNome(String nome) {
        if (vacinasTomadasVazias()) {
            for (int i = 0; i < quantidadeVacinasTomadas(); i++) {
            	Vacina v = buscaVacina(i);
                if (v.getVacina().equals(nome)) {
                    return v;
                }
            }
        }
        return null;
    }
    
    public boolean campanhasParticipadasVazias() {
    	return this.listaDeCampanhaParticipadas.isEmpty();
    }
    
    public int quantidadeCampanhasParticipadas() {
    	return listaDeCampanhaParticipadas.size();
    }
    
    public Campanha buscaCampanha(int i) {
    	return listaDeCampanhaParticipadas.get(i);
    }

    public Campanha getCampanhaPorNome(String nome) {
        if (campanhasParticipadasVazias()) {
            for (int i = 0; i < quantidadeCampanhasParticipadas(); i++) {
            	Campanha c = buscaCampanha(i);
                if (c.getVacina().equals(nome)) {
                    return c;
                }
            }
        }
        return null;
    }

    public boolean setListaDeCampanhaParticipadas(List<Campanha> listaDeCampanhaParticipadas) {
        this.listaDeCampanhaParticipadas = listaDeCampanhaParticipadas;
        return true;
    }

    public boolean setListaDeVacinaTomadas(List<Vacina> listaDeVacinaTomadas) {
        this.listaDeVacinaTomadas = listaDeVacinaTomadas;
        return true;
    }

}
