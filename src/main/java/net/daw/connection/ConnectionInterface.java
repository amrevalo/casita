/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.connection;

import java.sql.Connection;


/**
 *
 * @author alumno
 */
public interface ConnectionInterface {
 
    public Connection newConnection() throws Exception;
 
    public void disposeConnection() throws Exception;
}
