/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import net.daw.beans.PedidoBean;

import net.daw.beans.UsuarioBean;
import net.daw.helper.AppConfigurationHelper;
import static net.daw.helper.AppConfigurationHelper.getJsonMsgDepth;
import net.daw.helper.EncodingUtilHelper;
import net.daw.helper.FilterBeanHelper;
import net.daw.helper.Log4j;
import net.daw.helper.SqlBuilder;

/**
 *
 * @author alumno
 */
public class PedidoDao implements DaoTableInterface<PedidoBean>, DaoViewInterface<PedidoBean>{
    
    
    private String strTable = "pedido";
    private String strSQL = "select * from " + strTable + " WHERE 1=1 ";
    private Connection oConnection = null;

    public PedidoDao(Connection oPooledConnection) {
        oConnection = oPooledConnection;
    }

    @Override
    public PedidoBean get(PedidoBean oBean, int intExpand) throws Exception {
        PreparedStatement oPreparedStatement = null;
        ResultSet oResultSet = null;
        strSQL = "select * from " + strTable + " WHERE 1=1 ";
        strSQL += " AND id=" + oBean.getId();
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL);
            oResultSet = oPreparedStatement.executeQuery(strSQL);
            if (oResultSet.next()) {
                oBean.setFecha(oResultSet.getDate("fecha"));
                oBean.setId_usuario(oResultSet.getInt("id_usuario"));
                //pendiente la expansión ************************* %%%%%%
                if (intExpand > 0) {
                    UsuarioBean oUsuario = new UsuarioBean();
                    UsuarioDao oUsuarioDao = new UsuarioDao(oConnection);
                    oUsuario.setId(oBean.getId_usuario());
                    oUsuario = oUsuarioDao.get(oUsuario, --intExpand);
                    oBean.setObj_usuario(oUsuario);
                }
            } else {
                throw new Exception();
            }
        } catch (Exception ex) {
            //log4j 
            throw new Exception();
        } finally {
            if (oResultSet != null) {
                oResultSet.close();
            }
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }

        }
        return oBean;
    }

    @Override
    public Integer set(PedidoBean oBean) throws Exception {
        PreparedStatement oPreparedStatement = null;
        Integer iResult = 0;
        Boolean insert = true;
        try {
            if (oBean.getId() == null || oBean.getId() <= 0) {
                strSQL = "INSERT INTO " + strTable;
                strSQL += "(";               
                strSQL += "fecha,";
                strSQL += "id_usuario";
                strSQL += ")";
                strSQL += " VALUES ";
                strSQL += "(";
                strSQL += EncodingUtilHelper.stringifyAndQuotate(oBean.getFecha()) + ",";
                strSQL += oBean.getId_usuario() + " ";
                strSQL += ")";
            } else {
                insert = false;
                strSQL = "UPDATE " + strTable;
                strSQL += " SET ";
                strSQL += "fecha=" + EncodingUtilHelper.stringifyAndQuotate(oBean.getFecha())+",";
                strSQL += "id_usuario=" + oBean.getId_usuario() + " ";
                strSQL += "WHERE id=" + oBean.getId();
            }
            oPreparedStatement = oConnection.prepareStatement(strSQL, Statement.RETURN_GENERATED_KEYS);
            iResult = oPreparedStatement.executeUpdate();
            if (iResult < 1) {
                throw new Exception();
            }
            if (insert) {
                ResultSet oResultSet = oPreparedStatement.getGeneratedKeys();
                oResultSet.next();
                iResult = oResultSet.getInt(1);
            }
        } catch (Exception ex) {
            throw new Exception();
        } finally {
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
        return iResult;
    }

    //hecho por alberto
    @Override
    public Boolean remove(Integer id) throws Exception {
        boolean iResult = false;
        strSQL = "DELETE FROM " + strTable + " WHERE id=?";
        PreparedStatement oPreparedStatement = null;
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL);
            oPreparedStatement.setInt(1, id);
            iResult = oPreparedStatement.execute();
        } catch (Exception ex) {
            throw new Exception();
        } finally {
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
        return iResult;
    }

    @Override
    public Long getCount(ArrayList<FilterBeanHelper> alFilter) throws Exception {
        ResultSet oResultSet = null;
        PreparedStatement oPreparedStatement = null;
        Long iResult = 0L;
        strSQL = "SELECT COUNT(*) FROM " + strTable;
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL);
            oResultSet = oPreparedStatement.executeQuery(strSQL);
            //iResult = oResultSet.getLong(1);
            if (oResultSet.next()) {
                iResult = oResultSet.getLong("COUNT(*)");
            } else {
                throw new Exception();
            }
        } catch (Exception ex) {
            throw new Exception();
        } finally {
            if (oResultSet != null) {
                oResultSet.close();
            }
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
        return iResult;
    }

    @Override
    public ArrayList<PedidoBean> getPage(int intRegsPerPag, int intPage, LinkedHashMap<String, String> hmOrder, ArrayList<FilterBeanHelper> alFilter) throws Exception {
        //String strSQL1 = strSQL + SqlBuilder.buildSqlLimit(this.getCount(), intRegsPerPag, intPage);
        String strSQL1 = strSQL;
        strSQL1 += SqlBuilder.buildSqlFilter(alFilter);
        strSQL1 += SqlBuilder.buildSqlOrder(hmOrder);
        strSQL1 += SqlBuilder.buildSqlLimit(this.getCount(alFilter), intRegsPerPag, intPage);
        ArrayList<PedidoBean> aloBean = new ArrayList<>();
        PreparedStatement oPreparedStatement = null;
        ResultSet oResultSet = null;
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL1);
            oResultSet = oPreparedStatement.executeQuery(strSQL1);
            while (oResultSet.next()) {
                PedidoBean oBean = new PedidoBean();
                oBean.setId(oResultSet.getInt("id"));
                oBean = this.get(oBean, getJsonMsgDepth());
                aloBean.add(oBean);
                //aloBean.add(this.get(new UsuarioBean(oResultSet.getInt("id"))));                
            }
        } catch (Exception ex) {
            throw new Exception();
        } finally {
            if (oResultSet != null) {
                oResultSet.close();
            }
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
        return aloBean;
    }

//    public UsuarioBean getFromLoginAndPass(UsuarioBean oUsuarioBean) throws Exception {
//
//        PreparedStatement oPreparedStatement = null;
//        ResultSet oResultSet = null;
//        strSQL = "select * from " + strTable + " WHERE 1=1 ";
//        strSQL += " AND login='" + oUsuarioBean.getLogin() + "'";
//        strSQL += " AND pass='" + oUsuarioBean.getPass() + "'";
//
//        try {
//            oPreparedStatement = oConnection.prepareStatement(strSQL);
//            oResultSet = oPreparedStatement.executeQuery();
//            if (oResultSet.next()) {
//                oUsuarioBean.setId(oResultSet.getInt("id"));
//                oUsuarioBean.setDni(oResultSet.getString("dni"));
//                oUsuarioBean.setNombre(oResultSet.getString("nombre"));
//                oUsuarioBean.setPrimer_apellido(oResultSet.getString("primer_apellido"));
//                oUsuarioBean.setSegundo_apellido(oResultSet.getString("segundo_apellido"));
//                oUsuarioBean.setLogin(oResultSet.getString("login"));
//                oUsuarioBean.setPass(oResultSet.getString("pass"));
//                oUsuarioBean.setEmail(oResultSet.getString("email"));
//                oUsuarioBean.setId_tipousuario(oResultSet.getInt("id_tipousuario"));
//                //pendiente la expansión ************************* %%%%%%
//            } else {
//                throw new Exception();
//            }
//        } catch (Exception ex) {
//            //log4j 
//            throw new Exception();
//        } finally {
//            if (oResultSet != null) {
//                oResultSet.close();
//            }
//            if (oPreparedStatement != null) {
//                oPreparedStatement.close();
//            }
//
//        }
//        return oUsuarioBean;
//
//    }

    
    public Long getCountxusuario(ArrayList<FilterBeanHelper> alFilter, int id_usuario) throws Exception {
        PreparedStatement oPreparedStatement = null;
        ResultSet oResultSet = null;
        strSQL = "SELECT COUNT(*) FROM " + strTable;
        strSQL += " WHERE id_usuario=" + id_usuario;
        strSQL += SqlBuilder.buildSqlFilter(alFilter);
        Long iResult = 0L;
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL);
            oResultSet = oPreparedStatement.executeQuery(strSQL);
            if (oResultSet.next()) {
                iResult = oResultSet.getLong("COUNT(*)");
            } else {
                String msg = this.getClass().getName() + ": getCountxusuario";
                Log4j.errorLog(msg);
                throw new Exception(msg);
            }
        } catch (Exception ex) {
            String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName();
            Log4j.errorLog(msg, ex);
            throw new Exception(msg, ex);
        } finally {
            if (oResultSet != null) {
                oResultSet.close();
            }
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
        return iResult;
    }
    
    
    public ArrayList<PedidoBean> getPagexusuario(int intRegsPerPag, int intPage, LinkedHashMap<String, String> hmOrder, ArrayList<FilterBeanHelper> alFilter, int id_usuario) throws Exception {
        String strSQL1 = strSQL;
        strSQL1 += " and id_usuario=" + id_usuario + " ";
        strSQL1 += SqlBuilder.buildSqlFilter(alFilter);
        strSQL1 += SqlBuilder.buildSqlOrder(hmOrder);
        strSQL1 += SqlBuilder.buildSqlLimit(this.getCount(alFilter), intRegsPerPag, intPage);
        ArrayList<PedidoBean> aloBean = new ArrayList<>();
        PreparedStatement oPreparedStatement = null;
        ResultSet oResultSet = null;
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL1);
            oResultSet = oPreparedStatement.executeQuery(strSQL1);
            while (oResultSet.next()) {
                aloBean.add(this.get(new PedidoBean(oResultSet.getInt("id")), AppConfigurationHelper.getJsonMsgDepth()));
            }
        } catch (Exception ex) {
            String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName();
            Log4j.errorLog(msg, ex);
            throw new Exception(msg, ex);
        } finally {
            if (oResultSet != null) {
                oResultSet.close();
            }
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
        return aloBean;
    }
}
