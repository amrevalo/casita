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
import net.daw.beans.ProductoBean;
import static net.daw.helper.AppConfigurationHelper.getJsonMsgDepth;
import net.daw.helper.EncodingUtilHelper;
import net.daw.helper.FilterBeanHelper;
import net.daw.helper.SqlBuilder;

/**
 *
 * @author alumno
 */
public class ProductoDao implements DaoTableInterface<ProductoBean>, DaoViewInterface<ProductoBean>{
    
    private String strTable = "producto";
    private String strSQL = "select * from " + strTable + " WHERE 1=1 ";
    private Connection oConnection = null;

    public ProductoDao(Connection oPooledConnection) {
        oConnection = oPooledConnection;
    }

    @Override
    public ProductoBean get(ProductoBean oBean, int intExpand) throws Exception {
        PreparedStatement oPreparedStatement = null;
        ResultSet oResultSet = null;
        strSQL = "select * from " + strTable + " WHERE 1=1 ";
        strSQL += " AND id=" + oBean.getId();
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL);
            oResultSet = oPreparedStatement.executeQuery(strSQL);
            if (oResultSet.next()) {
                oBean.setCantidad(oResultSet.getInt("cantidad"));
                oBean.setPrecio(oResultSet.getDouble("precio"));
                oBean.setNombre(oResultSet.getString("nombre"));
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
    public Integer set(ProductoBean oBean) throws Exception {
        PreparedStatement oPreparedStatement = null;
        Integer iResult = 0;
        Boolean insert = true;
        try {
            if (oBean.getId() == null || oBean.getId() <= 0) {
                strSQL = "INSERT INTO " + strTable;
                strSQL += "(";               
                strSQL += "cantidad,";
                strSQL += "precio,";                
                strSQL += "nombre";
                strSQL += ")";
                strSQL += " VALUES ";
                strSQL += "(";
                strSQL += oBean.getCantidad();
                strSQL += oBean.getPrecio();
                strSQL +=  EncodingUtilHelper.quotate(oBean.getNombre());
                strSQL += ")";
            } else {
                insert = false;
                strSQL = "UPDATE " + strTable;
                strSQL += " SET ";
                strSQL += "cantidad=" + oBean.getCantidad()+",";
                strSQL += "precio=" + oBean.getPrecio()+",";
                strSQL += "nombre=" + EncodingUtilHelper.quotate(oBean.getNombre())+" ";
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
    public ArrayList<ProductoBean> getPage(int intRegsPerPag, int intPage, LinkedHashMap<String, String> hmOrder, ArrayList<FilterBeanHelper> alFilter) throws Exception {
        //String strSQL1 = strSQL + SqlBuilder.buildSqlLimit(this.getCount(), intRegsPerPag, intPage);
        String strSQL1 = strSQL;
        strSQL1 += SqlBuilder.buildSqlFilter(alFilter);
        strSQL1 += SqlBuilder.buildSqlOrder(hmOrder);
        strSQL1 += SqlBuilder.buildSqlLimit(this.getCount(alFilter), intRegsPerPag, intPage);
        ArrayList<ProductoBean> aloBean = new ArrayList<>();
        PreparedStatement oPreparedStatement = null;
        ResultSet oResultSet = null;
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL1);
            oResultSet = oPreparedStatement.executeQuery(strSQL1);
            while (oResultSet.next()) {
                ProductoBean oBean = new ProductoBean();
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
