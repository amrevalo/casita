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
import static net.daw.helper.AppConfigurationHelper.getJsonMsgDepth;
import net.daw.helper.EncodingUtilHelper;
import net.daw.helper.FilterBeanHelper;
import net.daw.helper.SqlBuilder;

/**
 *
 * @author alumno
 */
public class TipousuarioDao implements DaoTableInterface<TipousuarioBean>, DaoViewInterface<TipousuarioBean> {
    
    private String strTable = "tipousuario";
    private String strSQL = "select * from " + strTable + " WHERE 1=1 ";
    private Connection oConnection = null;

    public TipousuarioDao(Connection oPooledConnection) {
        oConnection = oPooledConnection;
    }

    @Override
    public TipousuarioBean get(TipousuarioBean oBean, int intExpand) throws Exception {
        PreparedStatement oPreparedStatement = null;
        ResultSet oResultSet = null;
        strSQL = "select * from " + strTable + " WHERE 1=1 ";
        strSQL += " AND id=" + oBean.getId();
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL);
            oResultSet = oPreparedStatement.executeQuery(strSQL);
            if (oResultSet.next()) {
                oBean.setDescripcion(oResultSet.getString("descripcion"));              
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
        return oBean;
    }

    @Override
    public Integer set(TipousuarioBean oBean) throws Exception {
        PreparedStatement oPreparedStatement = null;
        Integer iResult = 0;
        Boolean insert = true;
        try {
            if (oBean.getId() == null || oBean.getId() <= 0) {
                strSQL = "INSERT INTO " + strTable;
                strSQL += "(";
                strSQL += "descripcion";
                strSQL += ")";
                strSQL += " VALUES ";
                strSQL += "(";
                strSQL += EncodingUtilHelper.quotate(oBean.getDescripcion());
                strSQL += ")";
            } else {
                insert = false;
                strSQL = "UPDATE " + strTable;
                strSQL += " SET ";               
                strSQL += "descripcion=" + EncodingUtilHelper.quotate(oBean.getDescripcion())+" ";
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
    public ArrayList<TipousuarioBean> getPage(int intRegsPerPag, int intPage, LinkedHashMap<String, String> hmOrder, ArrayList<FilterBeanHelper> alFilter) throws Exception {
        //String strSQL1 = strSQL + SqlBuilder.buildSqlLimit(this.getCount(), intRegsPerPag, intPage);
        //ArrayList<TipousuarioBean> aloBean = new ArrayList<>();
        String strSQL1 = strSQL;
        strSQL1 += SqlBuilder.buildSqlFilter(alFilter);
        strSQL1 += SqlBuilder.buildSqlOrder(hmOrder);
        strSQL1 += SqlBuilder.buildSqlLimit(this.getCount(alFilter), intRegsPerPag, intPage);
        ArrayList<TipousuarioBean> aloBean = new ArrayList<>();
        PreparedStatement oPreparedStatement = null;
        ResultSet oResultSet = null;
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL1);
            oResultSet = oPreparedStatement.executeQuery(strSQL1);
            while (oResultSet.next()) {
                TipousuarioBean oBean = new TipousuarioBean();
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

    
    
}
