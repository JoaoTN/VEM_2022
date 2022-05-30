package br.udesc.ceavi.pin.infosaude.control;

import br.udesc.ceavi.pin.infosaude.modelo.Pessoa;
import br.udesc.ceavi.pin.infosaude.modelo.Endereco;
import br.udesc.ceavi.pin.infosaude.control.dao.ConexaoPostgresJDBC;
import br.udesc.ceavi.pin.infosaude.control.dao.PreparaStatement;
import br.udesc.ceavi.pin.infosaude.control.excecpton.DadosVaziosExcepitions;
import br.udesc.ceavi.pin.infosaude.control.excecpton.LoginJaRegistradoNaBaseDeDadosException;
import br.udesc.ceavi.pin.infosaude.modelo.Instituicao;
import br.udesc.ceavi.pin.infosaude.modelo.Profissional;
import br.udesc.ceavi.pin.infosaude.modelo.Sexo;
import br.udesc.ceavi.pin.infosaude.modelo.Usuario;
import br.udesc.ceavi.pin.infosaude.modelo.Usuario_Logado;
import br.udesc.ceavi.pin.infosaude.principal.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lucas
 */
public class PessoaControl {

	private Connection conexao;
    
    public Connection conexao() throws ClassNotFoundException, SQLException{
    	return this.conexao = ConexaoPostgresJDBC.getConnection();
    }
    
    public boolean validaCampoLogin(String login) throws SQLException, DadosVaziosExcepitions, LoginJaRegistradoNaBaseDeDadosException, ClassNotFoundException {
        boolean a = true;
        if (login.equals("")) {
            a = false;
            throw new DadosVaziosExcepitions("LOGIN INVALIDADO!");
        } else {
            String sql = "Select login from pessoa where login = ?";
            PreparedStatement stmt = null;
            try {
                stmt = this.conexao().prepareStatement(sql);
                stmt.setString(1, login);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String rsLogin = rs.getString("login");
                    if (login.equals(rsLogin)) {
                        a = false;
                        throw new LoginJaRegistradoNaBaseDeDadosException();
                    }
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
            return a;
        }
    }
    
    public ResultSet executePrepared(PreparedStatement stmt, String[] dados, String sqlQuery, long id, int option, java.sql.Date dataN) throws ClassNotFoundException, SQLException {        
    	stmt = this.conexao().prepareStatement(sqlQuery, option);
        stmt = getStatement().setPreparedLong(stmt, id, 1);
        stmt = getStatement().setPreparedString(stmt, dados[1], 2);
        stmt = getStatement().setPreparedString(stmt, dados[5], 3);
        stmt = getStatement().setPreparedString(stmt, dados[7], 4);
        stmt = getStatement().setPreparedString(stmt, dados[3], 5);
        stmt = getStatement().setPreparedString(stmt, dados[6], 6);
        stmt = getStatement().setPreparedString(stmt, dados[2], 7);
        stmt = getStatement().setPreparedString(stmt, dados[8], 9);
        stmt = getStatement().setPreparedDate(stmt, dataN, 8);

        stmt.executeUpdate();
        return stmt.getGeneratedKeys();
    }

    public boolean validaCampos(String cpf,
            String nome, String numero, String rg, String senha) throws DadosVaziosExcepitions, SQLException {
        boolean a = true;
        if (cpf.equals("")) {
            a = false;
            throw new DadosVaziosExcepitions("CPF INVALIDO!");
        }
        if (nome.equals("")) {
            a = false;
            throw new DadosVaziosExcepitions("NOME INVALIDADO!");
        }
        if (numero.equals("")) {
            a = false;
            throw new DadosVaziosExcepitions("NUMERO DO SUS INVALIDADO!");
        }
        if (rg.equals("")) {
            a = false;
            throw new DadosVaziosExcepitions("RG INVALIDADO!");
        }
        if (senha.equals("")) {
            a = false;
            throw new DadosVaziosExcepitions("SENHA INVALIDADA!");
        }
        return a;
    }
    
    public PreparaStatement getStatement() {
    	PreparaStatement ps = new PreparaStatement();
    	return ps;
    }
    
    public void setIDPessoa(Pessoa pessoa, long id) {
    	pessoa.setId(id);
    }
    
    public Object buscaDados(Pessoa pessoa, Endereco endereco, int i) {
    	if(i == 1) {
    		return pessoa.retornaUser();
    	}else {
    		return endereco.getId();
    	}
    }

    public Long inserir(Pessoa pessoa, Endereco endereco) throws SQLException, ClassNotFoundException {
        Long id = null;
        String sqlQuery = "insert into pessoa(id_endereco,nome_pessoa,login,senha,cpf,rg,numero_sus,data_nascimento,sexo) values(?,?,?,?,?,?,?,?,?)";

        PreparedStatement stmt = null;
        
        try {
        	long _id = (long) buscaDados(pessoa, endereco, 0);
            String[] dados = (String[]) buscaDados(pessoa, endereco, 1);
            
            ResultSet rs = executePrepared(stmt, dados, sqlQuery, id, Statement.RETURN_GENERATED_KEYS, new java.sql.Date(Long.parseLong(dados[4])));
            if (rs.next()) {
                id = rs.getLong(1);
            }
            setIDPessoa(pessoa, _id);
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

    public boolean buscaUsuario(Usuario_Logado p, String login, String senha, PreparedStatement stmt, String sqlUsuario) throws SQLException, ClassNotFoundException {
    	stmt = this.conexao().prepareStatement(sqlUsuario);
        stmt.setString(1, login);
        stmt.setString(2, login);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Long id_pessoa = rs.getLong(1);
            if (id_pessoa != null) {
                p = new Usuario(id_pessoa,
                        rs.getLong("id_usuario"),
                        rs.getString("cpf"),
                        new Date(rs.getString("data_nascimento")),
                        rs.getString("login"),
                        rs.getString("nome"),
                        rs.getString("numero_sus"),
                        rs.getString("rg"),
                        rs.getString("senha"),
                        Sexo.valueOf(rs.getString("sexo")));
                Main.setParametrosUsuario(p, rs.getLong("id_endereco"));
                return true;
            }
        }
        return false;
    }
    
    public boolean buscaProfissional(Usuario_Logado p, PreparedStatement stmt, boolean usuarioEncontrado, String sqlProfissional, String login, String senha) throws SQLException, ClassNotFoundException {
    	ResultSet rs = stmt.executeQuery();
        
    	if (!usuarioEncontrado) {
            stmt = this.conexao().prepareStatement(sqlProfissional);
            stmt.setString(1, login);
            stmt.setString(2, login);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Long id_pessoa = rs.getLong(1);
                if (id_pessoa != null) {
                    p = new Profissional(id_pessoa,
                            rs.getLong("id_profissional"),
                            rs.getString("cpf"),
                            new Date(rs.getString("data_nascimento")),
                            rs.getString("login"),
                            rs.getString("nome"),
                            rs.getString("numero_sus"),
                            rs.getString("rg"),
                            rs.getString("senha"),
                            Sexo.valueOf(rs.getString("sexo")));
                    Main.setParametrosUsuario(p, rs.getLong("id_endereco"));
                    return true;
                }
            }
        }
    	
    	return false;
    }
    
    public boolean buscaInstituicao(Usuario_Logado p, PreparedStatement stmt, boolean usuarioEncontrado, String sqlInstuicao, String login, String senha) throws SQLException, ClassNotFoundException {
    	ResultSet rs = stmt.executeQuery();
    	if (!usuarioEncontrado) {
            stmt = this.conexao().prepareStatement(sqlInstuicao);
            stmt.setString(1, login);
            stmt.setString(2, login);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Long id_instituicao = rs.getLong(1);
                if (id_instituicao != null) {
                    p = new Instituicao(id_instituicao, rs.getString("cnpj"), rs.getString("nome_instituicao"), rs.getString("senha"));
                    Main.setParametrosUsuario(p, rs.getLong("id_endereco"));
                    return true;
                }
            }
        }
    	return false;
    }

    public boolean login(String login, String senha) throws SQLException, ClassNotFoundException {
        String sqlUsuario = "select * from pessoa as p natural inner join usario where p.login = ? and p.senha = ?";
        String sqlProfissional = "select * from pessoa as p natural inner join usario where p.login = ? and p.senha = ?";
        String sqlInstuicao = "select * from instituicao as i where i.cnpj = ? and p.senha = ?";
        boolean usuarioEncontrado = false;
        PreparedStatement stmt = null;
        Usuario_Logado p = null;
        try {
        	usuarioEncontrado = buscaUsuario(p, login, senha, stmt, sqlUsuario);

            usuarioEncontrado = buscaProfissional(p, stmt, usuarioEncontrado, sqlProfissional, login, senha);
            
            usuarioEncontrado = buscaInstituicao(p, stmt, usuarioEncontrado, sqlInstuicao, login, senha);
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
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

        return true;
    }

    public boolean update(Pessoa pessoa) throws SQLException, ClassNotFoundException {
        boolean atualizado = false;
        Long id = null;
        String sql = "update pessoa set nome = ?, login = ?, senha = ?, cpf = ?, rg = ?, numero_sus = ?, data_nascimento = ?, sexo = ? where pessoa.id_pessoa = ?";
        PreparedStatement stmt = null;
        try {
            String[] dados = (String[]) buscaDados(pessoa, null, 1);
            ResultSet rs = executePrepared(stmt, dados, sql, id, Statement.RETURN_GENERATED_KEYS, new java.sql.Date(Long.parseLong(dados[4])));
            atualizado = (rs != null);
            this.conexao.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
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
