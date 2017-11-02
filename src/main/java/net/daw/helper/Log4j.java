/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.helper;
import org.apache.log4j.Logger;
/**
 *
 * @author alumno
 */
public class Log4j {
    public static void infoLog(String strMessage) {
        Logger log = Logger.getLogger("sissane-server");
        log.info(strMessage);
    }
 
    public static void errorLog(String strMessage, Exception e) {
        Logger log = Logger.getLogger("sissane-server");
        log.error(strMessage, e);
    }
 
    public static void errorLog(String strMessage) {
        Logger log = Logger.getLogger("sissane-server");
        log.error(strMessage);
    }
}
