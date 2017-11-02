/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.helper;

/**
 *
 * @author a022593391p
 */
public class ConnectionClassHelper {
    
    public static String getDatabaseName() {
        return "usuariodb";
    }

    public static String getDatabaseLogin() {
        return "root";
    }

    public static String getDatabasePassword() {
        return "bitnami";
    }

    public static String getDatabasePort() {
        return "3306";
    }

    public static String getDatabaseIP() {
        return "127.0.0.1";
    }

    public static String getConnectionChain() {
        return "jdbc:mysql://" + ConnectionClassHelper.getDatabaseIP() + ":" + ConnectionClassHelper.getDatabasePort() + "/" + ConnectionClassHelper.getDatabaseName();
    }
    
}
