package br.udesc.ceavi.pin.infosaude.control;

import br.udesc.ceavi.pin.infosaude.control.dao.ConexaoPostgresJDBC;
import br.udesc.ceavi.pin.infosaude.control.dao.PreparaStatement;
import br.udesc.ceavi.pin.infosaude.control.dao.ResultadoConsultas;
import br.udesc.ceavi.pin.infosaude.modelo.Campanha;
import br.udesc.ceavi.pin.infosaude.modelo.Vacina;
import br.udesc.ceavi.pin.infosaude.principal.Main;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author lucas
 */
public class CampanhaControl {

    private Connection conexao;
    
    public Connection conexao() throws ClassNotFoundException, SQLException{
    	return this.conexao = ConexaoPostgresJDBC.getConnection();
    }
    
    public long buscaData(Date data) {    	
    	return data.getTime();
    }
    
    public boolean validarCampos(String slogan, Date dataInicio, Date dataFim) {
        boolean a = true;
        
        long inicio = buscaData(dataInicio);
        long fim = buscaData(dataFim);

        if (slogan.equals("")) {
            a = false;
            JOptionPane.showMessageDialog(null, "INSIRA UM SLOGAN PARA A CAMPANHA");
        }
        if (inicio > fim) {
            a = false;
            JOptionPane.showMessageDialog(null, "DADO INVALIDO! DATA INSERIDA INVALIDA \nDIA NÃO CONSISTE!");
        }
        return a;
    }
    
    
    
    public String returnSlogin(Campanha campanha) {
    	return campanha.getSlogan();
    }
    
    public long returnDate(Campanha campanha, int i) {
    	return campanha.getData(i);
    }
    
    public Long inserir(Campanha campanha, long id_instituicao, long id_vacina) throws SQLException, ClassNotFoundException {
        Long id = null;
        String sqlQuery = "insert into campanha(id_instituicao,id_vacina,data_inicio,data_fim,slogam) values(?,?,?,?,?)";

        PreparedStatement stmt = null;
        try {
        	long dataInicio = returnDate(campanha, 1);
        	long dataFim = returnDate(campanha, 2);
        	String slogan = returnSlogin(campanha);
        	
        	String[] dados = {};
        	dados[0] = "" + id_instituicao;
        	dados[1] = "" + id_vacina;
        	dados[1] = "" + dataInicio;
        	dados[1] = "" + dataFim;
        	dados[1] = "" + slogan;
            
            ResultSet rs = getStatement().executePrepared(stmt, dados, sqlQuery);
            if (rs.next()) {
                id = getResultados().getResultLong(rs, 1);
            }
            this.conexao.commit();
        } catch (SQLException error) {
            this.conexao.rollback();
            throw error;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                }
            }
            if (conexao != null) {
                this.conexao.close();
            }
        }
        return id;
    }
    
    public PreparaStatement getStatement() {
    	PreparaStatement ps = new PreparaStatement();
    	return ps;
    }
    
    public ResultadoConsultas getResultados() {
    	ResultadoConsultas rs = new ResultadoConsultas();
    	return rs;
    }

    public String[] getResultSet(ResultSet rs) throws SQLException{
    	String[] dados = {};
    	
    	dados[0] = "" + getResultados().getResultLong(rs, "id_campanha");
    	dados[1] = "" + getResultados().getResultString(rs, "slogan");
    	dados[2] = "" + getResultados().getResultLong(rs, "id_vacina");
    	dados[3] = "" + getResultados().getResultLong(rs, "data_fim");
    	dados[4] = "" + getResultados().getResultLong(rs, "data_inicio");
    	dados[5] = "" + getResultados().getResultLong(rs, "nome_vacina");
    	
    	return dados;
    }
    
    public Campanha criaCampanha(String[] dados, int i) {
    	if(i == 1) {
    		long id_campanha = Long.parseLong(dados[0]);
        	String slogan = dados[1];
        	long id_vacina = Long.parseLong(dados[2]);
        	Date fim = new Date(Long.parseLong(dados[3]));
        	Date inicio = new Date(Long.parseLong(dados[4]));
        	
        	Campanha c = new Campanha(id_campanha, slogan, new Vacina(id_vacina), fim, inicio);
        	return c;
    	}else {
    		long id_campanha = Long.parseLong(dados[0]);
        	String slogan = dados[1];
        	long id_vacina = Long.parseLong(dados[2]);
        	Date fim = new Date(Long.parseLong(dados[3]));
        	Date inicio = new Date(Long.parseLong(dados[4]));
        	String nome_vacina = dados[5];
        	
        	Vacina vacina = new Vacina(id_vacina, nome_vacina);
            
        	Campanha c = new Campanha(id_campanha, slogan, vacina, fim, inicio);
        	return c;
    		
    	}
    }
    //Obtem as campanhas participada pelo usuario
    public List<Campanha> getCampanhaUsuario() throws SQLException, ClassNotFoundException {
        List<Campanha> listaDeCampanha = new ArrayList();
        String sqlQuery1 = "select c.id_campanha,c.slogam,c.id_vacina,c.data_inicio,c.data_fin"
                + "from carterinha natural inner join campanha"
                + "where c.id_usuario = ?";
        PreparedStatement stmt = null;
        ResultSet resultSet = getStatement().executePrepared(stmt, sqlQuery1);
        
        while (resultSet.next()) {
            
            String[] dados = getResultSet(resultSet);
            Campanha campanha = criaCampanha(dados, 1);
            listaDeCampanha.add(campanha);
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
            }
        }
        if (conexao != null) {
            this.conexao.close();
        }
        return listaDeCampanha;
    }

//Obtem todas as campanha
    public List<Campanha> getCampanhas() throws SQLException, ClassNotFoundException {
        List<Campanha> listaDeCampanha = new ArrayList();
        String sqlQuery = "select * from campanha natural inner join vacina";
        PreparedStatement stmt = this.conexao().prepareStatement(sqlQuery);
        stmt.execute();
        ResultSet resultSet = stmt.getResultSet();

        while (resultSet.next()) {
        	String[] dados = getResultSet(resultSet);
            Campanha campanha = criaCampanha(dados, 1);
            listaDeCampanha.add(campanha);
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
            }
        }
        if (conexao != null) {
            this.conexao.close();
        }
        return listaDeCampanha;
    }

    public Campanha buscarCampanhaPorIdVacina(Long id_vacina) throws ClassNotFoundException, SQLException {
        String sqlQuery = "select c.id_campanha, c.slogan, c.data_fim, c.data_inicio, v.nome_vacina "
                + "from campanha as c natural inner join vacina as v where c.id_vacina = ? and c.data_fim > ?";
        PreparedStatement stmt = null;
        Campanha campanha = null;
        try {
            ResultSet rs = getStatement().executePrepared(stmt, id_vacina, sqlQuery);
            
            if (rs.next()) {
            	String[] dados = getResultSet(rs);
                campanha = criaCampanha(dados, 2);
            }
            this.conexao.commit();
        } catch (SQLException error) {
            this.conexao.rollback();
            error.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                }
            }
            if (conexao != null) {
                this.conexao.close();
            }
        }
        return campanha;
    }
}
