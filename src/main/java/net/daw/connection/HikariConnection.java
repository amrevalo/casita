/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import net.daw.helper.ConnectionClassHelper;
import net.daw.helper.Log4j;

/**
 *
 * @author a022593391p
 */
public class HikariConnection implements ConnectionInterface {
    
    private HikariDataSource connectionPool = null;
    private Connection oConnection = null;

    @Override
    public Connection newConnection() throws Exception {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(ConnectionClassHelper.getConnectionChain());
        config.setUsername(ConnectionClassHelper.getDatabaseLogin());
        config.setPassword(ConnectionClassHelper.getDatabasePassword());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        try {
            HikariDataSource connectionPool = new HikariDataSource(config);
            oConnection = connectionPool.getConnection();
        } catch (SQLException ex) {
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
            if (connectionPool != null) {
                connectionPool.close();
            }
        } catch (SQLException ex) {
            String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName();
            Log4j.errorLog(msg, ex);
            throw new Exception(msg, ex);
        }
    }
}
