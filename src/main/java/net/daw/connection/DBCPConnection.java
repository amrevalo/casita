/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.connection;

import java.sql.Connection;
import java.sql.SQLException;
import net.daw.helper.ConnectionClassHelper;
import net.daw.helper.Log4j;
import org.apache.commons.dbcp.BasicDataSource;

/**
 *
 * @author a022593391p
 */
public class DBCPConnection implements ConnectionInterface {
    
    private BasicDataSource dataSource = null;
    private Connection oConnection = null;

    @Override
    public Connection newConnection() throws Exception {
        try {
            dataSource = new BasicDataSource();
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            dataSource.setUsername(ConnectionClassHelper.getDatabaseLogin());
            dataSource.setPassword(ConnectionClassHelper.getDatabasePassword());
            dataSource.setUrl(ConnectionClassHelper.getConnectionChain());
            dataSource.setValidationQuery("select 1");
            dataSource.setMaxActive(100);
            dataSource.setMaxWait(10000);
            dataSource.setMaxIdle(10);
            oConnection = dataSource.getConnection();
        } catch (Exception ex) {
            String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName();
            Log4j.errorLog(msg, ex);
            throw new Exception(msg, ex);
        }
        return oConnection;
    }

    @Override
    public void disposeConnection() throws Exception {
        try {
            if (oConnection != null) {
                oConnection.close();
            }
            if (dataSource != null) {
                dataSource.close();
            }
        } catch (SQLException ex) {
            String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName();
            Log4j.errorLog(msg, ex);
            throw new Exception(msg, ex);
        }
    }
}
