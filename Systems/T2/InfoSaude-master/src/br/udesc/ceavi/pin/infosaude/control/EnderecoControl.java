package br.udesc.ceavi.pin.infosaude.control;

import br.udesc.ceavi.pin.infosaude.control.dao.ConexaoPostgresJDBC;
import br.udesc.ceavi.pin.infosaude.control.dao.EnderecoStatement;
import br.udesc.ceavi.pin.infosaude.control.dao.ResultadoConsultas;
import br.udesc.ceavi.pin.infosaude.control.excecpton.DadosVaziosExcepitions;
import br.udesc.ceavi.pin.infosaude.modelo.Endereco;
import br.udesc.ceavi.pin.infosaude.modelo.Estado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lucas
 */
public class EnderecoControl extends EnderecoStatement{

	private Connection conexao;
    
    public Connection conexao() throws ClassNotFoundException, SQLException{
    	return this.conexao = ConexaoPostgresJDBC.getConnection();
    }
    
    public boolean validaCampos(String bairro, String cep, String cidade, int numero, String rua) throws DadosVaziosExcepitions {
        boolean a = true;

        if (bairro.equals("")) {
            a = false;
            throw new DadosVaziosExcepitions("BAIRRO INVALIDO!");
        }
        if (cep.equals("")) {
            a = false;
            throw new DadosVaziosExcepitions("CEP INVALIDO!");
        }
        if (cidade.equals("")) {
            a = false;
            throw new DadosVaziosExcepitions("CIDADE INVALIDO!");
        }
        if (numero == 0) {
            a = false;
            throw new DadosVaziosExcepitions("NUMERO DE RESIDENCIA INVALIDO!");
        }
        if (rua.equals("")) {
            a = false;
            throw new DadosVaziosExcepitions("RUA INVALIDA!");
        }

        return a;
    }
    
    public String[] getEnderco(Endereco endereco) {
    	String[] dados = endereco.retornaEnde();
    	return dados;
    }

    public Long inserir(Endereco endereco) throws SQLException, ClassNotFoundException {
        Long id = null;
        String sqlQuery = "insert into endereco(bairro,cep,cidade,complemento,numero,rua,estado,email,telefone) values(?,?,?,?,?,?,?,?,?)";

        PreparedStatement stmt = null;
        try {
        	String[] dados = getEnderco(endereco);
            ResultSet rs = getResult(stmt, dados, sqlQuery);
            if (rs.next()) {
                endereco.setId(rs.getLong(1));
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
            if (this.conexao != null) {
                this.conexao.close();
            }
        }
        return id;
    }
    
    public ResultadoConsultas getResultado() {
    	ResultadoConsultas rs = new ResultadoConsultas();
    	return rs;
    }

    public Endereco getEndereco(Long id_endereco) throws SQLException, ClassNotFoundException {
        Endereco endereco = null;
        String sqlQuery = "select * from endereco where id_endereco = ?";
        PreparedStatement stmt = null;
        try {
            ResultSet rs = executePrepared(stmt, id_endereco, sqlQuery);
            while (rs.next()) {
            	String bairro = getResultado().getResultString(rs, "bairro");
            	String cep = getResultado().getResultString(rs, "cep");
            	String cidade = getResultado().getResultString(rs, "cidade");
            	String complemento = getResultado().getResultString(rs, "complemento");
            	String email = getResultado().getResultString(rs, "email");
            	String rua = getResultado().getResultString(rs, "rua");
            	String telefone = getResultado().getResultString(rs, "telefone");
            	int numero = getResultado().getResultInt(rs, "numero");
            	
                endereco = new Endereco(id_endereco,bairro,cep,cidade,complemento,email,numero,rua,telefone,Estado.valueOf("estado"));
            }
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
            if (this.conexao != null) {
                this.conexao.close();
            }
        }
        return endereco;
    }

    public boolean update(Endereco endereco) throws SQLException, ClassNotFoundException {
        boolean atualizado = false;
        String sqlQuery = "UPDATE endereco SET bairro=?, cep=?, cidade=?, complemento=?, numero=?, rua=?, estado=?, email=?, telefone=? WHERE endereco.id_endereco = ?";
        PreparedStatement stmt = null;
        try {
        	String[] dados = endereco.retornaEnde();
            atualizado = executePrepared(stmt, dados, sqlQuery)  != null;
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
            if (this.conexao != null) {
                this.conexao.close();
            }
        }
        return atualizado;
    }
}
