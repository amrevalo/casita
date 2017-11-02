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
import org.vibur.dbcp.ViburDBCPDataSource;
import org.vibur.dbcp.ViburDBCPException;

/**
 *
 * @author a022593391p
 */
public class ViburConnection implements ConnectionInterface {
    
    private ViburDBCPDataSource dataSource = null;
    private Connection oConnection = null;

    @Override
    public Connection newConnection() throws Exception {

        try {

            dataSource = new ViburDBCPDataSource();

            dataSource.setJdbcUrl(ConnectionClassHelper.getConnectionChain());
            dataSource.setUsername(ConnectionClassHelper.getDatabaseLogin());
            dataSource.setPassword(ConnectionClassHelper.getDatabasePassword());

            dataSource.setPoolInitialSize(10);
            dataSource.setPoolMaxSize(100);

            dataSource.setConnectionIdleLimitInSeconds(30);
            dataSource.setTestConnectionQuery("isValid");

            dataSource.setLogQueryExecutionLongerThanMs(500);
            dataSource.setLogStackTraceForLongQueryExecution(true);

            dataSource.start();
            oConnection = dataSource.getConnection();

        } catch (SQLException | ViburDBCPException ex) {
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
