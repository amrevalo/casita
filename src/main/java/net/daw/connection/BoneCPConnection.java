/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.connection;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import java.sql.Connection;
import java.sql.SQLException;
import net.daw.helper.ConnectionClassHelper;
import net.daw.helper.Log4j;

/**
 *
 * @author a022593391p
 */
public class BoneCPConnection implements ConnectionInterface {
    
    private BoneCP connectionPool = null;
    private Connection oConnection = null;

    @Override
    public Connection newConnection() throws Exception {
        try {
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(ConnectionClassHelper.getConnectionChain());
            config.setUsername(ConnectionClassHelper.getDatabaseLogin());
            config.setPassword(ConnectionClassHelper.getDatabasePassword());
            config.setMinConnectionsPerPartition(1);
            config.setMaxConnectionsPerPartition(3);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config);
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
