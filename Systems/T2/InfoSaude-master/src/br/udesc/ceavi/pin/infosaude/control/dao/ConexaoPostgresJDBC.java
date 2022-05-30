
package br.udesc.ceavi.pin.infosaude.control.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta Classe define os atributos de Campanha
 *
 * @author Erick William Henschel
 * @author Fabio Frare
 * @author Gustavo de Carvalho Santos
 * @author Lucas Eduardo Nogueira
 * @author Ricardo José Pinto
 * @version 1.0
 * @since 03/11/2018
 */
public class ConexaoPostgresJDBC {

    /**
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */

    public static Connection getConnection() throws ClassNotFoundException, SQLException{
    	Connection con = null; 
    	Class.forName("org.postgresql.Driver");

        Properties properties = new Properties();
        properties.put("user", "postgres");
        properties.put("password", "123456");


        con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/InfoSaude?ApplicationName=Projeto-InfoSaude", properties);
        con.setAutoCommit(false);
        return con;
    }

}
