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
import net.daw.beans.TipousuarioBean;
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
public class UsuarioDao implements DaoTableInterface<UsuarioBean>, DaoViewInterface<UsuarioBean> {

    private String strTable = "usuario";
    private String strSQL = "select * from " + strTable + " WHERE 1=1 ";
    private Connection oConnection = null;

    public UsuarioDao(Connection oPooledConnection) {
        oConnection = oPooledConnection;
    }

    @Override
    public UsuarioBean get(UsuarioBean oBean, int intExpand) throws Exception {
        PreparedStatement oPreparedStatement = null;
        ResultSet oResultSet = null;
        strSQL = "select * from " + strTable + " WHERE 1=1 ";
        strSQL += " AND id=" + oBean.getId();
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL);
            oResultSet = oPreparedStatement.executeQuery(strSQL);
            if (oResultSet.next()) {
                oBean.setDni(oResultSet.getString("dni"));
                oBean.setNombre(oResultSet.getString("nombre"));
                oBean.setPrimer_apellido(oResultSet.getString("primer_apellido"));
                oBean.setSegundo_apellido(oResultSet.getString("segundo_apellido"));
                oBean.setLogin(oResultSet.getString("login"));
                oBean.setPass(oResultSet.getString("pass"));
                oBean.setEmail(oResultSet.getString("email"));
                oBean.setId_tipousuario(oResultSet.getInt("id_tipousuario"));
                //pendiente la expansión ************************* %%%%%%
                if (intExpand > 0) {
                    TipousuarioBean oTipousuario = new TipousuarioBean();
                    TipousuarioDao oTipousuarioDao = new TipousuarioDao(oConnection);
                    oTipousuario.setId(oBean.getId_tipousuario());
                    oTipousuario = oTipousuarioDao.get(oTipousuario, --intExpand);
                    oBean.setObj_tipousuario(oTipousuario);
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
    public Integer set(UsuarioBean oBean) throws Exception {
        PreparedStatement oPreparedStatement = null;
        Integer iResult = 0;
        Boolean insert = true;
        try {
            if (oBean.getId() == null || oBean.getId() <= 0) {
                strSQL = "INSERT INTO " + strTable;
                strSQL += "(";
                strSQL += "dni,";
                strSQL += "nombre,";
                strSQL += "primer_apellido,";
                strSQL += "segundo_apellido,";
                strSQL += "login,";
                strSQL += "pass,";
                strSQL += "email,";
                strSQL += "id_tipousuario";
                strSQL += ")";
                strSQL += " VALUES ";
                strSQL += "(";
                strSQL += EncodingUtilHelper.quotate(oBean.getDni()) + ",";
                strSQL += EncodingUtilHelper.quotate(oBean.getNombre()) + ",";
                strSQL += EncodingUtilHelper.quotate(oBean.getPrimer_apellido()) + ",";
                strSQL += EncodingUtilHelper.quotate(oBean.getSegundo_apellido()) + ",";
                strSQL += EncodingUtilHelper.quotate(oBean.getLogin()) + ",";
                strSQL += EncodingUtilHelper.quotate(oBean.getPass()) + ",";
                strSQL += EncodingUtilHelper.quotate(oBean.getEmail()) + ",";
                strSQL += oBean.getId_tipousuario();
                strSQL += ")";
            } else {
                insert = false;
                strSQL = "UPDATE " + strTable;
                strSQL += " SET ";
                strSQL += "dni=" + EncodingUtilHelper.quotate(oBean.getDni()) + ", ";
                strSQL += "nombre=" + EncodingUtilHelper.quotate(oBean.getNombre()) + ",";
                strSQL += "primer_apellido=" + EncodingUtilHelper.quotate(oBean.getPrimer_apellido()) + ",";
                strSQL += "segundo_apellido=" + EncodingUtilHelper.quotate(oBean.getSegundo_apellido()) + ",";
                strSQL += "login=" + EncodingUtilHelper.quotate(oBean.getLogin()) + ",";
                strSQL += "pass=" + EncodingUtilHelper.quotate(oBean.getPass()) + ",";
                strSQL += "email=" + EncodingUtilHelper.quotate(oBean.getEmail()) + ",";
                strSQL += "id_tipousuario=" + oBean.getId_tipousuario()+ " ";
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
        PreparedStatement oPreparedStatement = null;
        ResultSet oResultSet = null;
        strSQL = "SELECT COUNT(*) FROM " + strTable;
        strSQL += " WHERE 1=1 " + SqlBuilder.buildSqlFilter(alFilter);
        Long iResult = 0L;
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL);
            oResultSet = oPreparedStatement.executeQuery(strSQL);
            if (oResultSet.next()) {
                iResult = oResultSet.getLong("COUNT(*)");
            } else {
                String msg = this.getClass().getName() + ": getcount";
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

    @Override
    public ArrayList<UsuarioBean> getPage(int intRegsPerPag, int intPage, LinkedHashMap<String, String> hmOrder, ArrayList<FilterBeanHelper> alFilter) throws Exception {
        //String strSQL1 = strSQL + SqlBuilder.buildSqlLimit(this.getCount(), intRegsPerPag, intPage);
       // ArrayList<UsuarioBean> aloBean = new ArrayList<>();
        String strSQL1 = strSQL;
        strSQL1 += SqlBuilder.buildSqlFilter(alFilter);
        strSQL1 += SqlBuilder.buildSqlOrder(hmOrder);
        strSQL1 += SqlBuilder.buildSqlLimit(this.getCount(alFilter), intRegsPerPag, intPage);
        ArrayList<UsuarioBean> aloBean = new ArrayList<>();
        PreparedStatement oPreparedStatement = null;
        ResultSet oResultSet = null;
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL1);
            oResultSet = oPreparedStatement.executeQuery(strSQL1);
            while (oResultSet.next()) {
                UsuarioBean oBean = new UsuarioBean();
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

    public UsuarioBean getFromLoginAndPass(UsuarioBean oUsuarioBean) throws Exception {

        PreparedStatement oPreparedStatement = null;
        ResultSet oResultSet = null;
        strSQL = "select * from " + strTable + " WHERE 1=1 ";
        strSQL += " AND login='" + oUsuarioBean.getLogin() + "'";
        strSQL += " AND pass='" + oUsuarioBean.getPass() + "'";

        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL);
            oResultSet = oPreparedStatement.executeQuery();
            if (oResultSet.next()) {
                oUsuarioBean.setId(oResultSet.getInt("id"));
                oUsuarioBean.setDni(oResultSet.getString("dni"));
                oUsuarioBean.setNombre(oResultSet.getString("nombre"));
                oUsuarioBean.setPrimer_apellido(oResultSet.getString("primer_apellido"));
                oUsuarioBean.setSegundo_apellido(oResultSet.getString("segundo_apellido"));
                oUsuarioBean.setLogin(oResultSet.getString("login"));
                oUsuarioBean.setPass(oResultSet.getString("pass"));
                oUsuarioBean.setEmail(oResultSet.getString("email"));
                oUsuarioBean.setId_tipousuario(oResultSet.getInt("id_tipousuario"));
                //pendiente la expansión ************************* %%%%%%
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
        return oUsuarioBean;

    }

    
    public Long getCountxtipousuario(ArrayList<FilterBeanHelper> alFilter, int id_tipousuario) throws Exception {
        PreparedStatement oPreparedStatement = null;
        ResultSet oResultSet = null;
        strSQL = "SELECT COUNT(*) FROM " + strTable;
        strSQL += " WHERE id_tipousuario=" + id_tipousuario;
        strSQL += SqlBuilder.buildSqlFilter(alFilter);
        Long iResult = 0L;
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL);
            oResultSet = oPreparedStatement.executeQuery(strSQL);
            if (oResultSet.next()) {
                iResult = oResultSet.getLong("COUNT(*)");
            } else {
                String msg = this.getClass().getName() + ": getCountxtipousuario";
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
    
    
    public ArrayList<UsuarioBean> getPagextipousuario(int intRegsPerPag, int intPage, LinkedHashMap<String, String> hmOrder, ArrayList<FilterBeanHelper> alFilter, int id_tipousuario) throws Exception {
        String strSQL1 = strSQL;
        strSQL1 += " and id_tipousuario=" + id_tipousuario + " ";
        strSQL1 += SqlBuilder.buildSqlFilter(alFilter);
        strSQL1 += SqlBuilder.buildSqlOrder(hmOrder);
        strSQL1 += SqlBuilder.buildSqlLimit(this.getCount(alFilter), intRegsPerPag, intPage);
        ArrayList<UsuarioBean> aloBean = new ArrayList<>();
        PreparedStatement oPreparedStatement = null;
        ResultSet oResultSet = null;
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL1);
            oResultSet = oPreparedStatement.executeQuery(strSQL1);
            while (oResultSet.next()) {
                aloBean.add(this.get(new UsuarioBean(oResultSet.getInt("id")), AppConfigurationHelper.getJsonMsgDepth()));
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
